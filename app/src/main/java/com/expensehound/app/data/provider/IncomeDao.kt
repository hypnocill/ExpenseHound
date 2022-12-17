package com.expensehound.app.data.provider

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.expensehound.app.data.entity.Income
import com.expensehound.app.data.entity.IncomeSum
import com.expensehound.app.data.entity.RecurringInterval
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {
    @Query("SELECT * FROM income_items WHERE (:from IS NULL OR created_at >= :from) AND (:to IS NULL OR created_at <= :to)")
    fun getAll(from: Long? = null, to: Long? = null): Flow<List<Income>>

    @Insert
    fun insert(income: Income)

    @Delete
    fun delete (income: Income)

    @Query("UPDATE income_items SET name=:name, amount=:amount, comment=:comment, recurring_interval=:recurringInterval where uid=:uid")
    fun updateMainProperties(
        uid: Int,
        name: String,
        amount: Double,
        comment: String?,
        recurringInterval: RecurringInterval,
    )

    @Query("UPDATE income_items SET recurring_interval=:recurringInterval where uid=:uid")
    fun updateRecurringInterval(
        uid: Int,
        recurringInterval: RecurringInterval
    )

    @Query("SELECT * FROM income_items WHERE recurring_interval IS NOT 'NONE' AND (:from IS NULL OR created_at >= :from) AND (:to IS NULL OR created_at <= :to) ORDER BY created_at ASC")
    fun getAllWithRecurringIntervals(from: Long? = null, to: Long? = null): List<Income>

    @Query("SELECT SUM(income_items.amount) as amount FROM income_items WHERE (:from IS NULL OR created_at >= :from) AND (:to IS NULL OR created_at <= :to)")
    fun getSum(from: Long? = null, to: Long? = null): IncomeSum
}