package com.harshit.fixwheels.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harshit.fixwheels.activities.DetailedActivity
import com.harshit.fixwheels.R
import com.harshit.fixwheels.model.ViewAllModel

class ViewAllAdapter(var context: Context, list: MutableList<ViewAllModel>) : RecyclerView.Adapter<ViewAllAdapter.ViewHolder>() {
    private var list: MutableList<ViewAllModel>

    init {
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.shopping_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(list[position].img_url).into(holder.imageView)
        holder.name.text = list[position].name
        holder.description.text = list[position].description
        holder.rating.text = list[position].rating
        holder.price.text = list[position].price.toString()
        holder.itemView.setOnClickListener{
            val intent = Intent(context, DetailedActivity::class.java)
            intent.putExtra("name", list[position].name)
            intent.putExtra("description",list[position].description)
            intent.putExtra("rating",list[position].rating)
            intent.putExtra("price",list[position].price)
            intent.putExtra("image",list[position].img_url)
            intent.putExtra("type",list[position].type)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var name: TextView
        var description: TextView
        var price: TextView
        var rating: TextView

        init {
            imageView = itemView.findViewById<ImageView>(R.id.view_img)
            name = itemView.findViewById<TextView>(R.id.view_name)
            description = itemView.findViewById<TextView>(R.id.view_description)
            rating = itemView.findViewById<TextView>(R.id.view_rating)
            price = itemView.findViewById<TextView>(R.id.view_price)
        }
    }
}