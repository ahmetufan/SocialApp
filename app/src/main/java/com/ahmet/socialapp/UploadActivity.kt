package com.ahmet.socialapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_upload.*
import java.util.*

class UploadActivity : AppCompatActivity() {
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permisionLauncher: ActivityResultLauncher<String>
    var selectedPicture: Uri? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        registerLauncher()

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage

        imageView.setOnClickListener {


            if (ContextCompat.checkSelfPermission(
                    this@UploadActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this@UploadActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    Snackbar.make(it, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Give Permission") {
                            //Request Permission
                            permisionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }.show()
                } else {
                    //Request Permision
                    permisionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            } else {
                val intentToGalery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                //start activity for result
                activityResultLauncher.launch(intentToGalery)
            }

        }

        btnupload.setOnClickListener {

            val uuid = UUID.randomUUID()
            val imageName = "$uuid.jpg"

            val referance = storage.reference
            val imageReferance = referance.child("images").child(imageName)

            if (selectedPicture != null) {
                imageReferance.putFile(selectedPicture!!).addOnSuccessListener {
                    //Download url -> Firestore
                    val uploadPictureReferance = storage.reference.child("images").child(imageName)
                    uploadPictureReferance.downloadUrl.addOnSuccessListener {
                        val downloadUrl = it.toString()


                        if (auth.currentUser != null) {

                            val postMap = hashMapOf<String, Any>()
                            postMap.put("downloadurl", downloadUrl)
                            postMap.put("useremail", auth.currentUser!!.email.toString())
                            postMap.put("comment", commendText.text.toString())
                            postMap.put("date", Timestamp.now())

                            firestore.collection("Posts").add(postMap).addOnSuccessListener {
                                //Depolama ba??ar??l?? olursa ne olaca????

                                onBackPressed()

                            }.addOnFailureListener {
                                Toast.makeText(
                                    applicationContext,
                                    it.localizedMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                }.addOnFailureListener {
                    Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
    private fun registerLauncher() {

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        selectedPicture = intentFromResult.data
                        selectedPicture?.let {
                            imageView.setImageURI(it)
                        }
                    }
                }
            }

        permisionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { resut ->
                if (resut) {
                    // ??zin verildi
                    val intentToGalery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGalery)
                } else {
                    // ??zin verilmedi
                    Toast.makeText(applicationContext, "??zin verilmedi", Toast.LENGTH_SHORT).show()
                }
            }
    }

}