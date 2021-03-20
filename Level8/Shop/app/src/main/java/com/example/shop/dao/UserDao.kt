package com.example.myapplicationtest2.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.shop.model.User


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun logIn(user: User)

    @Delete
    suspend fun logOut(user: User)

    @Update
    suspend fun update(user: User)

    @Query("SELECT * FROM userTable limit 1")
    fun getUser():LiveData<User>

}