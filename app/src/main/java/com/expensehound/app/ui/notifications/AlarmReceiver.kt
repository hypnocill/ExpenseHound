package com.expensehound.app.ui.notifications

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.expensehound.app.R
import com.expensehound.app.ui.MainActivity
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi

class AlarmReceiver : BroadcastReceiver() {

    @OptIn(ExperimentalMaterialNavigationApi::class)
    override fun onReceive(context: Context, intent: Intent?) {
        val title = intent?.getStringExtra("purchaseItemName")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationIntent = Intent(context, MainActivity::class.java)
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            val builder = NotificationCompat.Builder(context, "channel1")
                .setChannelId("channel1")
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                .setContentTitle("Известие за покупка")
                .setContentText("$title очаква да бъде закупено")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .addAction(R.drawable.ic_baseline_arrow_drop_down_24,"Stop",pendingIntent)
                .setContentIntent(pendingIntent)

            val notificationManagerCompat = NotificationManagerCompat.from(context)
            notificationManagerCompat.notify(99, builder.build())
        }

    }

}