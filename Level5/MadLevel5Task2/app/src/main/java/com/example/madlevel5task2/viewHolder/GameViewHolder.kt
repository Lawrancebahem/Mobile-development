package com.example.madlevel5task2.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel5task2.databinding.ItemGameBinding
import com.example.madlevel5task2.model.Game
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GameViewHolder(view:View):RecyclerView.ViewHolder(view){

    private val binding = ItemGameBinding.bind(view)

    fun bindData(game: Game){
        binding.itemTitle.text = game.title
        binding.itemPlatform.text = game.platform
        binding.itemRelases.text = getFormattedDate(game.releaseDate)
    }


    /**
     * To format localDate
     */
    private fun getFormattedDate(releaseDate:LocalDate): String{
        return releaseDate.format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))
    }
}