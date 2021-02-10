package com.example.madlevel1exampletask1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.madlevel1exampletask1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

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
        binding.lowerBtn.setOnClickListener {
            onLowClick()
        }
        binding.higherBtn.setOnClickListener {
            onHighClick()
        }

        binding.equalBtn.setOnClickListener {
            onEqual()
        }
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

    private fun onHighClick() {
        rollDice()
        if (currentThrow > lastThrow){
            showMessage(getString(R.string.correct))
        }else{
            showMessage(getString(R.string.incorrect))
        }
    }
    private fun onLowClick() {
        rollDice()
        if (currentThrow < lastThrow){
            showMessage(getString(R.string.correct))
        }else{
            showMessage(getString(R.string.incorrect))
        }
    }

    /**
     * Once the equal button clicked
     */
    private fun onEqual() {
        rollDice()
        if (currentThrow == lastThrow){
            showMessage(getString(R.string.correct))
        }else{
            showMessage(getString(R.string.incorrect))
        }
    }

    /**
     * To get a random score
     */
    private fun rollDice(){
        lastThrow = currentThrow;
        currentThrow = (1..6).random()

        updateUI()
    }

    /**
     * To show a toast correct message
     */
    private fun showMessage(message:String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }

}