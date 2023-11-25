package com.plcoding.backgroundlocationtracking

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.plcoding.backgroundlocationtracking.Home.MainActivity
import com.plcoding.backgroundlocationtracking.LoginSignup.Login
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


class SplashActivity : AppCompatActivity(),
    EasyPermissions.PermissionCallbacks{

    private val PERMISSION_REQUEST_CODE = 1

    var session : SessionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        session = SessionManager(this)


        Log.d("aaaa","lunch")

    }


    override fun onResume() {
        requestPermissions()
        super.onResume()
    }

    private fun requestPermissions() {
        if (TrackingUtility.hasLocationPermissions(this)) {
            next()
        }else
        {


            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {

                EasyPermissions.requestPermissions(
                    this@SplashActivity,
                    "You need to accept permissions to use this app",
                    PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            } else {
                if(Build.VERSION.SDK_INT >= 33)
                {

                    PermissionsAPI_33()
                } else {

                    EasyPermissions.requestPermissions(
                        this@SplashActivity,
                        "You need to accept permissions to use this app",
                        PERMISSION_REQUEST_CODE,

                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_BACKGROUND_LOCATION

                    )
                }
        }




        }
    }




    fun PermissionsAPI_33()
    {
        EasyPermissions.requestPermissions(
            this,
            "You need to accept permissions to use this app",
            PERMISSION_REQUEST_CODE,

            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.POST_NOTIFICATIONS,
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION

        )
    }


    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        //next()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this@SplashActivity)
    }


    fun next()
    {
        Handler(Looper.myLooper()!!).postDelayed({
            if(session!!.userID.equals(""))
                startActivity(Intent(this, Login::class.java))
            else {
                startActivity(Intent(this, MainActivity::class.java))

            }
            finish()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
        }, 2500)
    }





}