package com.harshit.fixwheels.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.fixwheels.ExtraUtils
import com.harshit.fixwheels.adapters.OnCartItemDeletedListener
import com.harshit.fixwheels.R
import com.harshit.fixwheels.adapters.MyCartAdapter
import com.harshit.fixwheels.databinding.ActivityMyCartBinding
import com.harshit.fixwheels.model.MyCartModel

class MyCartActivity : AppCompatActivity(), OnCartItemDeletedListener {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var cartAdapter: MyCartAdapter
    private lateinit var cartModelList: MutableList<MyCartModel>
    private lateinit var binding:ActivityMyCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_cart)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        binding.progressbar.visibility = View.VISIBLE
        binding.recyclerview.visibility = View.GONE
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        cartModelList = mutableListOf()
        cartAdapter = MyCartAdapter(this, cartModelList,this)
        binding.recyclerview.adapter = cartAdapter

        firestore.collection(ExtraUtils.CurrentUser).document(auth.currentUser!!.uid)
            .collection(ExtraUtils.AddToCart)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (documentSnapshot in task.result!!.documents) {
                        val documentId = documentSnapshot.id
                        val cartModel = documentSnapshot.toObject(MyCartModel::class.java)
                        cartModel!!.documentId = documentId
                        cartModelList.add(cartModel)
                        cartAdapter.notifyDataSetChanged()
                        binding.progressbar.visibility = View.GONE
                        binding.recyclerview.visibility = View.VISIBLE
                    }
                    calculateTotalAmount(cartModelList)
                }
            }

        binding.buyNow.setOnClickListener {
            val list: MutableList<MyCartModel> = cartModelList

            if (list.isNotEmpty()) {
                for (model in list) {
                    val cartMap = hashMapOf(
                        "productName" to model.productName,
                        "productPrice" to model.productPrice,
                        "currentDate" to model.currentDate,
                        "currentTime" to model.currentTime,
                        "totalQuantity" to model.totalQuantity,
                        "totalPrice" to model.totalPrice
                    )

                    firestore.collection(ExtraUtils.CurrentUser).document(auth.currentUser!!.uid)
                        .collection(ExtraUtils.MyOrder)
                        .add(cartMap)
                        .addOnCompleteListener {
                            Toast.makeText(this, "Your Order Has Been Placed", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            startActivity(Intent(this, OrderPlaced::class.java))
        }

    }

    private fun calculateTotalAmount(cartModelList: MutableList<MyCartModel>) {
        var totalAmount = 0L
        for (myCartModel in cartModelList) {
            totalAmount += myCartModel.totalPrice
        }
        binding.textView7.text = "Total Amount: $totalAmount"
    }

    override fun onCartItemDeleted() {
        calculateTotalAmount(cartModelList)
    }

}