package com.expensehound.app.ui.screens.desires

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.expensehound.app.R
import com.expensehound.app.data.entity.Currency
import com.expensehound.app.data.entity.FulfilledDesire
import com.expensehound.app.data.entity.PurchaseItem
import com.expensehound.app.data.entity.getCurrencyString
import com.expensehound.app.ui.components.AppFilterChip
import com.expensehound.app.ui.components.AppItemCard
import com.expensehound.app.ui.components.DateTimePicker
import com.expensehound.app.ui.components.EmptyListText
import com.expensehound.app.ui.components.NewPurchaseScreenAnimated
import com.expensehound.app.ui.services.notifications.AppNotificationManager
import com.expensehound.app.ui.theme.margin_double
import com.expensehound.app.ui.theme.margin_half
import com.expensehound.app.ui.viewmodel.MainViewModel
import com.expensehound.app.utils.getStartOfMonthAsTimestamp
import com.expensehound.app.utils.loadPurchaseInputInState
import formatPrice
import java.text.DecimalFormat
import java.util.*

val df = DecimalFormat("#.##")

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun DesiresScreen(
    viewModel: MainViewModel,
    onItemClicked: (PurchaseItem, Int) -> Unit
) {
    val context = LocalContext.current
    val currency = getCurrencyString(context, Currency.BGN)
    var list = remember { mutableStateListOf<PurchaseItem>() }

    LaunchedEffect(key1 = viewModel.desiresFiltersMonth.value) {
        var from: Long? = null

        if (viewModel.desiresFiltersMonth.value) {
            from = getStartOfMonthAsTimestamp()
        }

        viewModel.getAllDesires(list, from)
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                AppFilterChip(
                    stringResource(id = R.string.filters_current_month),
                    viewModel.desiresFiltersMonth.value
                ) {
                    viewModel.setDesiresFilterMonth(!viewModel.desiresFiltersMonth.value)
                }
            }

            if (list.isEmpty()) {
                EmptyListText()
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(top = margin_half)
            ) {
                itemsIndexed(items = list) { index, item ->
                    AppItemCard(
                        title = item.name,
                        subtitle = formatPrice(item.price) + currency,
                        imageBitmap = item.image,
                        isCreatedAutomatically = item.createdAutomatically,
                        onClick = { onItemClicked(item, index) },
                        onEdit = {
                            viewModel.newFuturePurchaseIntent.value = true
                            loadPurchaseInputInState(viewModel.newFuturePurchaseInput, item)
                        },
                        onDelete = { viewModel.deletePurchaseItem(item.uid) }
                    ) {
                        FutureItemsAdditionalActionButtons(item, viewModel)
                    }
                }
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun FutureItemsAdditionalActionButtons(purchaseItem: PurchaseItem, viewModel: MainViewModel) {
    val context = LocalContext.current
    val openConvertDialog = remember { mutableStateOf(false) }
    val currentTime = Calendar.getInstance().timeInMillis

    var hasPendingNotification = false

    if (
        purchaseItem.notificationId != null
        && purchaseItem.notificationTimestamp != null
        && purchaseItem.notificationTimestamp >= currentTime
    ) {
        hasPendingNotification = true
    }

    val notificationIconTint =
        if (hasPendingNotification) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.outline

    Row {
        Icon(
            modifier = Modifier
                .size(margin_double)
                .fillMaxHeight()
                .clickable { openConvertDialog.value = true },
            painter = painterResource(id = R.drawable.ic_baseline_shopping_cart_24),
            contentDescription = ".",
            tint = MaterialTheme.colorScheme.outline
        )

        Icon(
            modifier = Modifier
                .size(margin_double)
                .fillMaxHeight()
                .clickable {
                    if (hasPendingNotification) {
                        viewModel.updatePurchaseItemNotification(purchaseItem.uid, null, null)

                        AppNotificationManager.cancelFuturePurchaseNotification(
                            context,
                            purchaseItem.notificationId!!
                        )
                        return@clickable
                    }

                    DateTimePicker.showDateTimePicker(context) {
                        if (it.timeInMillis < currentTime) {
                            return@showDateTimePicker
                        }

                        val notificationId =
                            AppNotificationManager.setFuturePurchaseNotification(
                                context,
                                it.timeInMillis,
                                purchaseItem
                            )

                        viewModel.updatePurchaseItemNotification(
                            purchaseItem.uid,
                            notificationId,
                            it.timeInMillis
                        )

                        Toast
                            .makeText(
                                context,
                                "Успешно добавено известие за ${purchaseItem.name}",
                                Toast.LENGTH_LONG
                            )
                            .show()
                    }
                },
            painter = painterResource(id = R.drawable.ic_baseline_notifications_24),
            contentDescription = ".",
            tint = notificationIconTint
        )
    }

    ConvertFutureExpenseToExpenseAlert(openConvertDialog, purchaseItem, viewModel)
}

@Composable
fun ConvertFutureExpenseToExpenseAlert(
    openDialog: MutableState<Boolean>,
    purchaseItem: PurchaseItem,
    viewModel: MainViewModel
) {
    val context = LocalContext.current

    if (!openDialog.value) {
        return
    }

    AlertDialog(
        onDismissRequest = {
            openDialog.value = false
        },
        title = {
            Text(text = "Потвърди")
        },
        text = {
            Text(text = "Ако вече си закупил ${purchaseItem.name}, можеш да го превърнеш в разход")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    openDialog.value = false

                    viewModel.updatePurchaseItemIsPurchased(purchaseItem.uid)
                    viewModel.insertFulfilledDesire(
                        FulfilledDesire(
                            purchaseItemId = purchaseItem.uid,
                            name = purchaseItem.name,
                            price = purchaseItem.price,
                            currency = purchaseItem.currency,
                            category = purchaseItem.category,
                            createdAt = System.currentTimeMillis()
                        )
                    )

                    Toast.makeText(
                        context,
                        "Успешно закупихте ${purchaseItem.name}",
                        Toast.LENGTH_LONG
                    ).show()
                }) {
                Text("Продължи")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    openDialog.value = false
                }) {
                Text("Откажи")
            }
        }
    )
}

