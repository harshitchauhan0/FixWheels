package com.harshit.fixwheels.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.harshit.fixwheels.ExtraUtils
import com.harshit.fixwheels.R
import com.harshit.fixwheels.databinding.ActivityUserDetailBinding
import com.harshit.fixwheels.model.BookingModel

class UserDetail : AppCompatActivity() {
    private lateinit var model:BookingModel
    private lateinit var binding:ActivityUserDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_user_detail)
        model = intent.getSerializableExtra(ExtraUtils.Booking.substring(0, ExtraUtils.Booking.length-1)) as BookingModel
        binding.UsernameTV.text = model.userName
        binding.vehicleTV.text = model.vehicle
        binding.descTV.text = model.description
        Glide.with(this).load(model.imageUri).into(binding.imageView)
        binding.timeTV.text = model.timestamp.toString()
        binding.locationTV.setOnClickListener {
            val latitude = model.latitude
            val longitude = model.longitude
            if (latitude!=null && longitude!=null && latitude.isNotEmpty() && longitude.isNotEmpty()) {
                val geoUri = "geo:$latitude,$longitude"
                val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
                mapIntent.setPackage("com.google.android.apps.maps")
                if (mapIntent.resolveActivity(packageManager) != null) {
                    startActivity(mapIntent)
                } else {
                    Toast.makeText(this, "Google Maps app not found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Latitude or Longitude is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}