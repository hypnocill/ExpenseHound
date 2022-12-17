package com.expensehound.app.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.expensehound.app.data.entity.Category
import com.expensehound.app.data.entity.FulfilledDesire
import com.expensehound.app.data.entity.Income
import com.expensehound.app.data.entity.PurchaseItem
import com.expensehound.app.data.entity.RecurringInterval
import com.expensehound.app.data.repository.FulfilledDesireRepository
import com.expensehound.app.data.repository.IncomeRepository
import com.expensehound.app.data.repository.PurchaseRepository
import com.expensehound.app.utils.BasePurchaseItemInput
import com.expensehound.app.utils.IncomeInput
import com.expensehound.app.utils.initBasePurchaseItemInput
import com.expensehound.app.utils.initIncomeInput

class MainViewModel(context: Context) : ViewModel() {
    private val repository = PurchaseRepository(context)
    private val fulfilledDesireRepository = FulfilledDesireRepository(context)
    private val incomeRepository = IncomeRepository(context)

    var selectedHomeTopAppBarTab = mutableStateOf(0)

    val newPurchaseInput: BasePurchaseItemInput
    var newPurchaseIntent = mutableStateOf(false)

    val newFuturePurchaseInput: BasePurchaseItemInput
    var newFuturePurchaseIntent = mutableStateOf(false)

    val newIncomeInput: IncomeInput
    var newIncomeIntent = mutableStateOf(false)

    val purchasesFiltersMonth = mutableStateOf(false)
    val desiresFiltersMonth = mutableStateOf(false)
    val incomeFiltersMonth = mutableStateOf(false)

    init {
        newPurchaseInput = initBasePurchaseItemInput()
        newFuturePurchaseInput = initBasePurchaseItemInput()
        newIncomeInput = initIncomeInput()
    }

    fun setSelectedHomeTopAppBarTab(selectedTabIndex: Int) {
        selectedHomeTopAppBarTab.value = selectedTabIndex
    }

    fun setPurchaseFilterMonth(value: Boolean) {
        purchasesFiltersMonth.value = value
    }

    fun setDesiresFilterMonth(value: Boolean) {
        desiresFiltersMonth.value = value
    }

    fun getAllPurchaseItems(
        container: SnapshotStateList<PurchaseItem>,
        from: Long? = null,
        to: Long? = null
    ) {
        repository.getAllPurchaseItems(container, from, to)
    }

    fun getAllDesires(
        container: SnapshotStateList<PurchaseItem>,
        from: Long? = null,
        to: Long? = null
    ) {
        repository.getAllDesires(container, from, to)
    }

    fun getPurchaseItemById(uid: Int, container: SnapshotStateList<PurchaseItem>) {
        repository.getPurchaseItemById(uid, container)
    }

    fun deletePurchaseItem(uid: Int) {
        repository.deletePurchaseItem(uid)
    }

    fun updatePurchaseItemIsPurchased(uid: Int) {
        repository.updatePurchaseItemIsPurchased(uid, true)
    }

    fun updatePurchaseItemNotification(
        uid: Int, notificationId: Int?, notificationTimestamp: Long?
    ) {
        repository.updatePurchaseItemNotification(uid, notificationId, notificationTimestamp)
    }

    fun updatePurchaseItem(
        uid: Int,
        name: String,
        image: Bitmap?,
        category: Category,
        price: Double,
        comment: String?,
        recurringInterval: RecurringInterval
    ) {
        repository.updatePurchaseItemMainProperties(
            uid,
            name,
            image,
            category,
            price,
            comment,
            recurringInterval
        )
    }

    fun insertPurchaseItem(purchaseItem: PurchaseItem) {
        repository.insertPurchaseItem(purchaseItem)
    }

    fun insertFulfilledDesire(fulfilledDesire: FulfilledDesire) {
        fulfilledDesireRepository.insert(fulfilledDesire)
    }

    fun getAllIncome(
        container: SnapshotStateList<Income>,
        from: Long? = null,
        to: Long? = null
    ) {
        incomeRepository.getAll(container, from, to)
    }

    fun insertIncome(income: Income) {
        incomeRepository.insert(income)
    }

    fun updateIncome(
        uid: Int,
        name: String,
        amount: Double,
        comment: String?,
        recurringInterval: RecurringInterval
    ) {
        incomeRepository.update(
            uid,
            name,
            amount,
            comment,
            recurringInterval
        )
    }

    fun setIncomeFilterMonth(value: Boolean) {
        incomeFiltersMonth.value = value
    }

    fun deleteIncome(income: Income) {
        incomeRepository.delete(income)
    }
}


