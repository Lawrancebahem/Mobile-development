package com.example.madlevel4task2.Adapter

import android.provider.Settings
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel4task2.Model.History
import com.example.madlevel4task2.Model.RPS
import com.example.madlevel4task2.R
import com.example.madlevel4task2.ViewHoler.HistoryViewHolder

class HistoryAdapter(private val historyList:ArrayList<History>):RecyclerView.Adapter<HistoryViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.result_item,parent, false))
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bindData(historyList[position])
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}