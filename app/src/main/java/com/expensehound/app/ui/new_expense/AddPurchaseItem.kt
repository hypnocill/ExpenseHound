package com.expensehound.app.ui.new_expense

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.expensehound.app.R
import com.expensehound.app.data.Category
import com.expensehound.app.ui.MainViewModel
import com.expensehound.app.ui.NewPurchaseItemInput
import com.expensehound.app.ui.theme.margin_standard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddPurchaseItem(demoViewModel: MainViewModel) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            demoViewModel.newPurchaseInput.image.value = it
        }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BackHandler(enabled = true, onBack = {
        demoViewModel.newPurchaseIntent.value = false
    })

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource, indication = null
            ) {
                keyboardController?.hide()
                focusManager.clearFocus(true)
            }, color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.height(margin_standard))
            OutlinedTextField(
                maxLines = 1,
                placeholder = { Text(stringResource(R.string.name)) },
                value = demoViewModel.newPurchaseInput.text.value,
                onValueChange = { demoViewModel.newPurchaseInput.text.value = it },
                modifier = Modifier
                    .padding(margin_standard)
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            )
            Row() {
                OutlinedTextField(
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text(stringResource(R.string.price)) },
                    value = demoViewModel.newPurchaseInput.price.value,
                    onValueChange = { value ->
                        demoViewModel.newPurchaseInput.price.value =
                            value.filter { it.isDigit() || it == '.' }
                    },
                    modifier = Modifier
                        .padding(margin_standard)
                        .fillMaxWidth(0.45f)
                )
                CategoryDropdown(demoViewModel.newPurchaseInput)
            }

            OutlinedTextField(
                maxLines = 10,
                placeholder = { Text(stringResource(R.string.comment)) },
                value = demoViewModel.newPurchaseInput.comment.value,
                onValueChange = { value ->
                    if (value.length < 500) {
                        demoViewModel.newPurchaseInput.comment.value = value
                    }
                },
                modifier = Modifier
                    .padding(margin_standard)
                    .fillMaxWidth()
            )

            Spacer(Modifier.height(margin_standard))

            if (demoViewModel.newPurchaseInput.image.value != null) {

                Image(
                    demoViewModel.newPurchaseInput.image.value!!.asImageBitmap(),
                    null,
                    modifier = Modifier
                        .size(200.dp)
                        .clickable { cameraLauncher.launch() })
            } else {
                IconButton(onClick = { cameraLauncher.launch() }) {
                    Icon(
                        modifier = Modifier.size(60.dp),
                        painter = painterResource(id = R.drawable.ic_outline_camera_alt_24),
                        contentDescription = ".",
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    input: NewPurchaseItemInput
) {
    val suggestions = Category.values()
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
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
                input.selectedCategory.value.displayName,
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
            suggestions.forEach { label ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    input.selectedCategory.value = label
                }, text = { Text(text = label.displayName) })
            }
        }
    }
}
