package com.ahmet.socialapp.Fragments

import android.app.AlertDialog
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ahmet.socialapp.ModelClasses.Chat
import com.ahmet.socialapp.ModelClasses.Users
import com.ahmet.socialapp.R
import com.ahmet.socialapp.UploadActivity
import com.ahmet.socialapp.WelcomeActivity
import com.ahmet.socialapp.adapters.UserAdapter
import com.ahmet.socialapp.adapters.ViewPagerAdapterr
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    var refUsers: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)



        view_pager.adapter = ViewPagerAdapterr(requireActivity())

        TabLayoutMediator(tab_layout, view_pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Home"
                }
                1 -> {
                    tab.text = "Chats"
                }
                2 -> {
                    tab.text = "Search"
                }
                3 -> {
                    tab.text = "Settings"
                }
                else -> {
                    throw Resources.NotFoundException("Pozisyon Yok")
                }
            }
        }.attach()

        //Display username and profil picture
        refUsers!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user: Users? = snapshot.getValue(Users::class.java)

                    user_name.text = user!!.getUserName()

                    Glide.with(this@MainFragment).load(user.getProfile())
                        .placeholder(R.drawable.profile).into(profile_image)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        addImage.setOnClickListener {

            val intent = Intent(requireContext(), UploadActivity::class.java)
            startActivity(intent)
        }

        OutImg.setOnClickListener {

            val alert = AlertDialog.Builder(context)
            alert.setTitle("Çıkış Yap")
            alert.setMessage("Çıkış yapmak istiyor musun ?")

            alert.setPositiveButton("Evet") { dialog, which ->

                FirebaseAuth.getInstance().signOut()

                val intent = Intent(requireContext(), WelcomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
            alert.setNegativeButton("Hayır") { dialog, which ->
            }
            alert.show()
        }

    }
    private fun updateStatus(status:String) {

        val ref=FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)

        val hashMap=HashMap<String,Any> ()
        hashMap["status"]=status
        ref!!.updateChildren(hashMap)
    }

    override fun onResume() {
        super.onResume()

        updateStatus("online")
    }

    override fun onPause() {
        super.onPause()

        updateStatus("offline")
    }
}
