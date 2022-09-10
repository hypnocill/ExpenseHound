package com.expensehound.app.data

import android.graphics.Bitmap

// move entities to separate classes
data class PurchaseItem(
    var id: Int = 0,
    val name: String,
    val image: Bitmap?,
    val imageLarge: String?,
    val category: Category,
    val price: Double,
    var comment: String = "",
    val currency: Currency = Currency.BGN
    // added date
)

// Add new Notification class that will be stored in Notifications table
// in relation to future items
data class FuturePurchaseItem(
    var id: Int = 0,
    val name: String,
    val image: Bitmap?,
    val imageLarge: String?,
    val category: Category,
    val price: Double,
    var comment: String = "",
    val currency: Currency = Currency.BGN,
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

