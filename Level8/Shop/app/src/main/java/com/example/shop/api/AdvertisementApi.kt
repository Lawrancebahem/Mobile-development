package com.example.shop.api

import com.example.shop.model.Product
import retrofit2.http.*

interface AdvertisementApi {

    @GET("/product/all")
    suspend fun getAllProducts():ArrayList<Product>

    @POST("/product/new-item")
    suspend fun insertProduct(@Body product: Product):Boolean

    @GET("/product/get-item/{id}")
    suspend fun getProduct(@Path("id") id:Long):Product
    
    @POST("/product/remove-item/{id}")
    suspend fun removeProduct(@Path("id") id:Long):Boolean

    @POST("user/insert-like")
    suspend fun insertLike(@Query("userId") userId:Long, @Query("productId") productId:Long):Boolean

    @GET("user/get-liked-products/{id}")
    suspend fun getUserLikes(@Path("id") id:Long):Set<Product>

    @HTTP(method = "DELETE", path = "user/delete-like")
    suspend fun removeLike(@Query("userId") userId:Long, @Query("productId") productId:Long):Boolean


}