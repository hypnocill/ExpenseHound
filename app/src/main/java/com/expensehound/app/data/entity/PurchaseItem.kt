package com.expensehound.app.data.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "purchase_items",
)
data class PurchaseItem(
    @PrimaryKey(autoGenerate = true) override var uid: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "image") val image: Bitmap?,
    @ColumnInfo(name = "category") val category: Category,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "is_purchased") val isPurchased: Boolean,
    @ColumnInfo(name = "comment") var comment: String = "",
    @ColumnInfo(name = "currency") val currency: Currency = Currency.BGN,
    @ColumnInfo(name = "notification_id") val notificationId: Int? = null,
    @ColumnInfo(name = "notification_timestamp") val notificationTimestamp: Long? = null,
    @ColumnInfo(name = "created_at") override val createdAt: Long,
    @ColumnInfo(name = "created_automatically") val createdAutomatically: Boolean = false,
    @ColumnInfo(name = "recurring_interval") override var recurringInterval: RecurringInterval = com.expensehound.app.data.entity.RecurringInterval.NONE,
): ItemWithRecurringInterval


