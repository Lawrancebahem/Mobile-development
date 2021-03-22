package com.example.myapplicationtest2.DAO

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.shop.dao.Converter
import com.example.shop.dao.RandomCodeDao
import com.example.shop.model.RandomCode
import com.example.shop.model.User


@Database(entities = [User::class, RandomCode::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class UserRoomDatabase : RoomDatabase() {

    abstract fun productDao(): UserDao
    abstract fun randomCodeDao(): RandomCodeDao

    companion object {
         const val DATABASE_NAME = "PRODUCT_DATABASE_1"
    }
}