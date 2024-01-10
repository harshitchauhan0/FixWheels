package com.harshit.fixwheels.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.fixwheels.ExtraUtils
import com.harshit.fixwheels.R
import com.harshit.fixwheels.databinding.ActivitySettingUpGarageBinding
import com.harshit.fixwheels.model.GarageModel

class SettingUpGarage : AppCompatActivity() {
    private lateinit var binding:ActivitySettingUpGarageBinding
    private lateinit var phoneNumber:String
    private lateinit var database: FirebaseFirestore
    private var userModel: GarageModel? = null
    private var uid:String? = null
    private var imageUri:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_setting_up_garage)
        phoneNumber = intent.getStringExtra(ExtraUtils.PHONE)!!
        database = FirebaseFirestore.getInstance()

        uid = FirebaseAuth.getInstance().uid
        if(uid == null){
            Log.v("TAG","Uid is null")
        }
        binding.letsGoBtn.setOnClickListener {
            if(imageUri == null){
                Toast.makeText(this,"Select The image",Toast.LENGTH_LONG).show()
            }
            else {
                setName()
            }
        }
        binding.welcomeImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(Intent.createChooser(intent,"Select image"),7)
        }
    }
    private fun setName() {
        val name = binding.garageNameET.text.trim().toString()
        if(name.isEmpty() or (name.length<3)){
            binding.garageNameET.setError("Name is not valid")
            return
        }
        setInProgress(true)

        userModel = GarageModel(name,"4.0",imageUri,binding.garageAddressET.text.trim().toString(),binding.garageServiceET.text.trim().toString(),uid!!,phoneNumber)

        uid?.let {  i ->
            database.collection(ExtraUtils.Garages).document(i).set(userModel!!).addOnCompleteListener {
                setInProgress(false)
                if(it.isSuccessful){
                    Toast.makeText(applicationContext,"Details Added", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, GarageActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
                else{
                    Log.v("TAG",it.exception.toString())
                }
            }
        }

    }


    private fun setInProgress(isProgress: Boolean){
        if(isProgress){
            binding.progressbar.visibility = View.VISIBLE
            binding.letsGoBtn.visibility = View.GONE
        }
        else{
            binding.progressbar.visibility = View.GONE
            binding.letsGoBtn.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if((requestCode == 7) && (resultCode == RESULT_OK) && (data != null) && (data.data != null)){
            imageUri = data.data.toString()
        }
    }
}