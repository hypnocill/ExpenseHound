package com.expensehound.app.ui.all_expenses

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.expensehound.app.R
import com.expensehound.app.data.PurchaseItem
import com.expensehound.app.ui.MainViewModel
import com.expensehound.app.ui.new_expense.AddPurchaseItem
import com.expensehound.app.ui.theme.margin_half
import com.expensehound.app.ui.theme.margin_standard
import com.expensehound.app.ui.theme.touchpoint_lg
import java.text.DecimalFormat

val df = DecimalFormat("#.##")

/**
 * The Main List Screen
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PurchasesScreen(
    onItemClicked: (PurchaseItem, Int) -> Unit,
    demoViewModel: MainViewModel,
) {
    if (!demoViewModel.purchasesList.isNullOrEmpty()) {

        Surface(color = MaterialTheme.colorScheme.background) {
            Column {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(top = margin_half)
                ) {
                    itemsIndexed(items = demoViewModel.purchasesList) { index, item ->
                        ListItem(
                            purchaseItem = item,
                            onItemClicked = { onItemClicked(it, index) },
                            modifier = Modifier.fillParentMaxWidth()
                        )
                    }
                }
            }

            val transformOrigin = TransformOrigin(0.95f, 0.95f)
            AnimatedVisibility(
                visible = demoViewModel.newPurchaseIntent.value,
                enter = scaleIn(
                    animationSpec = tween(
                        durationMillis = 200, easing = CubicBezierEasing(0.42f, 0.0f, 0.58f, 1.0f)
                    ), transformOrigin = transformOrigin
                ) + fadeIn(),
                exit = scaleOut(
                    animationSpec = tween(
                        durationMillis = 200, easing = CubicBezierEasing(0.42f, 0.0f, 0.58f, 1.0f)
                    ),
                    transformOrigin = transformOrigin,
                ) + fadeOut(),
            ) {
                AddPurchaseItem(demoViewModel)
            }

        }
    }
}

@OptIn(coil.annotation.ExperimentalCoilApi::class)
@Composable
private fun ListItem(
    purchaseItem: PurchaseItem, onItemClicked: (PurchaseItem) -> Unit, modifier: Modifier = Modifier
) {
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
        ) {
            if (purchaseItem.image == null) {
                Icon(
                    modifier = Modifier
                        .size(touchpoint_lg)
                        .fillMaxHeight(),
                    painter = painterResource(id = R.drawable.ic_outline_monetization_on_24),
                    contentDescription = "."
                )
            } else {
                Image(
                    purchaseItem.image.asImageBitmap(),
                    null,
                    modifier = Modifier,
                )
            }
            Spacer(Modifier.width(margin_half))
            Column() {
                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = purchaseItem.name,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = df.format(purchaseItem.price) + purchaseItem.currency.displayName,
                    style = MaterialTheme.typography.displaySmall,
                )
            }
        }
    }
}