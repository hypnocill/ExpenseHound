package com.expensehound.app.ui.screens.purchase_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.expensehound.app.R
import com.expensehound.app.data.entity.PurchaseItem
import com.expensehound.app.ui.screens.purchases.df
import com.expensehound.app.ui.theme.ComposeTemplateTheme
import com.expensehound.app.ui.theme.card_corner_radius_lg
import com.expensehound.app.ui.theme.margin_double
import com.expensehound.app.ui.theme.margin_standard

@Composable
fun PurchaseDetailsScreen(
    purchaseItem: PurchaseItem,
) {
    val clipboardManager: androidx.compose.ui.platform.ClipboardManager =
        LocalClipboardManager.current

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

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = margin_standard)
                ) {
                    Text(
                        text = purchaseItem.name,
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.fillMaxWidth(0.6f)
                    )

                    IconButton(onClick = {
                        val comment = if( purchaseItem.comment != "" ) "  (${purchaseItem.comment})" else ""
                        clipboardManager.setText(AnnotatedString(purchaseItem.name + ", " + purchaseItem.price.toString() + purchaseItem.currency.displayName + comment))
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_content_copy_24),
                            contentDescription = ".",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                Row(modifier = Modifier.padding(bottom = margin_standard)) {
                    Text(
                        text = purchaseItem.category.displayName,
                        style = MaterialTheme.typography.displaySmall,
                    )
                    Text(
                        text = " (" + df.format(purchaseItem.price) + purchaseItem.currency.displayName + ")",
                        style = MaterialTheme.typography.displaySmall,
                    )
                }

                if (purchaseItem.image != null) {
                    Box(modifier = Modifier.height(256.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = purchaseItem.image
                            ),
                            contentDescription = purchaseItem.name,
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                }

                if (purchaseItem.comment != "") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = purchaseItem.comment,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
                // This is to give the bottom sheet some room to scroll to fill height
                Spacer(Modifier.height(512.dp))


            }
        }
    }
}
