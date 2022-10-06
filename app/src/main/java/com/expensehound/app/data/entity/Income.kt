package com.expensehound.app.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.expensehound.app.R

@Entity(
    tableName = "income_items",
)
data class Income(
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "amount") val price: Double,
    @ColumnInfo(name = "recurring_interval") val recurringInterval: RecurringInterval? = null,
    @ColumnInfo(name = "comment") var comment: String = "",
    @ColumnInfo(name = "created_at") val createdAt: Long
)
