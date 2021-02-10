package com.example.madlevel4task2.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.madlevel4task2.DAO.HistoryDao
import com.example.madlevel4task2.Model.History


@Database(entities = [History::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class HistoryRoomDatabase:RoomDatabase(){

    abstract fun historyDao(): HistoryDao

    companion object{
        private const val DATABASE_NAME = "HISTORY_DATABASE"

        @Volatile
        private var historyRoomDatbaseInstance: HistoryRoomDatabase? = null
        fun getDatabase(context: Context): HistoryRoomDatabase? {
            if (historyRoomDatbaseInstance == null) {
                synchronized(HistoryRoomDatabase::class.java) {
                    if (historyRoomDatbaseInstance == null) {
                        historyRoomDatbaseInstance = Room.databaseBuilder(
                            context.applicationContext,
                            HistoryRoomDatabase::class.java,
                            DATABASE_NAME
                        ).build()
                    }
                }
            }
            return historyRoomDatbaseInstance
        }
    }
}