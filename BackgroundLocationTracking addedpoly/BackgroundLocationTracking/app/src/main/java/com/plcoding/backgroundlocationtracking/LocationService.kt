package com.plcoding.backgroundlocationtracking

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import com.plcoding.backgroundlocationtracking.Api.ApiList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit


class LocationService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient


    var session : SessionManager?= null


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        //api = RequestNetwork(applicationContext)
        session = SessionManager(applicationContext)

        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }


    private fun start() {


        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Location Tracking Enabled")
            .setContentText("Tracking..")
            .setSmallIcon(R.drawable.ic_menu_mylocation)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(156000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude.toString().takeLast(3)
                val long = location.longitude.toString().takeLast(3)
                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $long)"
                )

                Log.d("llll11",lat+"::"+long)
                api_Call(lat, long)

                notificationManager.notify(1, updatedNotification.build())
            }
            .launchIn(serviceScope)

        startForeground(1, notification.build())



    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }



    fun api_Call( lat : String, long: String)
    {
        /*api!!.startRequestNetwork(
            RequestNetworkController.GET,
            "https://jsonplaceholder.typicode.com/posts/1", "",
            _api_request_listener
        )*/




        val url = ApiList().BASE_URL+"method=store_location" +
                "&lat="+lat+"&lng="+long+"&user_id="+ session!!.userID

        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()


        //.header("Authorization", "Bearer ${HeaderBearerKey}")

        // Define the media type for JSON
        val mediaType = MediaType.parse("application/json")

        // Create the request body with the JSON data
        val requestBody = RequestBody.create(mediaType, "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"leader\"\n" +
                "}")


        //   .header("Authorization", "Bearer ${HeaderBearerKey}")
        val request = url.let {
            Request.Builder()

                .header("Content-Type", "application/json")
                .post(requestBody)
                .url(it)
                .build()
        }

        if (request != null) {
            okHttpClient.newCall(request).enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {

                    Log.i("xxx Fail", e.localizedMessage)
                }

                override fun onResponse(call: Call, response: okhttp3.Response) {

                    response.body()?.let {
                        val responseData = it.string()
                       /* kotlin.run {
                            UtilsClass.showToast(responseData,this@LocationService)
                        }*/

                        Log.i("xxx", responseData)

                    }
                }
            })
        }
    }


    /*    fun api_Call()
        {
            *//*api!!.startRequestNetwork(
                RequestNetworkController.GET,
                "https://jsonplaceholder.typicode.com/posts/1", "",
                _api_request_listener
            )*//*

            val url = "https://reqres.in/api/users"

            val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()


            //.header("Authorization", "Bearer ${HeaderBearerKey}")

            // Define the media type for JSON
            val mediaType = MediaType.parse("application/json")

            // Create the request body with the JSON data
            val requestBody = RequestBody.create(mediaType, "{\n" +
                    "    \"name\": \"morpheus\",\n" +
                    "    \"job\": \"leader\"\n" +
                    "}")


            //   .header("Authorization", "Bearer ${HeaderBearerKey}")
            val request = url?.let {
                okhttp3.Request.Builder()

                    .header("Content-Type", "application/json")
                    .post(requestBody)
                    .url(it)
                    .build()
            }

            if (request != null) {
                okHttpClient.newCall(request).enqueue(object : Callback {

                    override fun onFailure(call: Call, e: IOException) {

                        Log.i("xxx Fail", e.localizedMessage)
                    }

                    override fun onResponse(call: Call, response: okhttp3.Response) {

                        response.body()?.let {

                            val responseData = it.string()
                            Log.i("xxx", responseData)

                        }
                    }
                })
            }
        }*/





    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
       /* Intent(applicationContext, LocationService::class.java).apply {
            action = ACTION_START
            startService(this)
        }*/

    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}