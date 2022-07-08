package com.ahmet.socialapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toolbar
import com.ahmet.socialapp.Fragments.MainFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserId:String =""

    var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        mAuth=FirebaseAuth.getInstance()

        login_btn.setOnClickListener {
            loginUser()
        }

        kayit_image.setOnClickListener {

            val intent=Intent (this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }


    }

    private fun loginUser() {
        val email:String=email_login.text.toString()
        val password:String=password_login.text.toString()

        if (email == "" || password == "") {
            Toast.makeText(this@LoginActivity, "Lütfen boş bırakmayınız !", Toast.LENGTH_SHORT).show()
        } else {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val intent=Intent(this@LoginActivity,MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(this@LoginActivity, "Hata "+task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser != null) {
            val intent=Intent (this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}