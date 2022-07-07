package com.ahmet.socialapp.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmet.socialapp.adapters.UserAdapter
import com.ahmet.socialapp.ModelClasses.Users
import com.ahmet.socialapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchFragment : Fragment() {
    private var userAdapter: UserAdapter? = null
    private var mUsers: List<Users>? = null
    private var recyclerView:RecyclerView?=null
    private var seacrhEditText:EditText?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_search, container, false)


        recyclerView=view.findViewById(R.id.searchList)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager=LinearLayoutManager(context)
        seacrhEditText=view.findViewById(R.id.searchUsersET)

        mUsers = ArrayList()
        retrieveAllUsers()

        seacrhEditText!!.addTextChangedListener(object :TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(cs: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchForUsers(cs.toString().toLowerCase())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })


        return view
    }

    private fun retrieveAllUsers() {

        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid

        val refUsers = FirebaseDatabase.getInstance().reference.child("Users")

        refUsers.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                (mUsers as ArrayList<Users>).clear()
                if (seacrhEditText!!.text.toString() == "") {

                    for (snapshot2 in snapshot.children) {

                        val user: Users? = snapshot2.getValue(Users::class.java)
                        if (!(user!!.getUID()).equals(firebaseUserID)) {

                            (mUsers as ArrayList<Users>).add(user)
                        }
                    }
                    userAdapter = UserAdapter(context!!, mUsers!!, false)
                    recyclerView!!.adapter=userAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun searchForUsers(str: String) {

        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid

        val queryUsers = FirebaseDatabase.getInstance().reference.child("Users")
            .orderByChild("search").startAt(str).endAt(str + "\uf8ff")

        queryUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()

                for (snapshot2 in p0.children) {

                    val user: Users? = snapshot2.getValue(Users::class.java)
                    if ((user!!.getUID()) != firebaseUserID) {

                        (mUsers as ArrayList<Users>).add(user)
                    }
                }
                userAdapter = UserAdapter(context!!, mUsers!!, false)
                recyclerView!!.adapter=userAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }


}