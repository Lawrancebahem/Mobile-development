package com.example.madlevel7task2.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.madlevel7task2.Model.Quiz
import com.example.madlevel7task2.repsoitory.QuizRepository
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "FIRESTORE"

    private val quizRepository: QuizRepository = QuizRepository()

    val quizList: LiveData<ArrayList<Quiz>> = quizRepository.quizList
    val error: LiveData<String> = quizRepository.error

    private var _goodAnsweredQuestion:MutableLiveData<ArrayList<Quiz>> = MutableLiveData()

    val wrongAnsweredQuestions:LiveData<ArrayList<Quiz>> get() = _goodAnsweredQuestion

    fun setGoodAnsweredQuestion(list:ArrayList<Quiz>){
        this._goodAnsweredQuestion.value = list
    }
    /**
     * get all quizzes from firebase
     */
    fun getQuizzesList() {
        viewModelScope.launch {
            try {
                quizRepository.getQuizzes()
            } catch (ex: Exception) {
                Log.d(TAG, ex.message.toString())
            }
        }
    }
}