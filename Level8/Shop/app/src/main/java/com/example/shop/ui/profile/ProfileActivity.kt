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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    lateinit var adapter: ViewPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setUpTaps()
    }


    /**
     * add adapter to the viewPager
     */
    private fun setUpTaps() {
        adapter = ViewPageAdapter(supportFragmentManager)
        adapter.addFragment(ProfileFragment(), getString(R.string.myProf))
        adapter.addFragment(MyAdvertisementFragment(), getString(R.string.myAdvert))
        adapter.addFragment(FavouritesFragment(), getString(R.string.myFav))
        binding.viewPager.adapter = adapter

        binding.tabs.setupWithViewPager(binding.viewPager)
        configureBottomMenu()

        //set the fifth icon as checked
        binding.navigationBottom.menu.getItem(4).isChecked = true
    }


    /**
     * settings for the bottom menu
     */
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

    /**
     * helper method to navigate to a specific activity
     */
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