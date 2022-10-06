package com.expensehound.app.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.expensehound.app.ui.services.notifications.AppNotificationManager
import com.expensehound.app.ui.services.workers.RecurringIntervalWorker
import com.expensehound.app.ui.viewmodel.MainViewModel
import com.expensehound.app.ui.viewmodel.StatsViewModel
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi

@ExperimentalMaterialNavigationApi
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val demoViewModel = MainViewModel(this)
        val statsViewModel = StatsViewModel(this)

        setContent {
            App(demoViewModel, statsViewModel)
        }

        RecurringIntervalWorker.schedule(this)
        AppNotificationManager.createNotificationChannel(this)
    }
}

