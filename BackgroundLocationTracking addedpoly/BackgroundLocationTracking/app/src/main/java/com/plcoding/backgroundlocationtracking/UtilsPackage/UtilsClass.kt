package com.plcoding.backgroundlocationtracking.UtilsPackage

import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.widget.Toast
import com.plcoding.backgroundlocationtracking.UtilsPackage.UtilsClass
import android.app.ProgressDialog
import android.content.Context
import java.lang.Exception

object UtilsClass {
    fun isNetworkAvailable(applicationContext: Context): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager?.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun showAlert(msg: String, is_cancel: Boolean, title: String, ctx: Activity?) {
        try {
            AlertDialog.Builder(ctx)
                .setMessage(msg.trim { it <= ' ' })
                .setCancelable(is_cancel)
                .setTitle(title.trim { it <= ' ' })
                .setPositiveButton("OK") { dialogInterface, i -> }.create().show()
        } catch (e: Exception) {
            Toast.makeText(ctx, "LINE 34 $e", Toast.LENGTH_SHORT).show()
        }
    }

    fun showToast(_msg: String?, ctx: Context?) {
        Toast.makeText(ctx, _msg, Toast.LENGTH_SHORT).show()
    }

    fun show_no_connection(ctx: Activity) {
        if (isNetworkAvailable(ctx)) {
            showAlert(
                "Retry task again.",
                false, "Server slow! Try again", ctx
            )
        } else {
            showAlert(
                "Check your WIFI or Mobile Internet connection..",
                false, "No internet!", ctx
            )
        }
    }

    fun progress(ctx: Activity?, isShow: Boolean) {
        val prog = ProgressDialog(ctx)
        prog.setMessage("Just a moment..")
        prog.setCancelable(false)
        if (isShow) {
            prog.show()
        } else {
            prog.dismiss()
        }
    }







}