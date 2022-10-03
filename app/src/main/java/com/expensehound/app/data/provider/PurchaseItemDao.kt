package com.expensehound.app.data.provider

import android.graphics.Bitmap
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.expensehound.app.data.entity.Category
import com.expensehound.app.data.entity.PurchaseItem
import com.expensehound.app.data.entity.StatsPurchaseItemsByCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseItemDao {
    @Query("SELECT * FROM purchase_items")
    fun getAll(): Flow<List<PurchaseItem>>

    @Query("SELECT * FROM purchase_items WHERE is_purchased=1 ORDER BY uid DESC")
    fun getAllPurchaseItems(): Flow<List<PurchaseItem>>

    @Query("SELECT category, SUM(price) as sum_price, COUNT(*) as count FROM purchase_items WHERE is_purchased=1 GROUP BY category ORDER BY sum_price DESC")
    fun getPurchaseItemsGroupedByCategory(): Flow<List<StatsPurchaseItemsByCategory>>

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