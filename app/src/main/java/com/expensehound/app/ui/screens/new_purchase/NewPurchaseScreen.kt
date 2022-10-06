package com.expensehound.app.ui.screens.new_purchase

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.expensehound.app.R
import com.expensehound.app.data.entity.Category
import com.expensehound.app.data.entity.RecurringInterval
import com.expensehound.app.data.entity.getCategoryString
import com.expensehound.app.data.entity.getRecurringIntervalString
import com.expensehound.app.ui.components.NewPurchaseDropdown
import com.expensehound.app.ui.theme.margin_standard
import com.expensehound.app.ui.viewmodel.BasePurchaseItemInput
import com.expensehound.app.utils.resetNewPurchaseInput
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun NewPurchaseScreen(input: BasePurchaseItemInput, purchaseIntent: MutableState<Boolean>) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            input.image.value = it
        }

    LaunchedEffect(Unit) {
        delay(150)
        focusRequester.requestFocus()
    }

    BackHandler(enabled = true, onBack = {
        purchaseIntent.value = false
        resetNewPurchaseInput(input)
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
            OutlinedTextField(
                maxLines = 1,
                placeholder = { Text(stringResource(R.string.name)) },
                value = input.text.value,
                onValueChange = { input.text.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(margin_standard)
                    .focusRequester(focusRequester)
            )


            Row(
                modifier = Modifier
                    .padding(margin_standard)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text(stringResource(R.string.price)) },
                    value = input.price.value,
                    onValueChange = { value ->
                        input.price.value =
                            value.filter { it.isDigit() || it == '.' }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.70f)
                )

                IconButton(modifier = Modifier.fillMaxWidth(), onClick = { cameraLauncher.launch() }) {
                    Icon(
                        modifier = Modifier.size(60.dp),
                        painter = painterResource(id = R.drawable.ic_outline_camera_alt_24),
                        contentDescription = ".",
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                // Category dropdown
                NewPurchaseDropdown(
                    getCategoryString(context, input.selectedCategory.value),
                    Category.values() as Array<Any>,
                    { input.selectedCategory.value = it as Category },
                    { getCategoryString(context, it as Category) },
                    0.5f,
                    stringResource(id = R.string.category)
                )

                // Recurring Interval dropdown
                NewPurchaseDropdown(
                    getRecurringIntervalString(context, input.recurringInterval.value),
                    RecurringInterval.values() as Array<Any>,
                    { input.recurringInterval.value = it as RecurringInterval },
                    { getRecurringIntervalString(context, it as RecurringInterval) },
                    1f,
                    stringResource(id = R.string.recurring_interval_description)
                )
            }

            OutlinedTextField(
                maxLines = 10,
                placeholder = { Text(stringResource(R.string.comment)) },
                value = input.comment.value,
                onValueChange = { value ->
                    if (value.length < 500) {
                        input.comment.value = value
                    }
                },
                modifier = Modifier
                    .padding(margin_standard)
                    .height(100.dp)
                    .fillMaxWidth()
            )

            Spacer(Modifier.height(margin_standard))

            if (input.image.value != null) {
                Image(
                    input.image.value!!.asImageBitmap(),
                    null,
                    modifier = Modifier
                        .size(200.dp)
                        .clickable { input.image.value = null })
            }
        }
    }
}

