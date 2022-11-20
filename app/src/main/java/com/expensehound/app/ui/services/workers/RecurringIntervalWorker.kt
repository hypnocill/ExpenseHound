package com.expensehound.app.ui.services.workers

import android.content.Context
import android.util.Log
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
import java.util.*
import java.util.concurrent.TimeUnit

const val INTERVAL_HOURS: Long = 24
const val MILLISECONDS_IN_DAY: Int = 86400000

// This worker is NOT idempotent.
// SHOULD ONLY BE EXECUTED ONCE PER 24h
class RecurringIntervalWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val repository = PurchaseRepository(context)
        var list = repository.getAllWithRecurringIntervalSync()

        var daysToRun = (System.currentTimeMillis() - list.first().createdAt) / MILLISECONDS_IN_DAY

        if (daysToRun.toInt() == 0) {
            return Result.success()
        }

        if (daysToRun.toInt() > 60) {
            daysToRun = 60
        }

        repeat(daysToRun.toInt() ) {
            var list = repository.getAllWithRecurringIntervalSync()

            list.forEach {
                val calendar = Calendar.getInstance();
                calendar.setTimeInMillis(it.createdAt)

                val isDaily = it.recurringInterval == RecurringInterval.DAILY
                val isWeekly = it.recurringInterval == RecurringInterval.WEEKLY && isMonday(calendar)
                val isMonthly = it.recurringInterval == RecurringInterval.MONTHLY && isFirstDayOfMonth(calendar)

                if (isDaily || isWeekly || isMonthly) {
                    repository.insertPurchaseItemSync(
                        it.copy(
                            uid = 0,
                            createdAt = System.currentTimeMillis(),
                            createdAutomatically = true
                        )
                    )

                    repository.updatePurchaseItemRecurringIntervalSync(it, RecurringInterval.NONE)
                }
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
                15,
                TimeUnit.MINUTES
            ).build()

            val workManager = WorkManager.getInstance(context)

//DEBUGGING CODE
//val periodicRefreshRequest = OneTimeWorkRequest.Builder(
//    RecurringIntervalWorker::class.java,
//).build()

//DEBUGGING CODE
//workManager.enqueue(periodicRefreshRequest)

            workManager.enqueueUniquePeriodicWork(
                "worker",
                ExistingPeriodicWorkPolicy.KEEP,
                periodicRefreshRequest
            )
        }
    }
}


