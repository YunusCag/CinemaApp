package com.yunuscagliyan.sinemalog.utils

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import com.yunuscagliyan.sinemalog.R
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SinemalogApp:Application() {
    val CHANNEL_ID="channelID_Sinemalog"
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    private fun createNotificationChannel(){
        val channelName=resources.getString(R.string.notification_name)
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channel=
                NotificationChannel(CHANNEL_ID,channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                    lightColor= Color.GREEN
                    enableLights(true)
                }
            val manager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}