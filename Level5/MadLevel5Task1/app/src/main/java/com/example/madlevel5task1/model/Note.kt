package com.example.madlevel5task1.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "noteTable")
class Note(

    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "lastUpdated")
    var lastUpdated: Date,

    @ColumnInfo(name = "text")
    var text: String,

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id:Long ? = null
)