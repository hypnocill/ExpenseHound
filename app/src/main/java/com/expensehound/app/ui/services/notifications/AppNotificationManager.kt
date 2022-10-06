package com.expensehound.app.ui.services.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.expensehound.app.R
import com.expensehound.app.data.entity.PurchaseItem
import java.util.*
import kotlin.random.Random

class AppNotificationManager {
    @RequiresApi(Build.VERSION_CODES.M)
    companion object {

        fun notify(context: Context, pendingIntent: PendingIntent, channelId: String, title: String, text: String) {
            val builder = NotificationCompat.Builder(context, "channel1")
                .setChannelId(channelId)
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)

            val notificationManagerCompat = NotificationManagerCompat.from(context)
            notificationManagerCompat.notify(Random.nextInt(1, 1000), builder.build())
        }

        fun createNotificationChannel(context: Context) {

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channelID = "channel1"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "Alarmclock Channel"
                val description = " Reminder Alarm manager"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val notificationChannel = NotificationChannel(channelID, name, importance)
                notificationChannel.description = description

                notificationManager.createNotificationChannel(notificationChannel)
            }
        }

        fun cancelFuturePurchaseNotification(context: Context, notificationId: Int) {
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    notificationId,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )

            var alarmManager =
                context.getSystemService(ComponentActivity.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }

        fun setFuturePurchaseNotification(
            context: Context,
            timeMs: Long,
            purchaseItem: PurchaseItem
        ): Int {

            var alarmManager =
                context.getSystemService(ComponentActivity.ALARM_SERVICE) as AlarmManager

            val thuReq: Long = Calendar.getInstance().timeInMillis + 1
            var notificationId = thuReq.toInt() * -1

            val intent = Intent(context, AlarmReceiver::class.java)

            intent.putExtra("purchaseItemName", purchaseItem.name)

            intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)

            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    notificationId,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )

            AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager,
                AlarmManager.RTC_WAKEUP,
                timeMs,
                pendingIntent
            )

            return notificationId
        }
    }


}