package com.ismartautocare

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by programmer on 10/19/17.
 */
class FragmentHistoryDetail : Fragment() {

    lateinit var v: View
    lateinit var tv_plate_number: TextView
    lateinit var tv_province: TextView
    lateinit var tv_member_name: TextView
    lateinit var tv_member_tel: TextView
    lateinit var tv_member_id: TextView
    lateinit var item: ItemOfAll
    lateinit var plate: String
    lateinit var province: String
    lateinit var by_serves: String
    lateinit var by_release: String
    lateinit var by_check: String
    lateinit var member_name: String
    lateinit var member_tel: String
    lateinit var tv_total: TextView
    lateinit var tv_dis: TextView
    lateinit var tv_final: TextView
    lateinit var image_unpaid: ImageView
    lateinit var image_paid_cash: ImageView
    lateinit var image_paid_card: ImageView
    lateinit var image_member_paid: ImageView
    var paid_by: String = ""
    lateinit var lv_price: ListView
    lateinit var sharedPref: SharedPreferences
    lateinit var access_token: String
    lateinit var status: String
    lateinit var _id: String
    lateinit var tv_by_release: TextView
    lateinit var tv_rno: TextView
    lateinit var tv_dis_detail: TextView
    lateinit var tv_service_start: TextView
    lateinit var tv_service_release: TextView
    lateinit var tv_by_served: TextView
    lateinit var tv_by_check: TextView
    lateinit var date_time_start: String
    lateinit var date_time: String
    lateinit var image_member_car_size: ImageView
    var size: String = ""
    lateinit var image_dis: ImageView
    lateinit var brand: String
    lateinit var image_brand: ImageView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var v = inflater!!.inflate(R.layout.fragment_histiry_detail, container, false) as ViewGroup
        sharedPref = activity.getSharedPreferences("isac", 0)
        access_token = sharedPref.getString("access_token", "")

        Element(v)

        val args = arguments
        item = args.getSerializable("object") as ItemOfAll
//        Log.d("Tag","item : "+item)
        by_serves = ""
        by_check = ""
        by_release = ""
        try {
            val object_plate = JSONObject(item.getPlate().toString())
            val object_served = JSONObject(item.getServed().toString())
//            val object_member = JSONObject(item.getMember().toString())
            val object_released = JSONObject(item.getReleased().toString())
            val object_check = JSONObject(item.getChecked().toString())

            Log.d("Tag","object_plate : "+object_plate)
            plate = object_plate.getString("plate")
            province = object_plate.getString("province")
            by_serves = object_served.getString("by")
            date_time_start = object_served.getString("time")
            by_release = object_released.getString("by")
            date_time = object_released.getString("time")
            by_check = object_check.getString("by")

            val object_payment = JSONObject(item.getPayment().toString())
            val list_paid = arrayOf("cash", "member","card","unpaid")
            for (j in 0 until list_paid.size) {
                if (object_payment.has(list_paid[j]) && !object_payment.isNull(list_paid[j])) {
                    Log.d("Tag","loop : "+ list_paid[j])
                    paid_by = list_paid[j]
                }
            }

//            member_name = object_member.getString("name")
//            member_tel = object_member.getString("contact")
//            paid_by =  object_member.getString("paidby")
//            size = object_member.getString("size")

            _id = item.getId()
            tv_by_served.text = by_serves
            tv_by_check.text = by_check
            tv_by_release.text = by_release

            //Car
            tv_plate_number.text = plate
            tv_province.text = province

//            tv_member_name.text = member_name
//            tv_member_tel.text = member_tel

            //Service
            tv_total.text = item.getPrice()
            tv_dis.text = item.getDiscount()
            if(!item.getDiscount_detail().equals("")){
                tv_dis_detail.text = getString(R.string.new_service_discount)+" ("+item.getDiscount_detail()+")"
                image_dis.setImageResource(R.drawable.discount_yellow)
            }

            tv_final.text = item.getTotal()
            tv_rno.text = "#"+item.getRno()
            tv_service_start.text = convertStringToData(date_time_start)
            tv_service_release.text = convertStringToData(date_time)

            Log.d("Tag","size : "+item.getSize())
            if (size!!.equals("S", ignoreCase = true)) {
                image_member_car_size.setImageResource(R.drawable.car_type_s_1x)
            } else if (size!!.equals("M", ignoreCase = true)) {
                image_member_car_size.setImageResource(R.drawable.car_type_m_1x)
            } else if (size!!.equals("L", ignoreCase = true)) {
                image_member_car_size.setImageResource(R.drawable.car_type_l_1x)
            } else if (size!!.equals("XL", ignoreCase = true)) {
                image_member_car_size.setImageResource(R.drawable.car_type_xl_1x)
            } else if (size!!.equals("XXL", ignoreCase = true)) {
                image_member_car_size.setImageResource(R.drawable.car_type_xxl_1x)
            } else if (size!!.equals("SPORT", ignoreCase = true)) {
                image_member_car_size.setImageResource(R.drawable.car_type_sport_1x)
            } else if (size!!.equals("SUPER", ignoreCase = true)) {
                image_member_car_size.setImageResource(R.drawable.car_type_super_1x)
            }

            if(paid_by.equals("cash",true)){
                image_paid_cash.setImageResource(R.drawable.paid_by_cash_yellow_3x)
            }else if(paid_by.equals("card",true)){
                image_paid_cash.setImageResource(R.drawable.paid_by_card_yellow_3x)
            }else if(paid_by.equals("unpaid",true)){
                image_paid_cash.setImageResource(R.drawable.unpaid_yellow_3x)
            }else if(paid_by.equals("member paid",true)){
                image_paid_cash.setImageResource(R.drawable.member_paid_yellow_3x)
            }

        } catch (e: JSONException) {
//            e.printStackTrace()
        }

