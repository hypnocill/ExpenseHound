package com.expensehound.app.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.expensehound.app.data.entity.Income
import com.expensehound.app.data.entity.RecurringInterval
import com.expensehound.app.ui.viewmodel.MainViewModel

fun onIncomeInputSave(
    newIncomeIntent: MutableState<Boolean>,
    input: IncomeInput,
    context: Context,
    viewModel: MainViewModel,
) {
    if (input.name.value == IncomeInputInitialValues.name
        || input.amount.value == IncomeInputInitialValues.amount
    ) {
        val validationFailureString = context.getString(
            context.resources.getIdentifier(
                "added_failure_missing_fields",
                "string",
                context.packageName
            )
        )

        Toast.makeText(
            context,
            validationFailureString + input.name.value,
            Toast.LENGTH_LONG
        ).show()

        return
    }
    val newIncome =
        Income(
            name = input.name.value,
            amount = input.amount.value.toDouble(),
            comment = input.comment.value,
            createdAt = System.currentTimeMillis(),
            recurringInterval = input.recurringInterval.value
        )

    if (input.id.value != null) {
        viewModel.updateIncome(
            input.id.value!!,
            newIncome.name,
            newIncome.amount,
            newIncome.comment,
            newIncome.recurringInterval
        )
    } else {
        viewModel.insertIncome(newIncome)
    }

    val successString = context.getString(
        context.resources.getIdentifier(
            "added_success",
            "string",
            context.packageName
        )
    )

    Toast.makeText(
        context,
        successString + " " + input.name.value,
        Toast.LENGTH_LONG
    ).show()

    newIncomeIntent.value = false
    resetIncomeInput(input)
}

fun resetIncomeInput(item: IncomeInput) {
    item.id.value = IncomeInputInitialValues.id
    item.name.value = IncomeInputInitialValues.name
    item.amount.value = IncomeInputInitialValues.amount
    item.comment.value = IncomeInputInitialValues.comment
    item.recurringInterval.value = IncomeInputInitialValues.recurringInterval
}


fun initIncomeInput(): IncomeInput {
    return object : IncomeInput {
        override var id: MutableState<Int?> = mutableStateOf(IncomeInputInitialValues.id)
        override var name = mutableStateOf(IncomeInputInitialValues.name)
        override var amount = mutableStateOf(IncomeInputInitialValues.amount)
        override var comment = mutableStateOf(IncomeInputInitialValues.comment)
        override var recurringInterval: MutableState<RecurringInterval> =
            mutableStateOf(IncomeInputInitialValues.recurringInterval)
    }
}

fun loadIncomeInputInState(input: IncomeInput, item: Income) {
    input.id.value = item.uid
    input.name.value = item.name
    input.comment.value = item.comment.toString()
    input.amount.value = item.amount.toString()
    input.recurringInterval.value = item.recurringInterval
}

interface IncomeInput {
    var id: MutableState<Int?>
    var name: MutableState<String>
    var amount: MutableState<String>
    var comment: MutableState<String>
    var recurringInterval: MutableState<RecurringInterval>
}

object IncomeInputInitialValues {
    val id = null
    val name = ""
    val amount = ""
    val comment = ""
    val recurringInterval = RecurringInterval.NONE
}