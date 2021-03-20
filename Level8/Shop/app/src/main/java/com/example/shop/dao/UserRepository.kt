package com.example.myapplicationtest2.DAO

import androidx.lifecycle.LiveData
import com.example.shop.model.User
import javax.inject.Inject


class UserRepository @Inject constructor (private val userDao: UserDao){


    suspend fun logIn(user: User){
        userDao.logIn(user)
    }

    suspend fun logOut(user: User){
        userDao.logOut(user)
    }

    suspend fun update(user: User){
        userDao.update(user)
    }

    fun getUser():LiveData<User>{
        return userDao.getUser()
    }
}