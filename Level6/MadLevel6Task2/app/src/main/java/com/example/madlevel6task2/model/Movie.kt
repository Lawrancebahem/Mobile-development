package com.example.madlevel6task2.model

import com.google.gson.annotations.SerializedName

class Movie(
    @SerializedName("backdrop_path")
    val backdropPath: String,
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val title: String,//
    @SerializedName("vote_average")
    val voteAverage: Double,

) {
    override fun toString(): String {
        return "Movie(backdropPath='$backdropPath', overview='$overview', posterPath='$posterPath', releaseDate='$releaseDate', title='$title', voteAverage=$voteAverage)"
    }
}