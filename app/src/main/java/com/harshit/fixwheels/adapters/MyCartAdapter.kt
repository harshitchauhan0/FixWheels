package com.harshit.fixwheels.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.fixwheels.R
import com.harshit.fixwheels.model.MyCartModel

class MyCartAdapter(private val context: Context, private val cartModelList: MutableList<MyCartModel>) :
    RecyclerView.Adapter<MyCartAdapter.ViewHolder>() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.product_name)
        var price: TextView = itemView.findViewById(R.id.product_price)
        var date: TextView = itemView.findViewById(R.id.current_date)
        var time: TextView = itemView.findViewById(R.id.current_time)
        var quantity: TextView = itemView.findViewById(R.id.total_quantity)
        var totalPrice: TextView = itemView.findViewById(R.id.total_price)
        var deleteItem: ImageView = itemView.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.my_cart_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartModel = cartModelList[position]

        holder.name.text = cartModel.productName
        holder.price.text = cartModel.productPrice
        holder.date.text = cartModel.currentDate
        holder.time.text = cartModel.currentTime
        holder.quantity.text = cartModel.totalQuantity
        holder.totalPrice.text = cartModel.totalPrice.toString()

        holder.deleteItem.setOnClickListener {
            firestore.collection("CurrentUser").document(auth.currentUser!!.uid)
                .collection("AddToCart")
                .document(cartModel.documentId!!)
                .delete()
                .addOnCompleteListener { task: Task<Void?> ->
                    if (task.isSuccessful) {
                        cartModelList.toMutableList().removeAt(holder.adapterPosition)
                        notifyDataSetChanged()
                        Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error" + task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun getItemCount(): Int {
        return cartModelList.size
    }
}
