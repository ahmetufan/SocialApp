package com.ahmet.socialapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth:FirebaseAuth
    private lateinit var refUsers:DatabaseReference
    private var firebaseUserId:String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


     /*   val toolbar:Toolbar=findViewById(R.id.toolbar_register)
        supportActionBar!!.title="Register"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {

            val intent= Intent (this@RegisterActivity,WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
      */
        mAuth= FirebaseAuth.getInstance()

        register_btn.setOnClickListener {
            registerUser()
        }


    }

    private fun registerUser() {
        val username:String=username_register.text.toString()
        val email:String=email_register.text.toString()
        val password:String=password_register.text.toString()

        if (username == "" || email == "" || password == "") {
            Toast.makeText(this@RegisterActivity, "Please write", Toast.LENGTH_SHORT).show()
        } else {

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    firebaseUserId=mAuth.currentUser!!.uid
                    refUsers=FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserId)

                    val userHashMap= HashMap<String,Any>()
                    userHashMap["uid"]=firebaseUserId
                    userHashMap["username"]=username
                    userHashMap["profile"]="https://firebasestorage.googleapis.com/v0/b/socialapp-43664.appspot.com/o/profil.png?alt=media&token=8c2bc2bc-04e8-4693-b46c-3b91c07f6a49"
                    userHashMap["cover"]="https://firebasestorage.googleapis.com/v0/b/socialapp-43664.appspot.com/o/cover.jpg?alt=media&token=f55953ce-1c73-4d66-a752-9a6510c1183a"
                    userHashMap["status"]="offline"
                    userHashMap["search"]=username.toLowerCase()
                    userHashMap["facebook"]="https://www.m.facebook.com"
                    userHashMap["instagram"]="https://www.m.instagram.com"
                    userHashMap["website"]="www.google.com"

                    refUsers.updateChildren(userHashMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent=Intent(this@RegisterActivity,MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                    }

                } else {
                    Toast.makeText(this@RegisterActivity, "Error "+task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}