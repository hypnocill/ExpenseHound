package com.expensehound.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.expensehound.app.data.entity.Category
import com.expensehound.app.data.entity.PurchaseItem
import com.expensehound.app.data.entity.RecurringInterval
import com.expensehound.app.ui.viewmodel.MainViewModel

fun onPurchaseInputSave(
    purchaseIntent: MutableState<Boolean>,
    input: BasePurchaseItemInput,
    context: Context,
    viewModel: MainViewModel,
    isPurchased: Boolean
) {
    if (input.text.value == PurchaseItemInputInitialValues.text
        || input.price.value == PurchaseItemInputInitialValues.price
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
            validationFailureString + input.text.value,
            Toast.LENGTH_LONG
        ).show()

        return
    }
    val newPurchaseItem =
        PurchaseItem(
            name = input.text.value,
            image = input.image.value,
            category = input.selectedCategory.value,
            price = input.price.value.toDouble(),
            isPurchased = isPurchased,
            comment = input.comment.value,
            createdAt = System.currentTimeMillis(),
            recurringInterval = input.recurringInterval.value
        )

    if (input.id.value != null) {
        viewModel.updatePurchaseItem(
            input.id.value!!,
            newPurchaseItem.name,
            newPurchaseItem.image,
            newPurchaseItem.category,
            newPurchaseItem.price,
            newPurchaseItem.comment,
            newPurchaseItem.recurringInterval
        )
    } else {
        viewModel.insertPurchaseItem(newPurchaseItem)
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
        successString + " " + input.text.value,
        Toast.LENGTH_LONG
    ).show()

    purchaseIntent.value = false
    resetNewPurchaseInput(input)
}

fun loadPurchaseInputInState(input:  BasePurchaseItemInput, item: PurchaseItem) {
    input.id.value = item.uid
    input.text.value = item.name
    input.image.value = item.image
    input.selectedCategory.value = item.category
    input.comment.value = item.comment
    input.price.value = item.price.toString()
    input.recurringInterval.value = item.recurringInterval
}

fun resetNewPurchaseInput(item: BasePurchaseItemInput) {
    item.id.value = PurchaseItemInputInitialValues.id
    item.text.value = PurchaseItemInputInitialValues.text
    item.price.value = PurchaseItemInputInitialValues.price
    item.selectedCategory.value = PurchaseItemInputInitialValues.selectedCategory
    item.image.value = PurchaseItemInputInitialValues.image
    item.comment.value = PurchaseItemInputInitialValues.comment
    item.recurringInterval.value = PurchaseItemInputInitialValues.recurringInterval
}


fun initBasePurchaseItemInput(): BasePurchaseItemInput {
    return object : BasePurchaseItemInput {
        override var id: MutableState<Int?> = mutableStateOf(PurchaseItemInputInitialValues.id)
        override var text = mutableStateOf(PurchaseItemInputInitialValues.text)
        override var price = mutableStateOf(PurchaseItemInputInitialValues.price)
        override var selectedCategory =
            mutableStateOf(PurchaseItemInputInitialValues.selectedCategory)
        override var image: MutableState<Bitmap?> =
            mutableStateOf(PurchaseItemInputInitialValues.image)
        override var comment = mutableStateOf(PurchaseItemInputInitialValues.comment)
        override var recurringInterval: MutableState<RecurringInterval> =
            mutableStateOf(PurchaseItemInputInitialValues.recurringInterval)
    }
}

interface BasePurchaseItemInput {
    var id: MutableState<Int?>
    var text: MutableState<String>
    var price: MutableState<String>
    var selectedCategory: MutableState<Category>
    var image: MutableState<Bitmap?>
    var comment: MutableState<String>
    var recurringInterval: MutableState<RecurringInterval>
}

object PurchaseItemInputInitialValues {
    val id = null
    val text = ""
    val price = ""
    val image = null
    val selectedCategory = Category.OTHERS
    val comment = ""
    val recurringInterval = RecurringInterval.NONE
}
