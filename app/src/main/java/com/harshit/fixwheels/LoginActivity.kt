package com.harshit.fixwheels

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.harshit.fixwheels.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        setUpButtons()
    }
    private fun setUpButtons() {
        binding.signUp.setOnClickListener {
            val intent = Intent(this,SignupActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        binding.forgotpassword.setOnClickListener {
            val intent = Intent(this,ForgetActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        binding.loginBTN.setOnClickListener {
            if(TextUtils.isEmpty(binding.loginEmail.text) or TextUtils.isEmpty(binding.loginPassword.text)){
                Toast.makeText(this,"Fill details Correctly",Toast.LENGTH_LONG).show()
            }
            else if(binding.loginPassword.text.length < 6){
                Toast.makeText(this,"Password is too short",Toast.LENGTH_LONG).show()
            }
            else{
                SignIn(binding.loginEmail.text.toString(),binding.loginPassword.text.toString())
            }
        }
    }
    private fun SignIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                Log.v("TAG","Logged in")
                val verify = auth.currentUser?.isEmailVerified
                if(verify == true){
                    val intent = Intent(this,MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,"This is user is not verified",Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(this,it.exception.toString(),Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if((auth.currentUser!=null) and (auth.currentUser?.isEmailVerified == true)){
            val intent = Intent(this,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}