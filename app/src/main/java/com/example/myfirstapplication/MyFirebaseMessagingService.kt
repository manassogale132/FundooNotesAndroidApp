package com.example.myfirstapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    lateinit var notificationChannel: NotificationChannel
    lateinit var notificationManager: NotificationManager
    lateinit var builder : Notification.Builder
    private val desctiption = "Test Notification"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.notification?.body?.let {body ->
            remoteMessage.notification!!.title?.let { title ->
                getFirebaseMessage(title, body)
            }
        }
    }

    private fun getFirebaseMessage(title: String, msg: String){

        notificationManager =  this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //sdk version higher than 26
            notificationChannel = NotificationChannel("mtFirebaseChannel", desctiption, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, "mtFirebaseChannel")
                    .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setAutoCancel(true)
        } else {      //sdk version less than 26
            builder = Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
        }
        notificationManager.notify(101,builder.build())
    }
}