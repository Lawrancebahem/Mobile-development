package com.example.madlevel1exampletask1

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.madlevel1exampletask1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private var currentThrow: Int = 1
    private var lastThrow: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.lowerBtn.setOnClickListener(this)
        binding.higherBtn.setOnClickListener(this)
        binding.equalBtn.setOnClickListener(this)

        //update the user interface
        updateUI()
    }

    /**
     * To update the picture based on the score
     */
    private fun updateUI() {
        //set the text of the score to the last throw
        binding.scoreText.text = getString(R.string.last_throw, lastThrow)
        binding.imageView.setImageResource(resources.getIdentifier("dice${currentThrow}", "drawable", packageName))
    }

    /**
     * To get a random score
     */
    private fun rollDice() {
        lastThrow = currentThrow;
        currentThrow = (1..6).random()

        updateUI()
    }

    /**
     * To show a toast correct message
     */
    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * To handle all click buttons
     */
    override fun onClick(v: View?) {
        rollDice()
        val correct = when (v!!.id) {
            binding.equalBtn.id -> currentThrow == lastThrow
            binding.higherBtn.id -> currentThrow > lastThrow
            else -> currentThrow < lastThrow
        }
        when (correct) {
            true -> showMessage(getString(R.string.correct))
            else -> showMessage(getString(R.string.incorrect))
        }
    }
}