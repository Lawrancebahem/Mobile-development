package com.example.shop.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Api<out T>(private val cls: Class<T>) {



    companion object {

        private const val BASE_URL = "https://server-bd-shop.herokuapp.com/"
        private const val API_KEY = "api_key"
        private const val API_VALUE = "bb7c29934be6882f5417e3b31e8905be"
        public const val IMAGE_URL_VIEWER = "http://192.168.178.22/Users/lawrancebahem/Desktop/"
    }


    // to add the api key for each request
    private inner class Intercepter : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val urlWithAPI =
                chain.request().url.newBuilder().addQueryParameter(API_KEY, API_VALUE).build()
            val modifiedRequest = chain.request().newBuilder().url(urlWithAPI).build()
            return chain.proceed(modifiedRequest)
        }
    }

    fun createShopService(): T {
        // Create an OkHttpClient to be able to make a log of the network traffic
        val okHttpClient = OkHttpClient
            .Builder()
//                .addInterceptor(Intercepter())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(cls)
    }
}