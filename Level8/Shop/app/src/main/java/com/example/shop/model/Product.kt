package com.example.shop.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "productTable")
class Product(
        @PrimaryKey(autoGenerate = true)
        val id: Long? = 0,
        val title: String?,
        val description: String?,
        val price: Double?,
        val category: Category?,
        val images: ArrayList<String>?,
        val user:User?
)

