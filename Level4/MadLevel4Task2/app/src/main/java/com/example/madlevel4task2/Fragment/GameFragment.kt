package com.example.madlevel4task2.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.madlevel4task2.DAO.HistoryRepository
import com.example.madlevel4task2.Model.History
import com.example.madlevel4task2.Model.RPS
import com.example.madlevel4task2.R
import com.example.madlevel4task2.databinding.FragmentGameBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var historyRepository:HistoryRepository
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        historyRepository = HistoryRepository(requireContext())
        getStatistics()
    }

    fun initView(){

        binding.rockImage.setOnClickListener{
            onClick(RPS.ROCK)
        }

        binding.paperImage.setOnClickListener{
            onClick(RPS.PAPER)
        }

        binding.scissorsImage.setOnClickListener{
            onClick(RPS.SCISSORS)
        }
    }

    //to generate random enum
    fun getRandomRPS() = RPS.values().random()


    /**
     * Once the user clicks on on of the images
     */
    fun onClick(playerRPS:RPS){

        val computerRPS = getRandomRPS()
        val result = History.getWinner(computerRPS, playerRPS) // returns result object
        val resultText = History.determineResultAsText(result) // returns text

        binding.result.text = resultText
        binding.playerImage.setImageResource(History.getImageBasedOnRPS(playerRPS))
        binding.computerImage.setImageResource(History.getImageBasedOnRPS(computerRPS))

        val history = History(computerRPS, playerRPS, Date(),result)

        insertHistoryToDatabase(history)
    }


    /**
     * To insert history into the databse
     */
    fun insertHistoryToDatabase(history:History){
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO){
                historyRepository.insertHistory(history)
            }
            getStatistics()
        }
    }


    /**
     * Get statistics from the database and show them in text of statistics
     */
    fun getStatistics(){
        CoroutineScope(Dispatchers.Main).launch {
           val statistics =  withContext(Dispatchers.IO){
               historyRepository.getStatistics()
           }
            if ((statistics.totalDraws + statistics.totalWins + statistics.totalLoss) > 0){
                binding.statistics.text =  getString(R.string.statistics, statistics.totalWins, statistics.totalDraws, statistics.totalLoss)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}