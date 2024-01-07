package com.harshit.fixwheels.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.fixwheels.R
import com.harshit.fixwheels.databinding.ActivitySignupBinding
import com.harshit.fixwheels.model.UserModel

class SignupActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignupBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var uid:String
    private lateinit var firebase:FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firebase = FirebaseFirestore.getInstance()
        uid = ""
        val mobileRegex = Regex("[6-9][0-9]{9}")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        binding.apply {
            btnSignUp.setOnClickListener {

                val isMobileValid = mobileRegex.matches(etNumber.text.toString())
                if(TextUtils.isEmpty(etName.text) or TextUtils.isEmpty(etEmail.text) or TextUtils.isEmpty(etPassword.text) or TextUtils.isEmpty(etPassword1.text) or TextUtils.isEmpty(etNumber.text)){
                    Toast.makeText(applicationContext,"Fill Fields correctly",Toast.LENGTH_LONG).show()
                }
                else if(etPassword.text.length<6){
                    Toast.makeText(applicationContext,"Password is too short",Toast.LENGTH_LONG).show()
                }
                else if(etPassword.text.toString() != etPassword1.text.toString()){
                    Toast.makeText(applicationContext,"Password is not the same",Toast.LENGTH_LONG).show()
                }
                else if(!isMobileValid){
                    Toast.makeText(applicationContext,"This mobile number is not valid",Toast.LENGTH_LONG).show()
                }
                else{
                    signUp(etEmail.text.toString().trim(),etPassword.text.toString().trim(),etName.text.toString(),etNumber.text.toString())
                }
            }
        }
    }

    private fun signUp(email: String, password: String,userName: String,mobile: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                Log.v("TAG","Logged in")
                uid = auth.currentUser!!.uid
                firebase.collection("Users").document(uid).set(UserModel(userName,mobile,email,uid))
                auth.currentUser?.sendEmailVerification()?.addOnCompleteListener { task->
                    if(task.isSuccessful){
                        Toast.makeText(this,"Please Verify Your Email",Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(this,"There is some problem",Toast.LENGTH_LONG).show()
                    }
                }

            }
            else{
                Toast.makeText(this,it.exception.toString(),Toast.LENGTH_LONG).show()
            }
        }
    }
}