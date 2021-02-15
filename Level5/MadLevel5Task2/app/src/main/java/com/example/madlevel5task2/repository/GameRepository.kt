package com.example.madlevel5task2.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.madlevel5task2.dao.GameDao
import com.example.madlevel5task2.database.GameRoomDatabase
import com.example.madlevel5task2.model.Game

class GameRepository(context: Context){

    private val gameDao: GameDao

    init {
        val gameRoomDatabase = GameRoomDatabase.getDatabase(context)
        gameDao = gameRoomDatabase!!.gameDao()
    }

    fun getAllGames(): LiveData<List<Game>>{
        return gameDao.getAllGames()
    }


    suspend fun insertGame(game: Game){
        gameDao.insertGame(game)
    }

    suspend fun deleteGame(game: Game){
        gameDao.deleteGame(game)
    }

    suspend fun deleteAllGames(){
        gameDao.deleteAllGames()
    }
}