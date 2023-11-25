package com.plcoding.backgroundlocationtracking

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.IntegerRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.logicbeanzs.uberpolylineanimation.MapAnimator
import de.p72b.maps.animation.AnimatedPolyline


class MapAnimation : AppCompatActivity(), OnMapReadyCallback {

    private var mapView: MapView? = null
    private var mMap: GoogleMap? = null
    private var marker: Marker? = null

    //////
  /*  private var marker: Marker? = null
    private var animatedPolyline : Polyline? = null
    private val polylines: ArrayList<Polyline> = ArrayList()*/



      private lateinit var animatedPolyline: AnimatedPolyline
      private val wayPoints = mutableListOf(

          LatLng(40.7128, -74.0060),
    LatLng(40.6413, -73.7781),
    LatLng(40.7488, -73.9857),
    LatLng(40.7831, -73.9712),
    LatLng(40.5795, -74.1502)

      )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_animation)
        mapView = findViewById(R.id.mapView3)
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync(this)
    }


    override fun onStart() {
        mapView!!.onStart();
        super.onStart()
    }

    override fun onPause() {
        mapView!!.onPause();
        super.onPause()
    }

    override fun onDestroy() {
        mapView!!.onDestroy();
        super.onDestroy()
    }

    override fun onLowMemory() {
        mapView!!.onLowMemory();
        super.onLowMemory()
    }

    override fun onResume() {
        mapView!!.onResume();
        super.onResume()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        marker = googleMap.addMarker(
            MarkerOptions()
                .position(wayPoints.first())
                .title("Clocked in location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        )

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(wayPoints.first(), 15f))


        marker = googleMap.addMarker(
            MarkerOptions()
                .position(wayPoints.last())
                .title("Clocked out location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        )



        MapAnimator.setPolylineWidth(35f)// default is 8f
        MapAnimator.animateRoute(googleMap, wayPoints)

/*

    MapAnimator.setPercentCompletion(@IntegerRes time: Int) default is 2500 //amount of time to draw the initial polyline
        MapAnimator.setColorFillCompletion(@IntegerRes time: Int) default is 1800 //amount of time to refill the primary color
        MapAnimator.setDelayTime(@IntegerRes time: Int) default is 200 //amount of time for delaying to run the sequenceof animation
        MapAnimator.setPrimaryLineCompletion(@IntegerRes time: Int) default is 2000 //amount of time required for the animation to reach from point A to B
        MapAnimator.setPolylineWidth(width: Float) default is 8f

           fun setPercentCompletion(@IntegerRes time: Int) {
        duration = time
    }
*/


    }





}