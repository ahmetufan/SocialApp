package com.ahmet.messenger21

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    title = "Anasayfa"
                    navController.navigate(R.id.homeFragment)
                    true
                }

                R.id.usersFragment -> {
                    title = "Sohbet"
                    navController.navigate(R.id.usersFragment)
                    true
                }

                R.id.profileFragment -> {
                    title = "Profil"
                    navController.navigate(R.id.profileFragment)
                    true
                }

                R.id.uploadFragment -> {
                    title = "Yükle"
                    navController.navigate(R.id.uploadFragment)
                    true
                }

                else -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }


            }
        }


    }

}