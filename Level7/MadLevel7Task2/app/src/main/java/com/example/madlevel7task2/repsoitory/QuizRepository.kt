package com.example.madlevel7task2.repsoitory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.madlevel7task2.Model.Quiz
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.withTimeout

class QuizRepository {

    private var fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val quizzesDocument = fireStore.collection("Quizzes")

    private var _quizList: MutableLiveData<ArrayList<Quiz>> = MutableLiveData()
    private var _error: MutableLiveData<String> = MutableLiveData()


    val quizList: LiveData<ArrayList<Quiz>> get() = _quizList
    val error: LiveData<String> get() = _error

    suspend fun getQuizzes() {
        val dataList = ArrayList<Quiz>()
        try {
            withTimeout(5_000) {
                quizzesDocument.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (querySnapshot != null) {
                        for (document in querySnapshot.documents) {
                            val quiz = document.toObject(Quiz::class.java)
                            if (quiz != null) {
                                Log.d("The quiz", quiz.toString())
                                dataList.add(quiz)
                            }
                        }
                        _quizList.value = dataList
                    } else if (firebaseFirestoreException != null) {
                        _error.value = firebaseFirestoreException.message
                    }
                }
            }
        } catch (ex: Exception) {
            _error.value = ex.message
        }
    }


//    suspend fun createQuiz() {
//        val anwersList = arrayListOf("Lawrance", "Marc", "Tico")
//        val quizList = Quiz("What is your name?", anwersList, 1)
//        try {
//
//            withTimeout(5_000) {
//                quizzesDocument.add(quizList).addOnSuccessListener {
//                    Log.d("Data", "Has been inserted")
//                }
//            }
//        } catch (ex: Exception) {
//            Log.d("Repos", ex.message.toString())
//        }
//    }

}