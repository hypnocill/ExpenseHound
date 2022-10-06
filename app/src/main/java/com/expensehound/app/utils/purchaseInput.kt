package com.expensehound.app.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import com.expensehound.app.data.entity.PurchaseItem
import com.expensehound.app.ui.viewmodel.BasePurchaseItemInput
import com.expensehound.app.ui.viewmodel.MainViewModel
import com.expensehound.app.ui.viewmodel.PurchaseItemInputInitialValues

fun onPurchaseInputSave(
    purchaseIntent: MutableState<Boolean>,
    input: BasePurchaseItemInput,
    context: Context,
    viewModel: MainViewModel,
    isPurchased: Boolean
) {
    purchaseIntent.value = false

    if (input.text.value != PurchaseItemInputInitialValues.text
        && input.price.value != PurchaseItemInputInitialValues.price
    ) {
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
            viewModel.updatePurchaseItemMainProperties(
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

        Toast.makeText(
            context,
            "Успешно добавихте " + input.text.value,
            Toast.LENGTH_LONG
        ).show()
    }

    resetNewPurchaseInput(input)
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