package com.example.shop.ui.notification

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shop.R
import com.example.shop.databinding.ActivityNotificationBinding
import com.example.shop.ui.profile.ProfileActivity
import com.example.shop.ui.camera.CameraActivity
import com.example.shop.ui.chat.ChatActivity
import com.example.shop.ui.main.MainActivity

class NotificationActivity : AppCompatActivity() {


    private lateinit var binding:ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureBottomMenu()

        //set the fourth icon as checked
        binding.navigationBottom.menu.getItem(3).isChecked = true
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
                    navigateToDestination(
                        CameraActivity()
                    )
                }
                R.id.notfBtn -> {
                    navigateToDestination(NotificationActivity()
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