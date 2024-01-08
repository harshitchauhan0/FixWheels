package com.harshit.fixwheels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.fixwheels.adapters.ViewAllAdapter
import com.harshit.fixwheels.databinding.FragmentShopBinding
import com.harshit.fixwheels.model.ViewAllModel

class ShopFragment : Fragment() {
    private lateinit var binding:FragmentShopBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var adapter: ViewAllAdapter
    private lateinit var viewAllModelList: MutableList<ViewAllModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_shop, container, false)
        database = FirebaseFirestore.getInstance()
        binding.progressbar.visibility = View.VISIBLE
        binding.viewAllRec.layoutManager = LinearLayoutManager(activity)
        viewAllModelList = mutableListOf()
        adapter = ViewAllAdapter(requireActivity(), viewAllModelList)
        binding.viewAllRec.adapter = adapter

        database.collection(ExtraUtils.Products).get().addOnCompleteListener {
            if(it.isSuccessful){
                for(documentSnapshot in it.result.documents) {
                    val viewAllModel: ViewAllModel? =
                        documentSnapshot.toObject(ViewAllModel::class.java)
                    if (viewAllModel != null) {
                        viewAllModelList.add(viewAllModel)
                    }
                    adapter.notifyDataSetChanged()
                    binding.progressbar.visibility = View.GONE
                    binding.viewAllRec.visibility = View.VISIBLE
                }
            }
            else{
                Toast.makeText(requireActivity(),it.exception.toString(),Toast.LENGTH_SHORT).show()
            }
        }


        return binding.root
    }

}