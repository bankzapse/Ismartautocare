package com.ismartautocare

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.support.v4.app.Fragment
import android.os.Bundle
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.CountDownTimer
import android.support.annotation.RequiresApi
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import com.google.gson.Gson
import com.ismartautocare.service.ApiUtils
import com.ismartautocare.service.ResponseObject
import com.ismartautocare.service.SOAnswersResponse
import com.ismartautocare.service.SOService
import com.mikhaellopez.circularimageview.CircularImageView
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by programmer on 10/9/17.
 */
class FragmentNewServices : Fragment() {

    lateinit var lv_price: ListView
    lateinit var lv_box1: LinearLayout
    lateinit var lv_box2: LinearLayout
    lateinit var lv_search: ListView
    lateinit var sharedPref: SharedPreferences
    lateinit var access_token: String
    lateinit var itemslist: ArrayList<Item>
    lateinit var itemslist_model: ArrayList<Item>
    lateinit var itemslist_services: ArrayList<Item>
    lateinit var itemslist_member_plate: ArrayList<Item>
    lateinit var itemslist_member: ArrayList<Item>
    lateinit var numberPickerBand: NumberPicker
    lateinit var numberPickerModel: NumberPicker
    lateinit var numberPickerServices: NumberPicker
    lateinit var brand: String
    lateinit var responseList: ArrayList<String>
    lateinit var responseList_model: ArrayList<String>
    lateinit var responseList_model_id: ArrayList<String>
    lateinit var responseList_model_size: ArrayList<String>
    lateinit var responseList_model_services: ArrayList<String>
    lateinit var responseList_model_services_id: ArrayList<String>
    lateinit var responseList_model_services_code: ArrayList<String>
    lateinit var responseList_model_services_price: ArrayList<String>
    lateinit var responseList_model_services_period: ArrayList<String>
    lateinit var ArratList_listview: ArrayList<Item>
    lateinit var local: String
    lateinit var image_car_type: ImageView
    lateinit var tv_band: TextView
    lateinit var tv_model: TextView
    lateinit var image_add_services: ImageView
    lateinit var image_print_recipt: ImageView
    lateinit var adapter: AllAdapterListView
    lateinit var get_size: String
    lateinit var tv_total: TextView
    lateinit var tv_dis: TextView
    lateinit var tv_final: TextView
    lateinit var image_dis: ImageView
    lateinit var set_size: String
    lateinit var set_period: String
    lateinit var loop_price: JSONObject
    lateinit var send_type_dis: String
    lateinit var sc_view: ScrollView
    lateinit var ln_main: LinearLayout
    lateinit var et_plate_number: EditText
    lateinit var et_province_number: AutoCompleteTextView
    lateinit var et_name_driver: EditText
    lateinit var et_phone_driver: EditText
    lateinit var tv_exp_driver: TextView
    lateinit var plate_id: String
    lateinit var discount_detail: String
    lateinit var tv_served_by: TextView
    lateinit var payment_by: String
    lateinit var tv_dis_description: TextView
    lateinit var price: String
    lateinit var member_id: String
    lateinit var image_unpaid: ImageView
    lateinit var image_paid_cash: ImageView
    lateinit var image_paid_card: ImageView
    lateinit var image_member_paid: ImageView
    lateinit var lv_box_plate: LinearLayout
    lateinit var lv_add_info: LinearLayout
    lateinit var dialog_load_gif: Dialog
    lateinit var image_clear_info: ImageView
    lateinit var tv_service_start: TextView
    lateinit var tv_service_end: TextView
    lateinit var progress_bar_service_time: RoundCornerProgressBar
    lateinit var image_member_car_size: ImageView
    lateinit var id_model: String
    lateinit var view_member: View
    lateinit var imageprofile: CircularImageView
    lateinit var brand_search: String
    lateinit var model_search: String
    lateinit var size_search: String
    lateinit var gv_service: GridView
    internal var isAll = false

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val rootView = inflater!!.inflate(R.layout.fragment_new_service, container, false) as ViewGroup

        sharedPref = activity.getSharedPreferences("isac", 0)
        access_token = sharedPref.getString("access_token", "")

        Element(rootView)

        callService()

        ArratList_listview = ArrayList<Item>()

        send_type_dis = "bath"
        image_add_services.setOnClickListener {

            if (get_size!!.equals("", ignoreCase = true)) {
                Toast.makeText(context, "กรุณาเลือก รุ่น รถ", Toast.LENGTH_SHORT).show()
            } else {
                val item = Item()
                if (ArratList_listview.size > 0) {

                    var check = 0
                    for (i in 0 until ArratList_listview.size) {
                        if (responseList_model_services[numberPickerServices.value]!!.equals(ArratList_listview[i].desc, ignoreCase = true)) {
                            Toast.makeText(context, "มีรายการนี้อยู่แล้ว", Toast.LENGTH_SHORT).show()
                            check = 1
                            break
                        } else {
                            check = 0
                        }
                    }

                    LoopCheckNullListView(0)

                    if (check == 0) {
                        item.setDesc(responseList_model_services[numberPickerServices.value])
                        item.set_id(responseList_model_services_id[numberPickerServices.value])
                        item.setCode(responseList_model_services_code[numberPickerServices.value])
                        item.setPrice(set_size)
                        item.setPeriod(set_period)
                        ArratList_listview.add(item)
                        adapter.notifyDataSetChanged()
                    }

                } else {

                    LoopCheckNullListView(0)

                    item.setDesc(responseList_model_services[numberPickerServices.value])
                    item.set_id(responseList_model_services_id[numberPickerServices.value])
                    item.setCode(responseList_model_services_code[numberPickerServices.value])
                    item.setPrice(set_size)
                    item.setPeriod(set_period)
                    ArratList_listview.add(item)
                    setListView()

                }
                UpdateSumPrice(ArratList_listview, tv_total, tv_dis, tv_final, send_type_dis, image_dis,tv_service_end)
                tv_service_end.text = TimeEndService(ArratList_listview)
            }
        }

