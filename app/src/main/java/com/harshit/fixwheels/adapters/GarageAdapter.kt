package com.harshit.fixwheels.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harshit.fixwheels.model.GarageModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.harshit.fixwheels.activities.ProfileActivity
import com.harshit.fixwheels.R

class GarageAdapter(options: FirestoreRecyclerOptions<GarageModel>, context: Context):FirestoreRecyclerAdapter<GarageModel, GarageAdapter.MyViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GarageAdapter.MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.garage_item,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: GarageModel) {
        holder.name.text = model.name
        holder.rating.text = model.rating
        holder.status.text = model.description
        holder.address.text = model.address
        Glide.with(context).load(model.imageUri).into(holder.image)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra("id",model.id)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            ContextCompat.startActivity(context,intent,null)
        }
    }

    val context:Context
    init {
        this.context = context
    }
    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var name:TextView
        var rating:TextView
        var address:TextView
        var status:TextView
        var image:ImageView
        init {
            name = itemView.findViewById(R.id.name)
            rating= itemView.findViewById(R.id.rating)
            address = itemView.findViewById(R.id.address)
            status = itemView.findViewById(R.id.status)
            image = itemView.findViewById(R.id.profile_pic)

        }
    }

}