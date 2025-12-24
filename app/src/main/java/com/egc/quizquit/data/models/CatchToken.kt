package com.egc.quizquit.data.models

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CatchToken(
    @SerializedName("response_code")
    val responseCode: Int,
    @SerializedName("response_message")
    val responseMessage: String?,
    @SerializedName("token")
    val token: String
)