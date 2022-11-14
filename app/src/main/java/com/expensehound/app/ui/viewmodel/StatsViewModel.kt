package com.expensehound.app.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.expensehound.app.data.entity.FulfilledDesire
import com.expensehound.app.data.entity.StatsPurchaseItemsByCategory
import com.expensehound.app.data.repository.FulfilledDesireRepository

import com.expensehound.app.data.repository.PurchaseRepository

class StatsViewModel(context: Context) : ViewModel() {
    val purchaseRepository = PurchaseRepository(context)
    val desiresRepository = FulfilledDesireRepository(context)

    val statsFiltersMonth = mutableStateOf(false)

    fun setStatsFilterMonth(value: Boolean) {
        statsFiltersMonth.value = value
    }

    fun getAllFulfilledDesires(container: SnapshotStateList<FulfilledDesire>, from: Long? = null, to: Long? = null) {
        desiresRepository.getAll(container, from, to)
    }

    fun getAllPurchaseItemsGroupedByCategory(purchasesByCategoryList: SnapshotStateList<StatsPurchaseItemsByCategory>, from: Long? = null, to: Long? = null) {
        purchaseRepository.getAllPurchaseItemsGroupedByCategory(purchasesByCategoryList, from, to)
    }
}
