package com.expensehound.app.ui.services.workers

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.expensehound.app.data.entity.RecurringInterval
import com.expensehound.app.data.repository.PurchaseRepository
import com.expensehound.app.utils.isFirstDayOfMonth
import com.expensehound.app.utils.isMonday

const val INTERVAL_HOURS = 24

// This worker is NOT idempotent.
// SHOULD ONLY BE EXECUTED ONCE PER 24h.
class RecurringIntervalWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val repository = PurchaseRepository(context)
        val list = repository.getAllWithRecurringIntervalSync()

        list.forEach {
            val isDaily = it.recurringInterval == RecurringInterval.DAILY
            val isWeekly = it.recurringInterval == RecurringInterval.WEEKLY && isMonday()
            val isMonthly = it.recurringInterval == RecurringInterval.MONTHLY && isFirstDayOfMonth()

            if (isDaily || isWeekly || isMonthly) {
                repository.insertPurchaseItem(
                    it.copy(
                        uid = 0,
                        createdAt = System.currentTimeMillis()
                    )
                )

                repository.updatePurchaseItemRecurringInterval(it, RecurringInterval.NONE)
            }
        }

        return Result.success()
    }

    companion object {
        fun schedule(context: Context) {
//            val periodicRefreshRequest = PeriodicWorkRequest.Builder(
//                RecurringIntervalWorker::class.java, // Your worker class
//                INTERVAL_HOURS,
//                TimeUnit.HOURS
//            ).build()

            val periodicRefreshRequest = OneTimeWorkRequest.Builder(
                RecurringIntervalWorker::class.java, // Your worker class
            ).build()

            val workManager = WorkManager.getInstance(context)

            workManager.enqueue(periodicRefreshRequest)
//            workManager.enqueueUniquePeriodicWork(
//                "worker",
//                ExistingPeriodicWorkPolicy.REPLACE,
//                periodicRefreshRequest
//            )
        }
    }
}


