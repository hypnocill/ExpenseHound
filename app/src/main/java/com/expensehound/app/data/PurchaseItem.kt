package com.expensehound.app.data

import android.graphics.Bitmap
import java.util.Date

// move entities to separate classes
data class PurchaseItem(
    var id: Int = 0,
    var name: String,
    val image: Bitmap?,
    val imageLarge: String?,
    val category: Category,
    val price: Double,
    var comment: String = "",
    val currency: Currency = Currency.BGN,
    val notificationId: Int? = null,
    val notificationTimestamp: Long? = null,
    val deletedAt: Date? = null,
    val convertedAt: Date? = null
    // added date
)

enum class Currency(val displayName : String) {
    BGN("лв."),
    USD("$"),
    EUR("EUR")
}

enum class Category(val displayName : String) {
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

