package com.expensehound.app.ui.services.workers

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.expensehound.app.data.entity.Income
import com.expensehound.app.data.entity.ItemWithRecurringInterval
import com.expensehound.app.data.entity.PurchaseItem
import com.expensehound.app.data.entity.RecurringInterval
import com.expensehound.app.data.repository.IncomeRepository
import com.expensehound.app.data.repository.PurchaseRepository
import com.expensehound.app.ui.services.notifications.AppNotificationManager
import java.util.*

const val MILLISECONDS_IN_DAY: Long = 86400000
const val MILLISECONDS_IN_WEEK: Long = 604800000
const val MILLISECONDS_IN_MONTH: Long = 2592000000

class RecurringIntervalWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val purchaseRepo = PurchaseRepository(context)
        val incomeRepo = IncomeRepository(context)
        var items = getRecurringIntervalItems(purchaseRepo, incomeRepo)
        var hasAddedIncome = false
        var hasAddedPurchase = false

        val currentDayTimestamp = Calendar.getInstance().timeInMillis

        if (items.isEmpty()) {
            return Result.success()
        }

        var daysToRun = (currentDayTimestamp - items.first().createdAt) / MILLISECONDS_IN_DAY

        if (daysToRun.toInt() > 90) {
            daysToRun = 90
        }

        repeat(daysToRun.toInt()) {
            items = getRecurringIntervalItems(purchaseRepo, incomeRepo)

            items.forEach {
                val isAlreadyAddedToday = (currentDayTimestamp - it.createdAt) < MILLISECONDS_IN_DAY

                if (isAlreadyAddedToday) {
                    return@forEach
                }

                val isWeekly =
                    it.recurringInterval == RecurringInterval.WEEKLY && (currentDayTimestamp - it.createdAt) > MILLISECONDS_IN_WEEK
                val isMonthly =
                    it.recurringInterval == RecurringInterval.MONTHLY && (currentDayTimestamp - it.createdAt) > MILLISECONDS_IN_MONTH

                val shouldBeAdded = isWeekly || isMonthly

                if (!shouldBeAdded) {
                    return@forEach
                }

                if (it is PurchaseItem) {
                    hasAddedPurchase = true

                    purchaseRepo.insertPurchaseItemSync(
                        it.copy(
                            uid = 0,
                            createdAt = System.currentTimeMillis(),
                            createdAutomatically = true
                        )
                    )
                    purchaseRepo.updatePurchaseItemRecurringIntervalSync(it, RecurringInterval.NONE)
                }

                if (it is Income) {
                    hasAddedIncome = true

                    incomeRepo.insertSync(
                        it.copy(
                            uid = 0,
                            createdAt = System.currentTimeMillis(),
                            createdAutomatically = true
                        )
                    )
                    incomeRepo.updateRecurringIntervalSync(it, RecurringInterval.NONE)
                }
            }
        }

        if (hasAddedIncome) {
            AppNotificationManager.notifySimple(context,"channel1", "Известие" ,"Имате автоматично добавен доход")
        }

        if (hasAddedPurchase) {
            AppNotificationManager.notifySimple(context,"channel1", "Известие" ,"Имате автоматично добавен разход")
        }

        return Result.success()
    }

    fun getRecurringIntervalItems(
        purchaseRepository: PurchaseRepository,
        incomeRepository: IncomeRepository
    ): List<ItemWithRecurringInterval> {
        var purchases = purchaseRepository.getAllWithRecurringIntervalSync()
        var incomes = incomeRepository.getAllWithRecurringIntervalSync()

        return purchases + incomes
    }

    companion object {
        fun schedule(context: Context) {
//            val periodicRefreshRequest = PeriodicWorkRequest.Builder(
//                RecurringIntervalWorker::class.java,
//                15,
//                TimeUnit.MINUTES,
//                1,
//                TimeUnit.MINUTES
//            ).build()

            val workManager = WorkManager.getInstance(context)


//            workManager.enqueueUniquePeriodicWork(
//                "reccuring",
//                ExistingPeriodicWorkPolicy.REPLACE,
//                periodicRefreshRequest
//            )


            val periodicRefreshRequest = OneTimeWorkRequest.Builder(
                RecurringIntervalWorker::class.java,
            ).build()


            workManager.enqueue(periodicRefreshRequest)
        }
    }
}


