package com.expensehound.app.data.provider

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.expensehound.app.data.FulfilledDesire
import kotlinx.coroutines.flow.Flow

@Dao
interface FulfilledDesireDao {
    @Query("SELECT * FROM fulfilled_desires")
    fun getAll(): Flow<List<FulfilledDesire>>

    @Insert
    fun insert(fulfilledDesire: FulfilledDesire)

    @Insert
    fun insertAll(vararg fulfilledDesire: FulfilledDesire)

    @Delete
    fun delete(purchaseItem: FulfilledDesire)

    @Query("DELETE FROM fulfilled_desires WHERE uid = :uid")
    fun delete(uid: Int)
}