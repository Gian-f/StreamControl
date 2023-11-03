package com.br.streamcontrol.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.br.streamcontrol.data.dummy.NavigationItemDummy.navItems
import com.br.streamcontrol.domain.viewmodel.HomeViewModel
import com.br.streamcontrol.domain.routes.Router
import com.br.streamcontrol.domain.routes.Screen
import com.br.streamcontrol.domain.routes.SystemBackButtonHandler
import com.br.streamcontrol.ui.screens.bottomBar.CardsContent
import com.br.streamcontrol.ui.screens.bottomBar.ChartContent
import com.br.streamcontrol.ui.screens.bottomBar.ProfileContent
import com.br.streamcontrol.ui.screens.topBar.TopAppBarForCards
import com.br.streamcontrol.ui.screens.topBar.TopAppBarForChart
import com.br.streamcontrol.ui.screens.topBar.TopAppBarForHome
import com.br.streamcontrol.ui.screens.topBar.TopAppBarForProfile
import com.br.streamcontrol.ui.widgets.ConfirmDialog

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }
    val selectedItemState = remember { mutableIntStateOf(0) }

    homeViewModel.getUserData()

    Scaffold(
        topBar = {
            when (selectedItemState.intValue) {
                0 -> TopAppBarForHome()
                1 -> TopAppBarForCards()
                2 -> TopAppBarForChart()
                3 -> TopAppBarForProfile()
                else -> TopAppBarForHome()
            }
        },
        content = { contentPadding ->
            when (selectedItemState.intValue) {
                0 -> HomeContent(contentPadding, homeViewModel)
                1 -> CardsContent(contentPadding, homeViewModel)
                2 -> ChartContent(contentPadding, homeViewModel)
                3 -> ProfileContent(contentPadding, homeViewModel)
                else -> HomeContent(contentPadding, homeViewModel)
            }
        },
        bottomBar = {
            BottomNavigation(selectedItemState)
        },
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
private fun HomeContent(
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
                text = "${homeViewModel.username.value}",
                fontSize = TextUnit(22F, TextUnitType.Sp),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun BottomNavigation(selectedItemState: MutableState<Int>) {
    NavigationBar {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.selectedIcon, contentDescription = item.description) },
                selected = selectedItemState.value == index,
                onClick = {
                    selectedItemState.value = index
                }
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}