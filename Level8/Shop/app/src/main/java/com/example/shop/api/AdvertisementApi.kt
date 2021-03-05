package com.example.shop.api

import com.example.shop.model.Product
import com.example.shop.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AdvertisementApi {

    @GET("/product/all")
    suspend fun getAllProducts():ArrayList<Product>

    @POST("/product/new-item")
    suspend fun insertProduct(@Body product: Product):Boolean

    @GET("/product/get-item/{id}")
    suspend fun getProduct(@Path("id") id:Long):Product
    
    @POST("/product/remove-item/{id}")
    suspend fun removeProduct(@Path("id") id:Long):Boolean

}