package com.egc.quizquit.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.egc.quizquit.utils.urlDecodeUtf8
import com.egc.quizquit.R

@Composable
fun GameScreen(
    state: GameUiState,
    onAnswerClick: (String) -> Unit,
    onNextClick: () -> Unit,
    onRetryClick: () -> Unit,
    onExitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        when {
            state.isLoading -> LoadingContent()
            state.errorMessage != null -> ErrorContent(
                message = state.errorMessage,
                onRetryClick = onRetryClick,
                onExitClick = onExitClick
            )
            state.isFinished -> FinishedContent(
                score = state.score,
                total = state.totalQuestions,
                onExitClick = onExitClick,
                onPlayAgainClick = onRetryClick
            )
            else -> QuestionContent(
                state = state,
                onAnswerClick = onAnswerClick,
                onNextClick = onNextClick,
                onExitClick = onExitClick
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetryClick: () -> Unit,
    onExitClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.error_occurred_title),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onExitClick) { Text(stringResource(R.string.exit)) }
            Button(onClick = onRetryClick) { Text(stringResource(R.string.retry)) }
        }
    }
}

@Composable
private fun FinishedContent(
    score: Int,
    total: Int,
    onExitClick: () -> Unit,
    onPlayAgainClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.game_finished_title),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.score_format, score, total),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onExitClick) { Text(stringResource(R.string.back_to_menu)) }
            Button(onClick = onPlayAgainClick) { Text(stringResource(R.string.play_again)) }
        }
    }
}

@Composable
private fun QuestionContent(
    state: GameUiState,
    onAnswerClick: (String) -> Unit,
    onNextClick: () -> Unit,
    onExitClick: () -> Unit
) {
    val q = state.currentQuestion ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = state.progressText, style = MaterialTheme.typography.titleSmall)
            TextButton(onClick = onExitClick) { Text(stringResource(R.string.exit)) }
        }

        Text(
            text = q.category.urlDecodeUtf8(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = q.question.urlDecodeUtf8(),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(4.dp))

        state.shuffledAnswers.forEach { answer ->
            val enabled = state.selectedAnswer == null
            val isSelected = state.selectedAnswer == answer
            val isCorrectAnswer = answer == q.correctAnswer

            val colors = when {
                state.selectedAnswer == null -> ButtonDefaults.filledTonalButtonColors()
                isSelected && isCorrectAnswer -> ButtonDefaults.buttonColors()
                isSelected && !isCorrectAnswer -> ButtonDefaults.filledTonalButtonColors()
                !isSelected && isCorrectAnswer -> ButtonDefaults.buttonColors()
                else -> ButtonDefaults.filledTonalButtonColors()
            }

            FilledTonalButton(
                onClick = { onAnswerClick(answer) },
                enabled = enabled,
                colors = colors,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = answer.urlDecodeUtf8())
            }
        }

        val canGoNext = state.selectedAnswer != null
        Spacer(Modifier.weight(1f))

        Button(
            onClick = onNextClick,
            enabled = canGoNext,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.next))
        }
    }
}