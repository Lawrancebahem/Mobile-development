package com.example.madlevel6example.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.madlevel6example.repository.TriviaRepository
import kotlinx.coroutines.launch

class TrivialViewModel (application: Application):AndroidViewModel(application){

    private val trivialRepository = TriviaRepository()

    val trivial = trivialRepository.trivia

    private val _errorText: MutableLiveData<String> = MutableLiveData()

    val errorText:LiveData<String> get() = _errorText


    fun getTrivialNumber(){
        viewModelScope.launch {
            try {
                trivialRepository.getRandomNumberTrivia()
            }catch (ex:TriviaRepository.TriviaRefreshError){
                _errorText.value = ex.message
                Log.d("Trivial error",ex.cause.toString())
            }
        }
    }
}