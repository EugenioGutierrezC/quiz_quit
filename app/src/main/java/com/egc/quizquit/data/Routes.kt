package com.egc.quizquit.data

sealed class Routes(val route: String) {
    object MainMenu: Routes("mainMenu")
    object Game: Routes("game")
}