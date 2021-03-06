package com.example.shop.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu

import androidx.appcompat.app.AppCompatActivity
import com.example.shop.R
import com.example.shop.databinding.ActivityProfileBinding
import com.example.shop.ui.camera.CameraActivity
import com.example.shop.ui.chat.ChatActivity
import com.example.shop.ui.main.MainActivity
import com.example.shop.ui.notification.NotificationActivity
import com.example.shop.ui.profile.adapter.ViewPageAdapter
import com.example.shop.ui.profile.fragments.FavouritesFragment
import com.example.shop.ui.profile.fragments.MyAdvertisementFragment
import com.example.shop.ui.profile.fragments.ProfileFragment

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setUpTaps()
    }

    private fun setUpTaps() {
        val adapter = ViewPageAdapter(supportFragmentManager)
        adapter.addFragment(ProfileFragment(), "My Profile")
        adapter.addFragment(MyAdvertisementFragment(), "My Advertisement")
        adapter.addFragment(FavouritesFragment(), "My Favourites")
        binding.viewPager.adapter = adapter

        binding.tabs.setupWithViewPager(binding.viewPager)
        configureBottomMenu()


        //set the fifth icon as checked
        binding.navigationBottom.menu.getItem(4).isChecked = true
    }


    private fun configureBottomMenu() {
        val bottomNavigationView = binding.navigationBottom
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeBtn -> {
                    val i = Intent(this@ProfileActivity, MainActivity::class.java)
                    startActivity(i)
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
                    navigateToDestination(
                        NotificationActivity()
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
        val actv = Intent(this@ProfileActivity, activity::class.java)
        startActivity(actv)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.profile_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}