        gv_service.setOnItemClickListener { parent, view, position, id ->

            if (get_size!!.equals("", ignoreCase = true)) {
                Toast.makeText(context, "กรุณาเลือก รุ่น รถ", Toast.LENGTH_SHORT).show()
            } else {
                val item = Item()
                if (ArratList_listview.size > 0) {

                    var check = 0
                    for (i in 0 until ArratList_listview.size) {
                        if (responseList_model_services[position]!!.equals(ArratList_listview[i].desc, ignoreCase = true)) {
                            Toast.makeText(context, "มีรายการนี้อยู่แล้ว", Toast.LENGTH_SHORT).show()
                            check = 1
                            break
                        } else {
                            check = 0
                        }
                    }

                    LoopCheckNullListView(position)

                    if (check == 0) {
                        item.setDesc(responseList_model_services[position])
                        item.set_id(responseList_model_services_id[position])
                        item.setCode(responseList_model_services_code[position])
                        item.setPrice(set_size)
                        item.setPeriod(set_period)
                        ArratList_listview.add(item)
                        adapter.notifyDataSetChanged()
                    }

                } else {

                    LoopCheckNullListView(position)

                    item.setDesc(responseList_model_services[position])
                    item.set_id(responseList_model_services_id[position])
                    item.setCode(responseList_model_services_code[position])
                    item.setPrice(set_size)
                    item.setPeriod(set_period)
                    ArratList_listview.add(item)
                    setListView()

                }
                UpdateSumPrice(ArratList_listview, tv_total, tv_dis, tv_final, send_type_dis, image_dis,tv_service_end)
                tv_service_end.text = TimeEndService(ArratList_listview)
            }
        }

        tv_dis.text = "0.00"
        image_dis.setOnClickListener {
            PopupDiscount()
        }

