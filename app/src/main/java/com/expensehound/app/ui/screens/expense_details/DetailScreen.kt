package com.expensehound.app.ui.screens.expense_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DragHandle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.expensehound.app.data.PurchaseItem
import com.expensehound.app.ui.screens.all_expenses.df
import com.expensehound.app.ui.theme.ComposeTemplateTheme
import com.expensehound.app.ui.theme.card_corner_radius_lg
import com.expensehound.app.ui.theme.margin_double
import com.expensehound.app.ui.theme.margin_standard

@Composable
fun DetailBody(
    purchaseItem: PurchaseItem,
) {
    ComposeTemplateTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(card_corner_radius_lg)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = margin_double, end = margin_double)
            ) {
                Icon(
                    Icons.Outlined.DragHandle,
                    contentDescription = "Drag Sheet",
                )

                Text(
                    text = purchaseItem.name,
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.padding(top = margin_standard)
                )

                Row( modifier = Modifier.padding(bottom = margin_standard)) {

                    Text(
                        text = purchaseItem.category.displayName,
                        style = MaterialTheme.typography.displaySmall,
                    )
                    Text(
                        text = " (" + df.format(purchaseItem.price) + purchaseItem.currency.displayName + ")" ,
                        style = MaterialTheme.typography.displaySmall,
                    )
                }

                Box(modifier = Modifier.height(256.dp)) {
                    (purchaseItem.image ?: purchaseItem.imageLarge).let {
                        if (it == null) {
                            return@Box
                        }
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = it
                            ),
                            contentDescription = purchaseItem.name,
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                }

                // This is to give the bottom sheet some room to scroll to fill height
                Spacer(Modifier.height(512.dp))
            }
        }
    }
}
