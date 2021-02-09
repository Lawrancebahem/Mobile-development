package com.example.madlevel4task1.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "ProductTable")
class Product(
    @ColumnInfo
    val name:String,
    @ColumnInfo
    val amount:Int,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    val id:Long? = null)