package com.harshit.fixwheels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.harshit.fixwheels.databinding.ActivityOtwactivityBinding

class OTWActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOtwactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_otwactivity)
        binding.backIV.setOnClickListener{startActivity(Intent(this,MainActivity::class.java))}
    }
}