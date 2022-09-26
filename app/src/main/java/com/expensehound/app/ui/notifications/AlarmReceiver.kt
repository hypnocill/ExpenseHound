package com.expensehound.app.ui.notifications

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.expensehound.app.ui.MainActivity
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi

class AlarmReceiver : BroadcastReceiver() {

    @OptIn(ExperimentalMaterialNavigationApi::class)
    override fun onReceive(context: Context, intent: Intent?) {
        val title = intent?.getStringExtra("purchaseItemName")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationIntent = Intent(context, MainActivity::class.java)
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

            AppNotificationManager.notify(
                context,
                pendingIntent,
                "channel1",
                "Известие за покупка",
                "$title очаква да бъде закупено"
            )
        }

    }

}