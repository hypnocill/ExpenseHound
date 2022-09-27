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
    foreignKeys = [ForeignKey(
        entity = Notifications::class,
        parentColumns = arrayOf("uid"),
        childColumns = arrayOf("notification_id"),
        onDelete = ForeignKey.CASCADE
    )]
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
@Entity(tableName = "notifications")
data class Notifications(
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    @ColumnInfo(name = "timestamp") var timestamp: Long,
)

enum class Currency(val displayName: String) {
    BGN("лв."),
    USD("$"),
    EUR("EUR")
}

enum class Category(val displayName: String) {
    BILLS("Сметки"),
    FOOD("Храна"),
    RENT("Наем"),
    FUN("Забавление"),
    EDUCATION("Образование"),
    INVESTMENT("Инвестиции"),
    HEALTH("Здраве"),
    REPAIRS("Поддържка и ремонт"),
    OTHERS("Друго")
}

