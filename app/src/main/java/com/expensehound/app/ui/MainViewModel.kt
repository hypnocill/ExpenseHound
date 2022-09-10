package com.expensehound.app.ui

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.expensehound.app.data.Category
import com.expensehound.app.data.FuturePurchaseItem
import com.expensehound.app.data.PurchaseItem
import com.expensehound.app.data.StaticData

interface NewPurchaseItemInput {
    var text: MutableState<String>
    var price: MutableState<String>
    var selectedCategory: MutableState<Category>
    var image: MutableState<Bitmap?>
    var comment: MutableState<String>
}

object NewPurchaseItemInputInitialValues {
    val text = ""
    val price = ""
    val image = null
    val selectedCategory = Category.values()[0]
    val comment = ""

}

class MainViewModel : ViewModel() {
    var purchasesList = mutableStateListOf<PurchaseItem>()
    var futurePurchasesList = mutableStateListOf<FuturePurchaseItem>()
    var newPurchaseIntent = mutableStateOf(false)
    var newFuturePurchaseIntent = mutableStateOf(false)
    val newPurchaseInput: NewPurchaseItemInput

    init {
        purchasesList = StaticData.PURCHASES.values.toMutableStateList()
        futurePurchasesList = listOf<FuturePurchaseItem>().toMutableStateList()

        newPurchaseInput = object : NewPurchaseItemInput {
            override var text = mutableStateOf(NewPurchaseItemInputInitialValues.text)
            override var price = mutableStateOf(NewPurchaseItemInputInitialValues.price)
            override var selectedCategory = mutableStateOf(NewPurchaseItemInputInitialValues.selectedCategory)
            override var image: MutableState<Bitmap?> = mutableStateOf(NewPurchaseItemInputInitialValues.image)
            override var comment = mutableStateOf(NewPurchaseItemInputInitialValues.comment)
        }
    }
}
