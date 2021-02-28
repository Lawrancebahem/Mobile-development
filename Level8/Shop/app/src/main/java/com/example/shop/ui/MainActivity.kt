package com.example.shop.ui

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplicationtest2.DAO.UserRepository
import com.example.shop.R
import com.example.shop.databinding.ActivityMainBinding

import com.example.shop.viewModel.LoginViewModel
import com.example.shop.viewModel.NotLoggedInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val loginViewModel: LoginViewModel by viewModels()
    private val notLoggedInViewModel: NotLoggedInViewModel by viewModels()
    private lateinit var userDatabaseViewModel: UserDatabaseViewModel
    private lateinit var userRepository: UserRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController)

        userDatabaseViewModel = ViewModelProvider(this).get(UserDatabaseViewModel::class.java)
        userRepository = userDatabaseViewModel.userRepository
        toggleMenu()
    }

    companion object {

        const val COLOR_BLUE = "#FF29B6F6"
        const val COLOR_GRAY = "#FFBDBDBD"
    }


    fun toggleMenu() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.menu.isVisible =
                !(destination.id in arrayOf(R.id.loginFragment) || destination.id in arrayOf(R.id.registerFragment) ||  destination.id in arrayOf(R.id.cameraFragment))
        }
        binding.homeBtn.setOnClickListener(this)
        binding.chatBtn.setOnClickListener(this)
        binding.camBtn.setOnClickListener(this)
        binding.notfBtn.setOnClickListener(this)
        binding.profBtn.setOnClickListener(this)
    }


    /**
     * To navigate to notLogged in page and show the appropriate message
     */
    private fun navigateToNotLoggedIn(info: String, imageName: String) {
        notLoggedInViewModel._info.value = info
        notLoggedInViewModel._imageName.value = imageName
        navController.navigate(R.id.notLoggedIn)
    }

    private fun navigateToDestination(id: Int, info: String, imageName: String) {

        CoroutineScope(Dispatchers.Main).launch {
            val user = withContext(Dispatchers.IO){
                userRepository.getUser()
            }
            if (user.isNotEmpty()) {
                navController.navigate(id)
            } else {
                navigateToNotLoggedIn(info, imageName)
            }
        }
    }

    /**
     * To change the selected item's color of the menu
     */
    @SuppressLint("UseCompatTextViewDrawableApis")
    override fun onClick(v: View?) {
        resetMenuButtonsColor()
        return when (v?.id) {
            R.id.homeBtn -> {
                navController.navigate(R.id.homeFragment)
                setButtonColor(binding.homeBtn, COLOR_BLUE)
            }
            R.id.chatBtn -> {
                navigateToNotLoggedIn("Please log in to be able to see your chats", "chat")
                setButtonColor(binding.chatBtn, COLOR_BLUE)
            }
            R.id.camBtn -> {
                navigateToDestination(R.id.cameraFragment,"Please log in to be able to offer your items", "camera")
                setButtonColor(binding.camBtn, COLOR_BLUE)
            }
            R.id.notfBtn -> {
                navigateToNotLoggedIn("Please log in to be able to see your notification", "notification")
                setButtonColor(binding.notfBtn, COLOR_BLUE)
            }
            else -> {
                navigateToNotLoggedIn("Please log in to be able to see your profile", "profile")
                setButtonColor(binding.profBtn, COLOR_BLUE)
            }
        }
    }

    /**
     * To reset the color of the selected menu item
     */
    private fun resetMenuButtonsColor() {
        setButtonColor(binding.homeBtn, COLOR_GRAY)
        setButtonColor(binding.chatBtn, COLOR_GRAY)
        setButtonColor(binding.camBtn, COLOR_GRAY)
        setButtonColor(binding.notfBtn, COLOR_GRAY)
        setButtonColor(binding.profBtn, COLOR_GRAY)
    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    private fun setButtonColor(button: Button, color: String) {
        button.compoundDrawableTintList = ColorStateList.valueOf(Color.parseColor(color))
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}