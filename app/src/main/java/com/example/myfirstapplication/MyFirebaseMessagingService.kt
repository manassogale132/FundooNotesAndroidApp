package com.example.myfirstapplication

import android.annotation.SuppressLint
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.notification?.body?.let {
            remoteMessage.notification!!.title?.let { it1 ->
                getFirebaseMessage(it1, it)
            }
        }
    }

    private fun getFirebaseMessage(title: String, msg: String){
        val builder : NotificationCompat.Builder = NotificationCompat.Builder(this,"mtFirebaseChannel")
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(title)
            .setContentText(msg)
            .setAutoCancel(true)

        val manager : NotificationManagerCompat = NotificationManagerCompat.from(this)
        manager.notify(101,builder.build())
    }
}