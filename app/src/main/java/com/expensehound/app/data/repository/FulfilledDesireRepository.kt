package com.expensehound.app.data.repository

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.expensehound.app.data.FulfilledDesire
import com.expensehound.app.data.provider.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FulfilledDesireRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)

    fun getAll(container: SnapshotStateList<FulfilledDesire>) {
        CoroutineScope(Dispatchers.IO).launch {
            db.fulfilledDesireDao().getAll().collect {
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