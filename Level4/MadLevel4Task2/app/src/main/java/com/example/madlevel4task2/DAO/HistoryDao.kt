package com.example.madlevel4task2.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.madlevel4task2.Model.History
import com.example.madlevel4task2.Model.Result
import com.example.madlevel4task2.Model.ResultOverView

@Dao
interface HistoryDao {
    @Query("SELECT * FROM HISTORY_TABLE")
    suspend fun getHistory(): List<History>

    @Insert
    suspend fun insertHistory(history: History)

    @Query("DELETE FROM History_table")
    suspend fun deleteAllHistories()

    /**
     *  0 refers to win, 1 refers to draw and 2 refers to loss
     */
    @Query("SELECT count(case when result = 0 then 0 end) " +
            "as totalWins, count(case when result = 1 then 0 end) as totalDraws, " +
            "count(case when result = 2 then 0 end) as totalLoss FROM HISTORY_TABLE" )
    fun getStatistics():ResultOverView

    @Delete
    suspend fun deleteHistory(history:History)
}

