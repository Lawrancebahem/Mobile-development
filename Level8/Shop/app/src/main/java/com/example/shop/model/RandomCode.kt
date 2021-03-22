package com.example.shop.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp


@Entity(tableName = "randomCodeTime")
class RandomCode(

    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val timesTamp:Timestamp
) {
}