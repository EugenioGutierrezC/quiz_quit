package com.egc.quizquit.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egc.quizquit.domain.ITrivialRepository
import com.egc.quizquit.domain.TrivialResult
import com.egc.quizquit.data.models.UiQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GameUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val questions: List<UiQuestion> = emptyList(),
    val currentIndex: Int = 0,

    val shuffledAnswers: List<String> = emptyList(),
    val selectedAnswer: String? = null,
    val isAnswerCorrect: Boolean? = null,

    val score: Int = 0,
    val isFinished: Boolean = false
) {
    val currentQuestion: UiQuestion?
        get() = questions.getOrNull(currentIndex)

    val totalQuestions: Int
        get() = questions.size

    val progressText: String
        get() = if (totalQuestions == 0) "" else "${currentIndex + 1} / $totalQuestions"
}


@HiltViewModel
class GameViewModel @Inject constructor(
    private val trivialRepository: ITrivialRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState

    init {
        loadQuestions(amount = 10)
    }

    fun loadQuestions(amount: Int) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null, isFinished = false) }

        viewModelScope.launch {
            when (val result = trivialRepository.getQuestions(amount)) {
                is TrivialResult.Success -> {
                    val questions = result.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            questions = questions,
                            currentIndex = 0,
                            score = 0,
                            selectedAnswer = null,
                            isAnswerCorrect = null,
                            shuffledAnswers = buildAnswersFor(questions, 0),
                            isFinished = questions.isEmpty()
                        )
                    }
                }

                is TrivialResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.error.toString()
                        )
                    }
                }
            }
        }
    }

    fun onAnswerSelected(answer: String) {
        val state = _uiState.value
        if (state.isLoading || state.isFinished) return
        if (state.selectedAnswer != null) return

        val question = state.currentQuestion ?: return
        val correct = answer == question.correctAnswer

        _uiState.update {
            it.copy(
                selectedAnswer = answer,
                isAnswerCorrect = correct,
                score = if (correct) it.score + 1 else it.score
            )
        }
    }

    fun onNext() {
        val state = _uiState.value
        if (state.isLoading || state.isFinished) return

        val nextIndex = state.currentIndex + 1
        val hasNext = nextIndex < state.questions.size

        if (!hasNext) {
            _uiState.update { it.copy(isFinished = true) }
            return
        }

        _uiState.update {
            it.copy(
                currentIndex = nextIndex,
                selectedAnswer = null,
                isAnswerCorrect = null,
                shuffledAnswers = buildAnswersFor(it.questions, nextIndex)
            )
        }
    }

    fun onRetry() {
        loadQuestions(amount = 10)
    }

    private fun buildAnswersFor(questions: List<UiQuestion>, index: Int): List<String> {
        val q = questions.getOrNull(index) ?: return emptyList()
        return (q.incorrectAnswers + q.correctAnswer).shuffled()
    }
}
