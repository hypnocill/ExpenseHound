package com.expensehound.app.ui.services.notifications

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.expensehound.app.ui.MainActivity
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val title = intent?.getStringExtra("purchaseItemName")

        AppNotificationManager.notifySimple(context,"channel1", "Известие за покупка" ,"$title очаква да бъде закупено")
    }
}