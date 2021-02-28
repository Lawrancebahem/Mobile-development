package com.example.shop.api

import com.example.shop.model.User

import okhttp3.RequestBody

import okhttp3.MultipartBody
import retrofit2.http.*


interface RegisterApiService{

    @POST("user/new-user")
    suspend fun createNewUser(@Body user: User): User
}