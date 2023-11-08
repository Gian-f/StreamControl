package com.br.streamcontrol.ui.screens.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.br.streamcontrol.domain.viewmodel.HomeViewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import kotlin.random.Random

@Composable
fun ChartContent(
    contentPadding: PaddingValues,
    homeViewModel: HomeViewModel
) {
    Chart(contentPadding)
}

@Composable
fun Chart(
    contentPadding: PaddingValues,
) {
    val chartEntryModelProducer = ChartEntryModelProducer(getRandomEntries())

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 50.dp)
    ) {
        com.patrykandpatrick.vico.compose.chart.Chart(
            chart = columnChart(),
            chartModelProducer = chartEntryModelProducer,
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(),
        )
    }
}
fun getRandomEntries() = List(4) { entryOf(it, Random.nextFloat() * 16f) }


