package com.egc.quizquit.data.mappers

import com.egc.quizquit.data.models.Trivial
import com.egc.quizquit.data.models.UiQuestion
import com.egc.quizquit.utils.urlDecodeUtf8

fun Trivial.Result.toUiQuestion(): UiQuestion {
    val decodedCategory = category.urlDecodeUtf8()
    val decodedQuestion = question.urlDecodeUtf8()
    val decodedCorrect = correctAnswer.urlDecodeUtf8()
    val decodedIncorrect = incorrectAnswers.map { it.urlDecodeUtf8() }
    val decodedAnswers = (decodedIncorrect + decodedCorrect).shuffled()

    return UiQuestion(
        type = type,
        difficulty = difficulty,
        category = decodedCategory,
        question = decodedQuestion,
        correctAnswer = decodedCorrect,
        incorrectAnswers = decodedIncorrect,
        answers = decodedAnswers
    )
}