package com.expensehound.app.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.expensehound.app.R
import com.expensehound.app.ui.theme.margin_half
import com.expensehound.app.ui.theme.margin_standard

@Composable
fun AppItemCard(
    title: String,
    subtitle: String? = null,
    imageBitmap: Bitmap? = null,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    isCreatedAutomatically: Boolean,
    height: Dp = 150.dp,
    date: String? = null,
    additionalActions: @Composable() (() -> Unit)? = null
) {
    val openDeleteDialog = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(margin_half)
            .clickable { onClick() }
            .height(height)
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
                        text = title,
                        style = MaterialTheme.typography.displayLarge,
                    )

                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.headlineSmall,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }

                    if (isCreatedAutomatically) {
                        Text(
                            text = stringResource(id = R.string.recurring_interval_created_by),
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                }

                Row {
                    if (additionalActions != null) {
                        additionalActions()
                    }

                    AppItemCardActionButtons(
                        openDeleteDialog = openDeleteDialog,
                        name = title,
                        onEdit = onEdit,
                        onDelete = onDelete
                    )
                }
            }

            if (imageBitmap != null) {
                Image(
                    imageBitmap.asImageBitmap(),
                    null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(15.dp, 0.dp, 0.dp, 15.dp))
                )
            }

            if (date != null) {
                Box{
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = margin_standard, vertical = margin_half),
                        verticalArrangement = Arrangement.Bottom,
                    ) {
                        Text(
                            text = date,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
