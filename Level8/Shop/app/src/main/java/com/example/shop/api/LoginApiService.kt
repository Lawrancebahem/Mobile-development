package com.example.shop.api

import com.example.shop.model.User
import retrofit2.http.*

interface LoginApiService{

    @GET("user/{id}")
    suspend fun getUser(@Path("id") id:Long): User

    @POST("user/login")
    suspend fun login(@Query("email")email:String, @Query("password")password:String,  @Query("code")randomCode:Int):User
}