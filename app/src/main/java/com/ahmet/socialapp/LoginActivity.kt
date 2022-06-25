package com.ahmet.socialapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserId:String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


     /*   val toolbar: Toolbar =findViewById(R.id.toolbar_login)
        supportActionBar!!.title="Login"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {

            val intent= Intent (this@LoginActivity,WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
      */
        mAuth=FirebaseAuth.getInstance()

        login_btn.setOnClickListener {
            loginUser()
        }


    }

    private fun loginUser() {
        val email:String=email_login.text.toString()
        val password:String=password_login.text.toString()

        if (email == "" || password == "") {
            Toast.makeText(this@LoginActivity, "Please write", Toast.LENGTH_SHORT).show()
        } else {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val intent=Intent(this@LoginActivity,MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(this@LoginActivity, "Error "+task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
}