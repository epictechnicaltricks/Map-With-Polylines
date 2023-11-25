package com.plcoding.backgroundlocationtracking.LoginSignup

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.plcoding.backgroundlocationtracking.Api.ApiList
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import com.plcoding.backgroundlocationtracking.Api.RequestNetwork
import com.plcoding.backgroundlocationtracking.Api.RequestNetwork.RequestListener
import com.plcoding.backgroundlocationtracking.Api.RequestNetworkController
import com.plcoding.backgroundlocationtracking.ClockInClockOut.ClockInClockOutActivity
import com.plcoding.backgroundlocationtracking.Home.MainActivity
import com.plcoding.backgroundlocationtracking.R
import com.plcoding.backgroundlocationtracking.SessionManager
import com.plcoding.backgroundlocationtracking.UtilsPackage.UtilsClass


class Login : AppCompatActivity() {

    var id : EditText? = null
    var pass : EditText? = null

    var login_api: RequestNetwork? = null
    var login_api_listener: RequestListener? = null

     var prog: ProgressDialog? = null

    var session : SessionManager?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_login)
        id = findViewById(R.id.editTextEmail)
        pass = findViewById(R.id.editTextPassword)
        login_api = RequestNetwork(this)
        prog = ProgressDialog(this)
        prog!!.setMessage("Just a moment..")
        prog!!.setCancelable(false)

        session = SessionManager(this)

        login_api_listener = object : RequestListener {
            @Throws(JSONException::class)
            override fun onResponse(
                tag: String?,
                response: String,
                responseHeaders: HashMap<String?, Any?>?
            ) {
                prog!!.dismiss()
               //UtilsClass.progress(this@Login,false)
                Log.d("api_2023", response)
                try {
                    val gg = JSONObject(response)
                    val array = JSONArray(gg.getString("resultSet"))
                    if (response != "") {

                        val gg2 = JSONObject(array.getString(0))
                        session!!.userID = gg2.getString("user_id")
                        session!!.userName = gg2.getString("firstname")
                        session!!.userType = gg2.getString("usertype")
                        session!!.userPhonenumber = gg2.getString("userphone")
                        session!!.userEmail  = gg2.getString("useremail")
                        session!!.sessionId  = gg2.getString("session_id")
                        session!!.employeetaggedid  = gg2.getString("employee_tagged_id")




                        UtilsClass.showToast(session!!.userID,this@Login)

                        if (response.contains("200")) {
                           /* UtilsClass.showAlert(
                                array.toString(),
                                false,
                                "SUCCESS",
                                this@Login
                            )*/
                            finish()
                            startActivity(Intent(this@Login, MainActivity::class.java))


                        } else {
                            UtilsClass.showAlert(
                                "Please try again later..",
                                false,
                                "Failed to login",
                                this@Login
                            )
                        }
                    } else {
                        UtilsClass.show_no_connection(this@Login)
                    }
                } catch (e: Exception) {
                    UtilsClass.showAlert(
                        "Please try again later.. "+e,
                        false,
                        "Error Failed to login",
                        this@Login
                    )
                }
            }

            override fun onErrorResponse(tag: String, message: String) {
                prog!!.dismiss()
                UtilsClass.show_no_connection(this@Login);

            }
        }

        EnableAutoStart()
    }

    override fun onStart() {


        super.onStart()
    }

    fun login(view : View)
    {
        if(id!!.text.trim().isEmpty() || pass!!.text.trim().isEmpty())
        {
            UtilsClass.showToast("Please enter valid input!",this@Login)
        } else {
            prog!!.show()

            login_api!!.startRequestNetwork(
                RequestNetworkController.POST,
                ApiList().BASE_URL +
                        "method=login" +
                        "&username="+id!!.text.trim()
                        +"&password="+pass!!.text.trim(),
                "",
                login_api_listener
            )
        }
    }



    private fun EnableAutoStart() {
        try {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Important!")
            builder.setMessage("Please allow App to always run in the background, else our services can't be accessed when you are in distress")
            builder.setPositiveButton(
                android.R.string.ok
            ) { dialog, which ->
                for (intent in AUTO_START_INTENTS) {
                    if (packageManager.resolveActivity(
                            intent,
                            PackageManager.MATCH_DEFAULT_ONLY
                        ) != null
                    ) {
                        startActivity(intent)
                        break
                    }
                }
            }
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.show()
        } catch (e: Exception) {
        }
    }


    private val AUTO_START_INTENTS = arrayOf(
        Intent().setComponent(
            ComponentName(
                "com.samsung.android.lool",
                "com.samsung.android.sm.ui.battery.BatteryActivity"
            )
        ),
        Intent("miui.intent.action.OP_AUTO_START").addCategory(Intent.CATEGORY_DEFAULT),
        Intent().setComponent(
            ComponentName(
                "com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.letv.android.letvsafe",
                "com.letv.android.letvsafe.AutobootManageActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.huawei.systemmanager",
                "com.huawei.systemmanager.optimize.process.ProtectActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.coloros.safecenter",
                "com.coloros.safecenter.permission.startup.StartupAppListActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.coloros.safecenter",
                "com.coloros.safecenter.startupapp.StartupAppListActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.oppo.safe",
                "com.oppo.safe.permission.startup.StartupAppListActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.iqoo.secure",
                "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.iqoo.secure",
                "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.vivo.permissionmanager",
                "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
            )
        ),


        // 三星
        Intent().setComponent(ComponentName("com.samsung.android.sm_cn",
            "com.samsung.android.sm.autorun.ui.AutoRunActivity")),
        Intent().setComponent(ComponentName("com.samsung.android.sm_cn",
            "com.samsung.android.sm.ui.ram.AutoRunActivity")),
        Intent().setComponent(ComponentName("com.samsung.android.sm_cn",
            "com.samsung.android.sm.ui.appmanagement.AppManagementActivity")),
        Intent().setComponent(ComponentName("com.samsung.android.sm",
            "com.samsung.android.sm.autorun.ui.AutoRunActivity")),
        Intent().setComponent(ComponentName("com.samsung.android.sm",
            "com.samsung.android.sm.ui.ram.AutoRunActivity")),
        Intent().setComponent(ComponentName("com.samsung.android.sm",
            "com.samsung.android.sm.ui.appmanagement.AppManagementActivity")),
        Intent().setComponent(ComponentName("com.samsung.android.sm_cn",
            "com.samsung.android.sm.ui.cstyleboard.SmartManagerDashBoardActivity")),
        Intent().setComponent(ComponentName("com.samsung.android.sm",
            "com.samsung.android.sm.ui.cstyleboard.SmartManagerDashBoardActivity")),
        Intent().setComponent(ComponentName.unflattenFromString(
            "com.samsung.android.sm_cn/.app.dashboard.SmartManagerDashBoardActivity")),
        Intent().setComponent(ComponentName.unflattenFromString(
            "com.samsung.android.sm/.app.dashboard.SmartManagerDashBoardActivity")),








        Intent().setComponent(
            ComponentName(
                "com.asus.mobilemanager",
                "com.asus.mobilemanager.entry.FunctionActivity"
            )
        ).setData(
            Uri.parse("mobilemanager://function/entry/AutoStart")
        )
    )







}