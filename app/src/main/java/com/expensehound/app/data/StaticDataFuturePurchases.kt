package com.expensehound.app.data

@Suppress("max-line-length")
object StaticDataFuturePurchases {
    val PURCHASES: Map<String, PurchaseItem> = mapOf(
        "Dior Homme" to PurchaseItem(
            1,
            "Dior Homme",
            null,
            "https://placehold.co/128x256/ffc107/000000.png",
            Category.FUN,
            140.25
        ),
        "Tom Ford Grey Vetiver" to PurchaseItem(
            2,
            "Tom Ford Grey Vetiver",
            null,
            null,
            Category.FUN,
            250.0
        ),
    )
}