        lv_price.setOnTouchListener(OnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        })

        gv_service.setOnTouchListener(OnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        })

        lv_search.setOnTouchListener(OnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        })

        image_print_recipt.setOnClickListener {
            callServiceOnServices()
        }

        et_plate_number.addTextChangedListener(Watcher)
        val addresses = arrayOf("อำนาจเจริญ", "อ่างทอง", "กรุงเทพมหานคร", "บึงกาฬ", "บุรีรัมย์", "ฉะเชิงเทรา", "ชัยนาท", "ชัยภูมิ", "จันทบุรี", "เชียงใหม่", "เชียงราย", "ชลบุรี", "ชุมพร", "กาฬสินธุ์", "กำแพงเพชร", "กาญจนบุรี", "ขอนแก่น", "กระบี่", "ลำปาง", "ลำพูน", "เลย", "ลพบุรี", "แม่ฮ่องสอน", "มหาสารคาม", "มุกดาหาร", "นครนายก", "นครปฐม", "นครพนม", "นครราชสีมา", "นครสวรรค์", "นครศรีธรรมราช", "น่าน", "นราธิวาส", "นราธิวาส", "หนองบัวลำภู", "หนองคาย", "นนทบุรี", "ปทุมธานี", "ปัตตานี", "พังงา", "พัทลุง", "พะเยา", "เพชรบูรณ์", "เพชรบุรี", "พิจิตร", "พิษณุโลก", "พระนครศรีอยุธยา", "แพร่", "ภูเก็ต", "ปราจีนบุรี", "ประจวบคีรีขันธ์", "ระนอง", "ราชบุรี", "ระยอง", "ร้อยเอ็ด", "สระแก้ว", "สกลนคร", "สมุทรปราการ", "สมุทรสาคร", "สมุทรสงคราม", "สระบุรี", "สตูล", "สิงห์บุรี", "ศรีสะเกษ", "สงขลา", "สุโขทัย", "สุพรรณบุรี", "สุราษฎร์ธานี", "สุรินทร์", "ตาก", "ตรัง", "ตราด", "อุบลราชธานี", "อุดรธานี", "อุทัยธานี", "อุตรดิตถ์", "ยะลา", "ยโสธร")
        val adapter = ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, addresses) // This line is OK
        et_province_number.setAdapter(adapter)

        lv_search.visibility = View.GONE

        member_id = ""
        plate_id = ""
        lv_search.setOnItemClickListener { parent, view, position, id ->

            lv_box1.alpha = 1.0f
            lv_box2.visibility = View.VISIBLE
            lv_search.visibility = View.GONE

            var post = parent.getItemAtPosition(position) as Item
            var _id = post.get_id()
            var object_car_model = JSONObject(post.getCar_models().toString())
            id_model = object_car_model.getString("_id")
            brand_search = object_car_model.getString("brand")
            model_search = object_car_model.getString("model")
            size_search  = object_car_model.getString("size")
            Log.d("Tag","getDriver : "+post.getDriver())
            Log.d("Tag", "id_model : $id_model")
            var memberid = post._id
            plate_id = _id
            member_id = memberid
            et_plate_number.removeTextChangedListener(Watcher)
            et_plate_number.setText(post.getPlate())
            et_province_number.setText(post.getProvince())
            et_phone_driver.setText(post.getContact())
            et_name_driver.setText(post.getDriver())
            et_plate_number.addTextChangedListener(Watcher)

            for (i in 0 until itemslist.size) {
                brand = itemslist[i].getBrand()
                if(brand_search.equals(brand,true)){
//                    Log.d("Tag","brand : "+i+" "+brand)
                    numberPickerBand.value = i
                    tv_band.text = brand_search
                    setListModel(i)
                    for (j in 0 until responseList_model.size){
//                        Log.d("Tag","model : "+j+" "+responseList_model[j])
                        if(model_search.equals(responseList_model[j],true)){
                            Log.d("Tag","model : "+j+" "+responseList_model_id[j])
                            numberPickerModel.value = j
                            tv_model.text = model_search
                        }
                    }
                }
            }

            if (size_search.equals("S", ignoreCase = true)) {
                image_car_type.setImageResource(R.drawable.car_type_s_1x)
                get_size = "S"
            } else if (size_search.equals("M", ignoreCase = true)) {
                image_car_type.setImageResource(R.drawable.car_type_m_1x)
                get_size = "M"
            } else if (size_search.equals("L", ignoreCase = true)) {
                image_car_type.setImageResource(R.drawable.car_type_l_1x)
                get_size = "L"
            } else if (size_search.equals("XL", ignoreCase = true)) {
                image_car_type.setImageResource(R.drawable.car_type_xl_1x)
                get_size = "XL"
            } else if (size_search.equals("XXL", ignoreCase = true)) {
                image_car_type.setImageResource(R.drawable.car_type_xxl_1x)
                get_size = "XXL"
            } else if (size_search.equals("SPORT", ignoreCase = true)) {
                image_car_type.setImageResource(R.drawable.car_type_sport_1x)
                get_size = "SPORT"
            } else if (size_search.equals("SUPER", ignoreCase = true)) {
                image_car_type.setImageResource(R.drawable.car_type_super_1x)
                get_size = "SUPER"
            } else {
                image_car_type.setImageResource(R.drawable.car_type_1x)
                get_size = ""
            }

            callMemberDetail(post.get_id())

        }

        image_unpaid.setOnClickListener(clickaction)
        image_paid_cash.setOnClickListener(clickaction)
        image_paid_card.setOnClickListener(clickaction)
        image_member_paid.setOnClickListener(clickaction)
        image_unpaid.callOnClick()
        image_member_paid.isClickable = false
        image_member_paid.alpha = 0.5f

        payment_by = "unpaid"
        discount_detail = ""

        image_clear_info.setOnClickListener {
            ClearData()
        }

        lv_search.visibility = View.GONE
        tv_service_start.text = TimeCuurent()
        tv_service_end.text = TimeEndService(ArratList_listview)

        ln_main.setOnTouchListener {v: View, m: MotionEvent ->
            // Perform tasks here
            hideSoftKeyboard(v)
            true
        }

        return rootView
    }

    @Throws(ParseException::class)
    fun convertStringToData(stringData: String): String {

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")//yyyy-MM-dd'T'HH:mm:ss
//        val output = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val output = SimpleDateFormat("dd MMM YYYY")
        val data = sdf.parse(stringData)
        return output.format(data)
    }

    fun Element(rootView: ViewGroup) {

        //Main
        sc_view = rootView.findViewById(R.id.sc_view)
        ln_main = rootView.findViewById(R.id.ln_main)
        lv_box_plate = rootView.findViewById(R.id.lv_box_plate)
        image_clear_info = rootView.findViewById(R.id.image_clear_info)

        //Search
        et_plate_number = rootView.findViewById(R.id.et_plate_number)
        et_province_number = rootView.findViewById(R.id.et_province_number)
        lv_box1 = rootView.findViewById(R.id.lv_box1)
        lv_box2 = rootView.findViewById(R.id.lv_box2)
        lv_search = rootView.findViewById(R.id.lv_search)

        //Driver
        et_name_driver = rootView.findViewById(R.id.et_name_driver)
        et_phone_driver = rootView.findViewById(R.id.et_phone_driver)
        tv_exp_driver = rootView.findViewById(R.id.tv_exp_driver)

        //Car
        numberPickerBand = rootView.findViewById(R.id.numberPickerBand)
        numberPickerModel = rootView.findViewById(R.id.numberPickerModel)
        numberPickerServices = rootView.findViewById(R.id.numberPickerServices)
        lv_price = rootView.findViewById(R.id.lv_price)
        image_car_type = rootView.findViewById(R.id.image_car_type)
        tv_band = rootView.findViewById(R.id.tv_band)
        tv_model = rootView.findViewById(R.id.tv_model)

        numberPickerBand.wrapSelectorWheel = false
        numberPickerModel.wrapSelectorWheel = false
        numberPickerServices.wrapSelectorWheel = false

        //Servies
        image_add_services = rootView.findViewById(R.id.image_add_services)
        image_print_recipt = rootView.findViewById(R.id.image_print_recipt)
        tv_total = rootView.findViewById(R.id.tv_total)
        tv_dis = rootView.findViewById(R.id.tv_dis)
        tv_final = rootView.findViewById(R.id.tv_final)
        image_dis = rootView.findViewById(R.id.image_dis)
        tv_served_by = rootView.findViewById(R.id.tv_served_by)
        tv_dis_description = rootView.findViewById(R.id.tv_dis_description)
        gv_service = rootView.findViewById(R.id.gv_service)

        //Paid
        image_unpaid = rootView.findViewById(R.id.image_unpaid)
        image_paid_cash = rootView.findViewById(R.id.image_paid_cash)
        image_paid_card = rootView.findViewById(R.id.image_paid_card)
        image_member_paid = rootView.findViewById(R.id.image_member_paid)

        lv_add_info = rootView.findViewById(R.id.lv_add_info)
        tv_service_start = rootView.findViewById(R.id.tv_service_start)
        tv_service_end = rootView.findViewById(R.id.tv_service_end)
        progress_bar_service_time = rootView.findViewById(R.id.progress_bar_service_time)
        image_member_car_size = rootView.findViewById(R.id.image_member_car_size)
        imageprofile = rootView.findViewById(R.id.imageprofile)
        view_member = rootView.findViewById(R.id.view_member)

    }

    private val Watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.length == 0) {
                lv_box1.alpha = 1.0f
                lv_box2.visibility = View.VISIBLE
                lv_search.visibility = View.GONE
            }
            callMemberPlate(s)
        }

        override fun afterTextChanged(s: Editable) {

        }
    }

    var clickaction: View.OnClickListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.image_unpaid -> {
                payment_by = "unpaid"
                image_unpaid.setImageResource(R.drawable.unpaid_yellow_3x)
                image_paid_cash.setImageResource(R.drawable.paid_by_cash_3x)
                image_paid_card.setImageResource(R.drawable.paid_by_card_3x)
                image_member_paid.setImageResource(R.drawable.member_paid_3x)
            }
            R.id.image_paid_cash -> {
                payment_by = "cash"
                image_unpaid.setImageResource(R.drawable.unpaid_3x)
                image_paid_cash.setImageResource(R.drawable.paid_by_cash_yellow_3x)
                image_paid_card.setImageResource(R.drawable.paid_by_card_3x)
                image_member_paid.setImageResource(R.drawable.member_paid_3x)
            }
            R.id.image_paid_card -> {
                payment_by = "card"
                image_unpaid.setImageResource(R.drawable.unpaid_3x)
                image_paid_cash.setImageResource(R.drawable.paid_by_cash_3x)
                image_paid_card.setImageResource(R.drawable.paid_by_card_yellow_3x)
                image_member_paid.setImageResource(R.drawable.member_paid_3x)
            }
            R.id.image_member_paid -> {
                payment_by = "paid"
                image_unpaid.setImageResource(R.drawable.unpaid_3x)
                image_paid_cash.setImageResource(R.drawable.paid_by_cash_3x)
                image_paid_card.setImageResource(R.drawable.paid_by_card_3x)
                image_member_paid.setImageResource(R.drawable.member_paid_yellow_3x)
            }
        }
    }

    fun PopupDiscount() {

        if (ArratList_listview.size > 0) {
            val dialog_discount = Dialog(activity)
            dialog_discount.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog_discount.setContentView(R.layout.custom_dialog_discount)
            dialog_discount.setCancelable(false)
            dialog_discount.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val image_dis_close = dialog_discount.findViewById<View>(R.id.image_dis_close) as ImageView
            val tv_unit = dialog_discount.findViewById<View>(R.id.tv_unit) as TextView
            val bt_percen = dialog_discount.findViewById<View>(R.id.bt_percen) as Button
            val bt_confirm = dialog_discount.findViewById<View>(R.id.bt_confirm) as Button
            val et_dis = dialog_discount.findViewById<View>(R.id.et_dis) as EditText
            val et_description = dialog_discount.findViewById<View>(R.id.et_description) as EditText

            var type = "bath"
            var isAll = false
            bt_percen.setOnClickListener {
                if (!isAll) {
                    isAll = true
                    tv_unit.text = getString(R.string.new_service_bath)
                    bt_percen.text = getString(R.string.new_service_percent)
                    type = "bath"
                } else {
                    isAll = false
                    tv_unit.text = getString(R.string.new_service_percent)
                    bt_percen.text = getString(R.string.new_service_bath)
                    type = "percent"
                }
            }
            bt_confirm.setOnClickListener {
                var int_for_Dis = 0.00
                if (et_dis.text.toString()!!.equals("", true)) {
                    dialog_discount.dismiss()
                } else {
                    image_dis.setImageResource(R.drawable.discount_yellow)
                    if (type!!.equals("bath", true)) {
                        if(et_description.text.toString() != null && !et_description.text.toString().isEmpty()){
                            int_for_Dis = java.lang.Double.parseDouble(et_dis.text.toString())
                            DiscountPrice(int_for_Dis, "bath")
                            FinalPrice(tv_total, tv_dis, tv_final)
                            dialog_discount.dismiss()
                            sc_view.scrollTo(0, sc_view.bottom)
                            discount_detail = et_description.text.toString()
                            tv_dis_description.text = getString(R.string.new_service_discount)+"("+discount_detail +")"
                        }else{
                            Toast.makeText(context, "Please input description", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        int_for_Dis = java.lang.Double.parseDouble(et_dis.text.toString())
                        if (int_for_Dis > 100) {
                            Toast.makeText(context, "จำนวนต้องไม่มากกว่า 100", Toast.LENGTH_SHORT).show()
                        } else {
                            if(et_description.text.toString() != null && !et_description.text.toString().isEmpty()){
                                DiscountPrice(int_for_Dis, "percent")
                                FinalPrice(tv_total, tv_dis, tv_final)
                                dialog_discount.dismiss()
                                sc_view.scrollTo(0, sc_view.bottom)
                                discount_detail = et_description.text.toString()
                                tv_dis_description.text = getString(R.string.new_service_discount)+" ("+discount_detail +")"
                            }
                            else{
                                Toast.makeText(context, "Please input description", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            image_dis_close.setOnClickListener { dialog_discount.dismiss() }

            dialog_discount.show()
        } else {
            Toast.makeText(context, "กรุณาเลือกบริการ", Toast.LENGTH_SHORT).show()
        }

    }

    fun TimeCuurent() :String{
        val c = Calendar.getInstance()
        val dateformat = SimpleDateFormat("EEE dd MMM yyyy HH:mm")
        val datetime = dateformat.format(c.time)
        return datetime
    }

    fun TimeEndService(gettype: ArrayList<Item>) :String{
        val c = Calendar.getInstance()
        val dateformat = SimpleDateFormat("EEE dd MMM yyyy HH:mm")
        var get_period = UpdateSumPeriod(gettype)
        if(get_period>0){
            c.set(Calendar.MINUTE, c.get(Calendar.MINUTE)+get_period)
        }
        val datetime = dateformat.format(c.time)
        return datetime
    }

    fun UpdateSumPeriod(gettype: ArrayList<Item>): Int{
        var qtySum = 0
        var qtyNum: Int
        for (i in 0 until gettype.size) {
            var price = gettype[i].getPeriod().toString()
            qtyNum = Integer.parseInt(price)
            qtySum += qtyNum
        }
        return qtySum
    }

    fun UpdateSumPrice(gettype: ArrayList<Item>, tv_total: TextView, tv_dis: TextView, tv_final: TextView, get_type_dis: String, image_dis: ImageView, tv_service_end: TextView) {
        var qtySum = 0
        var qtyNum: Int

        if (gettype.size > 0) {
            for (i in 0 until gettype.size) {
                var price = gettype[i].getPrice().toString()
                qtyNum = Integer.parseInt(price)
                qtySum += qtyNum
            }
            val df = DecimalFormat("##.00")
            tv_total.text = df.format(qtySum).toString()
            FinalPrice(tv_total, tv_dis, tv_final)
        } else {
            image_dis.setImageResource(R.drawable.discount_black)
            tv_total.text = "0.00"
            tv_dis.text = "0.00"
            tv_final.text = "0.00"
        }
        tv_service_end.text = TimeEndService(gettype)
    }

    fun DiscountPrice(int_for_Dis: Double, s: String) {
        var dis_sum = 0.00
        val df = DecimalFormat("##.00")
        if (s!!.equals("bath", true)) {
            dis_sum = int_for_Dis
        } else if (s!!.equals("percent", true)) {
            var convert_percent = (java.lang.Double.parseDouble(tv_total.text.toString()) / 100.0f) * int_for_Dis
            dis_sum = convert_percent
        }
        if(dis_sum == 0.00){
            tv_dis.text = "0.00"
            image_dis.setImageResource(R.drawable.discount_black)
        }else{
            tv_dis.text = df.format(dis_sum).toString()
        }
    }

    fun FinalPrice(tv_total: TextView, tv_dis: TextView, tv_final: TextView) {

        val df = DecimalFormat("##.00")
        var final_sum = java.lang.Double.parseDouble(tv_total.text.toString()) - java.lang.Double.parseDouble(tv_dis.text.toString())

        if(final_sum == 0.00){
            tv_final.text = "0.00"
        }else{
            tv_final.text = df.format(final_sum).toString()
        }
    }

    private fun setListView() {
        adapter = AllAdapterListView(activity, R.layout.custom_services, ArratList_listview, tv_total, tv_dis, tv_final, send_type_dis, image_dis,tv_service_end)
        lv_price.adapter = adapter
    }

    fun LoopCheckNullListView(position : Int) {
        set_size = ""
        set_period = ""

        loop_price = JSONObject(responseList_model_services_price[position])
        var loop_period = JSONObject(responseList_model_services_period[position])

        if (get_size!!.equals("S", ignoreCase = true)) {
            set_size = loop_price.getString("S")
            set_period = loop_period.getString("S")
        } else if (get_size!!.equals("M", ignoreCase = true)) {
            set_size = loop_price.getString("M")
            set_period = loop_period.getString("M")
        } else if (get_size!!.equals("L", ignoreCase = true)) {
            set_size = loop_price.getString("L")
            set_period = loop_period.getString("L")
        } else if (get_size!!.equals("XL", ignoreCase = true)) {
            set_size = loop_price.getString("XL")
            set_period = loop_period.getString("XL")
        } else if (get_size!!.equals("XXL", ignoreCase = true)) {
            set_size = loop_price.getString("XXL")
            set_period = loop_period.getString("XXL")
        } else if (get_size!!.equals("SPORT", ignoreCase = true)) {
            set_size = loop_price.getString("SPORT")
            set_period = loop_period.getString("SPORT")
        } else if (get_size!!.equals("SUPER", ignoreCase = true)) {
            set_size = loop_price.getString("SUPER")
            set_period = loop_period.getString("SUPER")
        }

    }

    private fun callService() {
        Log.d("Tag", "access_token callService : " + access_token!!)
        val myService: SOService
        myService = ApiUtils.getSOService()
        myService.getCar("application/x-www-form-urlencoded", "Bearer " + access_token).enqueue(object : Callback<SOAnswersResponse> {

            override fun onResponse(call: Call<SOAnswersResponse>, response1: Response<SOAnswersResponse>) {
                if (response1.isSuccessful()) {

                    itemslist = response1.body()!!.result as ArrayList<Item>
                    itemslist_model = response1.body()!!.result as ArrayList<Item>

                    setListBand()

                } else {
                    Log.d("Tag", "Response error Car : " + response1.code())
                    clearActivity()
                }
            }

            override fun onFailure(call: Call<SOAnswersResponse>, t: Throwable) {
            }
        })

        myService.getMember("application/x-www-form-urlencoded", "Bearer " + access_token).enqueue(object : Callback<SOResponseOnService> {

            override fun onResponse(call: Call<SOResponseOnService>, response1: Response<SOResponseOnService>) {
                if (response1.isSuccessful) {

                    itemslist_member = response1.body()!!.result as ArrayList<Item>

                } else {
                    Log.d("Tag", "Response error getMember : " + response1.code())
                }
            }

            override fun onFailure(call: Call<SOResponseOnService>, t: Throwable) {
            }
        })

        myService.getServices("application/x-www-form-urlencoded", "Bearer " + access_token).enqueue(object : Callback<SOAnswersResponse> {

            override fun onResponse(call: Call<SOAnswersResponse>, response1: Response<SOAnswersResponse>) {
                if (response1.isSuccessful()) {

                    itemslist_services = response1.body()!!.result as ArrayList<Item>

                    setListServices()

                } else {
                    clearActivity()
                    Log.d("Tag", "Response error getServices : " + response1.code())
                }
            }

            override fun onFailure(call: Call<SOAnswersResponse>, t: Throwable) {
                Log.d("Tag", "Response call : " + call.request())
                Log.d("Tag", "Response t : " + t.message)
            }
        })
    }

    fun clearActivity() {
        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity.finish()
    }

    fun callServiceOnServices() {

//        PrintText("1959")

        val myService: SOService = ApiUtils.getSOService()
        var services_id_array = ArrayList<String>()
        var price_array = ArrayList<Double>()
        var period_array = ArrayList<Int>()
        Log.d("Tag", "member_id : " + member_id)
        if(et_name_driver.text.toString().equals("",true)){
            Toast.makeText(activity, "Please input name driver", Toast.LENGTH_SHORT).show()
        }else if(et_phone_driver.text.toString().equals("",true)){
            Toast.makeText(activity, "Please input phone driver", Toast.LENGTH_SHORT).show()
        }
        else if(tv_band.text.toString().equals("",true)){
            Toast.makeText(activity, "Please input brand", Toast.LENGTH_SHORT).show()
        }else if (tv_final.text.toString().equals("0.00", true)) {
            Toast.makeText(activity, "Please select service", Toast.LENGTH_SHORT).show()
        }
        else {
//            if (payment_by.equals("", true)) {
//                Toast.makeText(activity, "Please select payment type", Toast.LENGTH_SHORT).show()
//            }
            PopupLodGif()
            for (i in 0 until ArratList_listview.size) {
                Log.d("Tag", "ArratList_listview getPrice : " + ArratList_listview[i].getPrice())
                val d = java.lang.Double.parseDouble(ArratList_listview[i].getPrice())
                val integer = Integer.valueOf(ArratList_listview[i].getPeriod())
                services_id_array.add(ArratList_listview[i].get_id())
                price_array.add(d)
                period_array.add(integer)
            }

            myService.Onservices("Bearer " + access_token,et_plate_number.text.toString(),et_province_number.text.toString(),et_name_driver.text.toString(),
                    et_phone_driver.text.toString(),id_model, plate_id, member_id, tv_total.text.toString(), tv_dis.text.toString(), discount_detail,
                    tv_final.text.toString(), "Bank", payment_by, services_id_array, price_array, period_array).enqueue(object : Callback<ResponseObject> {

                override fun onResponse(call: Call<ResponseObject>, response1: Response<ResponseObject>) {
                    if (response1.isSuccessful) {
                        Log.d("Tag", "response result : " + response1.body()!!.result)
                        var object_rno = JSONObject(response1.body()!!.result.toString())
                        var string_rno = object_rno.getString("rno")
                        object : CountDownTimer(2000, 1000) {

                            override fun onTick(millisUntilFinished: Long) {
                                //here you can have your logic to set text to edittext
                            }

                            override fun onFinish() {
                                dialog_load_gif.dismiss()
                                PopupSuccess(string_rno)
                            }

                        }.start()
                    } else {
                        dialog_load_gif.dismiss()
                        Log.d("Tag", "Response message : " + response1.message())
                        Log.d("Tag", "Response error : " + response1.code())
                        Log.d("Tag", "call request url : " + call.request().url())
                        Log.d("Tag", "call request newBuilder : " + call.request().newBuilder())
                        Log.d("Tag", "call request tag : " + call.request().tag())
                    }
                }

                override fun onFailure(call: Call<ResponseObject>, t: Throwable) {
                    dialog_load_gif.dismiss()
                    Log.d("Tag", "Response call : " + call.request())
                    Log.d("Tag", "Response t : " + t.message)
                }
            })

        }
    }

    fun PopupLodGif() {

        dialog_load_gif = Dialog(activity)
        dialog_load_gif.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog_load_gif.setContentView(R.layout.custom_loading_image_gif)
        dialog_load_gif.setCancelable(true)
        dialog_load_gif.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val image_gif = dialog_load_gif.findViewById<View>(R.id.image_gif) as ImageView
        val imageViewTarget = GlideDrawableImageViewTarget(image_gif)
        Glide.with(this).load(R.raw.loading).into(imageViewTarget)

        dialog_load_gif.show()

    }

    fun PopupSuccess(string_rno : String) {
        val dialog_upload_success = Dialog(activity)
        dialog_upload_success.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog_upload_success.setContentView(R.layout.custom_dialog_success)
        dialog_upload_success.setCancelable(false)
        dialog_upload_success.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val lv_box_success = dialog_upload_success.findViewById<View>(R.id.lv_box_success) as LinearLayout
        lv_box_success.alpha = 0.9f

        val image_gif = dialog_upload_success.findViewById<View>(R.id.image_gif) as ImageView
        val imageViewTarget = GlideDrawableImageViewTarget(image_gif)
        Glide.with(this).load(R.raw.success).into(imageViewTarget)

        object : CountDownTimer(2000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
                dialog_upload_success.dismiss()
                PrintText(string_rno)
            }

        }.start()

        dialog_upload_success.show()

    }

    fun PrintText(string_rno : String) {
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

        MiniPrinterFunctions.PrintBitmapThai(activity, portName, portSettings,tv_final.text.toString(), source,et_plate_number.text.toString(),et_province_number.text.toString(),
                get_size,tv_model.text.toString(),et_name_driver.text.toString(),et_phone_driver.text.toString(), resources, source, paperWidth,ArratList_listview,tv_service_end.text.toString(),payment_by, true, true,string_rno)
        ClearData()
    }

    fun ClearData(){

        et_name_driver.text.clear()
        et_phone_driver.text.clear()
        et_plate_number.text.clear()
        et_province_number.text.clear()

        val ft = fragmentManager.beginTransaction()
        ft.detach(this).attach(this).commit()
    }

    private fun callMemberPlate(s: CharSequence) {
//        Log.d("Tag", "access_token : " + access_token!!)
        val myService: SOService
        myService = ApiUtils.getSOService()
        myService.getMemberPlateSearch("application/x-www-form-urlencoded", "Bearer " + access_token, s.toString()).enqueue(object : Callback<SOAnswersResponse> {

            override fun onResponse(call: Call<SOAnswersResponse>, response1: Response<SOAnswersResponse>) {
                if (response1.isSuccessful()) {
                    itemslist_member_plate = response1.body()!!.result as ArrayList<Item>
                    Log.d("Tag", "itemslist_member_plate size : " +itemslist_member_plate.size)

                    if(s.length>0){
                        if (itemslist_member_plate.size > 0) {
                            var adapter = AdapterListViewMemberPlate(activity, R.layout.custom_listview_member_plate, itemslist_member_plate)
                            lv_search.adapter = adapter

                            lv_box1.alpha = 0.5f
                            lv_box2.visibility = View.INVISIBLE
                            lv_search.visibility = View.VISIBLE

                        }else{
                            lv_box1.alpha = 1.0f
                            lv_box2.visibility = View.VISIBLE
                            lv_search.visibility = View.GONE
                        }
                    }else{
                        lv_box1.alpha = 1.0f
                        lv_box2.visibility = View.VISIBLE
                        lv_search.visibility = View.GONE
                    }
                } else {
                    Log.d("Tag", "Response message : " + response1.message())
                    Log.d("Tag", "Response error getMemberPlateSearch : " + response1.code() )
                }
            }

            override fun onFailure(call: Call<SOAnswersResponse>, t: Throwable) {
            }
        })
    }

    private fun callMemberDetail(id: CharSequence) {
        val myService: SOService
        myService = ApiUtils.getSOService()
        myService.getMemberDeatil("application/x-www-form-urlencoded", "Bearer " + access_token, "1d140a06d126321d42835e4c").enqueue(object : Callback<ResponseObject> {

            override fun onResponse(call: Call<ResponseObject>, response1: Response<ResponseObject>) {
                if (response1.isSuccessful()) {
                    try {
                        val gson = Gson()
                        val json = gson.toJson(response1.body()!!.result)
//                        Log.d("Tag","json : "+ json)
                        var object_member_detail = JSONObject(json)
                        Log.d("Tag","object_member_detail : "+ object_member_detail)

                        if (object_member_detail.getString("expired_date") != null && !object_member_detail.getString("expired_date").isEmpty()) {
                            var exp_date = convertStringToData(object_member_detail.getString("expired_date"))
                            var sub = resources.getString(R.string.new_service_exp_date)
                            tv_exp_driver.text = (sub + " " + exp_date)
                        }
                        image_member_paid.isClickable = true
                        image_member_paid.alpha = 1.0f
                        imageprofile.setBorderColor(R.color.white)
                        view_member.visibility = View.GONE
                        var size = object_member_detail.getString("size")
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

                    }catch (e: Exception){
                        Log.d("Tag", "e : " + e.message)
                        image_member_paid.isClickable = false
                        image_member_paid.alpha = 0.5f
                        imageprofile.setBorderColor(Color.parseColor("#d11111"))
                        view_member.visibility = View.VISIBLE
                    }

                } else {
                    Log.d("Tag", "Response message : " + response1.message())
                    Log.d("Tag", "Response error getMemberDeatil : " + response1.code())
                }
            }

            override fun onFailure(call: Call<ResponseObject>, t: Throwable) {
                Log.d("Tag", "call : " + call)
            }
        })
    }

    private fun setListBand() {

        responseList = ArrayList<String>()
        local = sharedPref.getString("locale", "") as String
        for (i in 0 until itemslist.size) {
            brand = itemslist[i].getBrand()
            responseList.add(brand)
        }

        val array = arrayOfNulls<String>(responseList.size)
        for (j in 0 until responseList.size) {
            array[j] = responseList[j]
        }

        numberPickerBand.minValue = 0
        numberPickerBand.maxValue = responseList.size - 1
        numberPickerBand.displayedValues = array
        numberPickerBand.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        tv_band.text = "Select brand"

        setDividerColor(numberPickerBand, Color.WHITE)
        setNumberPickerTextColor(numberPickerBand, Color.WHITE)
        val myValChangedListener = NumberPicker.OnValueChangeListener { picker, oldVal, newVal ->
            setListModel(picker.value)

            if (picker.value > 0) {
                tv_band.text = responseList[picker.value]
            } else {
                tv_band.text = "Select brand"
            }
            ClearServices()
        }

        setListModel(0)

        numberPickerBand.setOnValueChangedListener(myValChangedListener)
    }

    private fun setListModel(value: Int) {

        responseList_model = ArrayList<String>()
        responseList_model_id = ArrayList<String>()
        responseList_model_size = ArrayList<String>()

        image_car_type.setImageResource(R.drawable.car_type_1x)
        responseList_model.add("Select model")
        get_size = ""
        responseList_model_size.add("")
        numberPickerModel.value = 0
        tv_model.text = "Select model"

        val jsonArray_model = JSONArray(itemslist_model[value].getModels().toString())
        for (j in 0 until jsonArray_model.length()) {
            val json_data = jsonArray_model.getJSONObject(j)
            val id = json_data.getString("_id")
            val model = json_data.getString("name")
            val size = json_data.getString("size")
            responseList_model_id.add(id)
            responseList_model.add(model)
            responseList_model_size.add(size)
        }

        val array = arrayOfNulls<String>(responseList_model.size)

        for (j in 0 until responseList_model.size) {
            array[j] = responseList_model[j]
        }

        if(numberPickerBand.value > 0){
            numberPickerModel.isEnabled = true
            numberPickerModel.alpha = 1.0f
        }else{
            numberPickerModel.isEnabled = false
            numberPickerModel.alpha = 0.4f
        }
        try {
            numberPickerModel.minValue = 0
            numberPickerModel.maxValue = responseList_model.size - 1
            numberPickerModel.displayedValues = array
            numberPickerModel.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            setDividerColor(numberPickerModel, Color.WHITE)
            setNumberPickerTextColor(numberPickerModel, Color.WHITE)
            val myValChangedListener = NumberPicker.OnValueChangeListener { picker, oldVal, newVal ->
                //                Log.d("Tag","responseList_model size : "+ responseList_model_size[picker.value])
                if (responseList_model_size[picker.value].equals("S", ignoreCase = true)) {
                    image_car_type.setImageResource(R.drawable.car_type_s_1x)
                    get_size = "S"
                } else if (responseList_model_size[picker.value].equals("M", ignoreCase = true)) {
                    image_car_type.setImageResource(R.drawable.car_type_m_1x)
                    get_size = "M"
                } else if (responseList_model_size[picker.value].equals("L", ignoreCase = true)) {
                    image_car_type.setImageResource(R.drawable.car_type_l_1x)
                    get_size = "L"
                } else if (responseList_model_size[picker.value].equals("XL", ignoreCase = true)) {
                    image_car_type.setImageResource(R.drawable.car_type_xl_1x)
                    get_size = "XL"
                } else if (responseList_model_size[picker.value].equals("XXL", ignoreCase = true)) {
                    image_car_type.setImageResource(R.drawable.car_type_xxl_1x)
                    get_size = "XXL"
                } else if (responseList_model_size[picker.value].equals("SPORT", ignoreCase = true)) {
                    image_car_type.setImageResource(R.drawable.car_type_sport_1x)
                    get_size = "SPORT"
                } else if (responseList_model_size[picker.value].equals("SUPER", ignoreCase = true)) {
                    image_car_type.setImageResource(R.drawable.car_type_super_1x)
                    get_size = "SUPER"
                } else {
                    image_car_type.setImageResource(R.drawable.car_type_1x)
                    get_size = ""
                }

                if (picker.value > 0) {
                    tv_model.text = responseList_model[picker.value]
                    id_model = responseList_model_id[picker.value-1]
                } else {
                    tv_model.text = "Select model"
                }

                ClearServices()
            }
            numberPickerModel.setOnValueChangedListener(myValChangedListener)
        } catch (e: Exception) {
            Log.d("Tag", "Exception : " + e.message)
        }

    }

    fun ClearServices() {
        if (ArratList_listview.size > 0) {
            ArratList_listview.clear()
            adapter.notifyDataSetChanged()
            numberPickerServices.value = 0
            tv_total.text = "0"
            tv_dis.text = "0.00"
            tv_final.text = "0.00"
            image_dis.setImageResource(R.drawable.discount_black)

        }
    }

    private fun setListServices() {
//        item_services = Item()
        Log.d("Tag", "setListServices")
        try {
            responseList_model_services = ArrayList<String>()
            responseList_model_services_id = ArrayList<String>()
            responseList_model_services_code = ArrayList<String>()
            responseList_model_services_price = ArrayList<String>()
            responseList_model_services_period = ArrayList<String>()
            local = sharedPref.getString("locale", "") as String
            for (i in 0 until itemslist_services.size) {
                brand = itemslist_services[i].getDesc()
//            Log.d("Tag","brand : "+brand)
                var id_services = itemslist_services[i].get_id()
                var code = itemslist_services[i].getCode()
                responseList_model_services.add(brand)
                responseList_model_services_id.add(id_services)
                responseList_model_services_code.add(code)

                var price_ob = itemslist_services[i].getPrice_list()
//                Log.d("Tag","price_ob : "+price_ob)
                if (price_ob != null && price_ob.toString() != null) {
                    responseList_model_services_price.add(price_ob.toString())
                }

                var period_ob = itemslist_services[i].getPeriod_list()
//                Log.d("Tag","price_ob : "+price_ob)
                if (period_ob != null && period_ob.toString() != null) {
                    responseList_model_services_period.add(period_ob.toString())
                }

            }

            val array = arrayOfNulls<String>(responseList_model_services.size)
            for (j in 0 until responseList_model_services.size) {
                array[j] = responseList_model_services[j]
            }

            numberPickerServices.minValue = 0
            numberPickerServices.maxValue = responseList_model_services.size - 1
            numberPickerServices.displayedValues = array
            numberPickerServices.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            setDividerColor(numberPickerServices, Color.WHITE)
            setNumberPickerTextColor(numberPickerServices, Color.WHITE)
            val myValChangedListener = NumberPicker.OnValueChangeListener { picker, oldVal, newVal ->
            }

            numberPickerServices.setOnValueChangedListener(myValChangedListener)
            var adapter_gv = AllAdapterGridView(context,R.layout.custom_gv_service,responseList_model_services,responseList_model_services_code)
            gv_service.adapter = adapter_gv

        } catch (e: Exception) {
            Log.d("Tag", "error : " + e.message)
        }

    }

    private fun setDividerColor(picker: NumberPicker, color: Int) {

        val pickerFields = NumberPicker::class.java.declaredFields

        for (pf in pickerFields) {
            if (pf.name == "mSelectionDivider") {
                pf.isAccessible = true
                try {
                    val colorDrawable = ColorDrawable(color)
                    pf.set(picker, colorDrawable)
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                } catch (e: Resources.NotFoundException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }

                break
            }
        }
    }

    @SuppressLint("LongLogTag")
    fun setNumberPickerTextColor(numberPicker: NumberPicker, color: Int): Boolean {
        val count = numberPicker.childCount
        for (i in 0 until count) {
            val child = numberPicker.getChildAt(i)
            if (child is EditText) {
                try {
                    val selectorWheelPaintField = numberPicker.javaClass.getDeclaredField("mSelectorWheelPaint")
                    selectorWheelPaintField.isAccessible = true
                    (selectorWheelPaintField.get(numberPicker) as Paint).color = color
                    child.setTextColor(color)
                    numberPicker.invalidate()
                    return true
                } catch (e: NoSuchFieldException) {
                    Log.w("setNumberPickerTextColor", e)
                } catch (e: IllegalAccessException) {
                    Log.w("setNumberPickerTextColor", e)
                } catch (e: IllegalArgumentException) {
                    Log.w("setNumberPickerTextColor", e)
                }

            }
        }
        return false
    }

    fun hideSoftKeyboard(view: View) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
