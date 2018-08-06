package com.ismartautocare

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.starmicronics.stario.StarBluetoothManager
import com.starmicronics.stario.StarIOPort
import com.starmicronics.stario.StarIOPortException
import java.util.*
import android.net.ConnectivityManager
import android.widget.ProgressBar
import android.os.CountDownTimer
import android.widget.ImageView
import android.widget.TextView
import java.text.SimpleDateFormat

/**
 * Created by programmer on 10/12/17.
 */
class FragmentSetting : Fragment() {

    lateinit var v: View
    private var portName: String? = null
    private var portSettings: String? = null
    lateinit var view_internet: View
    lateinit var view_printer: View
    lateinit var progress: ProgressDialog
    lateinit var image_internet: ImageView
    lateinit var image_printer: ImageView
    lateinit var refresh_connect: ImageView
    lateinit var tv_print_status: TextView
    lateinit var refresh_date: ImageView
    lateinit var tv_date: TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        v = inflater!!.inflate(R.layout.fragment_setting, container, false)

        view_internet = v.findViewById(R.id.view_internet)
        view_printer = v.findViewById(R.id.view_printer)
        image_internet = v.findViewById(R.id.image_internet)
        image_printer = v.findViewById(R.id.image_printer)
        refresh_connect = v.findViewById(R.id.refresh_connect)
        tv_print_status = v.findViewById(R.id.tv_print_status)
        refresh_date = v.findViewById(R.id.refresh_date)
        tv_date = v.findViewById(R.id.tv_date)

        val pref = activity.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("bluetoothSettingPortName", "BT:Star Micronics")
        editor.putString("bluetoothSettingPortSettings", "portable;escpos")
        editor.putString("bluetoothSettingDeviceType", "PortablePrinter")
        editor.commit()

//        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        this.portName = pref.getString("bluetoothSettingPortName", "BT:Star Micronics")
        this.portSettings = pref.getString("bluetoothSettingPortSettings", "")
        val deviceType = if (pref.getString("bluetoothSettingDeviceType", "DesktopPrinter")!!.toUpperCase(Locale.US) == "DESKTOPPRINTER") StarBluetoothManager.StarDeviceType.StarDeviceTypeDesktopPrinter else StarBluetoothManager.StarDeviceType.StarDeviceTypePortablePrinter
        Log.d("Tag", "portName : " + portName)
        Log.d("Tag", "portSettings : " + portSettings)

        refresh_connect.setOnClickListener {
            progress = ProgressDialog(activity)
            progress.setTitle("Please Wait!!")
            progress.setMessage("Wait!!")
            progress.setCancelable(false)
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progress.show()

            object : CountDownTimer(2000, 1000) {

                override fun onTick(millisUntilFinished: Long) {
//                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000)
                    //here you can have your logic to set text to edittext
                }

                override fun onFinish() {
                    try {
                        if(isNetworkConnected()){
                            view_internet.setBackgroundColor(Color.parseColor("#d9c55d"))
                            image_internet.setImageResource(R.drawable.internet_con_yellow_3x)
                        }else{
                            view_internet.setBackgroundColor(Color.parseColor("#fff"))
                            image_internet.setImageResource(R.drawable.internet_con_3x)
                        }
                        GetStatus()
                    }catch (e : Exception){

                    }
                }

            }.start()

        }

        try {
            if(isNetworkConnected()){
                view_internet.setBackgroundColor(Color.parseColor("#d9c55d"))
                image_internet.setImageResource(R.drawable.internet_con_yellow_3x)
            }else{
                view_internet.setBackgroundColor(Color.parseColor("#fff"))
                image_internet.setImageResource(R.drawable.internet_con_3x)
            }
        }catch (e : Exception){

        }

        tv_date.text = TimeCuurent()

        refresh_date.setOnClickListener {
            tv_date.text = TimeCuurent()
        }

        return v
    }

    fun TimeCuurent() :String{
        val c = Calendar.getInstance()
        val dateformat = SimpleDateFormat("EEE dd MMM yyyy HH:mm:ss")
        val datetime = dateformat.format(c.time)

        return datetime
    }

    private fun isNetworkConnected(): Boolean {
        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return cm.activeNetworkInfo != null
    }

    fun GetStatus() {

        var port: StarIOPort? = null
        try {
            /*
         * using StarIOPort3.1.jar (support USB Port) Android OS Version: upper 2.2
         */
            port = StarIOPort.getPort(portName, portSettings, 10000, context)
            try {
                Thread.sleep(500)
            } catch (e: InterruptedException) {
            }

            val status = port!!.retreiveStatus()

            if (status.offline == false) {
//                val dialog = AlertDialog.Builder(context)
//                dialog.setNegativeButton("Ok", null)
//                val alert = dialog.create()
//                alert.setTitle("Printer")
//                alert.setMessage("Printer is Online")
//                alert.setCancelable(false)
//                alert.show()
                progress.dismiss()
                view_printer.setBackgroundColor(Color.parseColor("#d9c55d"))
                image_printer.setImageResource(R.drawable.printer_con_yellow_3x)
                tv_print_status.text = portName
            } else {
                var message = "Printer is offline"
                if (status.receiptPaperEmpty == true) {
                    message += "\nPaper is Empty"
                }
                if (status.coverOpen == true) {
                    message += "\nCover is Open"
                }
                val dialog = AlertDialog.Builder(context)
                dialog.setNegativeButton("Ok", null)
                val alert = dialog.create()
                alert.setTitle("Printer")
                alert.setMessage(message)
                alert.setCancelable(false)
                alert.show()
                progress.dismiss()
                view_printer.setBackgroundColor(Color.parseColor("#fff"))
                image_printer.setImageResource(R.drawable.printer_con_3x)
                tv_print_status.text = "Failed to open port"
            }
        } catch (e: StarIOPortException) {
            progress.dismiss()
            val dialog = AlertDialog.Builder(context)
            dialog.setNegativeButton("Ok", null)
            val alert = dialog.create()
            alert.setTitle("Failure")
            alert.setMessage("Failed to connect to printer")
            alert.setCancelable(false)
            alert.show()
        } finally {
            if (port != null) {
                try {
                    StarIOPort.releasePort(port)
                } catch (e: StarIOPortException) {
                }

            }
        }

    }
}