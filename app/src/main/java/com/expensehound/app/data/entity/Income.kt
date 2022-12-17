package com.expensehound.app.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.expensehound.app.R

@Entity(
    tableName = "income_items",
)
data class Income(
    @PrimaryKey(autoGenerate = true) override var uid: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "amount") val amount: Double,
    @ColumnInfo(name = "recurring_interval") override val recurringInterval: RecurringInterval,
    @ColumnInfo(name = "comment") var comment: String? = "",
    @ColumnInfo(name = "created_automatically") val createdAutomatically: Boolean = false,
    @ColumnInfo(name = "created_at") override val createdAt: Long

): ItemWithRecurringInterval

data class IncomeSum(
    var amount: Double
)
