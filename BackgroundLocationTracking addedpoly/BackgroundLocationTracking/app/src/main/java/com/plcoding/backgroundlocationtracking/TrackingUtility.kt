package com.plcoding.backgroundlocationtracking

import android.Manifest.permission.*
import android.content.Context
import android.os.Build
import pub.devrel.easypermissions.EasyPermissions

object TrackingUtility {

    fun hasLocationPermissions(context: Context) =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(
                context,
              ACCESS_FINE_LOCATION,
              ACCESS_COARSE_LOCATION
            )
        } else {

            if (Build.VERSION.SDK_INT >= 33)
            {
                EasyPermissions.hasPermissions(
                    context,
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION,
                    POST_NOTIFICATIONS,
                    ACCESS_BACKGROUND_LOCATION
                )
            }else
            {
                EasyPermissions.hasPermissions(
                    context,
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION,
                    ACCESS_BACKGROUND_LOCATION
                )
            }

        }
}