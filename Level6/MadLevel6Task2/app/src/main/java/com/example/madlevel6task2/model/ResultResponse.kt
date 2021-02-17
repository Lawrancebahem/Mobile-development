package com.example.madlevel6task2.model

import com.google.gson.annotations.SerializedName

class ResultResponse(
    @SerializedName("results")
    val moviesList:List<Movie>?
)