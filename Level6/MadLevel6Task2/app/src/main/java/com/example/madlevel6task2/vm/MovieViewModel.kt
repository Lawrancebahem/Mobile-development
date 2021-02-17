package com.example.madlevel6task2.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madlevel6task2.model.Movie
import com.example.madlevel6task2.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel:ViewModel(){

    private val movieRepository = MovieRepository()

    val moviesList = movieRepository.movieList
    private var _currentMovie:MutableLiveData<Movie> = MutableLiveData()

    val getMovie:LiveData<Movie> get() =_currentMovie

    fun setMovie(movie: Movie){
        _currentMovie.value = movie
    }
    fun getMoviesList(year:Int){

        viewModelScope.launch {
            try {
                movieRepository.getMoviesFromAPi(year)
            }catch (ex:MovieRepository.MovieRefreshError){
                Log.d("23-MovieViewModel error",ex.cause.toString())
            }
        }
    }
}