package com.example.myapplicationtest2.DAO

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shop.model.User


@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserRoomDatabase : RoomDatabase() {

    abstract fun productDao(): UserDao

    companion object {
         const val DATABASE_NAME = "PRODUCT_DATABASE_1"
    }
}