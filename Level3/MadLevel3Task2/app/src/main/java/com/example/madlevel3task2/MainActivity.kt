package com.example.madlevel3task2

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.madlevel3task2.databinding.ActivityMainBinding


const val KEY = "add_portal"
const val BUNDLE_KEY = "bundle_portal"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        binding.fab.setOnClickListener {
            navController.navigate(R.id.action_startFragment_to_portalFragment)
        }
        fabToggle()
    }

    /**
     * To toggle the fab
     * hide if it's in the portal fragment
     * show if it's in the start fragment
     */
    fun fabToggle() {
        findNavController(R.id.nav_host_fragment_content_main)
                .addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in arrayOf(R.id.portalFragment)){
                binding.fab.hide()
            }else{
                binding.fab.show()
            }
        }
    }
}