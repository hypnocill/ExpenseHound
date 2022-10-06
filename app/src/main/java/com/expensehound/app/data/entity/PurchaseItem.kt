package com.expensehound.app.data.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// ADD COLUMN TO INDICATE THAT IT WAS AUTOMATICALLY CREATED. Possible CREATED BY
@Entity(
    tableName = "purchase_items",
)
data class PurchaseItem(
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "image") val image: Bitmap?,
    @ColumnInfo(name = "category") val category: Category,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "is_purchased") val isPurchased: Boolean,
    @ColumnInfo(name = "comment") var comment: String = "",
    @ColumnInfo(name = "currency") val currency: Currency = Currency.BGN,
    @ColumnInfo(name = "notification_id") val notificationId: Int? = null,
    @ColumnInfo(name = "notification_timestamp") val notificationTimestamp: Long? = null,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "recurring_interval") var recurringInterval: RecurringInterval = com.expensehound.app.data.entity.RecurringInterval.NONE,
)


