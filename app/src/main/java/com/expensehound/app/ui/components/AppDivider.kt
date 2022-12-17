package com.expensehound.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.expensehound.app.ui.theme.margin_standard

@Composable
fun AppDivier() {
    Divider(
        color = MaterialTheme.colorScheme.outline,
        thickness = 0.5.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = margin_standard)
    )
}