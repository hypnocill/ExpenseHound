package com.expensehound.app.ui.screens.purchases

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.expensehound.app.R
import com.expensehound.app.data.entity.Currency
import com.expensehound.app.data.entity.PurchaseItem
import com.expensehound.app.data.entity.getCurrencyString
import com.expensehound.app.ui.components.AppFilterChip
import com.expensehound.app.ui.components.AppItemCard
import com.expensehound.app.ui.components.EmptyListText
import com.expensehound.app.ui.components.NewPurchaseScreenAnimated
import com.expensehound.app.ui.theme.margin_half
import com.expensehound.app.ui.viewmodel.MainViewModel
import com.expensehound.app.utils.getStartOfMonthAsTimestamp
import com.expensehound.app.utils.loadPurchaseInputInState
import formatPrice

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun PurchasesScreen(
    viewModel: MainViewModel,
    onItemClicked: (PurchaseItem, Int) -> Unit
) {
    val context = LocalContext.current
    val currency = getCurrencyString(context, Currency.BGN)
    var list = remember { mutableStateListOf<PurchaseItem>() }

    LaunchedEffect(key1 = viewModel.purchasesFiltersMonth.value) {
        var from: Long? = null

        if (viewModel.purchasesFiltersMonth.value) {
            from = getStartOfMonthAsTimestamp()
        }

        viewModel.getAllPurchaseItems(list, from)
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                AppFilterChip(
                    stringResource(id = R.string.filters_current_month),
                    viewModel.purchasesFiltersMonth.value
                ) {
                    viewModel.setPurchaseFilterMonth(!viewModel.purchasesFiltersMonth.value)
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
                            viewModel.newPurchaseIntent.value = true
                            loadPurchaseInputInState(viewModel.newPurchaseInput, item)
                        },
                        onDelete = { viewModel.deletePurchaseItem(item.uid) }
                    )
                }
            }
        }
    }
}

