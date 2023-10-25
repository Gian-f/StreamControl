package com.br.streamcontrol.ui.routes

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed class Screen(val route: String, val title: String) {
    object LoginScreen : Screen("login", "Login")
    object SignUpScreen : Screen("register", "Registro")
    object HomeScreen : Screen("home", "Home")
}


object Router {

    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.LoginScreen)

    fun navigateTo(destination : Screen){
        currentScreen.value = destination
    }
}