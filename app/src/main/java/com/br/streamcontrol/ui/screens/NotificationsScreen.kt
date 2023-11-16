package com.br.streamcontrol.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastDistinctBy
import com.br.streamcontrol.data.dummy.NotificationsData
import com.br.streamcontrol.domain.routes.Router
import com.br.streamcontrol.domain.routes.Screen
import com.br.streamcontrol.domain.routes.OnBackPress
import com.br.streamcontrol.ui.widgets.ConfirmDialog
import java.util.Date
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen() {
    val formattedDateAgo = formatDateAgo(Date())
    val openDialog = remember { mutableStateOf(false) }
    var notifications by remember { mutableStateOf(NotificationsData.items) }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Notificações", maxLines = 1, overflow = TextOverflow.Ellipsis)
                },
                navigationIcon = {
                    IconButton(onClick = { Router.navigateTo(Screen.HomeScreen) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Go back"
                        )
                    }
                },
                actions = {
                    if (notifications.isNotEmpty()) {
                        notifications.fastDistinctBy {  }
                        IconButton(
                            onClick = {
                                openDialog.value = true
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "Delete"
                                )
                            }
                        )
                    }
                }
            )
            ConfirmDialog(
                onConfirm = {
                    notifications = emptyList()
                },
                dialogState = openDialog,
                message = "Você deseja excluir todas as notificações?"
            )
        },
        content = { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val list = notifications
                items(count = list.size) { index ->
                    Column(modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp)) {
                        Text(
                            text = "Oferta imperdível $index",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Olha só o que você está perdendo..."
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = formattedDateAgo,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 4.dp, end = 4.dp),
                            color = Color.LightGray
                        )
                    }
                }
            }
        }
    )

    OnBackPress {
        Router.navigateTo(Screen.HomeScreen)
    }
}

fun formatDateAgo(date: Date): String {
    val currentTime = System.currentTimeMillis()
    val diffMillis = currentTime - date.time
    val diffHours = TimeUnit.MILLISECONDS.toHours(diffMillis)

    return if (diffHours < 1) {
        "Agora mesmo"
    } else {
        "$diffHours horas atrás"
    }
}