package com.egc.quizquit.network

import com.egc.quizquit.data.models.CatchToken
import com.egc.quizquit.data.models.Trivial
import retrofit2.http.GET
import retrofit2.http.Query

interface APITrivial {
    @GET("api_token.php?command=request")
    suspend fun getToken(): CatchToken

    @GET("api_count.php?command=reset")
    suspend fun resetToken(@Query("token") token: String): CatchToken

    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int,
        @Query("encode") encode: String = "url3986"
    ): Trivial
}