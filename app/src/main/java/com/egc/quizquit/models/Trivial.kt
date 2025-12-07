package com.egc.quizquit.models

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Trivial(
    @SerializedName("response_code")
    val responseCode: Int,
    @SerializedName("results")
    val results: List<Result>
) {
    @Keep
    data class Result(
        @SerializedName("type")
        val type: String,
        @SerializedName("difficulty")
        val difficulty: String,
        @SerializedName("category")
        val category: String,
        @SerializedName("question")
        val question: String,
        @SerializedName("correct_answer")
        val correctAnswer: String,
        @SerializedName("incorrect_answers")
        val incorrectAnswers: List<String>
    )
}