package com.egc.quizquit.data

enum class ApiResponseCode(val code: Int, val description: String) {
    SUCCESS(0, "Returned results successfully"),
    NO_RESULTS(1, "Could not return results. Not enough questions."),
    INVALID_PARAMETER(2, "Invalid parameter provided."),
    TOKEN_NOT_FOUND(3, "Session token does not exist."),
    TOKEN_EMPTY(4, "Session token exhausted. Reset needed."),
    RATE_LIMIT(5, "Too many requests (rate limit reached)."),
    UNKNOWN(-1, "Unknown response code");

    companion object {
        fun from(code: Int): ApiResponseCode =
            entries.find { it.code == code } ?: UNKNOWN
    }
}

sealed class ValidationResult {
    object Ok : ValidationResult()
    data class Recoverable(val message: String) : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}
