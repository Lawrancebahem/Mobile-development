package com.example.truthtableapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.truthtableapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show()


        binding.subBtn.setOnClickListener{
            onSubmit()
        }
    }

    /**
     * Check answers when submit button is clicked
     */
    private fun onSubmit() {
        val fieldOne:Int = if (binding.fieldOne.text.toString().equals(getString(R.string.True), ignoreCase = true)) 1 else 0
        val fieldTwo:Int = if (binding.fieldTwo.text.toString().equals(getString(R.string.False), ignoreCase = true)) 1 else 0
        val fieldThree:Int = if (binding.fieldThree.text.toString().equals(getString(R.string.False), ignoreCase = true)) 1 else 0
        val fieldFour:Int = if (binding.fieldFour.text.toString().equals(getString(R.string.False), ignoreCase = true)) 1 else 0
        val total:Int = fieldOne + fieldTwo  + fieldThree + fieldFour
        showToastMessage(total)
    }

    /**
     * Show toast message if it's correct
     */
    private fun showToastMessage(total:Int) {
        Toast.makeText(this, "You have $total good answers" , Toast.LENGTH_SHORT).show()
    }


}