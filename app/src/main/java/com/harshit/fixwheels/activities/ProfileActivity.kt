package com.harshit.fixwheels.activities

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.fixwheels.ExtraUtils
import com.harshit.fixwheels.R
import com.harshit.fixwheels.databinding.ActivityProfileBinding
import com.harshit.fixwheels.model.BookingModel
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding:ActivityProfileBinding
    private lateinit var vehicle:String
    private lateinit var garageId:String
    private var bitmap: Bitmap? = null
    private var imageUri: String? =null
    private lateinit var firebase:FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var uid:String
    private lateinit var calendar:Calendar
    private lateinit var dateFormat: SimpleDateFormat
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private var location:Location? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.backIV.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        calendar = Calendar.getInstance()
        dateFormat = SimpleDateFormat("dd-MM-yyyy")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser!!.uid
        firebase = FirebaseFirestore.getInstance()
        garageId = intent.getStringExtra(ExtraUtils.ID).toString()
        val array:List<String> = ExtraUtils.vehicleList
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
            if (ContextCompat.checkSelfPermission(applicationContext, CAMERA_SERVICE) == PackageManager.PERMISSION_GRANTED) {
                CaptureImage()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
            }
        }

        binding.saveBTN.setOnClickListener {
            saveData()
        }


        binding.clickHereTV.setOnClickListener {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                requestGPS()
            }
            else{
                getCurrentLocation()
            }
        }

    }

    private fun saveData() {
        val bookingModel = BookingModel(imageUri,location?.latitude.toString(),location?.longitude.toString(), Timestamp.now(),
            binding.problemET.text.toString(),uid,vehicle,garageId)
        val curr = dateFormat.format(calendar.time)
        firebase.collection(ExtraUtils.Booking).document(garageId).collection(curr)
            .add(bookingModel).addOnCompleteListener {
                if(it.isSuccessful){
                    startActivity(Intent(this, OTWActivity::class.java))
                }
                else{
                    Log.v("TAG",it.exception.toString())
                }
            }
    }

    private fun getCurrentLocation(){ if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
        }else{
            fusedLocationClient.lastLocation.addOnCompleteListener {
                if(it.isSuccessful){
                    location = it.result
                }
                else{
                    Toast.makeText(this,it.exception?.message,Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    fun bitmapToBase64(bitmap: Bitmap?) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        imageUri =  Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
    private fun requestGPS(){
        val builder = AlertDialog.Builder(this)
            .setMessage("Enable Gps").setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

        val alert = builder.create()
        alert.show()
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
            bitmapToBase64(bitmap)
        }
    }
}