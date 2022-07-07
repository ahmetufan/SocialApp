package com.ahmet.socialapp.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmet.socialapp.ModelClasses.Post
import com.ahmet.socialapp.R
import com.ahmet.socialapp.adapters.RecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var postArrayList: ArrayList<Post>
    private lateinit var adaptery: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        db = Firebase.firestore

        postArrayList = ArrayList<Post>()

        getData()

        recyclerViewHome.layoutManager = LinearLayoutManager(requireContext())
        adaptery = RecyclerAdapter(postArrayList)
        recyclerViewHome.adapter = adaptery

    }


    private fun getData() {

        //.ordeyby Query.Direction.Descending tarihe göre sıralar
        db.collection("Posts").orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->

                if (error != null) {
                    Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (value != null) {
                        if (!value.isEmpty) {

                            val documents = value.documents


                            //For loop'una almadan önce listeyi temizlemek bir kere yüklenen fotoları iki kere göstermemesi için
                            postArrayList.clear()

                            for (document in documents) {
                                //casting
                                val comment = document.get("comment") as String
                                val useremail = document.get("useremail") as String
                                val downloadurl = document.get("downloadurl") as String

                                // println(comment)

                                val post = Post(useremail, comment, downloadurl)

                                postArrayList.add(post)
                            }
                            adaptery.notifyDataSetChanged()
                        }
                    }
                }
            }
    }


}