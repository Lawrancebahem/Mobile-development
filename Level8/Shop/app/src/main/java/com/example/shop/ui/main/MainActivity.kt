package com.example.shop.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplicationtest2.DAO.UserRepository
import com.example.shop.R
import com.example.shop.databinding.ActivityMainBinding
import com.example.shop.model.User
import com.example.shop.ui.camera.CameraActivity
import com.example.shop.ui.chat.ChatActivity
import com.example.shop.ui.main.viewModel.NotLoggedInViewModel
import com.example.shop.ui.main.viewModel.UserDatabaseViewModel
import com.example.shop.ui.notification.NotificationActivity
import com.example.shop.ui.profile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val notLoggedInViewModel: NotLoggedInViewModel by viewModels()
    private lateinit var userDatabaseViewModel: UserDatabaseViewModel
    private lateinit var userRepository: UserRepository
    private lateinit var currentUser:LiveData<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController)

        userDatabaseViewModel = ViewModelProvider(this).get(UserDatabaseViewModel::class.java)
        currentUser = userDatabaseViewModel.userRepository.getUser()
        userRepository = userDatabaseViewModel.userRepository
        toggleMenu()
        configureBottomMenu()

        //set the first icon as checked
        binding.navigationBottom.menu.getItem(0).isChecked = true
    }


    /**
     * To navigate to the appropriate destination when the user clicks on a button
     */
    private fun configureBottomMenu() {
        val bottomNavigationView = binding.navigationBottom

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeBtn -> {
                    val i = Intent(this@MainActivity, MainActivity::class.java)
                    startActivity(i)
                }
                R.id.chatBtn -> {
                    navigateToDestination(
                        ChatActivity(),
                        "Please log in to be able to see your chats",
                        "chat"
                    )
                }
                R.id.camBtn -> {
                    navigateToDestination(
                        CameraActivity(),
                        "Please log in to be able to offer your items",
                        "camera"
                    )
                }
                R.id.notfBtn -> {
                    navigateToDestination(
                        NotificationActivity(),
                        "Please log in to be able to see your notification",
                        "notification"
                    )
                }
                else -> {
                    navigateToDestination(
                        ProfileActivity(),
                        "Please log in to be able to see your profile",
                        "profile"
                    )
                }
            }
            true
        }
    }

    fun toggleMenu() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.navigationBottom.isVisible =
                !(destination.id in arrayOf(R.id.loginFragment) || destination.id in arrayOf(R.id.registerFragment) || destination.id in arrayOf(
                    R.id.cameraFragment
                ))
        }
    }

    /**
     * To navigate to notLogged in page and show the appropriate message
     */
    private fun navigateToNotLoggedIn(info: String, imageName: String) {
        notLoggedInViewModel._info.value = info
        notLoggedInViewModel._imageName.value = imageName
        navController.navigate(R.id.notLoggedIn)
    }

    private fun navigateToDestination(activity: Activity, info: String, imageName: String) {
        val actv = Intent(this@MainActivity, activity::class.java)
        currentUser.observe(this@MainActivity, Observer { it ->
            if (it != null) {
                startActivity(actv)
            } else {
                navigateToNotLoggedIn(info, imageName)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}