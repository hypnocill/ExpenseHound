package com.expensehound.app.ui.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expensehound.app.R
import com.expensehound.app.data.entity.Currency
import com.expensehound.app.data.entity.FulfilledDesire
import com.expensehound.app.data.entity.PurchaseItem
import com.expensehound.app.data.entity.StatsPurchaseItemsByCategory
import com.expensehound.app.data.entity.getCategoryString
import com.expensehound.app.data.entity.getCurrencyString
import com.expensehound.app.ui.components.AppFilterChip
import com.expensehound.app.ui.components.StatsLegendRow
import com.expensehound.app.ui.screens.desires.df
import com.expensehound.app.ui.theme.ExpenseHoundTheme
import com.expensehound.app.ui.theme.card_corner_radius_lg
import com.expensehound.app.ui.theme.margin_half
import com.expensehound.app.ui.theme.margin_standard
import com.expensehound.app.ui.viewmodel.MainViewModel
import com.expensehound.app.ui.viewmodel.StatsViewModel
import com.expensehound.app.utils.getStartOfMonthAsTimestamp
import me.bytebeats.views.charts.pie.PieChart
import me.bytebeats.views.charts.pie.PieChartData
import me.bytebeats.views.charts.pie.render.SimpleSliceDrawer
import me.bytebeats.views.charts.simpleChartAnimation

@Composable
fun StatsScreen(viewModel: MainViewModel, statsViewModel: StatsViewModel) {
    val context = LocalContext.current
    var purchases = remember { mutableStateListOf<StatsPurchaseItemsByCategory>() }
    var desires = remember { mutableStateListOf<PurchaseItem>() }
    var fulfilledDesires = remember { mutableStateListOf<FulfilledDesire>() }

    LaunchedEffect(key1 = statsViewModel.statsFiltersMonth.value) {
        var from: Long? = null

        if (statsViewModel.statsFiltersMonth.value) {
            from = getStartOfMonthAsTimestamp()
        }

        viewModel.newPurchaseIntent.value = false
        viewModel.getAllDesires(desires, from)
        statsViewModel.getAllPurchaseItemsGroupedByCategory(purchases, from)
        statsViewModel.getAllFulfilledDesires(fulfilledDesires, from)
    }

    var totalSumPrice = 0.0
    val fulfilledDesiresCount = fulfilledDesires.size

    purchases.forEach {
        totalSumPrice += it.sumPrice
    }

    ExpenseHoundTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(card_corner_radius_lg)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End ) {
                    AppFilterChip(stringResource(id = R.string.filters_current_month), statsViewModel.statsFiltersMonth.value) {
                        statsViewModel.setStatsFilterMonth(!statsViewModel.statsFiltersMonth.value)
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(id = R.string.stats_expenses))
                    Text(text = df.format(totalSumPrice) + getCurrencyString(context, Currency.BGN), fontSize = 35.sp)
                }

                Divider(
                    color = MaterialTheme.colorScheme.outline,
                    thickness = 0.5.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = margin_standard)
                )

                Text(
                    text = stringResource(id = R.string.stats_expenses_by_categoy),
                    modifier = Modifier
                        .padding(vertical = margin_standard))

                Row() {
                    if (purchases.isNotEmpty()) {
                        PieChart(
                            pieChartData = PieChartData(
                                slices = purchases.mapIndexed { index, it ->
                                    PieChartData.Slice(
                                        value = it.sumPrice.toFloat(),
                                        color = StatsColor(index)
                                    )
                                }

                            ),
                            modifier = Modifier
                                .width(200.dp)
                                .height(200.dp),
                            animation = simpleChartAnimation(),
                            sliceDrawer = SimpleSliceDrawer(45F)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = margin_half, horizontal = margin_standard),
                ) {
                    purchases.mapIndexed { index, it ->
                        val text =
                            "${getCategoryString(context, it.category)} (${it.count}) - ${df.format(it.sumPrice)}лв."

                        StatsLegendRow(text, StatsColor(index))
                    }
                }

                Divider(
                    color = MaterialTheme.colorScheme.outline,
                    thickness = 0.5.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = margin_standard)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.stats_desires),
                        modifier = Modifier
                            .padding(vertical = margin_standard))

                    PieChart(
                        pieChartData = PieChartData(
                            listOf(
                                PieChartData.Slice(
                                    value = desires.size.toFloat(),
                                    color = StatsColor(STATS_COLOR_PRIMARY_INDEX)
                                ),
                                PieChartData.Slice(
                                    value = fulfilledDesiresCount.toFloat(),
                                    color = StatsColor(STATS_COLOR_SECONDARY_INDEX)
                                ),
                            )
                        ),
                        modifier = Modifier
                            .width(200.dp)
                            .height(200.dp),
                        animation = simpleChartAnimation(),
                        sliceDrawer = SimpleSliceDrawer(45F)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = margin_half, horizontal = margin_standard),
                    ) {
                        val desiresData = listOf(
                            object: FulfilledDesiresStatsLegendsRow {
                                override val text = "${stringResource(id = R.string.stats_desires_text)}  (${desires.size})"
                                override val color = StatsColor(STATS_COLOR_PRIMARY_INDEX)
                            },
                            object: FulfilledDesiresStatsLegendsRow {
                                override val text = "${stringResource(id = R.string.stats_fulfilled_desires)} (${fulfilledDesiresCount})"
                                override val color = StatsColor(STATS_COLOR_SECONDARY_INDEX)
                            }
                        )

                        desiresData.mapIndexed { _, it -> StatsLegendRow(it.text, it.color) }
                    }
                }
            }
        }
    }
}

interface FulfilledDesiresStatsLegendsRow {
    val text: String
    val color: Color
}

val STATS_COLOR_PRIMARY_INDEX = 6;
val STATS_COLOR_SECONDARY_INDEX = 7;

@Composable
fun StatsColor(index: Int): Color {
    val colors = listOf(
        MaterialTheme.colorScheme.outline,
        MaterialTheme.colorScheme.tertiaryContainer,
        MaterialTheme.colorScheme.surfaceTint,
        MaterialTheme.colorScheme.outlineVariant,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.onBackground,
        MaterialTheme.colorScheme.inversePrimary,
        MaterialTheme.colorScheme.secondaryContainer,
        MaterialTheme.colorScheme.scrim,
    )

    return colors[index]
}
