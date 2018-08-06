package com.ismartautocare

import android.app.Dialog
import android.content.*
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.ismartautocare.service.ApiUtils
import com.ismartautocare.service.SOService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import com.crashlytics.android.Crashlytics
import com.google.android.gms.gcm.GoogleCloudMessaging
import com.google.android.gms.iid.InstanceID
import com.google.gson.Gson
import com.starmicronics.starioextension.ICommandBuilder
import com.starmicronics.starioextension.StarIoExt
import io.fabric.sdk.android.Fabric
import org.json.JSONObject

/**
 * Created by programmer on 10/9/17.
 */
class LoginActivity : AppCompatActivity() {

    lateinit var bt_sign_in : Button
    lateinit var tv_forgot_pass: TextView
    lateinit var sharedPref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var check_remember: String
    private lateinit var refresh_token: String
    lateinit var et_username: EditText
    lateinit var et_password: EditText
    lateinit var et_username_s: String
    lateinit var et_pass_s: String
    lateinit var dialog_load_gif: Dialog
    private var cdt: CountDownTimer? = null
    lateinit var sv_box: ScrollView
    lateinit var bt_register: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_login)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        sharedPref = getSharedPreferences("isac", 0)
        editor = sharedPref.edit()
        check_remember = sharedPref.getString("remember", "")
        refresh_token = sharedPref.getString("refresh_token", "")

        bt_sign_in = findViewById(R.id.bt_sign_in)
        bt_register = findViewById(R.id.bt_register)
        tv_forgot_pass = findViewById(R.id.tv_forgot_pass)

        et_username = findViewById(R.id.et_username)
        et_password = findViewById(R.id.et_password)
        sv_box = findViewById(R.id.sv_box)

        bt_sign_in.setOnClickListener {
            CheckLogin(et_username,et_password)
        }

        tv_forgot_pass.setOnClickListener {
            PopupForgot("")
//            PopupForgotSuccess()
        }

        et_username.setText("bank")
        et_password.setText("bank12345")

//        et_username.setText("tao")
//        et_password.setText("password!9")

        bt_register.setOnClickListener {
            PopupForgot("register")
        }

        sv_box.setOnTouchListener {v: View, m: MotionEvent ->
            // Perform tasks here
            hideSoftKeyboard(v)
            true
        }

        SetLang()

    }

    fun CheckLogin(et_username: EditText, et_pass: EditText) {

        et_username_s = et_username.text.toString()
        et_pass_s = et_pass.text.toString()

        if (et_username_s.isEmpty()) {
            Toast.makeText(this@LoginActivity, R.string.login_please_username, Toast.LENGTH_SHORT).show()
        } else {
            if (et_pass_s.isEmpty()) {
                Toast.makeText(this@LoginActivity, R.string.login_please_pass, Toast.LENGTH_SHORT).show()
            } else {
                callService()
            }
        }
    }

    private fun callService() {
        PopupLodGif()
        val myService: SOService = ApiUtils.getSOService()
        myService.Login("alH8CcJWfindkW2x8","HBPqozvwldB6nHF1fnyxiP2AJlqJGqr4","password",et_username_s,et_pass_s).enqueue(object : Callback<Item> {

            override fun onResponse(call: Call<Item>, response1: Response<Item>) {
                if (response1.isSuccessful) {
                    object : CountDownTimer(3000, 1000) {

                        override fun onTick(millisUntilFinished: Long) {
                            //here you can have your logic to set text to edittext
                        }

                        override fun onFinish() {
                            dialog_load_gif.dismiss()

                            Log.d("Tag", "response access_token : " + response1.body()!!.access_token)
                            Log.d("Tag", "response refresh_token : " + response1.body()!!.refresh_token)

                            val gson = Gson()
                            val json = gson.toJson(response1.body()!!.userProfile)
                            var object_userprofile = JSONObject(json)
                            var object_branch = JSONObject(object_userprofile.getString("branch"))
                            var name = object_branch.getString("name")

                            editor.putString("access_token",response1.body()!!.access_token)
                            editor.putString("refresh_token",response1.body()!!.refresh_token)
                            editor.putString("branch_name",name)
                            editor.commit()

                            val intent = Intent(this@LoginActivity, ServiceActivity::class.java)
                            startActivity(intent)
                            finish()

                            Log.d("Tag", "Response body : " + response1.body())
                        }

                    }.start()

                } else {
                    dialog_load_gif.dismiss()

                    PopupForgot("error")
                }
            }

            override fun onFailure(call: Call<Item>, t: Throwable) {
                dialog_load_gif.dismiss()
                Log.d("Tag", "Failure : " + t.message)
                Log.d("Tag", "Failure call : " + call.clone())
//                Toast.makeText(this@LoginActivity,  R.string.login_meassage_er_user_pass_503, Toast.LENGTH_SHORT).show()
                PopupForgot("error")

            }
        })
    }

    fun PopupLodGif() {

        dialog_load_gif = Dialog(this@LoginActivity)
        dialog_load_gif.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog_load_gif.setContentView(R.layout.custom_loading_image_gif)
        dialog_load_gif.setCancelable(true)
        dialog_load_gif.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val image_gif = dialog_load_gif.findViewById<View>(R.id.image_gif) as ImageView
        val imageViewTarget = GlideDrawableImageViewTarget(image_gif)
        Glide.with(this).load(R.raw.loading).into(imageViewTarget)

        dialog_load_gif.show()

    }

    fun PopupForgot(s: String) {

        val dialog_upload = Dialog(this@LoginActivity)
        dialog_upload.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog_upload.setContentView(R.layout.custom_dialog_forgot_pass)
        dialog_upload.setCancelable(false)
        dialog_upload.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var bt_ok = dialog_upload.findViewById<View>(R.id.bt_ok) as Button
        var tv_topic = dialog_upload.findViewById<View>(R.id.tv_topic) as TextView
        var tv_sub_detail = dialog_upload.findViewById<View>(R.id.tv_sub_detail) as TextView
        if(s.equals("error",true)){
            tv_topic.text = resources.getString( R.string.login_meassage_er_system_error)
            tv_sub_detail.text = resources.getString( R.string.login_meassage_er_system_error_sub)
        }else if(s.equals("register",true)){
            tv_topic.text = resources.getString( R.string.login_meassage_er_register_under)
        }
        bt_ok.setOnClickListener {
            dialog_upload.dismiss()
        }
        dialog_upload.show()

    }

    fun SetLang() {
        val locale: Locale
        val lang = Locale.getDefault().displayLanguage
        if (lang.equals("english", ignoreCase = true)) {
            locale = Locale("en")
            editor.putString("locale", "en")
            editor.commit()
        } else {
            locale = Locale("th")
            editor.putString("locale", "th")
            editor.commit()
        }
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }

    fun hideSoftKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}

