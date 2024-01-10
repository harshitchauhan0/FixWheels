package com.harshit.fixwheels.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.harshit.fixwheels.ExtraUtils
import com.harshit.fixwheels.R
import com.harshit.fixwheels.databinding.ActivityGarageProfileBinding
import com.harshit.fixwheels.model.GarageModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GarageProfile : AppCompatActivity() {

    private lateinit var binding: ActivityGarageProfileBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var userModel: GarageModel
    private lateinit var launcher: ActivityResultLauncher<String>
    private var imageUri: Uri? = null
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_garage_profile)
        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        launcher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            try{
                if (it != null) {
                    imageUri = it
                    Glide.with(this).load(imageUri).apply(RequestOptions.circleCropTransform()).into(binding.profileImg)
                }
            }catch(e:Exception){
                e.printStackTrace()
            }

        }
        storageReference = FirebaseStorage.getInstance().reference.child(ExtraUtils.PROFILE).child(auth.uid!!)
        getUserData()
        binding.profileImg.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.update.setOnClickListener { updateBTNClick() }
    }

    private fun updateBTNClick() {
        val username = binding.profileName.text.toString()
        if(username.isEmpty() or (username.length<3)){
            binding.profileName.error = "Username is too short"
            return
        }
        userModel.name = username
        userModel.address = binding.Newaddress.text.toString()
        userModel.description = binding.NewDesc.text.toString()
        if(imageUri!=null){
            storageReference.putFile(imageUri!!).addOnCompleteListener { task->
                if(task.isSuccessful){
                    Toast.makeText(this,"Updated", Toast.LENGTH_LONG).show()
                    updateDatabase()
                }
                else{
                    Log.v("TAG",task.exception.toString())
                }
            }
        }
        else{
            updateDatabase()
        }
    }

    private fun updateDatabase(){
        CoroutineScope(Dispatchers.Default).launch {
            database.collection(ExtraUtils.Garages).document(auth.uid!!).set(userModel).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(applicationContext,"Update Successful",Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(applicationContext,"Update Failed",Toast.LENGTH_LONG).show()
                    Log.v("TAG",it.exception.toString())
                }
            }
        }
    }

    private fun getUserData() {
        CoroutineScope(Dispatchers.Default).launch {
            storageReference.downloadUrl.addOnCompleteListener {
                if(it.isSuccessful){
                    val uri = it.result
                    Glide.with(applicationContext).load(uri).into(binding.profileImg) }
                }
            }
            database.collection(ExtraUtils.Garages).document(auth.uid!!).get().addOnCompleteListener {
                if(it.isSuccessful){
                    userModel = it.result.toObject(GarageModel::class.java)!!
                    binding.profileName.setText(userModel.name)
                    binding.Newaddress.setText(userModel.address)
                    binding.NewDesc.setText(userModel.description)
                }
            }
        }
    }
}