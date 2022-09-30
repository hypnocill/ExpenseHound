package com.expensehound.app.data

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

// move entities to separate classes
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
)

// Currently not used
@Entity(
    tableName = "notifications",
    foreignKeys = [ForeignKey(
        entity = PurchaseItem::class,
        parentColumns = arrayOf("uid"),
        childColumns = arrayOf("purchase_item_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Notifications(
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    @ColumnInfo(name = "purchase_item_id") var purchaseItemId: Int,
    @ColumnInfo(name = "timestamp") var timestamp: Long,
)

// extract to new files
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
)

data class StatsPurchaseItemsByCategory(
    @ColumnInfo(name = "count") val count: Int,
    @ColumnInfo(name = "category") val category: Category,
    @ColumnInfo(name = "sum_price") val sumPrice: Double
)

enum class Currency(val displayName: String) {
    BGN("лв."),
    USD("$"),
    EUR("EUR")
}

enum class Category(val displayName: String) {
    OTHERS("Друго"),
    BILLS("Сметки"),
    FOOD("Храна"),
    RENT("Наем"),
    FUN("Забавление"),
    EDUCATION("Образование"),
    INVESTMENT("Инвестиции"),
    HEALTH("Здраве"),
    REPAIRS("Поддържка и ремонт"),
}