        lv_price.setOnTouchListener(View.OnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        })

        setListViewServicesDetail()

        return v
    }

    fun Element(v: ViewGroup) {

        //Car
        tv_plate_number = v.findViewById(R.id.tv_plate_number)
        tv_province = v.findViewById(R.id.tv_province)
        tv_member_name = v.findViewById(R.id.tv_member_name)
        tv_member_tel = v.findViewById(R.id.tv_member_tel)
        image_member_car_size = v.findViewById(R.id.image_member_car_size)
        image_brand = v.findViewById(R.id.image_brand)

        //Service
        tv_total = v.findViewById(R.id.tv_total)
        tv_dis = v.findViewById(R.id.tv_dis)
        tv_final = v.findViewById(R.id.tv_final)
        lv_price = v.findViewById(R.id.lv_price)

        //Paid
        image_unpaid = v.findViewById(R.id.image_unpaid)
        image_paid_cash = v.findViewById(R.id.image_paid_cash)
        image_paid_card = v.findViewById(R.id.image_paid_card)
        image_member_paid = v.findViewById(R.id.image_member_paid)

        //Process
        tv_rno = v.findViewById(R.id.tv_rno)
        tv_by_release = v.findViewById(R.id.tv_by_release)
        tv_dis_detail = v.findViewById(R.id.tv_dis_description)
        tv_service_start = v.findViewById(R.id.tv_service_start)
        tv_service_release = v.findViewById(R.id.tv_service_release)
        tv_by_served = v.findViewById(R.id.tv_by_served)
        tv_by_check = v.findViewById(R.id.tv_by_check)
        image_dis = v.findViewById(R.id.image_dis)

    }

    fun convertStringToData(stringData: String): String {

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

    private fun setListViewServicesDetail() {

        val sb = StringBuilder()
        try {
            val object_plate = JSONObject(item.getPlate().toString())
            val object_released = JSONObject(item.getReleased().toString())
            val object_model = JSONObject(object_plate.getString("model"))

            plate = object_plate.getString("plate")
            province = object_plate.getString("province")
            date_time = object_released.getString("time")
            brand = object_model.getString("brand")

            Glide.with(context)
                    .load(context.getResources().getString(R.string.base_url)+"carLogo/"+brand+".png")
                    .error(R.drawable.bmw)
                    .into(image_brand)

            val gson = Gson()
            val json = JSONArray(gson.toJson(item.getServices()).toString())
            Log.d("Tag", "json : $json")

        } catch (e: JSONException) {
            e.printStackTrace()
        }

//        val array_list = java.util.ArrayList<ItemOfAll>()

        var adapter = AllAdapterListViewHistoryDetail(activity, R.layout.custom_services,item.getServices())
        lv_price.adapter = adapter

    }

}
