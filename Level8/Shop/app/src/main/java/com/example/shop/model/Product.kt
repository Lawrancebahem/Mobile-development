package com.example.shop.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "productTable")
class Product(
        @PrimaryKey(autoGenerate = true)
        val id: Long?,
        val title: String?,
        val description: String?,
        val price: Double?,
        val category: Category?,
        val images: ArrayList<String>?,
        val date: String,
        val user: User?){




    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Product
        if (id != other.id) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (price != other.price) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (price?.hashCode() ?: 0)
        return result
    }




}



