package com.expensehound.app.data.provider

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.expensehound.app.data.Category
import com.expensehound.app.data.FulfilledDesire
import com.expensehound.app.data.Notifications
import com.expensehound.app.data.PurchaseItem
import java.util.concurrent.Executors

private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

@Database(entities = [PurchaseItem::class, Notifications::class, FulfilledDesire::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun purchaseItemDao(): PurchaseItemDao
    abstract fun fulfilledDesireDao(): FulfilledDesireDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app.db"
            )
                .addCallback(seedDatabaseCallback(context))
                .build()

        private fun seedDatabaseCallback(context: Context): Callback {

            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    ioThread {
                        val purchaseItems = getInstance(context).purchaseItemDao()
                        purchaseItems.insert(
                            PurchaseItem(
                                1,
                                "Кафе",
                                null,
                                Category.FUN,
                                30.54,
                                isPurchased = true
                            )
                        )
                        purchaseItems.insert(
                            PurchaseItem(
                                2,
                                "Квартира",
                                null,
                                Category.FUN,
                                10.0,
                                isPurchased = true
                            )
                        )
                        purchaseItems.insert(
                            PurchaseItem(
                                3,
                                "Кюфтета",
                                null,
                                Category.FUN,
                                5.0,
                                isPurchased = true
                            )
                        )
                        purchaseItems.insert(
                            PurchaseItem(
                                4,
                                "Пуловер Декатлон",
                                null,
                                Category.FUN,
                                25.0,
                                isPurchased = false
                            )
                        )
                    }
                }
            }
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}


fun ioThread(f: () -> Unit) {
    IO_EXECUTOR.execute(f)
}