package com.example.shop.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userTable")
class User(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val firstName: String? = null,
        val lastName: String? = null,
        val email: String? = null,
        val password: String? = null,
        val profilePicture: String? = null,
        val admin: Boolean? = false,
        val isBlocked: Boolean? = false,
) {

    override fun toString(): String {
        return "User(id=$id, firstName=$firstName, lastName=$lastName, profilePicture=$profilePicture, email=$email, password=$password, admin=$admin, isBlocked=$isBlocked)"
    }
}
