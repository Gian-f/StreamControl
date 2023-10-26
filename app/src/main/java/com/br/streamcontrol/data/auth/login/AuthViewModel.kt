package com.br.streamcontrol.data.auth.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.br.streamcontrol.data.rules.ValidationResult
import com.br.streamcontrol.data.rules.Validator
import com.br.streamcontrol.ui.routes.Router
import com.br.streamcontrol.ui.routes.Screen
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {
    private val TAG = AuthViewModel::class.simpleName

    var isEmailFieldTouched = mutableStateOf(false)
    var isPasswordFieldTouched = mutableStateOf(false)

    var loginUIState = mutableStateOf(
        LoginUIState(
            emailError = false,
            passwordError = false
        )
    )

    var allValidationsPassed = mutableStateOf(false)

    var loginInProgress = mutableStateOf(false)


    fun onEvent(event: LoginUIEvent) {
        when (event) {
            is LoginUIEvent.EmailChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    email = event.email
                )
                isEmailFieldTouched.value = true
            }

            is LoginUIEvent.PasswordChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    password = event.password
                )
                isPasswordFieldTouched.value = true
            }

            is LoginUIEvent.LoginButtonClicked -> {
                login()
            }
        }
        validateLoginUIDataWithRules()
    }

    private fun validateLoginUIDataWithRules() {
        val emailResult = if (isEmailFieldTouched.value && loginUIState.value.email.isNotEmpty()) {
            Validator.validateEmail(loginUIState.value.email)
        } else {
            ValidationResult(status = false)
        }

        val passwordResult =
            if (isPasswordFieldTouched.value && loginUIState.value.password.isNotEmpty()) {
                Validator.validatePassword(loginUIState.value.password)
            } else {
                ValidationResult(status = false)
            }

        loginUIState.value = loginUIState.value.copy(
            emailError = emailResult.status,
            passwordError = passwordResult.status
        )

        allValidationsPassed.value = emailResult.status == true && passwordResult.status == true
    }


    private fun login() {

        loginInProgress.value = true
        val email = loginUIState.value.email
        val password = loginUIState.value.password

        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d(TAG, "Inside_login_success")
                Log.d(TAG, "${it.isSuccessful}")

                if (it.isSuccessful) {
                    loginInProgress.value = false
                    Router.navigateTo(Screen.HomeScreen)
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Inside_login_failure")
                Log.d(TAG, it.localizedMessage)

                loginInProgress.value = false

            }
    }
}
