package com.example.myapplicationtest2.DAO

import androidx.lifecycle.LiveData
import com.example.shop.model.User
import javax.inject.Inject


class UserRepository @Inject constructor (private val userDao: UserDao){


    suspend fun logIn(product: User){
        userDao.logIn(product)
    }

    suspend fun logOut(product: User){
        userDao.logOut(product)
    }

    fun getUser():LiveData<User>{
        return userDao.getUser()
    }
}