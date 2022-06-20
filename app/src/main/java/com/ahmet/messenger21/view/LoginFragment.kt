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
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {
    private  var auth: FirebaseAuth? = null
    private  var firebaseUser: FirebaseUser? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Giriş yapan kişinin bir daha giriş yapmaması için
        val currentUser = auth!!.currentUser

        if (currentUser != null) {

            findNavController().navigate(R.id.action_loginFragment_to_homeFragment2)
        }
        btnLogin.setOnClickListener {
            val email = etMailLogin.text.toString()
            val passsword = etPasswordLogin.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(passsword)) {
                Toast.makeText(requireContext(),"email ve şifreyi girin", Toast.LENGTH_SHORT)
                    .show()
            } else {

                auth!!.signInWithEmailAndPassword(email, passsword)
                    .addOnCompleteListener(requireActivity()) {
                        if (it.isSuccessful) {
                            etMailLogin.setText("")
                            etPasswordLogin.setText("")

                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment2)
                        } else {
                            Toast.makeText(requireContext(),"email ve şifre bulunamadı",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
        btnSignUpLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_kayitFragment)
        }
    }
    }


