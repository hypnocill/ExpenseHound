package com.expensehound.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expensehound.app.R
import com.expensehound.app.ui.theme.margin_double
import com.expensehound.app.ui.theme.margin_standard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPurchaseDropdown(
    selectedValueAsString: String,
    items: Array<Any>,
    onSelect: (Any) -> Unit,
    getStringOnSelect: (Any) -> String,
    maxWidthFraction: Float = 1f,
    label: String = ""
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth(maxWidthFraction)) {
        if (label != "" ) {
            Text(
                label,
                modifier = Modifier.padding(start = margin_standard, top = margin_standard),
                fontSize = 12.sp,
                color = TextFieldDefaults.outlinedTextFieldColors().placeholderColor(enabled = true).value,
                maxLines = 1
            )
        }

        OutlinedButton(
            shape = TextFieldDefaults.outlinedShape,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = TextFieldDefaults.outlinedTextFieldColors().placeholderColor(
                    enabled = true
                ).value
            ),
            onClick = { expanded = !expanded },
            modifier = Modifier
                .padding(margin_standard)
                .defaultMinSize(
                    minWidth = TextFieldDefaults.MinWidth, minHeight = TextFieldDefaults.MinHeight
                )
        ) {
            Text(
                selectedValueAsString,
                color = TextFieldDefaults.outlinedTextFieldColors().placeholderColor(
                    enabled = true
                ).value
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_arrow_drop_down_24),
                contentDescription = "dropdownarrow",
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(margin_standard, 0.dp)
        ) {
            items.forEach {
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onSelect(it)
                    },
                    text = {
                        Text(getStringOnSelect(it))
                    }
                )
            }
        }
    }
}