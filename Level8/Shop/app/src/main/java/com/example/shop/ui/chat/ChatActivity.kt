package com.example.shop.ui.chat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shop.R
import com.example.shop.databinding.ActivityChatBinding
import com.example.shop.ui.profile.ProfileActivity
import com.example.shop.ui.camera.CameraActivity
import com.example.shop.ui.main.MainActivity

import com.example.shop.ui.notification.NotificationActivity

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


        configureBottomMenu()

        //set the second icon as checked
        binding.navigationBottom.menu.getItem(1).isChecked = true
    }


    private fun configureBottomMenu() {
        val bottomNavigationView = binding.navigationBottom
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeBtn -> {
                    val i = Intent(this@ChatActivity, MainActivity::class.java)
                    startActivity(i)
                }
                R.id.chatBtn -> {
                    navigateToDestination(ChatActivity())
                }
                R.id.camBtn -> {
                    navigateToDestination(CameraActivity()
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
            true
        }
    }



    private fun navigateToDestination(activity: Activity) {
        val actv = Intent(this@ChatActivity, activity::class.java)
        startActivity(actv)
    }
}