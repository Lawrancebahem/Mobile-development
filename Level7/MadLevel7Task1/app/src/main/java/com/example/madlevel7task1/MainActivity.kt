package com.example.madlevel7task1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.madlevel7task1.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->

            FirebaseFirestore.setLoggingEnabled(true)
            FirebaseApp.initializeApp(this)
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.createProfileFragment)
        }

        toggle()
    }


    fun toggle(){
        findNavController(R.id.nav_host_fragment_content_main).addOnDestinationChangedListener{_,destination,_->

            if (destination.id in arrayOf(R.id.createProfileFragment)){
                binding.fab.hide()
            }else{
                binding.fab.show()
            }
        }
    }
}
