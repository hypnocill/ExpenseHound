package com.expensehound.app.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.expensehound.app.R

@Composable
fun RecurringIntervalInfoDialog(isVisible: MutableState<Boolean>) {
    if (isVisible.value) {
        AlertDialog(
            onDismissRequest = { isVisible.value = false },
            title = {
                Text(text = stringResource(id = R.string.info))
            },
            text = {
                Text(text = stringResource(id = R.string.recurring_interval_info))
            },
            confirmButton = {
                TextButton(onClick = {isVisible.value = false})
                { Text(text = stringResource(id = R.string.agreed)) }
            },
        )
    }
}