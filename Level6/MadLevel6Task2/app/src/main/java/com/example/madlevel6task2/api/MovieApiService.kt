package com.example.madlevel6task2.api

import com.example.madlevel6task2.model.ResultResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService{
    private val DEFAULT_YEAR: Int get() = 2021

    @GET("discover/movie")
    suspend fun getMovies(@Query("primary_release_year") year:Int?=DEFAULT_YEAR):ResultResponse
}