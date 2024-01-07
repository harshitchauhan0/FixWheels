package com.harshit.fixwheels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.harshit.fixwheels.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding:ActivityProfileBinding
    private lateinit var vehicle:String
    private lateinit var id:String
    private lateinit var name:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_profile)
        binding.backIV.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
        name =intent.getStringExtra("name").toString()
        id = intent.getStringExtra("id").toString()
        val array:List<String> = listOf("Car","Motor Cycle","Truck","Others")
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,array)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                vehicle = parent.getItemAtPosition(position).toString()
                Toast.makeText(applicationContext,"Selected: $vehicle", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.saveBTN.setOnClickListener {
            saveData()
            startActivity(Intent(this,OTWActivity::class.java))
        }

    }

    private fun saveData() {

    }
}