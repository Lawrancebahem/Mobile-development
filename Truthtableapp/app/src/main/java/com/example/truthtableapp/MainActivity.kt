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

    private fun onSubmit() {
        var fieldOne = binding.fieldOne.text.toString()
        var fieldTwo = binding.fieldTwo.text.toString()
        var fieldThree = binding.fieldThree.text.toString()
        var fieldFour = binding.fieldFour.text.toString()
        if (fieldOne == getString(R.string.True) && fieldTwo == getString(R.string.False) && fieldThree == getString(R.string.False)
            && fieldFour == getString(R.string.False) ) {
            onCorrectAnswer()
        } else {
            onInCorrectAnswer()
        }
    }

    private fun onCorrectAnswer() {
        Toast.makeText(this, getString(R.string.correct), Toast.LENGTH_SHORT).show()

    }

    private fun onInCorrectAnswer() {
        Toast.makeText(this, getString(R.string.incorrect), Toast.LENGTH_SHORT).show()
    }
}