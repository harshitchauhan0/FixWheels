package com.harshit.fixwheels

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.harshit.fixwheels.activities.LoginActivity

class LogoutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AlertDialog.Builder(activity).setTitle("LOG OUT")
            .setIcon(com.google.android.gms.base.R.drawable.common_full_open_on_phone)
            .setMessage("Do You Want to Log Out?")
                .setPositiveButton("Yes") { _, _ ->
                Toast.makeText(context, "You are logged out", Toast.LENGTH_LONG).show()
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(context, LoginActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                }
            .setNegativeButton("No"){ dialog, _ ->
                dialog.dismiss()
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container,HomeFragment()).commit()
            }.create().show()
        return inflater.inflate(R.layout.fragment_logout, container, false)
    }

}