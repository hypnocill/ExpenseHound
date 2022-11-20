package com.expensehound.app.data.repository

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.expensehound.app.data.entity.Category
import com.expensehound.app.data.entity.PurchaseItem
import com.expensehound.app.data.entity.RecurringInterval
import com.expensehound.app.data.entity.StatsPurchaseItemsByCategory
import com.expensehound.app.data.provider.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PurchaseRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)

    fun getAllPurchaseItems(
        container: SnapshotStateList<PurchaseItem>,
        from: Long? = null,
        to: Long? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            db.purchaseItemDao().getAllPurchaseItems(from, to).collect {
                container.clear()
                it.forEach { item ->
                    container.add(item)
                }
            }
        }
    }

    fun getAllPurchaseItemsGroupedByCategory(
        container: SnapshotStateList<StatsPurchaseItemsByCategory>,
        from: Long? = null,
        to: Long? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            db.purchaseItemDao().getPurchaseItemsGroupedByCategory(from, to).collect {
                container.clear()
                it.forEach { item ->
                    container.add(item)
                }
            }
        }
    }

    fun getAllDesires(
        container: SnapshotStateList<PurchaseItem>,
        from: Long? = null,
        to: Long? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            db.purchaseItemDao().getAllDesires(from, to).collect {
                container.clear()
                it.forEach { item ->
                    container.add(item)
                }
            }
        }
    }

    fun getPurchaseItemById(uid: Int, container: SnapshotStateList<PurchaseItem>) {
        CoroutineScope(Dispatchers.IO).launch {
            db.purchaseItemDao().getById(uid).collect {
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

    fun updatePurchaseItemNotification(
        uid: Int,
        notificationId: Int?,
        notificationTimestamp: Long?
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            db.purchaseItemDao()
                .updatePurchaseItemNotification(uid, notificationId, notificationTimestamp)
        }
    }

    // Refactor this function so that all params are optional and there's no need to pass params that will not be updated
    fun updatePurchaseItemMainProperties(
        uid: Int,
        name: String,
        image: Bitmap?,
        category: Category,
        price: Double,
        comment: String?,
        recurringInterval: RecurringInterval,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            db.purchaseItemDao().updateMainProperties(
                uid,
                name,
                image,
                category,
                price,
                comment,
                recurringInterval,
            )
        }
    }

    // SYNC

    fun getAllWithRecurringIntervalSync(from: Long? = null, to: Long? = null): List<PurchaseItem> {
        return db.purchaseItemDao().getAllWithRecurringIntervals(from, to)
    }

    fun insertPurchaseItemSync(purchaseItem: PurchaseItem) {
        db.purchaseItemDao().insert(purchaseItem)
    }

    //TODO: Remove this function and use "updateMainProperties" only, when refactored
    fun updatePurchaseItemRecurringIntervalSync(
        purchaseItem: PurchaseItem,
        recurringInterval: RecurringInterval
    ) {
            db.purchaseItemDao().updateMainProperties(
                purchaseItem.uid,
                purchaseItem.name,
                purchaseItem.image,
                purchaseItem.category,
                purchaseItem.price,
                purchaseItem.comment,
                recurringInterval,
            )
    }
}