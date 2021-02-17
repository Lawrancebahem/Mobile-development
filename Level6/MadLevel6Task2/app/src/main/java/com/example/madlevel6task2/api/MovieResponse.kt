package com.example.madlevel6task2.api

import com.example.madlevel6task2.model.Movie
import com.google.gson.annotations.SerializedName

class MovieResponse(
    @SerializedName("page")
    var page: Int? = null,
    @SerializedName("total_pages")
    var totalPages: Int? = null,
    @SerializedName("total_results")
    var totalResults: Int? = null,
    var results: List<Movie>? = null
) {

}