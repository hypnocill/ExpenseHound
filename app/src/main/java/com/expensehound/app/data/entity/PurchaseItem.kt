package com.expensehound.app.data.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

// Extract notification data to a separate table
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
    @ColumnInfo(name = "created_at") val createdAt: Long
)

// The app currently works with LEV only
enum class Currency(val displayName: String) {
    BGN("лв."),
    USD("$"),
    EUR("EUR")
}

enum class Category(val displayName: String) {
    OTHERS("Друго"),
    CLOTHING("Облекло"),
    BILLS("Сметки"),
    FOOD("Храна"),
    RENT("Наем"),
    FUN("Забавление"),
    EDUCATION("Образование"),
    INVESTMENT("Инвестиции"),
    HEALTH("Здраве"),
    REPAIRS("Поддържка и ремонт"),
}

