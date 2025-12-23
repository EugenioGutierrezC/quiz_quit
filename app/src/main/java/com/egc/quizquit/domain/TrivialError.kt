package com.egc.quizquit.domain

sealed interface TrivialError {
    data object Network : TrivialError
    data class Token(val message: String) : TrivialError
    data class Api(val code: Int, val message: String) : TrivialError
    data class Unknown(val throwable: Throwable) : TrivialError
}