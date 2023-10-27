package com.br.streamcontrol.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.br.streamcontrol.R
import com.br.streamcontrol.data.auth.login.AuthViewModel
import com.br.streamcontrol.data.auth.login.LoginUIEvent
import com.br.streamcontrol.ui.routes.Router
import com.br.streamcontrol.ui.routes.Screen
import com.br.streamcontrol.ui.widgets.ButtonComponent
import com.br.streamcontrol.ui.widgets.ClickableLoginTextComponent
import com.br.streamcontrol.ui.widgets.DividerTextComponent
import com.br.streamcontrol.ui.widgets.HeadingTextComponent
import com.br.streamcontrol.ui.widgets.MyTextFieldComponent
import com.br.streamcontrol.ui.widgets.PasswordTextFieldComponent

@Composable
fun LoginScreen(loginViewModel: AuthViewModel = viewModel()) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.banner),
            alignment = Alignment.Center,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentDescription = "banner"
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                alignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(100.dp),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.height(50.dp))

            HeadingTextComponent(value = stringResource(id = R.string.login))

            Spacer(modifier = Modifier.height(20.dp))

            MyTextFieldComponent(
                labelValue = stringResource(id = R.string.email),
                painterResource(id = R.drawable.message),
                onTextChanged = {
                    loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                },
                fieldName = "E-mail",
                errorStatus = loginViewModel.loginUIState.value.emailError,
            )

            Spacer(modifier = Modifier.height(10.dp))

            PasswordTextFieldComponent(
                labelValue = stringResource(id = R.string.password),
                painterResource(id = R.drawable.lock),
                onTextSelected = {
                    loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
                },
                errorStatus = loginViewModel.loginUIState.value.passwordError,
            )

            Spacer(modifier = Modifier.height(40.dp))

            ButtonComponent(
                value = stringResource(id = R.string.login),
                onButtonClicked = {
                    loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                },
                isEnabled = loginViewModel.allValidationsPassed.value
            )

            Spacer(modifier = Modifier.height(20.dp))

            DividerTextComponent()

            Spacer(modifier = Modifier.height(20.dp))

            ClickableLoginTextComponent(
                tryingToLogin = false,
                onTextSelected = {
                    Router.navigateTo(Screen.SignUpScreen)
                }
            )
        }
        if (loginViewModel.loginError.value || loginViewModel.loginInProgress.value) {
            LoginBottomSheet(
                errorMessage = loginViewModel.errorMessage.value,
                onDismiss = {
                    loginViewModel.onEvent(LoginUIEvent.DismissError)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginBottomSheet(
    errorMessage: String?,
    onDismiss: () -> Unit,
) {

    val sheetState = rememberModalBottomSheetState(true)
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            onDismiss.invoke()
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (errorMessage != null) {
                    Text(
                        text = "Atenção!",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = errorMessage,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        onClick = {
                            onDismiss.invoke()
                        },
                        shape = RoundedCornerShape(15.dp),
                    ) {
                        Text(
                            text = "OK",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 18.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    CircularProgressIndicator()
                }
            }
        }
    )
}

