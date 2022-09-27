package com.expensehound.app.ui

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.expensehound.app.data.Category
import com.expensehound.app.data.PurchaseItem

import com.expensehound.app.data.repository.PurchaseRepository

class MainViewModel(context: Context) : ViewModel() {
    private val repository = PurchaseRepository(context)

    var purchasesList = mutableStateListOf<PurchaseItem>()
    var futurePurchasesList = mutableStateListOf<PurchaseItem>()

    val newPurchaseInput: BasePurchaseItemInput
    var newPurchaseIntent = mutableStateOf(false)

    val newFuturePurchaseInput: BasePurchaseItemInput
    var newFuturePurchaseIntent = mutableStateOf(false)

    init {
        repository.getAllPurchaseItems(purchasesList)
        repository.getAllFuturePurchaseItems(futurePurchasesList)

        newPurchaseInput = initBasePurchaseItemInput()
        newFuturePurchaseInput = initBasePurchaseItemInput()
    }

    fun deletePurchaseItem(uid: Int) {
        repository.deletePurchaseItem(uid)
    }

    fun updatePurchaseItemIsPurchased(uid: Int, isPurchased: Boolean) {
        repository.updatePurchaseItemIsPurchased(uid, isPurchased)
    }

    fun updatePurchaseItemMainProperties(
        uid: Int,
        name: String,
        image: Bitmap?,
        category: Category,
        price: Double,
        comment: String?
    ) {
        repository.updatePurchaseItemMainProperties(
            uid,
            name,
            image,
            category,
            price,
            comment
        )
    }

    fun insertPurchaseItem(purchaseItem: PurchaseItem) {
        repository.insertPurchaseItem(purchaseItem)
    }
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
    }
}

interface BasePurchaseItemInput {
    var id: MutableState<Int?>
    var text: MutableState<String>
    var price: MutableState<String>
    var selectedCategory: MutableState<Category>
    var image: MutableState<Bitmap?>
    var comment: MutableState<String>
}

object PurchaseItemInputInitialValues {
    val id = null
    val text = ""
    val price = ""
    val image = null
    val selectedCategory = Category.values()[0]
    val comment = ""
}
