package com.example.madlevel6task2.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.madlevel6task2.api.MovieApi
import com.example.madlevel6task2.api.MovieApiService
import com.example.madlevel6task2.model.Movie
import kotlinx.coroutines.withTimeout

class MovieRepository{

    private val movieApiService:MovieApiService = MovieApi.createMovieApiService()

    private val _moviesList:MutableLiveData<List<Movie>> = MutableLiveData()

    val movieList:LiveData<List<Movie>> get() = _moviesList


    suspend fun getMoviesFromAPi(year:Int)  {
        try {
            //timeout the request after 5 seconds
            val resultResponse = withTimeout(5_000) {
                movieApiService.getMovies(year)
            }
            //assign the
            Log.d("ResultResponse", resultResponse.moviesList.toString())
            _moviesList.value = resultResponse.moviesList
        } catch (error: Throwable) {
            throw MovieRefreshError("Unable to refresh movies", error)
        }
    }
    class MovieRefreshError(message: String, cause: Throwable) : Throwable(message, cause)
}