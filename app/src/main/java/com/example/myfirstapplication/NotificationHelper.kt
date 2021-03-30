package com.example.myfirstapplication

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*


class NotificationHelper(base: Context?) : ContextWrapper(base) {

    private val CHANNEL_NAME = "High priority channel"
    private val CHANNEL_ID = "com.example.notifications$CHANNEL_NAME"
    lateinit var notificationManager: NotificationManager

    companion object {
        private const val TAG = "NotificationHelper"
    }

    init {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //sdk version higher than 26
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.description = "this is the description of the channel."
            notificationChannel.lightColor = Color.RED
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun sendHighPriorityNotification(title: String?, body: String?, activityName: Class<*>?) {
        val intent = Intent(this, activityName)
        val pendingIntent = PendingIntent.getActivity(this, 267, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle().setSummaryText("summary").setBigContentTitle(title).bigText(body))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(this).notify(Random().nextInt(), notification)
    }
}