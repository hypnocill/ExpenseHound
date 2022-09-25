package com.expensehound.app.ui.screens.stats

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.expensehound.app.data.PurchaseItem
import com.expensehound.app.ui.MainViewModel
import com.expensehound.app.ui.theme.ComposeTemplateTheme
import com.expensehound.app.ui.theme.card_corner_radius_lg


@Composable
fun StatsScreen(
    onItemClicked: (PurchaseItem, Int) -> Unit,
    demoViewModel: MainViewModel,
) {
    ComposeTemplateTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(card_corner_radius_lg)
        ) {
            Text(text = "Implement")
        }
    }
}
