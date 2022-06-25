package com.ahmet.socialapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmet.socialapp.ModelClasses.Chat
import com.ahmet.socialapp.ModelClasses.Users
import com.ahmet.socialapp.adapters.ChatAdaptor
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    var firebaseUser: FirebaseUser? = null
    var referance: DatabaseReference? = null
    var chatList = ArrayList<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatRecyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val intent = intent
        val userId = intent.getStringExtra("userId")
        val userName = intent.getStringExtra("userName")

        imgBackChat.setOnClickListener {
            onBackPressed()
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser
        referance = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)

        referance!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.getValue(Users::class.java)
                tvUserName.text = user!!.getUserName()

                if (user.getProfile() == "") {
                    imgProfile.setImageResource(R.drawable.profile)
                } else {
                    Glide.with(this@ChatActivity).load(user.getProfile()).into(imgProfile)

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

                Toast.makeText(applicationContext, "Gönderilecek mesaj yok !", Toast.LENGTH_SHORT)
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

        var referance: DatabaseReference? = FirebaseDatabase.getInstance().getReference()

        var hashMap: HashMap<String, String> = HashMap()
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
                val chatAdapter = ChatAdaptor(this@ChatActivity, chatList)
                chatRecyclerview.adapter = chatAdapter
                chatAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}