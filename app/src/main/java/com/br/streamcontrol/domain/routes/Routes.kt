package com.br.streamcontrol.domain.routes

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed class Screen {
    data object LoginScreen : Screen()
    data object SignUpScreen : Screen()
    data object HomeScreen : Screen()
    data object ProfileScreen : Screen()
    data object SettingsScreen : Screen()
    data object NotificationsScreen : Screen()
}


object Router {

    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.LoginScreen)

    fun navigateTo(destination: Screen) {
        currentScreen.value = destination
    }
}