package com.ismartautocare

import android.app.Dialog
import android.app.FragmentTransaction
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.bumptech.glide.Glide
import org.json.JSONException
import org.json.JSONObject
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ismartautocare.service.ApiUtils
import com.ismartautocare.service.SOService
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by programmer on 10/10/17.
 */
class FragmentOnServiceDetail : Fragment() {

    lateinit var v: View
    lateinit var tv_plate_number: TextView
    lateinit var tv_province: TextView
    lateinit var tv_member_name: TextView
    lateinit var tv_member_tel: TextView
    lateinit var tv_member_id: TextView
    lateinit var item: ItemOfOnservice
    var plate: String = ""
    var province: String = ""
    lateinit var by: String
    var member_name: String = ""
    var member_tel: String = ""
    lateinit var tv_total: TextView
    lateinit var tv_dis: TextView
    lateinit var tv_final: TextView
    lateinit var image_unpaid: ImageView
    lateinit var image_paid_cash: ImageView
    lateinit var image_paid_card: ImageView
    lateinit var image_member_paid: ImageView
    var paid_by: String = ""
    lateinit var lv_price: ListView
    lateinit var ArratList_listview: ArrayList<Item>
    lateinit var sharedPref: SharedPreferences
    lateinit var access_token: String
    lateinit var status: String
    lateinit var _id: String
    lateinit var image_qc_check: ImageView
    lateinit var tv_by: TextView
    lateinit var tv_dis_description: TextView
    lateinit var object_checked: JSONObject
    var time_seved: String = ""
    lateinit var tv_service_start: TextView
    lateinit var image_brand: ImageView
    var brand: String = ""
    var model: String = ""
    var size: String = ""
    var size_driver: String = ""
    var expired_date: String = ""
    lateinit var tv_model: TextView
    lateinit var image_car_type: ImageView
    lateinit var image_driver_car_type: ImageView
    lateinit var tv_exp_driver: TextView
    lateinit var tv_date_time_release: TextView
    lateinit var tv_date_time_current: TextView
    lateinit var progress_bar_start: RoundCornerProgressBar
    lateinit var progress_bar_finish: RoundCornerProgressBar
    lateinit var image_finish: ImageView
    lateinit var image_check_status: ImageView
    lateinit var tv_rno: TextView
    lateinit var dialog_check_quality: Dialog
    var isAll = false
    lateinit var image_qc_check_popup: ImageView
    lateinit var image_release: ImageView
    lateinit var disable_release: String
    var sms: Boolean = false
    lateinit var image_print_recipt: ImageView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater!!.inflate(R.layout.fragment_on_service_detail, container, false) as ViewGroup

        sharedPref = activity.getSharedPreferences("isac", 0)
        access_token = sharedPref.getString("access_token", "")

        Element(v)

        val args = arguments
        item = args.getSerializable("object") as ItemOfOnservice

        try {
            val gson = Gson()
            val json = gson.toJson(item)
            Log.d("Tag","json : "+ json)
            object_checked = JSONObject(json)
            var job = JSONObject(json)
            val checked_ob = job.getString("checked")
            Log.d("Tag","checked_ob : "+ checked_ob)
            image_qc_check.setImageResource(R.drawable.release_3x)
            status = "released"
        }catch (e: Exception){
            image_qc_check.setImageResource(R.drawable.checked_3x)
            status = "checked"
        }

        try {
            val object_plate = JSONObject(item.getPlate().toString())
            val object_served = JSONObject(item.getServed().toString())
            val object_member = JSONObject(item.getMember().toString())
            val object_payment = JSONObject(item.getPayment().toString())

            plate = object_plate.getString("plate")
            province = object_plate.getString("province")
            member_name = object_plate.getString("driver")
            member_tel = object_plate.getString("contact")

            by = object_served.getString("by")
            time_seved = object_served.getString("time")


            size_driver = CheckNull(object_member,"size")

            expired_date = CheckNull(object_member,"expired_date")

            val object_model = JSONObject(object_plate.getString("model"))
            brand = object_model.getString("brand")
            model = object_model.getString("model")
            size = object_model.getString("size")

            val list_paid = arrayOf("cash", "member","card","unpaid")
            for (j in 0 until list_paid.size) {
                if (object_payment.has(list_paid[j]) && !object_payment.isNull(list_paid[j])) {
                    Log.d("Tag","loop : "+ list_paid[j])
                    paid_by = list_paid[j]
                }
            }

        } catch (e: JSONException) {
//            Log.d("Tag","error : "+  e.printStackTrace())
            Log.d("Tag","error : "+  e.message)
        }

