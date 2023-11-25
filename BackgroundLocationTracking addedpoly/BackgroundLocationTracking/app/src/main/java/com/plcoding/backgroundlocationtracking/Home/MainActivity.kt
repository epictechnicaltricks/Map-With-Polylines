package com.plcoding.backgroundlocationtracking.Home

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.plcoding.backgroundlocationtracking.Home.frags.Frg_Track
import com.plcoding.backgroundlocationtracking.Home.frags.Frg_History
import com.plcoding.backgroundlocationtracking.Home.frags.Frg_Account
import android.os.Bundle
import com.plcoding.backgroundlocationtracking.R
import com.plcoding.backgroundlocationtracking.UtilsPackage.UtilsClass
import android.content.Intent
import android.widget.Toast
import android.app.Activity
import android.content.IntentSender
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*

class MainActivity : AppCompatActivity() {
    //	private ViewPager viewpager1;
    private var bottomnavigation1: BottomNavigationView? = null
    var fragment1 = Frg_Track()
    var fragment2 = Frg_History()
    var fragment3 = Frg_Account()
    var active: Fragment = fragment1
    val fm = supportFragmentManager
    override fun onCreate(_savedInstanceState: Bundle?) {
        super.onCreate(_savedInstanceState)
        setContentView(R.layout.home)
        fm.beginTransaction().add(R.id.frame_layout, fragment3, "3").hide(fragment3).commit()
        fm.beginTransaction().add(R.id.frame_layout, fragment2, "2").hide(fragment2).commit()
        fm.beginTransaction().add(R.id.frame_layout, fragment1, "1").commit()
        initialize()
        initializeLogic()
    }

    private fun initialize() {
        bottomnavigation1 = findViewById(R.id.bottomnavigation1)


        /*Window w = getWindow();
		w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
				WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);*/
        //setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        window.statusBarColor = resources.getColor(R.color.primaryTextColor)
        fm.beginTransaction().hide(active).show(fragment1).commit()
        active = fragment1
        bottomnavigation1!!.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            bottomnavigation1!!.getMenu().getItem(item.itemId).isChecked = true
            when (item.itemId) {
                0 -> {
                    fm.beginTransaction().hide(active).show(fragment1).commit()
                    active = fragment1
                }
                1 -> {
                    fm.beginTransaction().hide(active).show(fragment2).commit()
                    active = fragment2
                }
                2 -> {
                    fm.beginTransaction().hide(active).show(fragment3).commit()
                    active = fragment3
                }
                3 -> UtilsClass.showToast("More", this@MainActivity)
            }
            false
        })
    }

    private fun initializeLogic() {
        bottomnavigation1!!.menu.add(0, 0, 0, "Track").setIcon(R.drawable.track)
        bottomnavigation1!!.menu.add(0, 1, 0, "History").setIcon(R.drawable.history)
        bottomnavigation1!!.menu.add(0, 2, 0, "Account").setIcon(R.drawable.user)
        bottomnavigation1!!.menu.add(0, 3, 0, "More").setIcon(R.drawable.more)
        bottomnavigation1!!.menu.getItem(0).isChecked = true


        enableLocation()

    }

    override fun onActivityResult(_requestCode: Int, _resultCode: Int, _data: Intent?) {
        super.onActivityResult(_requestCode, _resultCode, _data)
    }

    @Deprecated("")
    fun showMessage(_s: String?) {
        Toast.makeText(applicationContext, _s, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val win = activity.window
            val winParams = win.attributes
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }
    }


    private fun enableLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.apply {
            priority =LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 30 * 1000.toLong()
            fastestInterval = 5 * 1000.toLong()
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result=
            LocationServices.getSettingsClient(this@MainActivity).checkLocationSettings(builder.build())
        result.addOnCompleteListener {
            try {
                val response: LocationSettingsResponse = it.getResult(ApiException::class.java)
                println("location>>>>>>> ${response.locationSettingsStates!!.isGpsPresent}")
                if(response.locationSettingsStates!!.isGpsPresent)
                {

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

        } else {
            Log.d("TAG", "CANCEL")
            enableLocation()
           // UtilsClass.showToast("Please Accept Location.",this@MainActivity)
        }
    }

}