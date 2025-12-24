package com.egc.quizquit.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.egc.quizquit.data.Routes
import com.egc.quizquit.ui.screens.GameScreen
import com.egc.quizquit.ui.screens.GameViewModel
import com.egc.quizquit.ui.screens.MainMenuScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.MainMenu.route) {
        composable(route = Routes.MainMenu.route) {
            MainMenuScreen(
                onStartGameClick = { navController.navigate(route = Routes.Game.route) }
            )
        }
        composable(
            route = Routes.Game.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            val viewModel: GameViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsState()

            GameScreen(
                state = state,
                onAnswerClick = viewModel::onAnswerSelected,
                onNextClick = viewModel::onNext,
                onRetryClick = viewModel::onRetry,
                onExitClick = { navController.popBackStack() }
            )

        }
    }
}