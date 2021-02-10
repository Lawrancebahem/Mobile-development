package com.example.madlevel4task2.ViewHoler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel4task2.Model.History
import com.example.madlevel4task2.databinding.ResultItemBinding

class HistoryViewHolder(view:View):RecyclerView.ViewHolder(view){

    private val binding = ResultItemBinding.bind(view)

    /**
     * bind data of this given view to the given data of this history
     */
    fun bindData(history:History){
        binding.resultText.text = History.determineResultAsText(history.result) // returns text
        binding.timeResult.text = history.date.toString()
        binding.itemComputerImage.setImageResource(History.getImageBasedOnRPS(history.RPSComputer))
        binding.itemPalyerImage.setImageResource(History.getImageBasedOnRPS(history.RPSPlayer))
    }
}