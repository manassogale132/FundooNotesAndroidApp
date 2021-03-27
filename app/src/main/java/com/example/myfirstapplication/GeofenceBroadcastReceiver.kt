package com.example.myfirstapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.myfirstapplication.Activities.MapsActivity
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent


class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val notificationHelper = NotificationHelper(context)

        val geofencingEvent = GeofencingEvent.fromIntent(intent)  //handling geofencing events inside BroadcastReceiver
        if (geofencingEvent.hasError()) {
            Log.d("geofencingEventCheck", "onReceive: Error receiving geofence event...");
            return;
        }

        val transitionType = geofencingEvent.geofenceTransition  //transition type enter , dwell, exit

        //when statement on transitionType
        when (transitionType) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT).show()
                notificationHelper.sendHighPriorityNotification("Fundoo notes geofence reminder!","GEOFENCE_TRANSITION_ENTER",MapsActivity::class.java)
            }
            Geofence.GEOFENCE_TRANSITION_DWELL -> {
                Toast.makeText(context, "GEOFENCE_TRANSITION_DWELL", Toast.LENGTH_SHORT).show()
                notificationHelper.sendHighPriorityNotification("Fundoo notes geofence reminder!","GEOFENCE_TRANSITION_DWELL",MapsActivity::class.java)
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                Toast.makeText(context, "GEOFENCE_TRANSITION_EXIT", Toast.LENGTH_SHORT).show()
                notificationHelper.sendHighPriorityNotification("Fundoo notes geofence reminder!","GEOFENCE_TRANSITION_EXIT",MapsActivity::class.java)
            }
        }
    }
}