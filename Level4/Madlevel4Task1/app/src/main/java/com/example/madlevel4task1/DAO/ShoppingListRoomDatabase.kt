package com.example.madlevel4task1.DAO

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.madlevel4task1.Model.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class ShoppingListRoomDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDAO

    companion object {
        private const val DATABASE_NAME = "SHOPPING_LIST_DATABSE"

        @Volatile
        private var shoppingListDatabseInstance: ShoppingListRoomDatabase? = null

        fun getDatabase(context: Context): ShoppingListRoomDatabase {
            if (shoppingListDatabseInstance != null) {
                return shoppingListDatabseInstance!!
            }
            synchronized(this) {
                shoppingListDatabseInstance  = Room.databaseBuilder(context.applicationContext, ShoppingListRoomDatabase::class.java, DATABASE_NAME)
                    .build()
                return shoppingListDatabseInstance!!
            }
        }
    }
}