package com.harshit.fixwheels.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.harshit.fixwheels.ExtraUtils
import com.harshit.fixwheels.R
import com.harshit.fixwheels.databinding.ActivityGarageLoginBinding
import com.harshit.fixwheels.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class GarageLogin : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var phoneNumber:String
    private lateinit var garageloginbinding:ActivityGarageLoginBinding
    private lateinit var verificationCode:String
    private lateinit var token: PhoneAuthProvider.ForceResendingToken
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        garageloginbinding = DataBindingUtil.setContentView(this,R.layout.activity_garage_login)

        auth = FirebaseAuth.getInstance()
        garageloginbinding.progressbar.visibility = View.GONE

        garageloginbinding.countryCodeHolder.registerCarrierNumberEditText(garageloginbinding.number)
        garageloginbinding.generateBTN.setOnClickListener {
            if(garageloginbinding.countryCodeHolder.isValidFullNumber){
                phoneNumber = garageloginbinding.countryCodeHolder.fullNumberWithPlus
                sendOTP(false)
            }
            else{
                Toast.makeText(applicationContext,"Phone Number is not valid", Toast.LENGTH_LONG).show()
            }
        }

        garageloginbinding.submitBTN.setOnClickListener {
            val otpTyped = garageloginbinding.otpET.text.toString()
            val credential = PhoneAuthProvider.getCredential(verificationCode,otpTyped)
            signIn(credential)
        }

        garageloginbinding.resendTV.setOnClickListener {
            sendOTP(true)
        }
    }

    private fun sendOTP(isResend: Boolean){
        setInProgress(true)
        startResendTimer()
        val builder = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(object :
                PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    signIn(p0)
                    setInProgress(false)
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Log.v("TAG",p0.message.toString())
                    Toast.makeText(applicationContext,"OTP VERIFICATION FAILED", Toast.LENGTH_LONG).show()
                    setInProgress(false)
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    this@GarageLogin.verificationCode = p0
                    token = p1
                    Toast.makeText(applicationContext,"OTP SENT", Toast.LENGTH_LONG).show()
                    setInProgress(false)
                }

            })

        if(isResend){
//            Toast.makeText(applicationContext,"Resending",Toast.LENGTH_LONG).show()
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(token).build())
        }
        else{
//            Toast.makeText(applicationContext,"New ",Toast.LENGTH_LONG).show()
            PhoneAuthProvider.verifyPhoneNumber(builder.build())
        }
    }

    private fun startResendTimer() {
        var timing = 60L
        garageloginbinding.resendTV.isEnabled = false

        CoroutineScope(Dispatchers.Main).launch {
            while (timing > 0) {
                timing--
                garageloginbinding.resendTV.text = "Resend in $timing s"
                delay(1000) // Wait for 1 second using coroutine delay

                if (timing == 0L) {
                    garageloginbinding.resendTV.isEnabled = true
                }
            }
        }
    }

    private fun signIn(credential: PhoneAuthCredential) {
        setInProgress(true)
        CoroutineScope(Dispatchers.Main).launch {
            auth.signInWithCredential(credential).addOnCompleteListener {
                setInProgress(false)
                if(it.isSuccessful){
                    Toast.makeText(applicationContext,"OTP Verification done", Toast.LENGTH_LONG).show()
                    startActivity(Intent(applicationContext,SettingUpGarage::class.java).putExtra(ExtraUtils.PHONE,phoneNumber))
                }
                else{
                    Toast.makeText(applicationContext,"OTP Verification failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setInProgress(isProgress: Boolean){
        runOnUiThread {
            if(isProgress){
                garageloginbinding.progressbar.visibility = View.VISIBLE
                garageloginbinding.submitBTN.visibility = View.GONE
            }
            else{
                garageloginbinding.progressbar.visibility = View.GONE
                garageloginbinding.submitBTN.visibility = View.VISIBLE
            }
        }
    }

}