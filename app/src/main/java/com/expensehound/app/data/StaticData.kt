package com.expensehound.app.data

@Suppress("max-line-length")
object StaticData {
    val PURCHASES: Map<String, PurchaseItem> = mapOf(
        "кафе" to PurchaseItem(
            1,
            "Кафе",
            null,
            "https://placehold.co/128x256/ffc107/000000.png",
            Category.FUN,
            30.54
        ),
        "квартира" to PurchaseItem(
            2,
            "Квартира",
            null,
            null,
            Category.FUN,
            10.0
        ),
        "кюфтета" to PurchaseItem(
            3,
            "Кюфтета",
            null,
            null,
            Category.FUN,
            5.0
        ),

    )
}
