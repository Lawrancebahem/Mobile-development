package com.example.madlevel6example.api

import com.example.madlevel6example.model.Trivial
import retrofit2.http.GET

interface TrivialApiService {

    @GET("/random/trivia?json")
    suspend fun getRandomNumberTrivial():Trivial
}