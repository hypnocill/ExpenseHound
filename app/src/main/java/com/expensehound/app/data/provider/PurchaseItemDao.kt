package com.expensehound.app.data.provider

import android.graphics.Bitmap
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.expensehound.app.data.Category
import com.expensehound.app.data.PurchaseItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseItemDao {
    @Query("SELECT * FROM purchase_items")
    fun getAll(): Flow<List<PurchaseItem>>

    @Query("SELECT * FROM purchase_items WHERE is_purchased=1")
    fun getAllPurchaseItems(): Flow<List<PurchaseItem>>

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
}