package com.harshit.fixwheels.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.harshit.fixwheels.R
import com.harshit.fixwheels.databinding.ActivityForgetBinding

class ForgetActivity : AppCompatActivity() {
    private lateinit var binding:ActivityForgetBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget)
        auth = FirebaseAuth.getInstance()
        binding.resetBTN.setOnClickListener {
            if(TextUtils.isEmpty(binding.emailLogin.text)){
                Toast.makeText(this,"Email is empty",Toast.LENGTH_LONG).show()
            }
            else{
                val email = binding.emailLogin.text.toString()
                auth.sendPasswordResetEmail(email).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this,"Please check your email",Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(this,it.exception.toString(),Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}