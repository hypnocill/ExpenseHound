package com.expensehound.app.ui.screens.income

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.expensehound.app.R
import com.expensehound.app.data.entity.RecurringInterval
import com.expensehound.app.data.entity.getRecurringIntervalString
import com.expensehound.app.ui.components.NewPurchaseDropdown
import com.expensehound.app.ui.theme.margin_standard
import com.expensehound.app.utils.IncomeInput
import com.expensehound.app.utils.resetIncomeInput
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun NewIncomeScreen(input: IncomeInput, newIncomeIntent: MutableState<Boolean>) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(Unit) {
        delay(150)
        focusRequester.requestFocus()
    }

    BackHandler(enabled = true, onBack = {
        newIncomeIntent.value = false
        resetIncomeInput(input)
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
                value = input.name.value,
                onValueChange = { input.name.value = it },
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
                    placeholder = { Text(stringResource(R.string.amount)) },
                    value = input.amount.value,
                    onValueChange = { value ->
                        input.amount.value =
                            value.filter { it.isDigit() || it == '.' }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            // Recurring Interval dropdown
            NewPurchaseDropdown(
                getRecurringIntervalString(context, input.recurringInterval.value),
                RecurringInterval.values() as Array<Any>,
                { input.recurringInterval.value = it as RecurringInterval },
                { getRecurringIntervalString(context, it as RecurringInterval) },
                1f,
                stringResource(id = R.string.recurring_interval_description)
            )

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
        }
    }
}

