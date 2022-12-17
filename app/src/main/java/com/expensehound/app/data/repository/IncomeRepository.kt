package com.expensehound.app.data.repository

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.expensehound.app.data.entity.Income
import com.expensehound.app.data.entity.IncomeSum
import com.expensehound.app.data.entity.RecurringInterval
import com.expensehound.app.data.provider.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IncomeRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)

    fun getAll(
        container: SnapshotStateList<Income>,
        from: Long? = null,
        to: Long? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            db.incomeDao().getAll(from, to).collect {
                container.clear()
                it.forEach { item ->
                    container.add(item)
                }
            }
        }
    }

    fun insert(income: Income) {
        CoroutineScope(Dispatchers.IO).launch {
            db.incomeDao().insert(income)
        }
    }

    // Refactor this function so that all params are optional and there's no need to pass params that will not be updated
    fun update(
        uid: Int,
        name: String,
        amount: Double,
        comment: String?,
        recurringInterval: RecurringInterval,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            db.incomeDao().updateMainProperties(
                uid,
                name,
                amount,
                comment,
                recurringInterval,
            )
        }
    }

    fun delete(income: Income) {
        CoroutineScope(Dispatchers.IO).launch {
            db.incomeDao().delete(income)
        }
    }

    fun getSum(incomeSum: MutableState<IncomeSum>, from: Long? = null, to: Long? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            incomeSum.value.amount = db.incomeDao().getSum(from, to).amount
        }
    }

    // SYNC

    fun getAllWithRecurringIntervalSync(from: Long? = null, to: Long? = null): List<Income> {
        return db.incomeDao().getAllWithRecurringIntervals(from, to)
    }

    //TODO: Remove this function and use "updateMainProperties" only, when refactored
    fun updateRecurringIntervalSync(
        income: Income,
        recurringInterval: RecurringInterval
    ) {
        db.incomeDao().updateMainProperties(
            income.uid,
            income.name,
            income.amount,
            income.comment,
            recurringInterval,
        )
    }

    fun insertSync(purchaseItem: Income) {
        db.incomeDao().insert(purchaseItem)
    }
}