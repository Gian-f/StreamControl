package com.br.streamcontrol.application

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.br.streamcontrol.data.home.HomeViewModel
import com.br.streamcontrol.ui.routes.Router
import com.br.streamcontrol.ui.routes.Screen
import com.br.streamcontrol.ui.screens.LoginScreen

@Composable
fun App(homeViewModel: HomeViewModel = viewModel()) {

    homeViewModel.checkForActiveSession()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {

        if (homeViewModel.isUserLoggedIn.value == true) {
            Router.navigateTo(Screen.HomeScreen)
        }

        Crossfade(targetState = Router.currentScreen, label = "") { currentState ->
            when (currentState.value) {
                is Screen.LoginScreen -> {
                    LoginScreen()
                }
                else -> {}
            }
        }
    }
}