        _id = item.getId()
        tv_by.text = by

        //Car
        tv_plate_number.text = plate
        tv_province.text = province

        tv_member_name.text = member_name
        tv_member_tel.text = member_tel

        //Service
        tv_total.text = item.getPrice().toString()
//        tv_total.text = Integer.parseInt(item.getPrice().toString()).toString()
        tv_dis.text = item.getDiscount()
        tv_final.text = item.getTotal()
        if(!item.getDiscount_detail().equals("",true)){
            tv_dis_description.text = getString(R.string.new_service_discount)+" ("+item.getDiscount_detail()+")"
        }
        tv_rno.text = "#"+item.getRno()
        tv_service_start.text = TimeStartService(time_seved)
        tv_date_time_release.text =  TimeEndService(item.getServices(),time_seved)
        tv_date_time_current.text = TimeCuurent()

        if(paid_by.equals("cash",true)){
            image_paid_cash.setImageResource(R.drawable.paid_by_cash_yellow_3x)
        }else if(paid_by.equals("card",true)){
            image_paid_card.setImageResource(R.drawable.paid_by_card_yellow_3x)
        }else if(paid_by.equals("unpaid",true)){
            image_unpaid.setImageResource(R.drawable.unpaid_yellow_3x)
        }else if(paid_by.equals("member",true)){
            image_member_paid.setImageResource(R.drawable.member_paid_yellow_3x)
        }

        if(!expired_date.equals("",true)){
            tv_exp_driver.text = TimeStartService(expired_date)
        }

        SetCareType(size,image_car_type)
        SetCareType(size_driver,image_driver_car_type)

        Glide.with(context)
                .load(context.getResources().getString(R.string.base_url)+"carLogo/"+brand+".png")
                .error(R.drawable.bmw)
                .into(image_brand)
        tv_model.text = model

        if (object_checked.has("checked") && !object_checked.isNull("checked")) {
            var checked = object_checked.getString("checked")
        } else {
            Log.d("Tag","checked null")
        }

