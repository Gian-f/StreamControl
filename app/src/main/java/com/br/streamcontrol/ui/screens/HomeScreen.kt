package com.br.streamcontrol.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.br.streamcontrol.data.dummy.NavigationItemDummy.navItems
import com.br.streamcontrol.data.home.HomeViewModel
import com.br.streamcontrol.ui.routes.Router
import com.br.streamcontrol.ui.routes.Screen
import com.br.streamcontrol.ui.routes.SystemBackButtonHandler
import com.br.streamcontrol.ui.widgets.ConfirmDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }
    homeViewModel.getUserData()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Home", maxLines = 1, overflow = TextOverflow.Ellipsis)
                },
                actions = {
                    IconButton(
                        onClick = { },
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsNone,
                            contentDescription = "Notifications"
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(70.dp))
        },
        content = { contentPadding ->
            ContentHeader(contentPadding, homeViewModel)
        },
        bottomBar = {
            BottomNavigation()
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add")
            }
        }
    )

    SystemBackButtonHandler {
        openDialog.value = true
    }

    ConfirmDialog(
        dialogState = openDialog,
        message = "Tem certeza que deseja sair?",
        onConfirm = {
            homeViewModel.logout()
            Router.navigateTo(Screen.LoginScreen)
        },
    )
}

@Composable
private fun ContentHeader(
    contentPadding: PaddingValues,
    homeViewModel: HomeViewModel
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPadding)
    ) {
        Icon(
            imageVector = Icons.Rounded.Person,
            modifier = Modifier.size(50.dp),
            contentDescription = ""
        )
        Column {
            Text(
                text = "Seja bem vindo,",
                fontSize = TextUnit(18F, TextUnitType.Sp),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                text = "${homeViewModel.emailId.value}",
                fontSize = TextUnit(22F, TextUnitType.Sp),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun BottomNavigation() {

    var selectedItem by remember { mutableIntStateOf(0) }

    NavigationBar {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.selectedIcon, contentDescription = item.description) },
                selected = selectedItem == index,
                onClick = { selectedItem = index }
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}