package com.harshit.fixwheels

import android.content.Intent
import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import com.google.android.material.slider.LabelFormatter

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.slider.RangeSlider
import com.harshit.fixwheels.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var shopFragment: ShopFragment
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        homeFragment = HomeFragment()
        shopFragment = ShopFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container,homeFragment).commit()
//        binding.profileBTN.setOnClickListener {
//            startActivity(Intent(this,ProfileActivity::class.java))
//        }

        binding.bottomNavigation.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener {
            when(it.itemId){
                R.id.explore ->{
                    supportFragmentManager.beginTransaction().replace(R.id.container,homeFragment).commit()
                    return@OnItemSelectedListener true
                }
                R.id.shopping ->{
                    supportFragmentManager.beginTransaction().replace(R.id.container,shopFragment).commit()
                    return@OnItemSelectedListener true
                }
                else -> return@OnItemSelectedListener false
            }

        })
    }
}