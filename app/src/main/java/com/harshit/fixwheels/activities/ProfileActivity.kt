package com.harshit.fixwheels.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.fixwheels.R
import com.harshit.fixwheels.databinding.ActivityProfileBinding
import java.io.ByteArrayOutputStream

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding:ActivityProfileBinding
    private lateinit var vehicle:String
    private lateinit var id:String
    private lateinit var name:String
    private var bitmap: Bitmap? = null
    private lateinit var firebase:FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.backIV.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        auth = FirebaseAuth.getInstance()
        firebase = FirebaseFirestore.getInstance()
        name = intent.getStringExtra("name").toString()
        id = intent.getStringExtra("id").toString()
        val array:List<String> = listOf("Car","Motor Cycle","Truck","Others")
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,array)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                vehicle = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.addImageTV.setOnClickListener {
            if (checkPermission()) {
                CaptureImage()
            } else {
                requestPermission()
            }
        }

        binding.saveBTN.setOnClickListener {
            saveData()
//            startActivity(Intent(this, OTWActivity::class.java))
        }

    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
    }

    private fun checkPermission(): Boolean {
        val cameraPerm = ContextCompat.checkSelfPermission(applicationContext, CAMERA_SERVICE)
        return cameraPerm == PackageManager.PERMISSION_GRANTED
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
    private fun saveData() {

    }

    private fun temp(){
        if(bitmap!=null){
            val base64Image = bitmapToBase64(bitmap!!)

            val documentReference = firebase.collection("images").document("imageDocument")

            val data = hashMapOf(
                "imageData" to base64Image
            )

            documentReference.set(data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()) {
            val camera_permission = grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (camera_permission) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
                CaptureImage()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun CaptureImage() {
        val take_picture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(take_picture, 123)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == RESULT_OK) {
            val extra = data!!.extras
            bitmap = extra!!["data"] as Bitmap?
        }
    }
}