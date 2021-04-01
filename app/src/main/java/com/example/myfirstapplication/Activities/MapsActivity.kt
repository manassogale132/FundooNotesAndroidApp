package com.example.myfirstapplication.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myfirstapplication.GeofenceHelper
import com.example.myfirstapplication.R
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var geofencingClient: GeofencingClient
    private  var FINE_LOCATION_ACCESS_REQUEST_CODE = 10001

    private val GEOFENCE_RADIUS = 70f

    private var geofenceHelper: GeofenceHelper? = null

    //private val GEOFENCE_ID = "SOME_GEOFENCE_ID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        geofencingClient = LocationServices.getGeofencingClient(this)
        geofenceHelper = GeofenceHelper(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Setting default current location
        val wadala = LatLng(19.01373769462871, 72.85106551611266)  //19.01373769462871, 72.85106551611266
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(wadala, 16F))

        enableUserLocation()

        mMap.setOnMapLongClickListener(this)

        mMap.setOnMapClickListener(this)
    }

    private fun enableUserLocation() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true  //if permission is granted enable the user location
        } else {
            //ask for permission
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                //we need to show user a dialog why permission is needed
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    FINE_LOCATION_ACCESS_REQUEST_CODE)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    FINE_LOCATION_ACCESS_REQUEST_CODE)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if(requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //we have the permission
                mMap.isMyLocationEnabled = true
            }else{

            }
        }

        if(requestCode == 10002) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //we have the permission
                Toast.makeText(this,"You can add geofences",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Background location access is necessary",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapLongClick(latLng: LatLng) {
        if(Build.VERSION.SDK_INT >= 29){
            //we need background permission
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                addMarker(latLng)
                addCircle(latLng,GEOFENCE_RADIUS)
                addGeofence(latLng,GEOFENCE_RADIUS)
            } else {
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission
                        .ACCESS_BACKGROUND_LOCATION)) {
                    ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission
                        .ACCESS_BACKGROUND_LOCATION),
                        10002)
                }else {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                        10002)
                }
            }
        }else{
            addMarker(latLng)
            addCircle(latLng,GEOFENCE_RADIUS)
            addGeofence(latLng,GEOFENCE_RADIUS)
        }
    }

    private fun addGeofence(latLng: LatLng, radius : Float) {  //to add geofence ,calling the methods from "GeofenceHelper"

        val current = System.currentTimeMillis() / 1000
        val geofenceId = current.toString()

        val geofence : Geofence? = geofenceHelper?.getGeofence(geofenceId, latLng, radius,
            Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_DWELL
                            or Geofence.GEOFENCE_TRANSITION_EXIT)

        Log.d("geofenceId", "onSuccess: $geofenceId")

        val geofencingRequest : GeofencingRequest = geofenceHelper!!.getGeofencingRequest(geofence!!)
5
        val pendingIntent : PendingIntent = geofenceHelper!!.pendingIntent()

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            return
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
            .addOnSuccessListener {
                Log.d("GeofenceCheck", "onSuccess: Geofence Added")
            }
            .addOnFailureListener{
                val errorMessage = geofenceHelper!!.getErrorString(it)
                Log.d("GeofenceCheck", "onFailure: $errorMessage")
            }
    }

    private fun addMarker(latLng: LatLng){
        val markerOptions : MarkerOptions = MarkerOptions().position(latLng)
        mMap.addMarker(markerOptions)
    }

    private fun addCircle(latLng: LatLng, radius : Float){
        val circleOptions = CircleOptions()
        circleOptions.center(latLng)
        circleOptions.radius(radius.toDouble())
        circleOptions.strokeColor(Color.argb(55, 0, 0, 255))
        circleOptions.fillColor(Color.argb(45, 0, 0, 255))
        circleOptions.strokeWidth(4f)
        mMap.addCircle(circleOptions)
    }

    override fun onMapClick(p0: LatLng?) {
        val pendingIntent : PendingIntent = geofenceHelper!!.pendingIntent()

        mMap.clear()
        geofencingClient.removeGeofences(pendingIntent)
            .addOnSuccessListener {
                Log.d("GeofenceCheck", "onSuccess: Geofence Removed")
            }
            .addOnFailureListener{
                val errorMessage = geofenceHelper!!.getErrorString(it)
                Log.d("GeofenceCheck", "onFailure: $errorMessage")
            }
    }
}