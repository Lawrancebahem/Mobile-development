package com.example.madlevel7example.model

data class Quiz(
    val question: String,
    val answer: String
){

    override fun toString(): String {
        return "Quiz(question='$question', answer='$answer')"
    }
}