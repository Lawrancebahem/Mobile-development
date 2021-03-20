package com.example.shop.api

import com.example.shop.model.User

import retrofit2.http.*


interface RegisterApiService{

    @POST("user/new-user")
    suspend fun createNewUser(@Body user: User): User

    @GET("user/resend-code")
    suspend fun resendVerificationCode(@Query("email") email:String):Boolean

    @POST("user/update-profile")
    suspend fun updateProfile(@Body user:User):User
}