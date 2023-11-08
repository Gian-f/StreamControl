package com.br.streamcontrol.ui.screens.topBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import com.br.streamcontrol.domain.routes.Router
import com.br.streamcontrol.domain.routes.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarForHome() {
    CenterAlignedTopAppBar(
        title = {
            Text("Home", maxLines = 1, overflow = TextOverflow.Ellipsis)
        },
        actions = {
            IconButton(
                onClick = {
                    Router.navigateTo(Screen.NotificationsScreen)
                },
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications"
                )
            }
        }
    )
}