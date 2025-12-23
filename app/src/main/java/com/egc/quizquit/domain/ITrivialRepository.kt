package com.egc.quizquit.domain

import com.egc.quizquit.models.Trivial

interface ITrivialRepository {
    suspend fun getQuestions(amount: Int): TrivialResult<List<Trivial.Result>>
}