package com.example.madlevel5task1.databaase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.madlevel5task1.dao.NoteDao
import com.example.madlevel5task1.model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class NoteRoomDatabase :RoomDatabase(){

    abstract fun notDao(): NoteDao

    companion object{
        private const val DATABASE_NAME = "NOTEPAD_DATABASE2"

        @Volatile
        private var notepadDatabaseInstance:NoteRoomDatabase? = null


        fun getDatabase(context:Context):NoteRoomDatabase?{
            if (notepadDatabaseInstance  == null){
                synchronized(NoteRoomDatabase::class.java){
                    if (notepadDatabaseInstance == null){
                        notepadDatabaseInstance = Room.databaseBuilder(context.applicationContext, NoteRoomDatabase::class.java, DATABASE_NAME)
                            .fallbackToDestructiveMigration()

                                //to add a note to the database
                            .addCallback(object :RoomDatabase.Callback(){
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)
                                    notepadDatabaseInstance?.let {database ->
                                        CoroutineScope(Dispatchers.IO).launch {
                                            database.notDao().insertNote(Note("Title", Date(),""))
                                        }
                                    }
                                }
                            })
                            .build()
                    }
                }
            }
            return notepadDatabaseInstance
        }
    }
}