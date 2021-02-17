package com.example.madlevel6task2.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieApi{

    companion object{
        private const val BASE_URL = "https://api.themoviedb.org/3/"
        private const val API_KEY = "api_key"
        private const val API_VALUE = "bb7c29934be6882f5417e3b31e8905be"
        const val IMAGE_VIEW_URL = "https://image.tmdb.org/t/p/w500/"
        // to add the api key for each request
        class Intercepter:Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                val urlWithAPI =  chain.request().url.newBuilder().addQueryParameter(API_KEY, API_VALUE).build()
                val modifiedRequest = chain.request().newBuilder().url(urlWithAPI).build()
                return chain.proceed(modifiedRequest)
            }
        }

        fun createMovieApiService():MovieApiService{
            // Create an OkHttpClient to be able to make a log of the network traffic
            val okHttpClient = OkHttpClient
                .Builder()
                .addInterceptor(Intercepter())
                .addInterceptor(HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

           return Retrofit
               .Builder()
               .baseUrl(BASE_URL)
               .client(okHttpClient)
               .addConverterFactory(GsonConverterFactory.create())
               .build()
               .create(MovieApiService::class.java)
        }
    }
}