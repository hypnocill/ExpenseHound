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
import com.expensehound.app.data.entity.PurchaseItem
import com.expensehound.app.ui.screens.desires.DeleteAlert
import com.expensehound.app.ui.theme.margin_double
import com.expensehound.app.ui.viewmodel.BasePurchaseItemInput


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun PurchaseItemCardMainActionButtons(
    openDeleteDialog: MutableState<Boolean>,
    purchaseIntent: MutableState<Boolean>,
    purchaseItemInput: BasePurchaseItemInput,
    purchaseItem: PurchaseItem,
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
                        purchaseIntent.value = true
                        purchaseItemInput.id.value = purchaseItem.uid
                        purchaseItemInput.text.value = purchaseItem.name
                        purchaseItemInput.image.value = purchaseItem.image
                        purchaseItemInput.selectedCategory.value = purchaseItem.category
                        purchaseItemInput.comment.value = purchaseItem.comment
                        purchaseItemInput.price.value = purchaseItem.price.toString()
                        purchaseItemInput.recurringInterval.value = purchaseItem.recurringInterval
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

        DeleteAlert(openDeleteDialog, purchaseItem, onDelete)
    }
}