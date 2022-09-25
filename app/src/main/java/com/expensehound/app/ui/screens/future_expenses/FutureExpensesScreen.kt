package com.expensehound.app.ui.screens.future_expenses

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.expensehound.app.R
import com.expensehound.app.data.PurchaseItem
import com.expensehound.app.ui.MainViewModel
import com.expensehound.app.ui.components.DateTimePicker
import com.expensehound.app.ui.notifications.AppNotificationManager
import com.expensehound.app.ui.screens.all_expenses.ListItemMainActionButtonsRow
import com.expensehound.app.ui.screens.all_expenses.ShowAddPurchaseItem
import com.expensehound.app.ui.theme.margin_half
import com.expensehound.app.ui.theme.margin_standard
import com.expensehound.app.ui.theme.touchpoint
import java.text.DecimalFormat
import java.util.*


val df = DecimalFormat("#.##")

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun FutureExpensesScreen(
    onItemClicked: (PurchaseItem, Int) -> Unit,
    demoViewModel: MainViewModel,
) {
    if (!demoViewModel.futurePurchasesList.isNullOrEmpty()) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(top = margin_half)
                ) {
                    itemsIndexed(items = demoViewModel.futurePurchasesList) { index, item ->
                        ListItem(
                            purchaseItem = item,
                            onItemClicked = { onItemClicked(it, index) },
                            modifier = Modifier.fillParentMaxWidth(),
                            viewModel = demoViewModel,
                        )
                    }
                }
            }

            ShowAddPurchaseItem(
                isVisible = demoViewModel.newFuturePurchaseIntent.value,
                input = demoViewModel.newFuturePurchaseInput,
                purchaseIntent = demoViewModel.newPurchaseIntent
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(coil.annotation.ExperimentalCoilApi::class)
@Composable
private fun ListItem(
    purchaseItem: PurchaseItem,
    onItemClicked: (PurchaseItem) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    val openConvertDialog = remember { mutableStateOf(false) }
    val openDeleteDialog = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val currentTime = Calendar.getInstance().timeInMillis

    var hasPendingNotification = false
    val isConvertedToPurchase = purchaseItem.convertedAt != null

    if (
        purchaseItem.notificationId != null
        && purchaseItem.notificationTimestamp != null
        && purchaseItem.notificationTimestamp >= currentTime
    ) {
        hasPendingNotification = true
    }
    Log.d("asd", "NEW")
    val notificationIconTint =
        if (hasPendingNotification) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.outline

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(margin_half)
            .clickable { onItemClicked(purchaseItem) },
    ) {
        Row(
            modifier = modifier
                .padding(horizontal = margin_standard, vertical = margin_half)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {

            Column(modifier = Modifier.width(240.dp)) {
                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = purchaseItem.name,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = df.format(purchaseItem.price) + purchaseItem.currency.displayName,
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.Gray
                )
                Row {
                    Icon(
                        modifier = Modifier
                            .size(touchpoint)
                            .fillMaxHeight()
                            .clickable { openConvertDialog.value = true },
                        painter = painterResource(id = R.drawable.ic_baseline_check_circle_24),
                        contentDescription = ".",
                        tint = if (!isConvertedToPurchase) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.surfaceTint
                    )

                    if (!isConvertedToPurchase) {
                        Icon(
                            modifier = Modifier
                                .size(touchpoint)
                                .fillMaxHeight()
                                .clickable {
                                    val index =
                                        viewModel.futurePurchasesList.indexOfFirst { it.id == purchaseItem.id };

                                    if (hasPendingNotification) {
                                        viewModel.futurePurchasesList[index] =
                                            purchaseItem.copy(
                                                notificationId = null,
                                                notificationTimestamp = null
                                            )
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
                                        viewModel.futurePurchasesList[index] =
                                            purchaseItem.copy(
                                                notificationId = notificationId,
                                                notificationTimestamp = it.timeInMillis
                                            )
                                    }
                                },
                            painter = painterResource(id = R.drawable.ic_baseline_notifications_24),
                            contentDescription = ".",
                            tint = notificationIconTint
                        )
                    }
                }
            }

            Column {
                ListItemMainActionButtonsRow(
                    openDeleteDialog = openDeleteDialog,
                    purchaseIntent = viewModel.newFuturePurchaseIntent,
                    purchaseItemInput = viewModel.newFuturePurchaseInput,
                    purchaseItem = purchaseItem,
                    purchaseItemsList = viewModel.futurePurchasesList,
                    isConvertedToPurchase
                )

                ConvertFutureExpenseToExpenseAlert(openConvertDialog, purchaseItem, viewModel)

            }
        }
    }
}

@Composable
fun ConvertFutureExpenseToExpenseAlert(
    openDialog: MutableState<Boolean>,
    purchaseItem: PurchaseItem,
    viewModel: MainViewModel
) {

    if (!openDialog.value) {
        return Unit
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

                    val index = viewModel.futurePurchasesList.indexOfFirst { it.id == purchaseItem.id }
                    val updatedPurchaseItem = purchaseItem.copy(
                        convertedAt = Calendar.getInstance().time,
                        notificationId = null,
                        notificationTimestamp = null
                    )
                    viewModel.futurePurchasesList[index] = updatedPurchaseItem

                    viewModel.purchasesList.add(
                        updatedPurchaseItem.copy(id = viewModel.purchasesList.lastIndex)
                    )
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

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun DeleteAlert(
    openDialog: MutableState<Boolean>,
    purchaseItem: PurchaseItem,
    purchaseItemsList: SnapshotStateList<PurchaseItem>
) {

    val context = LocalContext.current

    if (!openDialog.value) {
        return Unit
    }

    AlertDialog(
        onDismissRequest = {
            openDialog.value = false
        },
        title = {
            Text(text = "Потвърди")
        },
        text = {
            Text(text = "Сигурни ли сте, че искате да изтриете ${purchaseItem.name}?")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    openDialog.value = false
                    purchaseItemsList.removeIf { it.id == purchaseItem.id }

                    Toast.makeText(
                        context,
                        "Успешно изтрихте " + purchaseItem.name,
                        Toast.LENGTH_LONG
                    ).show()
                }) {
                Text("Да")
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
