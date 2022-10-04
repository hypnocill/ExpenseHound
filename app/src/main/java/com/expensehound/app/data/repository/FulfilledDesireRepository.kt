package com.expensehound.app.data.repository

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.expensehound.app.data.entity.FulfilledDesire
import com.expensehound.app.data.provider.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FulfilledDesireRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)

    fun getAll(container: SnapshotStateList<FulfilledDesire>, from: Long? = null, to: Long? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            db.fulfilledDesireDao().getAll(from, to).collect {
                container.clear()
                it.forEach { item ->
                    container.add(item)
                }
            }
        }
    }

    fun insert(fulfilledDesire: FulfilledDesire) {
        CoroutineScope(Dispatchers.IO).launch {
            db.fulfilledDesireDao().insert(fulfilledDesire)
        }
    }
}