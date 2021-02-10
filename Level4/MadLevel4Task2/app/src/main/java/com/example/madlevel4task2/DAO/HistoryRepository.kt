package com.example.madlevel4task2.DAO

import android.content.Context
import android.content.res.Resources
import com.example.madlevel4task2.Model.History
import com.example.madlevel4task2.Model.RPS
import com.example.madlevel4task2.Model.Result
import com.example.madlevel4task2.Model.ResultOverView
import com.example.madlevel4task2.R

class HistoryRepository(context:Context){

    private val historyDao:HistoryDao

    init {
        val historyRoomDatabase = HistoryRoomDatabase.getDatabase(context)
        historyDao = historyRoomDatabase!!.historyDao()
    }

    suspend fun getHistory(): List<History>{
        return historyDao.getHistory()
    }

    suspend fun insertHistory(history: History){
        historyDao.insertHistory(history)
    }

    suspend fun deleteAllHistories(){
        historyDao.deleteAllHistories()
    }

    suspend fun deleteHistory(history: History){
        historyDao.deleteHistory(history)
    }

    suspend fun getStatistics():ResultOverView{
        return historyDao.getStatistics()
    }
}