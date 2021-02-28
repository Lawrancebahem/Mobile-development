package com.example.myapplicationtest2.DAO

import androidx.room.*
import com.example.shop.model.User


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun logIn(user: User)

    @Delete
    suspend fun logOut(user: User)

    @Query("SELECT * FROM userTable")
    suspend fun getUser():List<User>

}