package com.ahmet.messenger21.view

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.ahmet.messenger21.R
import com.ahmet.messenger21.model.User
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.IOException
import java.util.*


class ProfileFragment : Fragment() {
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var databaseReferance: DatabaseReference
    private lateinit var etuserName:EditText


    private var filePath: Uri? = null
    private val PICK_IMAGE_REQUEST: Int = 2020

    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etuserName=view.findViewById(R.id.etuserName)


        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        databaseReferance =
            FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        databaseReferance.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.getValue(User::class.java)
                etuserName.setText(user!!.userName)


                if (user!!.profilImage == 0) {
                    userImage.setImageResource(R.drawable.profil)
                } else {
                    Glide.with(this@ProfileFragment).load(user.profilImage).into(userImage)
                }

                imgProfile


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),error.message, Toast.LENGTH_SHORT).show()
            }
        })


        userImage.setOnClickListener {
            chooseImage()
        }
        btnSave.setOnClickListener {
            uploadImage()
            progressBar.visibility = View.VISIBLE
        }
    }
    private fun chooseImage() {

        val intent: Intent = Intent()
        intent.type = "Image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode != null) {
            filePath = data!!.data

            try {
                var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, filePath)
                userImage.setImageBitmap(bitmap)
                btnSave.visibility = View.VISIBLE

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    private fun uploadImage() {
        if (filePath != null) {


            var ref: StorageReference = storageRef.child("Image/" + UUID.randomUUID().toString())
            ref.putFile(filePath!!)
                .addOnSuccessListener {

                    val hashmap: HashMap<String, String> = HashMap()
                    hashmap.put("userName", etuserName.text.toString())
                    hashmap.put("profilImage",filePath.toString())

                    databaseReferance.updateChildren(hashmap as Map<String, Any>)

                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Yükleniyor", Toast.LENGTH_SHORT).show()
                    btnSave.visibility = View.GONE
                }

                .addOnFailureListener {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Başarısız " + it.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()

                }


        }
    }



}