package com.expensehound.app.data.provider

import android.graphics.Bitmap
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.expensehound.app.data.Category
import com.expensehound.app.data.PurchaseItem
import com.expensehound.app.data.StatsPurchaseItemsByCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Dao
interface PurchaseItemDao {
    @Query("SELECT * FROM purchase_items")
    fun getAll(): Flow<List<PurchaseItem>>

    @Query("SELECT * FROM purchase_items WHERE is_purchased=1")
    fun getAllPurchaseItems(): Flow<List<PurchaseItem>>

    @Query("SELECT category, SUM(price) as sum_price, COUNT(*) as count FROM purchase_items WHERE is_purchased=1 GROUP BY category ORDER BY COUNT(*) DESC")
    fun getPurchaseItemsGroupedByCategory():  Flow<List<StatsPurchaseItemsByCategory>>

    @Query("SELECT * FROM purchase_items WHERE is_purchased=0")
    fun getAllFuturePurchaseItems(): Flow<List<PurchaseItem>>

    @Insert
    fun insert(purchaseItem: PurchaseItem)

    @Insert
    fun insertAll(vararg purchaseItems: PurchaseItem)

    @Delete
    fun delete(purchaseItem: PurchaseItem)

    @Query("DELETE FROM purchase_items WHERE uid = :uid")
    fun delete(uid: Int)

    @Query("UPDATE purchase_items SET is_purchased=:isPurchased where uid=:uid")
    fun updateIsPurchased(uid: Int, isPurchased: Boolean)

    @Query("UPDATE purchase_items SET name=:name, image=:image, category=:category, price=:price, comment=:comment where uid=:uid")
    fun updateMainProperties(
        uid: Int,
        name: String,
        image: Bitmap?,
        category: Category,
        price: Double,
        comment: String?
    )

    @Query("UPDATE purchase_items SET notification_id=:notificationId, notification_timestamp=:notificationTimestamp where uid=:uid")
    fun updatePurchaseItemNotification(uid: Int, notificationId: Int?, notificationTimestamp: Long?)
}