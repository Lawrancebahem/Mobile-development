package com.example.shop.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.shop.model.RandomCode

@Dao
interface RandomCodeDao {

    @Insert
    suspend fun insertResendCode(randomCodeTime: RandomCode)


    @Query("SELECT * FROM randomCodeTime limit 1")
    fun getResendCode(): LiveData<RandomCode>

    @Query("DELETE FROM randomCodeTime")
    suspend fun deleteResendCode()
}