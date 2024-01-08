package com.harshit.fixwheels.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.fixwheels.ExtraUtils
import com.harshit.fixwheels.R
import com.harshit.fixwheels.adapters.GarageAdapter
import com.harshit.fixwheels.databinding.FragmentHomeBinding
import com.harshit.fixwheels.model.GarageModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: GarageAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false)
        val query = FirebaseFirestore.getInstance().collection(ExtraUtils.Garages)
        val options = FirestoreRecyclerOptions.Builder<GarageModel>()
            .setQuery(query, GarageModel::class.java)
            .build()
        adapter = GarageAdapter(options,requireActivity())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.adapter = adapter
        adapter.startListening()
        return binding.root
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