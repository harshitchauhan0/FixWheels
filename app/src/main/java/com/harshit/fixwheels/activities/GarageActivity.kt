package com.harshit.fixwheels.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.harshit.fixwheels.ExtraUtils
import com.harshit.fixwheels.R
import com.harshit.fixwheels.adapters.BookingAdapter
import com.harshit.fixwheels.databinding.ActivityGarageBinding
import com.harshit.fixwheels.model.BookingModel
import com.harshit.fixwheels.model.GarageModel
import java.text.SimpleDateFormat
import java.util.Calendar

class GarageActivity : AppCompatActivity() {
    private lateinit var binding:ActivityGarageBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var adapter: BookingAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var calender:Calendar
    private lateinit var dateFormat: SimpleDateFormat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_garage)
        binding.profileBTN.setOnClickListener {
            startActivity(Intent(this,GarageProfile::class.java))
        }
        calender = Calendar.getInstance()
        dateFormat = SimpleDateFormat("dd-MM-yyyy")
        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        getBookings()
    }

    private fun getBookings(){
        val curr = dateFormat.format(calender.time)
        val query: Query = database.collection(ExtraUtils.Booking).document(auth.uid!!).collection(curr)
            .orderBy("timestamp", Query.Direction.DESCENDING)

        val options = FirestoreRecyclerOptions.Builder<BookingModel>().setQuery(query, BookingModel::class.java).build()

        adapter = BookingAdapter(options, applicationContext)
        val manager = LinearLayoutManager(this)
        manager.reverseLayout = true
        binding.userRV.layoutManager = manager
        binding.userRV.adapter = adapter
        adapter.startListening()
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onResume() {
        super.onResume()
        adapter.startListening()
    }
}