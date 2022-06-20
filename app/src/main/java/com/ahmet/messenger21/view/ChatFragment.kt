package com.ahmet.messenger21.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmet.messenger21.R
import com.ahmet.messenger21.adaptor.ChatAdaptor
import com.ahmet.messenger21.model.Chat
import com.ahmet.messenger21.model.User
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment() {
    var firebaseUser: FirebaseUser? = null
    var referance: DatabaseReference? = null
    var chatList = ArrayList<Chat>()
    var userId = ""
    private lateinit var chatAdapter:ChatAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        arguments?.let {
            userId = ChatFragmentArgs.fromBundle(it).userId
            val userName = ChatFragmentArgs.fromBundle(it).userName
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser
        referance = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)


        referance!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.getValue(User::class.java)
                tvUserName.text.toString()
                tvUserName.text = user!!.userName

                if (user.profilImage == 0) {
                    imgProfile.setImageResource(R.drawable.profil)
                } else {
                    Glide.with(this@ChatFragment).load(user.profilImage).into(imgProfile)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        btnSendMessage.setOnClickListener {
            val message: String = etMessage.text.toString()
            btnSendMessage.setBackgroundResource(R.drawable.buttonarkaplan2)

            if (message.isEmpty()) {
                Toast.makeText(requireContext(), "Gönderilecek mesaj yok !", Toast.LENGTH_SHORT)
                    .show()
                etMessage.setText("")
            } else {
                btnSendMessage.setBackgroundResource(R.drawable.buttonarkaplan)
                sendMessage(firebaseUser!!.uid, userId, message)
                etMessage.setText("")
            }
        }


        readMessage(firebaseUser!!.uid, userId)
    }
    private fun sendMessage(senderId: String, receiverId: String, message: String) {

        val referance: DatabaseReference = FirebaseDatabase.getInstance().getReference()

        val hashMap: HashMap<String, String> = HashMap()
        hashMap.put("sendeId", senderId)
        hashMap.put("receiverId", receiverId)
        hashMap.put("message", message)

        referance!!.child("Chat").push().setValue(hashMap)
    }

    fun readMessage(senderId: String, receiverId: String) {

        val databaseReferance: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Chat")

        databaseReferance.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (datasnapShot: DataSnapshot in snapshot.children) {
                    val chat = datasnapShot.getValue(Chat::class.java)

                    if (chat!!.sendeId.equals(senderId) && chat!!.receiverId.equals(receiverId) ||
                        chat!!.sendeId.equals(receiverId) && chat!!.receiverId.equals(senderId)
                    ) {
                        chatList.add(chat)
                    }
                }
                chatAdapter = ChatAdaptor(requireContext(), chatList)
                chatRecyclerview.adapter = chatAdapter
              //  chatAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


}
