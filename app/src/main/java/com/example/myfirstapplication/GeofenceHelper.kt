package com.example.myfirstapplication

import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.maps.model.LatLng


class GeofenceHelper(base: Context) : ContextWrapper(base) {   //ContextWrapper - use hte context using 'this' keyword in this class

    private val TAG = "GeofenceHelper"
    private lateinit var pendingIntent: PendingIntent

    fun getGeofencingRequest(geofence: Geofence): GeofencingRequest {    //Method returing GeofencingRequest
        return GeofencingRequest.Builder()
            .addGeofences(listOf(geofence)) // you can either add single geofence or list of geofence
                /*if you are already inside a geofence that you created , then whether this geofence should be
                triggred or not (INITIAL_TRIGGER_ENTER - in this case it will be triggred */
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .build()
    }

    fun getGeofence(ID: String, latLng: LatLng, radius: Float, transitionTypes: Int): Geofence? {  //Method returing GeofencingResult
        return Geofence.Builder()
            .setCircularRegion(latLng.latitude, latLng.longitude, radius)
            .setRequestId(ID)  //ID is unique for each Geofence you create
            .setTransitionTypes(transitionTypes)  //enter , dwell , exit
            /*setLoiteringDelay-after entring the geofence , how many you seconds you want to be notified that you are dwelling
             in the geofence*/
            .setLoiteringDelay(3000)
            .setExpirationDuration(Geofence.NEVER_EXPIRE) //NEVER_EXPIRE - dont want geofence to expire
            .build()
    }

    fun pendingIntent(): PendingIntent {   //Method to get PendingIntent
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 2607, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
            /*FLAG_UPDATE_CURRENT - we can get same pending intent back when we call addGeofences and removeGefences
                from geofencingClient*/
        return pendingIntent
    }

    fun getErrorString(e: Exception): String? {   //this method will return error string in case of failure
        if (e is ApiException) {
            when (e.statusCode) {
                GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> return "GEOFENCE_NOT_AVAILABLE"
                GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> return "GEOFENCE_TOO_MANY_GEOFENCES"
                GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS -> return "GEOFENCE_TOO_MANY_PENDING_INTENTS"
            }
        }
        return e.localizedMessage
    }
}