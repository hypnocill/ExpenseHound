package com.expensehound.app.ui.screens.purchase_details

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DragHandle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.expensehound.app.R
import com.expensehound.app.data.entity.Category
import com.expensehound.app.data.entity.Currency
import com.expensehound.app.data.entity.PurchaseItem
import com.expensehound.app.data.entity.getCategoryString
import com.expensehound.app.data.entity.getCurrencyString
import com.expensehound.app.data.entity.getRecurringIntervalString
import com.expensehound.app.ui.screens.purchases.df
import com.expensehound.app.ui.theme.ComposeTemplateTheme
import com.expensehound.app.ui.theme.card_corner_radius_lg
import com.expensehound.app.ui.theme.margin_double
import com.expensehound.app.ui.theme.margin_standard
import com.expensehound.app.ui.viewmodel.MainViewModel
import com.expensehound.app.utils.convertTimestampToDatetimeString

@Composable
fun PurchaseDetailsScreen(
    viewModel: MainViewModel,
    uid: Int,
) {
    val context = LocalContext.current
    var purchaseItems = remember { mutableStateListOf<PurchaseItem>() }

    LaunchedEffect(key1 = true) {
        viewModel.getPurchaseItemById(uid, purchaseItems)
    }

    val clipboardManager: androidx.compose.ui.platform.ClipboardManager =
        LocalClipboardManager.current

    ComposeTemplateTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(card_corner_radius_lg)
        ) {
            val purchaseItem = if (purchaseItems.isNotEmpty()) purchaseItems.first()
            else PurchaseItem(
                -1,
                "",
                null,
                Category.OTHERS,
                0.00,
                false,
                "",
                Currency.BGN,
                createdAt = -1
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = margin_double, end = margin_double)
                    .verticalScroll(rememberScrollState())

            ) {
                Icon(
                    Icons.Outlined.DragHandle,
                    contentDescription = "Drag Sheet",
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = purchaseItem.name,
                        style = MaterialTheme.typography.displayLarge,
                    )

                    IconButton(onClick = {
                        val comment =
                            if (purchaseItem.comment != "") "  (${purchaseItem.comment})" else ""

                        clipboardManager.setText(
                            AnnotatedString(
                                purchaseItem.name + ", " + purchaseItem.price.toString() + getCurrencyString(
                                    context,
                                    purchaseItem.currency
                                ) + comment
                            )
                        )
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_content_copy_24),
                            contentDescription = ".",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                DetailsRow(
                    stringResource(id = R.string.category),
                    getCategoryString(context, purchaseItem.category)
                )

                val price = df.format(purchaseItem.price).toString() + getCurrencyString(
                    context,
                    purchaseItem.currency
                )
                DetailsRow(stringResource(id = R.string.price), price)

                Row(modifier = Modifier.fillMaxWidth()) {
                    DetailsRow(
                        stringResource(id = R.string.recurring_interval_description),
                        getRecurringIntervalString(
                            context,
                            purchaseItem.recurringInterval,
                        )
                    ) {
                        IconButton(onClick = {
                            Log.d("asd", "CLICKED")
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_info_24),
                                contentDescription = ".",
                                tint = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }

                DetailsRow(
                    stringResource(id = R.string.created_at),
                    convertTimestampToDatetimeString(purchaseItem.createdAt)
                )
                DetailsRow(stringResource(id = R.string.comment), purchaseItem.comment)

                Box(
                    modifier = Modifier
                        .height(256.dp)
                        .padding(vertical = margin_standard)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = purchaseItem.image
                        ),
                        contentDescription = purchaseItem.name,
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier.fillMaxHeight()
                    )
                }

                // This is to give the bottom sheet some room to scroll to fill height
                Spacer(Modifier.height(200.dp))
            }

        }
    }
}

@Composable
fun DetailsRow(
    title: String,
    description: String,
    actionButton: @Composable (() -> Unit?)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = margin_standard),
    ) {
        Column {
            Text(
                text = "$title:",
                style = MaterialTheme.typography.titleSmall,
            )

            Text(
                text = description,
                style = MaterialTheme.typography.titleMedium,
            )
        }

        if (actionButton != null) {
            actionButton()
        }
    }
}

