package com.egc.quizquit.data.models

data class UiQuestion(
    val type: String,
    val difficulty: String,
    val category: String,
    val question: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>,
    val answers: List<String>
)
