package com.expensehound.app.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.expensehound.app.data.entity.PurchaseItem
import com.expensehound.app.data.entity.getCurrencyString
import com.expensehound.app.ui.screens.purchases.df
import com.expensehound.app.ui.theme.margin_half
import com.expensehound.app.ui.theme.margin_standard
import com.expensehound.app.ui.viewmodel.BasePurchaseItemInput

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun PurchaseItemCard(
    purchaseItem: PurchaseItem,
    purchaseIntent: MutableState<Boolean>,
    basePurchaseItemInput: BasePurchaseItemInput,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    additionalActions: @Composable() (() -> Unit)? = null
) {
    val context = LocalContext.current
    val openDeleteDialog = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(margin_half)
            .clickable { onClick() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = margin_standard, vertical = margin_half)
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column() {
                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = purchaseItem.name,
                        style = MaterialTheme.typography.displayLarge,
                    )
                    Text(
                        text = df.format(purchaseItem.price) + getCurrencyString(context, purchaseItem.currency),
                        style = MaterialTheme.typography.headlineSmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }

                Row {
                    if (additionalActions != null) {
                        additionalActions()
                    }

                    PurchaseItemCardMainActionButtons(
                        openDeleteDialog = openDeleteDialog,
                        purchaseIntent = purchaseIntent,
                        purchaseItemInput = basePurchaseItemInput,
                        purchaseItem = purchaseItem,
                        onDelete = onDelete
                    )
                }
            }

            if (purchaseItem.image != null) {
                Image(
                    purchaseItem.image.asImageBitmap(),
                    null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxHeight().clip(RoundedCornerShape(15.dp, 0.dp, 0.dp, 15.dp))
                )
            }
        }
    }
}
