package com.expensehound.app.data.entity

import androidx.room.ColumnInfo

data class StatsPurchaseItemsByCategory(
    @ColumnInfo(name = "count") val count: Int,
    @ColumnInfo(name = "category") val category: Category,
    @ColumnInfo(name = "sum_price") val sumPrice: Double
)