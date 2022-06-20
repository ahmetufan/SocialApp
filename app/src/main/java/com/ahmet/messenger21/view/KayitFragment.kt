package com.ahmet.messenger21.view

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.ahmet.messenger21.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_kayit.*

class KayitFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReferance: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kayit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSignUp.setOnClickListener {
            val userName=etName.text.toString()
            val email=etMail.text.toString()
            val password=etPassword.text.toString()
            val confirmPassword=etConfirmPassword.text.toString()

            if (TextUtils.isEmpty(userName)) {
                Toast.makeText(requireContext(), "username boş", Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(requireContext(), "username boş", Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(requireContext(), "username boş", Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(requireContext(), "username boş", Toast.LENGTH_SHORT).show()
            }
            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "şifreler aynı değil", Toast.LENGTH_SHORT).show()
            }
            registrUser(userName, email, password)
        }



    }
    private fun registrUser(userName:String,email:String,password:String) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(requireActivity(), ) {
                if (it.isSuccessful) {
                    var user: FirebaseUser?=auth.currentUser
                    var userId:String=user!!.uid

                    databaseReferance= FirebaseDatabase.getInstance().getReference("Users").child(userId)

                    var hashMap:HashMap<String,String> = HashMap()
                    hashMap.put("userId",userId)
                    hashMap.put("userName",userName)
                    hashMap.put("profilImage","")

                    databaseReferance.setValue(hashMap).addOnCompleteListener(requireActivity(), ) {
                        if (it.isSuccessful) {
                            // open activity
                            findNavController().navigate(R.id.action_kayitFragment_to_loginFragment)
                        }
                    }
                }
            }
    }
}