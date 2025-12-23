package com.egc.quizquit.domain

sealed class TrivialResult<out T> {
    data class Success<T>(val data: T) : TrivialResult<T>()
    data class Error(val error: TrivialError) : TrivialResult<Nothing>()
}