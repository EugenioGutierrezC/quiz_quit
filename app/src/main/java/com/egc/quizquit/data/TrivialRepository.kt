package com.egc.quizquit.data

import android.util.Log
import com.egc.quizquit.network.APITrivial
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class TrivialRepository @Inject constructor(
    private val apiTrivial: APITrivial,
    private val tokenManager: TokenManager
) {
    suspend fun getQuestions(amount: Int) = withValidToken {
        runCatching {
            val token = tokenManager.getToken().firstOrNull()
            if (token == null) {
                getToken()
            } else {
                val questions = apiTrivial.getQuestions(amount)
                validateResponseCode(questions.responseCode, token)
                    .handle(onOk = { /*TODO implement*/})
            }
        }.onFailure { exception ->
            Log.e("TrivialRepository", "Error getting questions", exception)
        }
    }

    private suspend fun getToken() {
        runCatching {
            val tokenResponse = apiTrivial.getToken()
            validateResponseCode(tokenResponse.responseCode)
                .handle(onOk = { tokenManager.saveToken(tokenResponse.token) })
        }.onFailure { exception ->
            Log.e("TrivialRepository", "Error getting token", exception)
        }
    }

    private suspend fun resetToken(token: String) {
        runCatching {
            val catchTokenResponse = apiTrivial.resetToken(token)
            validateResponseCode(catchTokenResponse.responseCode, token)
                .handle(onOk = { tokenManager.saveToken(catchTokenResponse.token) })

        }.onFailure { exception ->
            Log.e("TrivialRepository", "Error resetting token", exception)
        }
    }

    private suspend fun <T> withValidToken(
        action: suspend (token: String) -> T
    ): T {
        val token = tokenManager.getToken().firstOrNull()
            ?: run {
                getToken()
                tokenManager.getToken().firstOrNull()
            } ?: throw IllegalStateException("Token retrieval failed")

        return try {
            action(token)
        } catch (exception: Exception) {
            resetToken(token)
            val newToken = tokenManager.getToken().firstOrNull()
                ?: throw IllegalStateException("Token retrieval failed")
            action(newToken)
        }
    }

    private suspend fun validateResponseCode(
        responseCode: Int,
        token: String? = null
    ): ValidationResult {
        return when (val result = ApiResponseCode.from(responseCode)) {
            ApiResponseCode.SUCCESS -> ValidationResult.Ok
            ApiResponseCode.NO_RESULTS -> ValidationResult.Recoverable(result.description)
            ApiResponseCode.INVALID_PARAMETER -> ValidationResult.Error(result.description)
            ApiResponseCode.TOKEN_NOT_FOUND -> {
                getToken()
                ValidationResult.Recoverable(result.description)
            }

            ApiResponseCode.TOKEN_EMPTY -> {
                if (token != null) {
                    resetToken(token)
                    ValidationResult.Recoverable(result.description)
                } else {
                    ValidationResult.Error("${result.description} but no token detected")
                }
            }

            ApiResponseCode.RATE_LIMIT -> ValidationResult.Recoverable(result.description)
            ApiResponseCode.UNKNOWN -> ValidationResult.Error(result.description)
        }
    }

    inline fun <T> ValidationResult.handle(
        onOk: () -> T,
        onRecoverable: (String) -> Unit = { Log.w("TrivialRepository", it) },
        onError: (String) -> Unit = { throw IllegalStateException(it) }
    ): T? = when (this) {
        ValidationResult.Ok -> onOk()
        is ValidationResult.Recoverable -> {
            onRecoverable(message)
            null
        }

        is ValidationResult.Error -> {
            onError(message)
            null
        }
    }
}