package com.egc.quizquit.domain

import com.egc.quizquit.data.models.UiQuestion

interface ITrivialRepository {
    suspend fun getQuestions(amount: Int): TrivialResult<List<UiQuestion>>
}