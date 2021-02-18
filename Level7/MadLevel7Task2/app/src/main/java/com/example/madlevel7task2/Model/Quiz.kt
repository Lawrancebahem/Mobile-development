package com.example.madlevel7task2.Model

data class Quiz(
    val question:String? = null,
    val answersList:ArrayList<String> ? = ArrayList(),
    val goodAnswer:Int? = 0
){

    override fun toString(): String {
        return "Quiz(question=$question, answersList=$answersList, goodAnswer=$goodAnswer)"
    }
}