package com.expensehound.app.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.expensehound.app.R
import com.expensehound.app.ui.theme.margin_double


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun AppItemCardActionButtons(
    openDeleteDialog: MutableState<Boolean>,
    name: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    disableEdit: Boolean = false
) {
    Row {
        if (!disableEdit) {
            Icon(
                modifier = Modifier
                    .size(margin_double)
                    .fillMaxHeight()
                    .clickable {
                        onEdit()
                    },
                painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                contentDescription = ".",
                tint = MaterialTheme.colorScheme.outline
            )
        }

        Icon(
            modifier = Modifier
                .size(margin_double)
                .fillMaxHeight()
                .clickable { openDeleteDialog.value = true },
            painter = painterResource(id = R.drawable.ic_baseline_delete_forever_24),
            contentDescription = ".",
            tint = MaterialTheme.colorScheme.onErrorContainer
        )

        DeleteAlert(openDeleteDialog, name, onDelete)
    }
}