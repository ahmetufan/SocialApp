package com.ahmet.socialapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmet.socialapp.ModelClasses.Chatlist
import com.ahmet.socialapp.ModelClasses.Users
import com.ahmet.socialapp.R
import com.ahmet.socialapp.adapters.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatsFragment : Fragment() {

    private var userAdapter:UserAdapter? = null
    private var mUsers: List<Users>? = null
    private var usersChatList: List<Chatlist>? = null
    lateinit var recycler_view_chatlist:RecyclerView
    private var firebaseUser:FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_chats, container, false)

        recycler_view_chatlist=view.findViewById(R.id.recycler_view_chatlist)
        recycler_view_chatlist.setHasFixedSize(true)
        recycler_view_chatlist.layoutManager=LinearLayoutManager(context)

        firebaseUser=FirebaseAuth.getInstance().currentUser

        usersChatList=ArrayList()

        val ref= FirebaseDatabase.getInstance().reference.child("ChatList").child(firebaseUser!!.uid)
        ref!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                (usersChatList as ArrayList).clear()

                for (dataSnapshot in p0.children) {

                    val chatlist=dataSnapshot.getValue(Chatlist::class.java)

                    (usersChatList as ArrayList).add(chatlist!!)
                }
                retrieveChatList()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        return view
    }

    private fun retrieveChatList() {

        mUsers=ArrayList()

        val ref=FirebaseDatabase.getInstance().reference.child("Users")
        ref!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                (mUsers as ArrayList).clear()

                for (dataSnapShot in p0.children) {

                    val user=dataSnapShot.getValue(Users::class.java)

                    for (eachChatList in usersChatList!!) {

                        if (user!!.getUID().equals(eachChatList.getId())) {

                            (mUsers as ArrayList).add(user!!)
                        }
                    }
                }
                if (activity != null) {

                    userAdapter= UserAdapter(context!!,(mUsers as ArrayList<Users>),true)
                    recycler_view_chatlist.adapter=userAdapter
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}