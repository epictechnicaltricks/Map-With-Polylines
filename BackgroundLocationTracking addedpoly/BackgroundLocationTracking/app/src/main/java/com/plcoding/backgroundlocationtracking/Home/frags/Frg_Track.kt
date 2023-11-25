package com.plcoding.backgroundlocationtracking.Home.frags

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.plcoding.backgroundlocationtracking.LocationService
import com.plcoding.backgroundlocationtracking.R
import com.plcoding.backgroundlocationtracking.SessionManager
import java.text.SimpleDateFormat
import java.util.*

class Frg_Track : Fragment() {
    var clock_btn: RelativeLayout? = null
    var clock_layout: LinearLayout? = null
    var isClockin = false
    var clock_msg: TextView? = null
    var clock_msg2: TextView? = null
    var time: TextView? = null
    var clock_status: TextView? = null
    var clock_in_anim: LottieAnimationView? = null
    var clock_out_anim: LottieAnimationView? = null
    var session: SessionManager? = null
    override fun onCreateView(
        _inflater: LayoutInflater,
        _container: ViewGroup?,
        _savedInstanceState: Bundle?
    ): View {
        val _view = _inflater.inflate(R.layout.frg_track, _container, false)
        initialize(_view)
        initializeLogic()
        return _view
    }

    private fun initialize(_view: View) {
        session = SessionManager(activity)
        clock_btn = _view.findViewById(R.id.clock_btn)
        clock_layout = _view.findViewById(R.id.clock_layout)
        clock_msg = _view.findViewById(R.id.clock_msg)
        clock_msg2 = _view.findViewById(R.id.clock_msg2)
        time = _view.findViewById(R.id.time)
        clock_status = _view.findViewById(R.id.clock_status)
        clock_in_anim = _view.findViewById(R.id.animationView)
        clock_out_anim = _view.findViewById(R.id.animationView2)
        clock_in_anim!!.setAnimation(R.raw.clock_in)
        clock_out_anim!!.setAnimation(R.raw.clock_in)
        clock_layout!!.setVisibility(View.GONE)
        if (session!!.clock_status_check == "true") {
            isClockin = false
            clock_out_show()
        } else {
            isClockin = true
            clock_in_show()
        }
    }

    private fun initializeLogic() {
        clock_btn!!.setOnLongClickListener {
            _clickAnimation(clock_layout)
            if (isClockin) {
                clock_in_fun()
            } else {
                clock_out_fun()
            }
            true
        }
    }

    fun _clickAnimation(_view: View?) {
        val fade_in = ScaleAnimation(
            0.8f,
            1f,
            0.8f,
            1f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.7f
        )
        fade_in.duration = 300
        fade_in.fillAfter = true
        _view!!.startAnimation(fade_in)
    }

    fun clock_in_fun() {
        session!!.clock_status_check = "true"
        clock_btn!!.alpha = 0f
        clock_btn!!.visibility = View.GONE
        clock_layout!!.visibility = View.VISIBLE
        clock_btn!!.isEnabled = false
        time!!.text = current_time()

        //	UtilsClass.showToast("Tracking started!",getActivity());
        val intent = Intent(activity, LocationService::class.java)
        intent.action = LocationService.ACTION_START
        requireActivity().startService(intent)
    }

    fun clock_out_fun() {
        session!!.clock_status_check = "false"
        clock_btn!!.alpha = 0f
        clock_btn!!.visibility = View.GONE
        clock_layout!!.visibility = View.VISIBLE
        clock_btn!!.isEnabled = false
        time!!.text = current_time()
        val intent = Intent(activity, LocationService::class.java)
        intent.action = LocationService.ACTION_STOP
        requireActivity().startService(intent)
    }

    fun clock_out_show() {

        //LottieAnimationView statusView = (LottieAnimationView) findViewById(R.id.check_animation_view);
        //anim.setColorFilter(new PorterDuffColorFilter(0XFF00a5ff, PorterDuff.Mode.SRC_ATOP));

        //anim.setColorFilter(0xff00a5ff);
        clock_out_anim!!.visibility = View.VISIBLE
        clock_in_anim!!.visibility = View.GONE
        clock_status!!.text = "Clock out"
        time!!.setTextColor(resources.getColor(R.color.primaryTextColor_orange))
        clock_msg!!.text = "Clocked out at"
        clock_msg2!!.text = "Loaction tracking stopped"
    }

    fun clock_in_show() {
        clock_out_anim!!.visibility = View.GONE
        clock_in_anim!!.visibility = View.VISIBLE
        clock_status!!.text = "Clock in"
        time!!.setTextColor(resources.getColor(R.color.primaryTextColor))
        clock_msg!!.text = "Clocked in at"
        clock_msg2!!.text = "Tracking your loaction"
    }

    fun current_time(): String {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("hh:mm aaa")
        return sdf.format(cal.time)
    }


}