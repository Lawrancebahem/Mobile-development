package com.example.shop.ui.camera

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.shop.R
import com.example.shop.databinding.ActivityCameraBinding
import com.example.shop.ui.chat.ChatActivity
import com.example.shop.ui.profile.ProfileActivity
import com.example.shop.ui.main.MainActivity
import com.example.shop.ui.notification.NotificationActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CameraActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_camera)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


        configureBottomMenu()


        //set the third icon as checked
        binding.navigationBottom.menu.getItem(2).isChecked = true

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_camera)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun configureBottomMenu() {
        val bottomNavigationView = binding.navigationBottom
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeBtn -> {
                    val i = Intent(applicationContext, MainActivity::class.java)
                    startActivity(i)
                    overridePendingTransition(0,0)
                    finish()
                }
                R.id.chatBtn -> {
                    navigateToDestination(ChatActivity())
                }
                R.id.camBtn -> {
                    navigateToDestination(CameraActivity()
                    )
                }
                R.id.notfBtn -> {
                    navigateToDestination(
                        NotificationActivity()
                    )
                }
                else -> {
                    navigateToDestination(ProfileActivity())
                }
            }
            finish()
            true
        }
    }


    private fun navigateToDestination(activity: Activity) {
        val actv = Intent(applicationContext, activity::class.java)
        startActivity(actv)
        overridePendingTransition(0,0)
    }
}