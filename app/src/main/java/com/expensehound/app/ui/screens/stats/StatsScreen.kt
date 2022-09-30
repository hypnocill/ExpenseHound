package com.expensehound.app.ui.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expensehound.app.R
import com.expensehound.app.data.StatsPurchaseItemsByCategory
import com.expensehound.app.ui.screens.future_expenses.df
import com.expensehound.app.ui.theme.ComposeTemplateTheme
import com.expensehound.app.ui.theme.card_corner_radius_lg
import com.expensehound.app.ui.theme.margin_half
import com.expensehound.app.ui.theme.margin_quarter
import com.expensehound.app.ui.theme.margin_standard
import me.bytebeats.views.charts.pie.PieChart
import me.bytebeats.views.charts.pie.PieChartData
import me.bytebeats.views.charts.pie.render.SimpleSliceDrawer
import me.bytebeats.views.charts.simpleChartAnimation


@Composable
fun StatsScreen(
    items: SnapshotStateList<StatsPurchaseItemsByCategory>
) {

    val COLORS = listOf<Color>(
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

    var totalSumPrice = 0.0

    items.forEach {
        totalSumPrice += it.sumPrice
    }

    ComposeTemplateTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(card_corner_radius_lg)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(id = R.string.stats_expenses))
                    Text(text = totalSumPrice.toString() + "лв.", fontSize = 35.sp)
                }

                Divider(
                    color = MaterialTheme.colorScheme.outline,
                    thickness = 0.5.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = margin_standard)
                )

                Row(

                ) {
                    if (items.isNotEmpty()) {
                        PieChart(
                            pieChartData = PieChartData(
                                slices = items.mapIndexed { index, it ->
                                    PieChartData.Slice(
                                        value = it.count.toFloat(),
                                        color = COLORS[index]
                                    )
                                }

                            ),
                            modifier = Modifier
                                .width(200.dp)
                                .height(200.dp),
                            animation = simpleChartAnimation(),
                            sliceDrawer = SimpleSliceDrawer(35F)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = margin_half, horizontal = margin_standard),
                ) {
                    items.mapIndexed { index, it ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(
                                horizontal = margin_half,
                                vertical = margin_quarter
                            )
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(margin_standard),
                                painter = painterResource(id = R.drawable.ic_baseline_circle_24),
                                contentDescription = ".",
                                tint = COLORS[index]
                            )
                            Text(
                                modifier = Modifier.padding(horizontal = margin_half),
                                text = "${it.category.displayName} (${it.count}) - ${df.format(it.sumPrice)}лв."
                            )
                        }
                    }
                }

            }
        }
    }
}
