package com.expensehound.app.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.expensehound.app.data.Category
import com.expensehound.app.data.PurchaseItem
import com.expensehound.app.data.StatsPurchaseItemsByCategory
import com.expensehound.app.data.provider.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PurchaseRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)

    fun getAllPurchaseItems(container: SnapshotStateList<PurchaseItem>) {
        CoroutineScope(Dispatchers.IO).launch {
            db.purchaseItemDao().getAllPurchaseItems().collect {
                container.clear()
                it.forEach { item ->
                    container.add(item)
                }
            }
        }
    }

    fun getAllPurchaseItemsGroupedByCategory(container: SnapshotStateList<StatsPurchaseItemsByCategory>) {
        CoroutineScope(Dispatchers.IO).launch {
            db.purchaseItemDao().getPurchaseItemsGroupedByCategory().collect {
                container.clear()
                it.forEach { item ->
                    container.add(item)
                }
            }
        }
    }

    fun getAllFuturePurchaseItems(container: SnapshotStateList<PurchaseItem>) {
        CoroutineScope(Dispatchers.IO).launch {
            db.purchaseItemDao().getAllFuturePurchaseItems().collect {
                container.clear()
                it.forEach { item ->
                    container.add(item)
                }
            }
        }
    }

    fun insertPurchaseItem(purchaseItem: PurchaseItem) {
        CoroutineScope(Dispatchers.IO).launch {
            db.purchaseItemDao().insert(purchaseItem)
        }
    }

    fun deletePurchaseItem(uid: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            db.purchaseItemDao().delete(uid)
        }
    }

    fun updatePurchaseItemIsPurchased(uid: Int, isPurchased: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            db.purchaseItemDao().updateIsPurchased(uid, isPurchased)
        }
    }

    fun updatePurchaseItemNotification(uid: Int, notificationId: Int?, notificationTimestamp: Long?) {
        CoroutineScope(Dispatchers.IO).launch {
            db.purchaseItemDao().updatePurchaseItemNotification(uid, notificationId, notificationTimestamp)
        }
    }

    fun updatePurchaseItemMainProperties(
        uid: Int,
        name: String,
        image: Bitmap?,
        category: Category,
        price: Double,
        comment: String?
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            db.purchaseItemDao().updateMainProperties(
                uid,
                name,
                image,
                category,
                price,
                comment
            )
        }
    }
}