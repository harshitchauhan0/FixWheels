package com.harshit.fixwheels.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.harshit.fixwheels.ExtraUtils
import com.harshit.fixwheels.R
import com.harshit.fixwheels.activities.UserDetail
import com.harshit.fixwheels.model.BookingModel

class BookingAdapter(options: FirestoreRecyclerOptions<BookingModel?>, var context: Context) :
    FirestoreRecyclerAdapter<BookingModel, BookingAdapter.BookingViewModel>(options) {
    override fun onBindViewHolder(
        holder: BookingViewModel,
        position: Int,
        model: BookingModel
    ) {
        holder.vehicle.text = model.vehicle
        holder.booking_desc.text = model.vehicle
        holder.itemView.setOnClickListener {
            val intent = Intent(context,UserDetail::class.java)
            intent.putExtra(ExtraUtils.Booking.substring(0,ExtraUtils.Booking.length-1),model)
            ContextCompat.startActivity(context,intent,null)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewModel {
        val view: View = LayoutInflater.from(context).inflate(R.layout.userforgarage_item, parent, false)
        return BookingViewModel(view)
    }

    inner class BookingViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var vehicle = itemView.findViewById<TextView>(R.id.booking_vehicle)
        var booking_desc = itemView.findViewById<TextView>(R.id.booking_desc)
    }
}