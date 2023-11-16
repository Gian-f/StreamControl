package com.br.streamcontrol.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.br.streamcontrol.R
import com.br.streamcontrol.data.dummy.NavigationItemDummy.navItems
import com.br.streamcontrol.domain.routes.OnBackPress
import com.br.streamcontrol.domain.routes.Router
import com.br.streamcontrol.domain.routes.Screen
import com.br.streamcontrol.domain.viewmodel.HomeViewModel
import com.br.streamcontrol.domain.viewmodel.LocationViewModel
import com.br.streamcontrol.ui.permissions.RequestPermission
import com.br.streamcontrol.ui.screens.header.TopAppBarForCards
import com.br.streamcontrol.ui.screens.header.TopAppBarForChart
import com.br.streamcontrol.ui.screens.header.TopAppBarForHome
import com.br.streamcontrol.ui.screens.header.TopAppBarForProfile
import com.br.streamcontrol.ui.screens.navigation.CardsContent
import com.br.streamcontrol.ui.screens.navigation.ChartContent
import com.br.streamcontrol.ui.screens.navigation.ProfileContent
import com.br.streamcontrol.ui.widgets.ConfirmDialog
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.entryModelOf

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }
    val selectedItemState = remember { mutableIntStateOf(0) }
    val locationViewModel: LocationViewModel = viewModel()
    RequestPermission(locationViewModel)
    homeViewModel.getUserData()
    homeViewModel.getAllUsers()

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
                1 -> CardsContent(contentPadding)
                2 -> ChartContent(contentPadding, homeViewModel)
                3 -> ProfileContent(contentPadding, homeViewModel)
                else -> HomeContent(contentPadding, homeViewModel)
            }
        },
        bottomBar = {
            BottomNavigation(selectedItemState, homeViewModel)
        },
        floatingActionButton = {
            if (selectedItemState.intValue == 0) {
                ExtendedFloatingActionButton(
                    onClick = { /*TODO*/ },
                    content = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.TwoTone.Add, contentDescription = "")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Adicionar")
                        }
                    }
                )
            }
            if (selectedItemState.intValue == 1) {
                ExtendedFloatingActionButton(
                    modifier = Modifier.height(70.dp),
                    onClick = { Router.navigateTo(Screen.CardDetailsScreen) },
                    content = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.TwoTone.Add, contentDescription = "")
                        }
                    }
                )
            }
        }
    )

    OnBackPress {
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
    homeViewModel: HomeViewModel,
) {
    val imageUri by remember { mutableStateOf(homeViewModel.localUserPhoto.value) }
    Column {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(contentPadding)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = CircleShape,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                if (imageUri != Uri.EMPTY) {
                    AsyncImage(
                        modifier = Modifier.size(50.dp),
                        contentScale = ContentScale.FillBounds,
                        model = imageUri,
                        contentDescription = null,
                        alignment = Alignment.Center
                    )
                } else {
                    Box(
                        modifier = Modifier.size(60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            modifier = Modifier.size(30.dp),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Column {
                Text(
                    text = "Seja bem vindo,",
                    fontSize = TextUnit(16F, TextUnitType.Sp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    text = homeViewModel.emailId.collectAsState().value,
                    fontSize = TextUnit(20F, TextUnitType.Sp),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                UsageCard()
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
                EntertainmentCard()
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Serviços adicionados", fontWeight = FontWeight.SemiBold)
                    Text(text = "Ver mais")
                }
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    item {
                        ElevatedCard(
                            modifier = Modifier
                                .width(120.dp)
                                .height(150.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.netflix),
                                modifier = Modifier
                                    .height(120.dp)
                                    .fillMaxWidth()
                                    .padding(22.dp)
                                    .clip(CircleShape),
                                contentDescription = "netflix"
                            )
                            Text(
                                text = "Netflix", textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    item {
                        ElevatedCard(
                            modifier = Modifier
                                .width(120.dp)
                                .height(150.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.primevideo),
                                alignment = Alignment.Center,
                                modifier = Modifier
                                    .height(120.dp)
                                    .fillMaxWidth()
                                    .padding(22.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.FillBounds,
                                contentDescription = "prime"
                            )
                            Text(
                                text = "Prime",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    item {
                        ElevatedCard(
                            modifier = Modifier
                                .width(120.dp)
                                .height(150.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .clickable {

                                }
                        ) {
                            Image(
                               imageVector = Icons.Outlined.Add,
                                alignment = Alignment.Center,
                                modifier = Modifier
                                    .height(120.dp)
                                    .fillMaxWidth()
                                    .padding(22.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.FillBounds,
                                contentDescription = "Adicionar"
                            )
                            Text(
                                text = "Adicionar",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UsageCard() {
    var cardHeight by remember { mutableStateOf(150.dp) }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Uso",
                fontSize = TextUnit(17F, TextUnitType.Sp),
                fontWeight = FontWeight.W600,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            IconButton(onClick = { cardHeight = if (cardHeight == 150.dp) 250.dp else 150.dp }) {
                Icon(
                    imageVector = if (cardHeight == 150.dp) Icons.Outlined.KeyboardArrowDown else Icons.Outlined.KeyboardArrowUp,
                    contentDescription = "arrowDown"
                )
            }
        }
        Row {
            Column {

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .width(150.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.netflix),
                        modifier = Modifier.height(30.dp),
                        contentDescription = "netflix"
                    )
                    Text(
                        text = "Netflix",
                        fontSize = TextUnit(16F, TextUnitType.Sp),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "18:27 h",
                    fontSize = TextUnit(20F, TextUnitType.Sp),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Essa semana",
                    textAlign = TextAlign.End,
                    fontSize = TextUnit(13F, TextUnitType.Sp),
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            val chartEntryModel = entryModelOf(6f, 2f, 10f)
            Charts(chartEntryModel)
        }
    }
}


@Composable
private fun Charts(chartEntryModel: ChartEntryModel) {
    ProvideChartStyle(rememberChartStyle(chartColors)) {
        Chart(
            chart = lineChart(),
            model = chartEntryModel,
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(valueFormatter = bottomAxisValueFormatter),
            marker = rememberMarker(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EntertainmentCard() {
    var cardHeight by remember { mutableStateOf(120.dp) }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Entretenimento",
                    fontSize = TextUnit(17F, TextUnitType.Sp),
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                BadgedBox(badge = {
                    Badge {
                        Text("2")
                    }
                }) {}
            }
            IconButton(onClick = { cardHeight = if (cardHeight == 120.dp) 250.dp else 120.dp }) {
                Icon(
                    imageVector = if (cardHeight == 120.dp) Icons.Outlined.KeyboardArrowDown else Icons.Outlined.KeyboardArrowUp,
                    contentDescription = "arrow"
                )
            }
        }
        Text(
            text = "R$ 250,00",
            textAlign = TextAlign.Center,
            fontSize = TextUnit(22F, TextUnitType.Sp),
            fontWeight = FontWeight.W500,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )
        Text(
            text = "Total Gasto",
            textAlign = TextAlign.Center,
            fontSize = TextUnit(13F, TextUnitType.Sp),
            fontWeight = FontWeight.W500,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(40.dp))
        Divider(modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn {
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.netflix),
                            modifier = Modifier
                                .height(30.dp)
                                .width(25.dp),
                            contentDescription = "netflix"
                        )
                        Text(
                            text = "Netflix",
                            fontSize = TextUnit(16F, TextUnitType.Sp),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    Text(
                        text = "R$ 100,00",
                        fontSize = TextUnit(14F, TextUnitType.Sp),
                        fontWeight = FontWeight.W500,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.primevideo),
                            modifier = Modifier
                                .height(30.dp)
                                .width(25.dp),
                            contentDescription = "amazon"
                        )
                        Text(
                            text = "Prime Video",
                            fontSize = TextUnit(16F, TextUnitType.Sp),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    Text(
                        text = "R$ 150,00",
                        fontSize = TextUnit(14F, TextUnitType.Sp),
                        fontWeight = FontWeight.W500,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigation(selectedItemState: MutableState<Int>, homeViewModel: HomeViewModel) {
    NavigationBar {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.selectedIcon, contentDescription = item.description) },
                selected = selectedItemState.value == index,
                onClick = {
                    selectedItemState.value = index
                    if (index == 0) {
                        homeViewModel.getAllUsers()
                    }
                }
            )
        }
    }
}
