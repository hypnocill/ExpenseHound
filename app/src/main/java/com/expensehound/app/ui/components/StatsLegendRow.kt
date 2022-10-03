package com.expensehound.app.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.expensehound.app.R
import com.expensehound.app.ui.theme.margin_half
import com.expensehound.app.ui.theme.margin_quarter
import com.expensehound.app.ui.theme.margin_standard

@Composable
fun StatsLegendRow(text: String, color: Color) {
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
            tint = color
        )
        Text(
            modifier = Modifier.padding(horizontal = margin_half),
            text = text
        )
    }
}
