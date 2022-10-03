package com.expensehound.app.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.expensehound.app.data.entity.FulfilledDesire
import com.expensehound.app.data.entity.StatsPurchaseItemsByCategory
import com.expensehound.app.data.repository.FulfilledDesireRepository

import com.expensehound.app.data.repository.PurchaseRepository

class StatsViewModel(context: Context) : ViewModel() {
    val purchaseRepository = PurchaseRepository(context)
    val desiresRepository = FulfilledDesireRepository(context)

    fun getAllFulfilledDesires(container: SnapshotStateList<FulfilledDesire>) {
        desiresRepository.getAll(container)
    }

    fun getAllPurchaseItemsGroupedByCategory(purchasesByCategoryList: SnapshotStateList<StatsPurchaseItemsByCategory>) {
        purchaseRepository.getAllPurchaseItemsGroupedByCategory(purchasesByCategoryList)
    }
}