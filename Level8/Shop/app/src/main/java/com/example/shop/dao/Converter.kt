package com.example.shop.dao

import androidx.room.TypeConverter
import com.example.shop.model.VerificationToken
import java.sql.Timestamp
import java.util.*

class Converter {

    @TypeConverter
    fun fromTokenToString(value: VerificationToken): String {
        return value.token!!
    }

    @TypeConverter
    fun fromStringToToken(value: String): VerificationToken {
       val verificationToken = VerificationToken()
        verificationToken.token = value
        return  verificationToken
    }




    @TypeConverter
    fun fromTimestamp(value: Long?): Timestamp?{
        return Timestamp(value!!)
    }

    @TypeConverter
    fun toTimestamp(timestamp: Timestamp?): Long? = timestamp?.time
}