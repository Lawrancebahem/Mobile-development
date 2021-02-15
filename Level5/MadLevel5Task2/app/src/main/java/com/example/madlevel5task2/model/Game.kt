package com.example.madlevel5task2.model

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

//date.monthOfYear.getAsText() to get month name
@Entity(tableName = "gameTable")
class Game(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        val id:Long? = null,

        @ColumnInfo(name = "title")
        val title:String,

        @ColumnInfo(name = "platform")
        val platform:String,

        @ColumnInfo(name = "release_date")
        val releaseDate:LocalDate
)