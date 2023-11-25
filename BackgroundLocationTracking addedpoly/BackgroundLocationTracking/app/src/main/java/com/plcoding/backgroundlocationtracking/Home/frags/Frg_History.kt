package com.plcoding.backgroundlocationtracking.Home.frags


import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationRequest.create
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.plcoding.backgroundlocationtracking.Api.RequestNetwork
import com.plcoding.backgroundlocationtracking.R
import com.plcoding.backgroundlocationtracking.UtilsPackage.UtilsClass
import de.p72b.maps.animation.AnimatedPolyline


class Frg_History : Fragment(), OnMapReadyCallback {

    var history_api: RequestNetwork? = null
    var history_api_listener: RequestNetwork.RequestListener? = null

    //////
    private var mapView: MapView? = null
    private var mMap: GoogleMap? = null


    private var mark: Marker? = null

    var mFusedLocationClient: FusedLocationProviderClient? = null
    var PERMISSION_ID = 44

    var progress: ProgressDialog? = null

  /*  private lateinit var animatedPolyline: AnimatedPolyline
    private val wayPoints = mutableListOf(
        LatLng(20.296059, 85.824539),
        LatLng(20.296051, 85.824545),

    )*/

    private var user_Latitude = 0.0
    private var user_Longitude = 0.0

    override fun onCreateView(
        _inflater: LayoutInflater,
        _container: ViewGroup?,
        _savedInstanceState: Bundle?
    ): View {
        val _view = _inflater.inflate(R.layout.frg_history_layout, _container, false)
        initialize(_view)


        /////



        mapView!!.onCreate(_savedInstanceState)






        initializeLogic()



        return _view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView!!.getMapAsync(this)

        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialize(_view: View) {

        mapView = _view.findViewById(R.id.mapView3)
        progress = ProgressDialog(requireActivity())
        progress!!.setMessage("Finding all routes..")
        //progress!!.show()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());


    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation = locationResult.lastLocation
            user_Latitude =  mLastLocation!!.latitude
            user_Longitude = mLastLocation.longitude
            //progress!!.dismiss()
            add_marker(user_Latitude, user_Longitude)

        }
    }

    override fun onResume() {
        mapView!!.onResume();

        super.onResume()
    }

    private fun initializeLogic() {

       /* _l_location_listener = LocationListener { _param1: Location ->
            val _lat = _param1.latitude
            val _lng = _param1.longitude
            val _acc = _param1.accuracy.toDouble()
            user_Latitude = _lat
            user_Longitude = _lng




        }*/


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



    private fun getMyLocation() {


        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            mMap!!.isMyLocationEnabled = true

            Log.d("2323","212121")

            mFusedLocationClient!!.lastLocation.addOnCompleteListener { task ->
                val location = task.result
                if (location == null) {
                    requestNewLocationData()
                } else {
                    add_marker(location.latitude, location.longitude)
                }
            }
            requestNewLocationData()
         /*   mMap!!.setOnMyLocationChangeListener { location: Location ->


                val ltlng = LatLng(location.latitude, location.longitude)
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(ltlng, 16f)
                mMap!!.animateCamera(cameraUpdate)
                add_marker(location.latitude, location.longitude)
                // mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(ltlng, 15f))
            }*/
        }




    }


    private fun enableLocation() {


        val locationRequest = create()
        locationRequest.apply {
            priority = PRIORITY_HIGH_ACCURACY
            interval = 30 * 1000.toLong()
            fastestInterval = 5 * 1000.toLong()
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result=
            activity?.let { LocationServices.getSettingsClient(it).checkLocationSettings(builder.build()) }
        result!!.addOnCompleteListener {
            try {
                val response: LocationSettingsResponse = it.getResult(ApiException::class.java)
                println("location>>>>>>> ${response.locationSettingsStates!!.isGpsPresent}")
                if(response.locationSettingsStates!!.isGpsPresent)
                {
                    getMyLocation()
                }

            }catch (e: ApiException){
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                        try {
                            val intentSenderRequest =
                                e.status.resolution?.let { it1 -> IntentSenderRequest.Builder(it1).build() }
                            launcher.launch(intentSenderRequest)
                        } catch (e: IntentSender.SendIntentException) {
                        }
                }
            }
        }
    }


    private var launcher=  registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("TAG", "OK")

            getMyLocation()

        } else {
            Log.d("TAG", "CANCEL")
            enableLocation()
            // UtilsClass.showToast("Please Accept Location.",this@MainActivity)
        }
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        Log.d("2323343","212121")
        //UtilsClass.showToast("Please Accept Location.sadasdad",requireActivity())
        enableLocation()

      /*  animatedPolyline = AnimatedPolyline(
            mMap!!,
            wayPoints,
            polylineOptions = getPolylineOptions(),
            duration = 1250,
            interpolator = DecelerateInterpolator(),
            animatorListenerAdapter = getListener()
        )
        animatedPolyline.startWithDelay(1000)*/

    }

  /*  private fun clearAnimation() {
        animatedPolyline.remove()
    }

    private fun getListener(): AnimatorListenerAdapter {
        return object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                animatedPolyline.start()
            }
        }
    }

    private fun getPolylineOptions(): PolylineOptions {
        return PolylineOptions()
            .color(ContextCompat.getColor(requireActivity(), R.color.primaryTextColor))
            .pattern(
                listOf(
                    Dot(), Gap(20F)
                )
            )
    }

    private fun addOriginAndDestination() {
        mMap!!.addMarker(MarkerOptions().position(wayPoints.first()))
        mMap!!.addMarker(MarkerOptions().position(wayPoints.last()))
    }
*/

    fun add_marker(lat: Double, lng: Double) {
        val location = LatLng(lat, lng)
        //map.addMarker(new MarkerOptions().position(location).title("Delivery Boy"));


        //poly_line(20.296059,85.824539,20.265164,85.786319,map);

        // Move the camera to the marker location and set zoom level
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

        // Custom marker icon (assuming you named your custom marker icon as "custom_marker_icon")
        if (mark != null) {
            mark!!.remove()
        }
        mark = mMap!!.addMarker(
            MarkerOptions().position(location)
                .title("You")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.track))
        )


        // mMap.addMarker(markerOptions);
    }


}