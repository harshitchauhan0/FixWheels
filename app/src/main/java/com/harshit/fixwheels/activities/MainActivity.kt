package com.harshit.fixwheels.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import androidx.databinding.DataBindingUtil
import com.google.android.material.navigation.NavigationBarView
import com.harshit.fixwheels.HomeFragment
import com.harshit.fixwheels.LogoutFragment
import com.harshit.fixwheels.R
import com.harshit.fixwheels.ShopFragment
import com.harshit.fixwheels.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var shopFragment: ShopFragment
    private lateinit var logoutFragment: LogoutFragment
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        homeFragment = HomeFragment()
        logoutFragment = LogoutFragment()
        shopFragment = ShopFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container,homeFragment).commit()
        binding.GoToCart.setOnClickListener {
            val intent = Intent(this, MyCartActivity::class.java)
            startActivity(intent)
        }

        binding.orderBTN.setOnClickListener {
            val intent = Intent(this, OrderPlaced::class.java)
            startActivity(intent)
        }

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
                R.id.logout->{
                    supportFragmentManager.beginTransaction().replace(R.id.container,logoutFragment).commit()
                    return@OnItemSelectedListener true
                }
                else -> return@OnItemSelectedListener false
            }

        })
    }
}