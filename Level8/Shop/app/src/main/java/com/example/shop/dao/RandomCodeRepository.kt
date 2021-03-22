package com.example.shop.dao

import androidx.lifecycle.LiveData
import com.example.shop.model.RandomCode
import javax.inject.Inject

class RandomCodeRepository @Inject constructor (private val randomCodeDao: RandomCodeDao) {


    suspend fun insertResendCode(randomCodeTime: RandomCode){
        randomCodeDao.insertResendCode(randomCodeTime)
    }


    fun getResendCode(): LiveData<RandomCode>{
        return randomCodeDao.getResendCode()
    }

    suspend fun deleteResendCode(){
        randomCodeDao.deleteResendCode()
    }
}