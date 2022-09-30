package com.expensehound.app.ui

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.expensehound.app.data.Category
import com.expensehound.app.data.FulfilledDesire
import com.expensehound.app.data.PurchaseItem
import com.expensehound.app.data.StatsPurchaseItemsByCategory
import com.expensehound.app.data.repository.FulfilledDesireRepository

import com.expensehound.app.data.repository.PurchaseRepository

class StatsViewModel(context: Context) : ViewModel() {
    val repository = PurchaseRepository(context)

    fun getAllPurchaseItemsGroupedByCategory(purchasesByCategoryList: SnapshotStateList<StatsPurchaseItemsByCategory>) {
        repository.getAllPurchaseItemsGroupedByCategory(purchasesByCategoryList)
    }
}
