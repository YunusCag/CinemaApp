package com.yunuscagliyan.sinemalog.service

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.yunuscagliyan.sinemalog.MainActivity
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.utils.AppConstant
import java.util.concurrent.TimeUnit

class NotificationBroadcast:BroadcastReceiver() {
    private val NOTIFICATION_ID=0

    override fun onReceive(context: Context?, intent: Intent?) {
        setUpNotifications(context!!)
        val constraints= Constraints.Builder()
            .setRequiresCharging(false)
            .build()
        val workRequest: PeriodicWorkRequest = PeriodicWorkRequestBuilder<RemindWork>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
    private fun setUpNotifications(context:Context) {

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val title=context.resources.getString(R.string.notification_title)
        val content=context.resources.getString(R.string.notification_content)

        val notification = NotificationCompat.Builder(context, AppConstant.CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFICATION_ID,notification)
    }
}