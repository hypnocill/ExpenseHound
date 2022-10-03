package com.expensehound.app.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// There's data duplication and loose reference purchaseItemId because the purchase item could be deleted/edited
// but the fulfilled desire should remain
@Entity(
    tableName = "fulfilled_desires",
)
data class FulfilledDesire(
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    @ColumnInfo(name = "purchase_item_id") var purchaseItemId: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "category") val category: Category,
    @ColumnInfo(name = "currency") val currency: Currency = Currency.BGN,
    @ColumnInfo(name = "created_at") val createdAt: Long
)