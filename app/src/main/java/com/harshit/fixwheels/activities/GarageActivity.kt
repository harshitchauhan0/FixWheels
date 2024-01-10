package com.harshit.fixwheels.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.harshit.fixwheels.R
import com.harshit.fixwheels.databinding.ActivityGarageBinding

class GarageActivity : AppCompatActivity() {
    private lateinit var binding:ActivityGarageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_garage)



    }
}