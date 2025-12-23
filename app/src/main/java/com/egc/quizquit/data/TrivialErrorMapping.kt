package com.egc.quizquit.data

import com.egc.quizquit.domain.TrivialError
import java.io.IOException

fun Throwable.toTrivialError(): TrivialError = when (this) {
    is IOException -> TrivialError.Network
    else -> TrivialError.Unknown(this)
}