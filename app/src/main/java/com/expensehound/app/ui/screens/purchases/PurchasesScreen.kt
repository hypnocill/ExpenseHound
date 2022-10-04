package com.expensehound.app.ui.screens.purchases

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.expensehound.app.R
import com.expensehound.app.data.entity.PurchaseItem
import com.expensehound.app.ui.components.AppFilterChip
import com.expensehound.app.ui.components.NewPurchaseScreenAnimated
import com.expensehound.app.ui.components.PurchaseItemCard
import com.expensehound.app.ui.theme.margin_half
import com.expensehound.app.ui.viewmodel.MainViewModel
import com.expensehound.app.utils.getStartOfMonthAsTimestamp
import java.text.DecimalFormat

val df = DecimalFormat("#.##")

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun PurchasesScreen(
    onItemClicked: (PurchaseItem, Int) -> Unit,
    viewModel: MainViewModel,
) {
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
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End ) {
                AppFilterChip(stringResource(id = R.string.filters_current_month), viewModel.purchasesFiltersMonth.value) {
                    viewModel.setPurchaseFilterMonth(!viewModel.purchasesFiltersMonth.value)
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(top = margin_half)
            ) {
                itemsIndexed(items = list) { index, item ->
                    PurchaseItemCard(
                        item,
                        viewModel.newPurchaseIntent,
                        viewModel.newPurchaseInput,
                        { onItemClicked(item, index) },
                        { viewModel.deletePurchaseItem(item.uid) }
                    )
                }
            }
        }
        NewPurchaseScreenAnimated(
            isVisible = viewModel.newPurchaseIntent.value,
            input = viewModel.newPurchaseInput,
            purchaseIntent = viewModel.newPurchaseIntent
        )
    }
}

