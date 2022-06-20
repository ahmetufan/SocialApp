package com.ahmet.messenger21.view

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmet.messenger21.R
import com.ahmet.messenger21.adaptor.UserAdaptor
import com.ahmet.messenger21.model.User
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_users.*


class UsersFragment : Fragment() {
    private lateinit var userAdapter: UserAdaptor
    var userList=ArrayList<User>()
    private lateinit var auth: FirebaseAuth
    private lateinit var  databaseReferance: DatabaseReference
    private lateinit var  firabase: FirebaseUser



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        userRecyclerview.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)



        imgProfile2.setOnClickListener {

            findNavController().navigate(R.id.action_usersFragment_to_profileFragment2)
        }
        getUsersList()

        signout.setOnClickListener {

            val alert=AlertDialog.Builder(requireContext())
            alert.setTitle("Çıkış Yap")
            alert.setMessage("Çıkış yapmak istiyor musun ?")
            alert.setPositiveButton("Evet"){dialog, which ->
                auth.signOut()
                findNavController().navigate(R.id.action_usersFragment_to_loginFragment)
            }
            alert.setNegativeButton("Hayır"){dialog,which ->
            }
            alert.show()
        }


    }
    fun getUsersList() {

        firabase=FirebaseAuth.getInstance().currentUser!!


        databaseReferance= FirebaseDatabase.getInstance().getReference("Users")

        databaseReferance.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                val currentUser=snapshot.getValue(User::class.java)

                if (currentUser!!.profilImage == 0) {

                    imgProfile2.setImageResource(R.drawable.cennet)
                } else {
                    Glide.with(this@UsersFragment).load(currentUser.profilImage).into(imgProfile2)
                }

                for (datasnapShot: DataSnapshot in snapshot.children) {
                    val user=datasnapShot.getValue(User::class.java)

                    if (!user!!.userId.equals(firabase.uid)) {
                        userList.add(user!!)
                    }
                }
                userAdapter= UserAdaptor(requireContext(),userList)
                userRecyclerview.adapter=userAdapter
                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }



}