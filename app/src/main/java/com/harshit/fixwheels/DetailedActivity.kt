package com.harshit.fixwheels

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.fixwheels.databinding.ActivityDetailedBinding
import com.harshit.fixwheels.model.ViewAllModel
import java.text.SimpleDateFormat
import java.util.Calendar

class DetailedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailedBinding
    private var totalQuantity: Long = 1L
    private var totalPrice: Long = 1L
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var viewAllModel: ViewAllModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detailed)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        if (intent.extras != null) {
            viewAllModel = ViewAllModel(
                intent.getStringExtra("name"),
                intent.getStringExtra("description"),
                intent.getStringExtra("rating"),
                intent.getStringExtra("image"),
                intent.getStringExtra("type"),
                intent.getLongExtra("price",0)
            )
        }

        if (viewAllModel != null) {
            Glide.with(applicationContext).load(viewAllModel?.img_url).into(binding.detailedImg)
            binding.detailesRating.text = viewAllModel?.rating
            binding.detailedDec.text = viewAllModel?.description
            binding.detailedPrice.text = "Price :Rs " + viewAllModel?.price + " "
            totalPrice = viewAllModel?.price?.times(totalQuantity) ?: 0
        }

        binding.addToCart.setOnClickListener {
            addToCart()
        }

        binding.addItem.setOnClickListener(View.OnClickListener {
            if (totalQuantity < 10) {
                totalQuantity++
                binding.quantity.text = totalQuantity.toString()
                totalPrice = viewAllModel?.price?.times(totalQuantity) ?: 0
            }
        })


        binding.removeItem.setOnClickListener {
            if (totalQuantity > 1) {
                totalQuantity--
                binding.quantity.text = totalQuantity.toString()
                totalPrice = viewAllModel?.price?.times(totalQuantity) ?: 0
            }
        }


    }

    private fun addToCart() {
        val calender = Calendar.getInstance()
        val currentDate = SimpleDateFormat("MM/dd/yyyy")
        val saveCurrentDate = currentDate.format(calender.time)

        val currentTime = SimpleDateFormat("HH:mm:ss a")
        val saveCurrentTime = currentTime.format(calender.time)

        val cartMap = hashMapOf(
            "productName" to viewAllModel?.name!!,
            "productPrice" to binding.detailedPrice.text.toString(),
            "currentDate" to saveCurrentDate,
            "currentTime" to saveCurrentTime,
            "totalQuantity" to binding.quantity.text.toString(),
            "totalPrice" to totalPrice
        )

        firestore.collection("CurrentUser").document(auth.currentUser!!.uid)
            .collection("AddToCart")
            .add(cartMap)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Added To Cart", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }


    }
}