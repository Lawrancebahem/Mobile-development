package com.example.madlevel5task2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel5task2.R
import com.example.madlevel5task2.model.Game
import com.example.madlevel5task2.viewHolder.GameViewHolder

class GameAdapter(private val gameList:ArrayList<Game>):RecyclerView.Adapter<GameViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false))
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bindDate(gameList[position])
    }

    override fun getItemCount(): Int {
        return gameList.size
    }
}