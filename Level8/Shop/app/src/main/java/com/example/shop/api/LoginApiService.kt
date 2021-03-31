package com.example.shop.api

import com.example.shop.model.User
import retrofit2.Response
import retrofit2.http.*

interface LoginApiService{

    @GET("user/{id}")
    suspend fun getUser(@Path("id") id:Long): User

    @POST("user/login")
    suspend fun login(@Query("email")email:String, @Query("password")password:String,  @Query("code")randomCode:Int):Response<User>

    @POST("user/reset-password")
    suspend fun resetPassword(@Query("email")email:String):Response<Boolean>

    @POST("user/reset-password/check-code")
    suspend fun checkResetPasswordCode(@Query("email")email:String, @Query("code")randomCode:Int):Response<Boolean>

    @POST("user/new-password")
    suspend fun setNewPassword(@Query("email")email:String, @Query("password") password: String, @Query("code")randomCode:Int):Response<Boolean>


}