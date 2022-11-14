package com.expensehound.app.ui.services.workers

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.expensehound.app.data.entity.RecurringInterval
import com.expensehound.app.data.repository.PurchaseRepository
import com.expensehound.app.utils.isFirstDayOfMonth
import com.expensehound.app.utils.isMonday
import java.util.concurrent.TimeUnit

const val INTERVAL_HOURS: Long = 24

// This worker is NOT idempotent.
// SHOULD ONLY BE EXECUTED ONCE PER 24h.
// REFACTOR to
class RecurringIntervalWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val repository = PurchaseRepository(context)
        val list = repository.getAllWithRecurringIntervalSync()

        // instead of assuming this worker will always be executed once a day,
        // get the date of the last item with recurring interval, calculate the days different from current time,
        // and loop each day to add all for that day
        list.forEach {
            val isDaily = it.recurringInterval == RecurringInterval.DAILY
            val isWeekly = it.recurringInterval == RecurringInterval.WEEKLY && isMonday()
            val isMonthly = it.recurringInterval == RecurringInterval.MONTHLY && isFirstDayOfMonth()

            if (isDaily || isWeekly || isMonthly) {
                repository.insertPurchaseItem(
                    it.copy(
                        uid = 0,
                        createdAt = System.currentTimeMillis(),
                        createdAutomatically = true
                    )
                )

                repository.updatePurchaseItemRecurringInterval(it, RecurringInterval.NONE)
            }
        }

        return Result.success()
    }

    companion object {
        fun schedule(context: Context) {
            val periodicRefreshRequest = PeriodicWorkRequest.Builder(
                RecurringIntervalWorker::class.java,
                INTERVAL_HOURS,
                TimeUnit.HOURS,

            ).build()

//           THIS IS JUST FOR DEBUGGING PURPOSES
//            val periodicRefreshRequest = OneTimeWorkRequest.Builder(
//                RecurringIntervalWorker::class.java,
//            ).build()

            val workManager = WorkManager.getInstance(context)

//            THIS IS JUST FOR DEBUGGING PURPOSES
//            workManager.enqueue(periodicRefreshRequest)
            workManager.enqueueUniquePeriodicWork(
                "worker",
                ExistingPeriodicWorkPolicy.KEEP,
                periodicRefreshRequest
            )
        }
    }
}


