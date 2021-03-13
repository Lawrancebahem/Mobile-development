package com.example.shop.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productTable")
class Product(
    @PrimaryKey(autoGenerate = true)
    val productId: Long?,
    val title: String?,
    val description: String?,
    val price: Double?,
    val category: Category?,
    val images: ArrayList<String>?,
    val seen: Long? = 0,
    val date: String,
    val user: User?,
    val totalLikes: Long? = 0
) {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Product
        if (productId != other.productId) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (price != other.price) return false
        return true
    }

    override fun hashCode(): Int {
        var result = productId?.hashCode() ?: 0
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (price?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Product(id=$productId, title=$title, description=$description, price=$price, category=$category, images=$images, seen=$seen, date='$date', user=$user, totalLikes=$totalLikes)"
    }
}