        lv_price.setOnTouchListener(View.OnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        })

        image_qc_check.setOnClickListener {

            if(status.equals("checked",true)){
                PopupCheckQuality(item.getServices(),size,image_qc_check)
            }else{
                PopupCheckRelease()
            }

//            callServiceOnServicesStatus()
//
//            (context as ServiceActivity).setAdapter(1)
//
////            val ft = fragmentManager.beginTransaction()
////            ft.detach(this).attach(this).commit()
//
//            val toFragment = FragmentOnService()
//            val fm = fragmentManager
//            val ft = fm.beginTransaction()
//            ft.replace(R.id.show_fragment, toFragment, "fragment_on_service")
//            ft.addToBackStack(FragmentTransaction.TRANSIT_FRAGMENT_FADE.toString())
//            ft.commit()

        }

        setListViewServicesDetail()

        if ( item.getChecked().toString() != null && !TextUtils.equals( item.getChecked().toString(), "null") && !TextUtils.isEmpty( item.getChecked().toString())) {
            Log.d("Tag", "isEmpty")
            image_finish.setImageResource(R.drawable.finish_1x_yellow)
            progress_bar_start.progress = 1000f
            image_check_status.setImageResource(R.drawable.confirm_2x_re)
            progress_bar_finish.progress = 500f
        } else {
            image_finish.setImageResource(R.drawable.finish_1x)
        }

        image_print_recipt.setOnClickListener {
            RePrintText()
        }

        return v
    }

    fun CheckNull(object_check: JSONObject, s: String): String {
        var string_check = ""
        if (object_check.has(s) && !object_check.isNull(s)) {
            string_check = object_check.getString(s)
        }
        return string_check
    }

    fun SetCareType(size: String, image_type: ImageView) {
        if (size!!.equals("S", ignoreCase = true)) {
            image_type.setImageResource(R.drawable.car_type_s_1x)
        } else if (size!!.equals("M", ignoreCase = true)) {
            image_type.setImageResource(R.drawable.car_type_m_1x)
        } else if (size!!.equals("L", ignoreCase = true)) {
            image_type.setImageResource(R.drawable.car_type_l_1x)
        } else if (size!!.equals("XL", ignoreCase = true)) {
            image_type.setImageResource(R.drawable.car_type_xl_1x)
        } else if (size!!.equals("XXL", ignoreCase = true)) {
            image_type.setImageResource(R.drawable.car_type_xxl_1x)
        } else if (size!!.equals("SPORT", ignoreCase = true)) {
            image_type.setImageResource(R.drawable.car_type_sport_1x)
        } else if (size!!.equals("SUPER", ignoreCase = true)) {
            image_type.setImageResource(R.drawable.car_type_super_1x)
        }
    }

    fun Element(v: ViewGroup) {

        //Car
        tv_plate_number = v.findViewById(R.id.tv_plate_number)
        tv_province = v.findViewById(R.id.tv_province)
        tv_member_name = v.findViewById(R.id.tv_member_name)
        tv_member_tel = v.findViewById(R.id.tv_member_tel)
        image_brand = v.findViewById(R.id.image_brand)
        tv_model = v.findViewById(R.id.tv_model)
        image_car_type = v.findViewById(R.id.image_car_type)
        image_driver_car_type = v.findViewById(R.id.image_driver_car_type)
        tv_exp_driver = v.findViewById(R.id.tv_exp_driver)

        //Service
        tv_total = v.findViewById(R.id.tv_total)
        tv_dis = v.findViewById(R.id.tv_dis)
        tv_final = v.findViewById(R.id.tv_final)
        lv_price = v.findViewById(R.id.lv_price)
        tv_dis_description = v.findViewById(R.id.tv_dis_description)
        tv_service_start = v.findViewById(R.id.tv_service_start)

        //Paid
        image_unpaid = v.findViewById(R.id.image_unpaid)
        image_paid_cash = v.findViewById(R.id.image_paid_cash)
        image_paid_card = v.findViewById(R.id.image_paid_card)
        image_member_paid = v.findViewById(R.id.image_member_paid)

        //Process
        tv_date_time_current = v.findViewById(R.id.tv_date_time_current)
        tv_date_time_release = v.findViewById(R.id.tv_date_time_release)
        image_qc_check = v.findViewById(R.id.image_qc_check)
        tv_by = v.findViewById(R.id.tv_by)
        progress_bar_start = v.findViewById(R.id.progress_bar_start)
        progress_bar_finish = v.findViewById(R.id.progress_bar_finish)
        image_finish = v.findViewById(R.id.image_finish)
        image_check_status = v.findViewById(R.id.image_check_status)
        tv_rno = v.findViewById(R.id.tv_rno)
        image_print_recipt = v.findViewById(R.id.image_print_recipt)
    }

    fun TimeCuurent() :String{
        val c = Calendar.getInstance()
        val dateformat = SimpleDateFormat("EEE dd MMM yyyy HH:mm")
        val datetime = dateformat.format(c.time)
        return datetime
    }

    fun TimeStartService(stringData: String): String {

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")//yyyy-MM-dd'T'HH:mm:ss
        //        val output = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val output = SimpleDateFormat("EEE dd MMM yyyy HH:mm")
        var data: Date? = null
        try {
            data = sdf.parse(stringData)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return output.format(data)
    }

    fun TimeEndService(gettype: ArrayList<ItemServicePrice>,time_seved: String) :String{
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")//yyyy-MM-dd'T'HH:mm:ss
        //        val output = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var data_time: Date? = null
        var datetime = ""
        try {
            data_time = sdf.parse(time_seved)

        val c = Calendar.getInstance()
        val dateformat = SimpleDateFormat("EEE dd MMM yyyy HH:mm")
        var get_period = UpdateSumPeriod(gettype)
        if(get_period>0){
            data_time!!.minutes = data_time!!.minutes+get_period
            c.time = data_time

        }
            datetime = dateformat.format(c.time)

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return datetime
    }

    fun UpdateSumPeriod(gettype: ArrayList<ItemServicePrice>): Int{
        var qtySum = 0
        var qtyNum: Int
        try {
            for (i in 0 until gettype.size) {
                val object_period = JSONObject(gettype[i].getPeriod().toString())
                var period = object_period.getString(size)
                qtyNum = Integer.parseInt(period)
                qtySum += qtyNum
            }
            Log.d("Tag","qtySum : "+qtySum)
        }catch (e : Exception){}

        return qtySum
    }

    private fun setListViewServicesDetail() {

        val gsonBuilder = GsonBuilder().create()
        val jsonFromPojo = gsonBuilder.toJson(item.getServices())
        Log.d("Tag","jsonFromPojo : $jsonFromPojo")
        Log.d("Tag","jsonFromPojo : "+item.getServices())

        ArratList_listview = ArrayList()

        var adapter = AllAdapterListViewOnServiceDetail(activity, R.layout.custom_services,item.getServices(),size)
        lv_price.adapter = adapter

    }

    fun PopupCheckQuality(services: ArrayList<ItemServicePrice>, size: String, image_qc_check: ImageView) {

        dialog_check_quality = Dialog(activity)
        dialog_check_quality.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog_check_quality.setContentView(R.layout.dialog_quality_checked)
        dialog_check_quality.setCancelable(false)
        dialog_check_quality.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var lv_price = dialog_check_quality.findViewById<View>(R.id.lv_price) as ListView
        var rl_close = dialog_check_quality.findViewById<View>(R.id.rl_close) as RelativeLayout
        image_qc_check_popup = dialog_check_quality.findViewById<View>(R.id.image_qc_check_popup) as ImageView
        var image_check_all = dialog_check_quality.findViewById<View>(R.id.image_check_all) as ImageView
        var switch_sms = dialog_check_quality.findViewById<View>(R.id.switch_sms) as Switch

        var adapter = AllAdapterListViewCheckQuality(activity, R.layout.custom_check_quality,item.getServices(),size,"",image_qc_check_popup,image_check_all)
        lv_price.adapter = adapter

        rl_close.setOnClickListener {
            dialog_check_quality.dismiss()
        }

        switch_sms.setOnCheckedChangeListener { buttonView, isChecked ->
            // do something, the isChecked will be
            // true if the switch is in the On position
            Log.d("Tag","isChecked : $isChecked")
            sms = isChecked

            Log.d("Tag","sms : $sms")
        }

        image_qc_check_popup.isClickable = false
        image_qc_check_popup.alpha = 0.5f
        image_check_all.setOnClickListener {
            if (!isAll) {
                isAll = true
                var adapter = AllAdapterListViewCheckQuality(activity, R.layout.custom_check_quality,item.getServices(),size,"All",image_qc_check_popup,image_check_all)
                lv_price.adapter = adapter
                image_check_all.setImageResource(R.drawable.check_all_2x_true)
                image_qc_check_popup.isClickable = true
                image_qc_check_popup.alpha = 1.0f
            } else {
                isAll = false
                var adapter = AllAdapterListViewCheckQuality(activity, R.layout.custom_check_quality,item.getServices(),size,"",image_qc_check_popup,image_check_all)
                lv_price.adapter = adapter
                image_check_all.setImageResource(R.drawable.check_all_2x)
                image_qc_check_popup.isClickable = false
                image_qc_check_popup.alpha = 0.5f
            }
        }

        image_qc_check_popup.setOnClickListener {
            callServiceOnServicesStatus()
        }

        dialog_check_quality.show()
        val window = dialog_check_quality.getWindow()
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

    }

    fun PopupCheckRelease() {

        dialog_check_quality = Dialog(activity)
        dialog_check_quality.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog_check_quality.setContentView(R.layout.dialog_release)
        dialog_check_quality.setCancelable(false)
        dialog_check_quality.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var rl_close = dialog_check_quality.findViewById<View>(R.id.rl_close) as RelativeLayout
        var tv_code = dialog_check_quality.findViewById<View>(R.id.tv_code) as TextView
        var tv_phone = dialog_check_quality.findViewById<View>(R.id.tv_phone) as TextView
        var image_check_return = dialog_check_quality.findViewById<View>(R.id.image_check_return) as ImageView
        var image_check_four_digit = dialog_check_quality.findViewById<View>(R.id.image_check_four_digit) as ImageView
        var image_unpaid = dialog_check_quality.findViewById<View>(R.id.image_unpaid) as ImageView
        var image_paid_card = dialog_check_quality.findViewById<View>(R.id.image_paid_card) as ImageView
        var image_paid_cash = dialog_check_quality.findViewById<View>(R.id.image_paid_cash) as ImageView
        var image_member_paid = dialog_check_quality.findViewById<View>(R.id.image_member_paid) as ImageView
        var image_check_paid = dialog_check_quality.findViewById<View>(R.id.image_check_paid) as ImageView
        image_release = dialog_check_quality.findViewById<View>(R.id.image_release) as ImageView

        tv_code.text = "#"+item.getRno()
        tv_phone.text = member_tel

        image_check_return.visibility = View.GONE
        image_check_four_digit.visibility = View.GONE
        image_check_paid.visibility = View.GONE

        CheckPaid(paid_by,image_paid_cash,image_paid_card,image_unpaid,image_member_paid)

        rl_close.setOnClickListener {
            dialog_check_quality.dismiss()
        }

        image_release.setOnClickListener {
            callServiceOnServicesStatus()
        }

        dialog_check_quality.show()
        val window = dialog_check_quality.getWindow()
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

    }

    fun CheckPaid(paid_by: String, image_paid_cash: ImageView, image_paid_card: ImageView, image_unpaid: ImageView, image_member_paid: ImageView) {
        image_paid_cash.visibility = View.GONE
        image_paid_card.visibility = View.GONE
        image_unpaid.visibility = View.GONE
        image_member_paid.visibility = View.GONE

        if(paid_by.equals("cash",true)){
            image_paid_cash.setImageResource(R.drawable.paid_by_cash_yellow_3x)
            image_paid_cash.visibility = View.VISIBLE
        }else if(paid_by.equals("card",true)){
            image_paid_card.setImageResource(R.drawable.paid_by_card_yellow_3x)
            image_paid_card.visibility = View.VISIBLE
        }else if(paid_by.equals("unpaid",true)){
            image_unpaid.setImageResource(R.drawable.unpaid_yellow_3x)
            image_unpaid.visibility = View.VISIBLE
        }else if(paid_by.equals("member",true)){
            image_member_paid.setImageResource(R.drawable.member_paid_yellow_3x)
            image_member_paid.visibility = View.VISIBLE
        }

    }

    fun CheckDiableCheck(image_qc_check_popup: ImageView){

        image_qc_check_popup.isClickable = true
        image_qc_check_popup.alpha = 1.0f
    }

    fun callServiceOnServicesStatus() {
        Log.d("Tag","status : "+ status)

        val myService: SOService = ApiUtils.getSOService()
        myService.OnservicesStatus("Bearer " + access_token,_id,status,sms).enqueue(object : Callback<SOResponseOnServiceStatus> {

            override fun onResponse(call: Call<SOResponseOnServiceStatus>, response1: Response<SOResponseOnServiceStatus>) {
                if (response1.isSuccessful) {
                    Log.d("Tag", "response result : " + response1.body()!!.result )
                    (context as ServiceActivity).setAdapter(1)

                    dialog_check_quality.dismiss()
                    val toFragment = FragmentOnService()
                    val fm = fragmentManager
                    val ft = fm.beginTransaction()
                    ft.replace(R.id.show_fragment, toFragment, "fragment_on_service")
                    ft.addToBackStack(FragmentTransaction.TRANSIT_FRAGMENT_FADE.toString())
                    ft.commit()
//                    if(status.equals("checked",true)){
//                        // Reload current fragment
//                        var frg: Fragment? = null
//                        frg = activity.supportFragmentManager.findFragmentByTag("fragment_on_service_detail")
//                        val ft = activity.supportFragmentManager.beginTransaction()
//                        ft.detach(frg)
//                        ft.attach(frg)
//                        ft.commit()
//                    }else{
//                        val toFragment = FragmentOnService()
//                        val fm = fragmentManager
//                        val ft = fm.beginTransaction()
//                        ft.replace(R.id.show_fragment, toFragment, "fragment_on_service")
//                        ft.addToBackStack(FragmentTransaction.TRANSIT_FRAGMENT_FADE.toString())
//                        ft.commit()
//                    }
                } else {
                    Log.d("Tag", "Response message : " + response1.message())
                    Log.d("Tag", "Response error : " + response1.code())
                }
            }

            override fun onFailure(call: Call<SOResponseOnServiceStatus>, t: Throwable) {
                Log.d("Tag", "Response call : "+call.request())
                Log.d("Tag", "Response t : "+t.message)
            }
        })

    }

    fun RePrintText() {
        val pref = activity.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("bluetoothSettingPortName", "BT:Star Micronics")
        editor.putString("bluetoothSettingPortSettings", "portable;escpos")
        editor.putString("bluetoothSettingDeviceType", "PortablePrinter")
        editor.putString("bluetoothSettingDeviceType", "PortablePrinter")
        editor.commit()

        var portName = pref.getString("bluetoothSettingPortName", "BT:Star Micronics")
        var portSettings = pref.getString("bluetoothSettingPortSettings", "")

        val source = R.drawable.image_title_print
        val paperWidth = 384 // 2inch (384 dot)

        MiniPrinterFunctions.PrintBitmapThaiReprint(activity, portName, portSettings,tv_final.text.toString(), source,tv_plate_number.text.toString(),tv_province.text.toString(),
                size,tv_model.text.toString(),tv_member_name.text.toString(),tv_member_tel.text.toString(), resources, source, paperWidth,item.getServices(),tv_date_time_release.text.toString(),paid_by, true, true,item.getRno())

    }

}
