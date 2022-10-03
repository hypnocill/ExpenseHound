package com.expensehound.app.ui.screens.purchases

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.expensehound.app.R
import com.expensehound.app.data.entity.PurchaseItem
import com.expensehound.app.ui.components.NewPurchaseScreenAnimated
import com.expensehound.app.ui.components.PurchaseItemCard
import com.expensehound.app.ui.screens.desires.DeleteAlert
import com.expensehound.app.ui.theme.margin_double
import com.expensehound.app.ui.theme.margin_half
import com.expensehound.app.ui.viewmodel.BasePurchaseItemInput
import com.expensehound.app.ui.viewmodel.MainViewModel
import java.text.DecimalFormat

val df = DecimalFormat("#.##")

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun PurchasesScreen(
    onItemClicked: (PurchaseItem, Int) -> Unit,
    viewModel: MainViewModel,
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(top = margin_half)
            ) {
                itemsIndexed(items = viewModel.purchasesList) { index, item ->
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
