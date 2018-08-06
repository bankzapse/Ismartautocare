package com.ismartautocare

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.*
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import com.roughike.bottombar.BottomBar
import com.ismartautocare.service.ApiUtils
import com.ismartautocare.service.SOService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import com.google.gson.Gson
import com.ismartautocare.service.ResponseObject
import com.llollox.androidtoggleswitch.widgets.ToggleSwitch
import org.json.JSONObject
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

/**
 * Created by programmer on 10/9/17.
 */
class ServiceActivity : FragmentActivity() {

    var mSelectedItem: Int = 0
    var mPager: CustomViewPagerTest? = null
    private lateinit var mAdapter: PageAdapter
    lateinit var bottomBar: BottomBar
    private var TRANSFORM_CLASSES: ArrayList<TransformerItem>? = null
    lateinit var tv_title: TextView
    lateinit var titile_list:ArrayList<String>
    lateinit var lv_back:LinearLayout
    lateinit var sharedPref: SharedPreferences
    lateinit var access_token: String
    private lateinit var itemslist: ArrayList<ItemMemberType>
    lateinit var image_add_member: ImageView
    lateinit var image_logout: ImageView
    lateinit var numberPickerType: NumberPicker
    lateinit var responseList: ArrayList<String>
    lateinit var responseList_id: ArrayList<String>
    lateinit var type: String
    lateinit var type_id: String
    var get_id_type: String = ""
    lateinit var branch_name: String
    lateinit var dialog_load_gif: Dialog
    lateinit var dialog_member :Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        sharedPref = getSharedPreferences("isac", 0)
        access_token = sharedPref.getString("access_token", "")
        branch_name = sharedPref.getString("branch_name", "")

        tv_title = findViewById(R.id.tv_title)
        lv_back = findViewById(R.id.lv_back)
        image_add_member = findViewById(R.id.image_add_member)
        image_logout = findViewById(R.id.image_logout)
        lv_back.visibility = View.GONE
        image_logout.visibility = View.GONE
        image_add_member.visibility = View.GONE

//        TRANSFORM_CLASSES = ArrayList()
//        TRANSFORM_CLASSES!!.add(TransformerItem(AccordionTransformer::class.java))

        mAdapter = PageAdapter(supportFragmentManager)

        mPager = findViewById<View>(R.id.pager) as CustomViewPagerTest
        mPager!!.setAdapter(mAdapter)
//        mPager!!.setPageTransformer(true, TRANSFORM_CLASSES!!.get(0).clazz.newInstance())
        mPager!!.setCurrentItem(0)
        mPager!!.offscreenPageLimit = 0

        titile_list = ArrayList<String>()
        titile_list.add(getString(R.string.new_service_new_service))
        titile_list.add(getString(R.string.on_service_service))
        titile_list.add(getString(R.string.history_history))
        titile_list.add(getString(R.string.member_member))
        titile_list.add(getString(R.string.setting_setting))

        bottomBar = findViewById<View>(R.id.bottomBar) as BottomBar
        bottomBar.setOnTabSelectListener { tabId ->
            if (tabId == R.id.new_service) {
                // The tab with id R.id.tab_favorites was selected,
                // change your content accordingly.
                mPager!!.setCurrentItem(0)
                tv_title.text = titile_list[0]
                lv_back.visibility = View.GONE
                image_add_member.visibility = View.GONE
                image_logout.visibility = View.GONE

            }else if(tabId == R.id.on_service){
                mPager!!.setCurrentItem(1)
                tv_title.text = titile_list[1]
                lv_back.visibility = View.GONE
                image_add_member.visibility = View.GONE
                image_logout.visibility = View.GONE
                onBackPressed()

            }else if(tabId == R.id.history){
                mPager!!.setCurrentItem(2)
                tv_title.text = titile_list[2]
                lv_back.visibility = View.GONE
                image_add_member.visibility = View.GONE
                image_logout.visibility = View.GONE
            }else if(tabId == R.id.member){
                mPager!!.setCurrentItem(3)
                tv_title.text = titile_list[3]
                lv_back.visibility = View.GONE
                image_add_member.visibility = View.VISIBLE
                image_logout.visibility = View.GONE
//                PopupUnderDevelop()
            }else if(tabId == R.id.setting){
                mPager!!.setCurrentItem(4)
                tv_title.text = titile_list[4]
                lv_back.visibility = View.GONE
                image_add_member.visibility = View.GONE
                image_logout.visibility = View.VISIBLE
            }
        }

        lv_back.setOnClickListener {
            onBackPressed()
        }

        image_logout.setOnClickListener {
            PopupLogout()
        }

        image_add_member.setOnClickListener {
            PopupAddMember()
        }

    }

    fun PopupAddMember() {

        dialog_member = Dialog(this@ServiceActivity)
        dialog_member.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog_member.setContentView(R.layout.dialog_add_member)
        dialog_member.setCancelable(false)
        dialog_member.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var rl_close = dialog_member.findViewById<View>(R.id.rl_close) as RelativeLayout

        var et_name_driver = dialog_member.findViewById<View>(R.id.et_name_driver) as EditText
        var et_brd_driver = dialog_member.findViewById<View>(R.id.et_brd_driver) as EditText
        var et_phone_driver = dialog_member.findViewById<View>(R.id.et_phone_driver) as EditText
        var et_email_driver = dialog_member.findViewById<View>(R.id.et_email_driver) as EditText
        var et_address_driver = dialog_member.findViewById<View>(R.id.et_address_driver) as EditText
        var et_id_member_driver = dialog_member.findViewById<View>(R.id.et_id_member_driver) as EditText

        var image_member_car_s = dialog_member.findViewById<View>(R.id.image_member_car_s) as ImageView
        var image_member_car_m = dialog_member.findViewById<View>(R.id.image_member_car_m) as ImageView
        var image_member_car_l = dialog_member.findViewById<View>(R.id.image_member_car_l) as ImageView
        var image_member_car_xl = dialog_member.findViewById<View>(R.id.image_member_car_xl) as ImageView
        var image_member_car_xxl = dialog_member.findViewById<View>(R.id.image_member_car_xxl) as ImageView
        var image_member_car_sport = dialog_member.findViewById<View>(R.id.image_member_car_sport) as ImageView
        var image_member_car_super = dialog_member.findViewById<View>(R.id.image_member_car_super) as ImageView
        numberPickerType = dialog_member.findViewById<View>(R.id.numberPickerType) as NumberPicker

        var tv_date = dialog_member.findViewById<View>(R.id.tv_date) as TextView
        var tv_issuedfrom = dialog_member.findViewById<View>(R.id.tv_issuedfrom) as TextView
        var sw_sms = dialog_member.findViewById<View>(R.id.sw_sms) as Switch

        var bt_confirm = dialog_member.findViewById<View>(R.id.bt_confirm) as Button

        tv_date.text = TimeCuurent()
        tv_issuedfrom.text = branch_name

        numberPickerType.wrapSelectorWheel = false
        callServiceType()

        var Checked: Boolean = false
        sw_sms!!.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d("Tag","isChecked : "+isChecked)
            Checked = isChecked
        }

        var check_paid_type: String
        val toggleSwitch = dialog_member.findViewById<View>(R.id.multiple_toggle_switch) as ToggleSwitch
        toggleSwitch.setCheckedPosition(0)
        check_paid_type = "cash"
        toggleSwitch!!.onChangeListener = object : ToggleSwitch.OnChangeListener {
            override fun onToggleSwitchChanged(i: Int) {
                if(i == 0){
                    check_paid_type = "cash"
                }else{
                    check_paid_type = "credit card"
                }
            }
        }

        var list_image = arrayOf(image_member_car_s,image_member_car_m,image_member_car_l,image_member_car_xl,image_member_car_xxl,image_member_car_sport,image_member_car_super)
        var size: String = ""
        image_member_car_s.setOnClickListener {
            CheckCarSize(list_image,0)
            size = "S"
        }
        image_member_car_m.setOnClickListener {
            CheckCarSize(list_image,1)
            size = "M"
        }
        image_member_car_l.setOnClickListener {
            CheckCarSize(list_image,2)
            size = "L"
        }
        image_member_car_xl.setOnClickListener {
            CheckCarSize(list_image,3)
            size = "XL"
        }
        image_member_car_xxl.setOnClickListener {
            CheckCarSize(list_image,4)
            size = "XXL"
        }
        image_member_car_sport.setOnClickListener {
            CheckCarSize(list_image,5)
            size = "SPORT"
        }
        image_member_car_super.setOnClickListener {
            CheckCarSize(list_image,6)
            size = "SUPER"
        }

        rl_close.setOnClickListener {
            dialog_member.dismiss()
        }
        bt_confirm.setOnClickListener {
            callServiceCreateMembers(et_id_member_driver,et_name_driver,et_phone_driver,et_email_driver,et_brd_driver,et_address_driver,get_id_type,size,check_paid_type,Checked)
        }

        dialog_member.show()
        val window = dialog_member.getWindow()
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
    }

    fun TimeCuurent() :String{
        val c = Calendar.getInstance()
        val dateformat = SimpleDateFormat("EEE dd MMM yyyy HH:mm")
        val datetime = dateformat.format(c.time)
        return datetime
    }


    private fun callServiceType() {
        Log.d("Tag", "access_token : " + access_token!!)
        val myService: SOService
        myService = ApiUtils.getSOService()
        myService.getMemberType("application/x-www-form-urlencoded", "Bearer " + access_token).enqueue(object : Callback<SOMemberType> {

            override fun onResponse(call: Call<SOMemberType>, response1: Response<SOMemberType>) {
                if (response1.isSuccessful()) {
                    itemslist = response1.body()!!.result as ArrayList<ItemMemberType>
                    setListType()
                } else {
                    Log.d("Tag", "Response error : " + response1.code())
                }
            }

            override fun onFailure(call: Call<SOMemberType>, t: Throwable) {
                Log.d("Tag", "call : " + call)
            }
        })
    }

    private fun callServiceCreateMembers(et_id_member_driver: EditText, et_name_driver: EditText, et_phone_driver: EditText, et_email_driver: EditText, et_brd_driver: EditText,
                                         et_address_driver: EditText, get_id_type: String, size: String, check_paid_type: String, checked: Boolean) {

        if(et_id_member_driver.text.toString().isEmpty()){
            Toast.makeText(this@ServiceActivity,  "Please input code member", Toast.LENGTH_SHORT).show()
        }else if(et_name_driver.text.toString().isEmpty()){
            Toast.makeText(this@ServiceActivity,  "Please input name member", Toast.LENGTH_SHORT).show()
        }else if(et_phone_driver.text.toString().isEmpty()){
            Toast.makeText(this@ServiceActivity,  "Please input phone number", Toast.LENGTH_SHORT).show()
        }else if(!isEmailValid(et_email_driver.text.toString())){
            Toast.makeText(this@ServiceActivity,  "Please input format email", Toast.LENGTH_SHORT).show()
        }else if(et_brd_driver.text.toString().isEmpty()){
            Toast.makeText(this@ServiceActivity,  "Please input birth date", Toast.LENGTH_SHORT).show()
        }else if(et_address_driver.text.toString().isEmpty()){
            Toast.makeText(this@ServiceActivity,  "Please input address", Toast.LENGTH_SHORT).show()
        }else if(get_id_type.isEmpty()){
            Toast.makeText(this@ServiceActivity,  "Please select type", Toast.LENGTH_SHORT).show()
        }else if(size.isEmpty()){
            Toast.makeText(this@ServiceActivity,  "Please select size", Toast.LENGTH_SHORT).show()
        }else{
            PopupLodGif()
            Log.d("Tag","check : "+et_id_member_driver.text+" "+et_name_driver.text+" "+et_phone_driver.text+" "+et_email_driver.text+" "
                    +et_brd_driver.text+" "+et_address_driver.text+" "+get_id_type+" "+size+" "+check_paid_type+" "+checked)
            val myService: SOService
            myService = ApiUtils.getSOService()
            myService.CreateMembers( "application/x-www-form-urlencoded","Bearer " + access_token,et_id_member_driver.text.toString(),et_name_driver.text.toString(),et_phone_driver.text.toString(),et_email_driver.text.toString(),
                    et_brd_driver.text.toString(),et_address_driver.toString(),get_id_type,size,check_paid_type,checked).enqueue(object : Callback<ResponseObject> {

                override fun onResponse(call: Call<ResponseObject>, response1: Response<ResponseObject>) {
                    if (response1.isSuccessful()) {
                        Log.d("Tag", "Response result : " + response1.body()!!.result)
                        object : CountDownTimer(3000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                //here you can have your logic to set text to edittext
                            }
                            override fun onFinish() {
                                dialog_load_gif.dismiss()
                                PopupSuccess()
                            }
                        }.start()
                    } else {
                        dialog_load_gif.dismiss()
                        Log.d("Tag", "Response error : " + response1.code())
                    }
                }
                override fun onFailure(call: Call<ResponseObject>, t: Throwable) {
                    Log.d("Tag", "call : " + call)
                    dialog_load_gif.dismiss()
                }
            })
        }
    }

    fun isEmailValid(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun setListType() {
        responseList = ArrayList<String>()
        responseList_id = ArrayList<String>()
        responseList.add("Select type")
        for (i in 0 until itemslist.size) {
            type = itemslist[i].getType()
            type_id = itemslist[i].get_id()
            responseList.add(type)
            responseList_id.add(type_id)
        }

        val array = arrayOfNulls<String>(responseList.size)
        for (j in 0 until responseList.size) {
            array[j] = responseList[j]
        }

        numberPickerType.minValue = 0
        numberPickerType.maxValue = responseList.size - 1
        numberPickerType.displayedValues = array
        numberPickerType.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        setDividerColor(numberPickerType, Color.WHITE)
        setNumberPickerTextColor(numberPickerType, Color.WHITE)

        val myValChangedListener = NumberPicker.OnValueChangeListener { picker, oldVal, newVal ->
            if (picker.value > 0) {
                get_id_type = responseList_id[picker.value-1]
                Log.d("Tag", "get_id_type : " + get_id_type)
            }else{
                get_id_type = ""
            }

        }
        numberPickerType.setOnValueChangedListener(myValChangedListener)
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

    fun CheckCarSize(list_image: Array<ImageView>, position: Int) {
        val strings_not_check = arrayOf( R.drawable.car_type_s_1x_w,  R.drawable.car_type_m_1x_w, R.drawable.car_type_l_1x_w, R.drawable.car_type_xl_1x_w, R.drawable.car_type_xxl_1x_w,
                R.drawable.car_type_sport_1x_w, R.drawable.car_type_super_1x_w)
        val strings_check_check = arrayOf( R.drawable.car_type_s_1x,R.drawable.car_type_m_1x,R.drawable.car_type_l_1x,R.drawable.car_type_xl_1x,R.drawable.car_type_xxl_1x,R.drawable.car_type_sport_1x,
                R.drawable.car_type_super_1x)
        for (i in 0 until list_image.size){
            if(position == i){
                list_image[i].setImageResource(strings_check_check[i])
            }else{
                list_image[i].setImageResource(strings_not_check[i])
            }
        }
    }

    private class PageAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

        override fun getItem(position: Int): Fragment? {

            when (position) {
                0 -> {
                    val args = Bundle()
//                    args.putString("list_json", itemslist.toString())
                    val tab1 = FragmentNewServices()
                    tab1.arguments = args
                    return tab1
                }
                1 -> {
                    val args = Bundle()
//                    args.putString("list_json", itemslist.toString())
                    val tab1 = FragmentOnService()
                    tab1.arguments = args
                    return tab1
                }
                2 -> {
                    val args = Bundle()
//                    args.putString("list_json", itemslist.toString())
                    val tab1 = FragmentHistory()
                    tab1.arguments = args
                    return tab1
                }
                3 -> {
                    val args = Bundle()
//                    args.putString("list_json", itemslist.toString())
                    val tab1 = FragmentMember()
                    tab1.arguments = args
                    return tab1
                }
                4 -> {
                    val args = Bundle()
//                    args.putString("list_json", itemslist.toString())
                    val tab1 = FragmentSetting()
                    tab1.arguments = args
                    return tab1
                }
                else -> return null
            }

        }

        override fun getItemPosition(`object`: Any?): Int {
            return POSITION_NONE
        }

        override fun getCount(): Int {
            return 5
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val mFragmentTags = HashMap<Int, String>()
            val obj = super.instantiateItem(container, position)
            if (obj is Fragment) {
                // record the fragment tag here.
                val tag = obj.tag
                mFragmentTags.put(position, tag)
            }
            return obj
        }

    }

    private class TransformerItem(internal val clazz: Class<out ViewPager.PageTransformer>) {

        internal val title: String

        init {
            title = clazz.simpleName
        }

        override fun toString(): String {
            return title
        }

    }

    fun PopupLogout() {
        val dialog_upload = Dialog(this@ServiceActivity)
        dialog_upload.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog_upload.setContentView(R.layout.custom_dialog_logout)
        dialog_upload.setCancelable(true)
        dialog_upload.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var bt_ok = dialog_upload.findViewById<View>(R.id.bt_ok) as Button

        bt_ok.setOnClickListener {
            dialog_upload.dismiss()
            val intent = Intent(this@ServiceActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        dialog_upload.show()
    }

    fun PopupLodGif() {

        dialog_load_gif = Dialog(this@ServiceActivity)
        dialog_load_gif.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog_load_gif.setContentView(R.layout.custom_loading_image_gif)
        dialog_load_gif.setCancelable(true)
        dialog_load_gif.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val image_gif = dialog_load_gif.findViewById<View>(R.id.image_gif) as ImageView
        val imageViewTarget = GlideDrawableImageViewTarget(image_gif)
        Glide.with(this).load(R.raw.loading).into(imageViewTarget)

        dialog_load_gif.show()

    }

    fun PopupSuccess() {
        val dialog_upload_success = Dialog(this@ServiceActivity)
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
                dialog_member.dismiss()
            }

        }.start()

        dialog_upload_success.show()

    }


    fun setAdapter(position: Int) {

//        val manager = supportFragmentManager
//        val trans = manager.beginTransaction()
//        trans.remove(FragmentOnServiceDetail())
//        trans.commit()
//        manager.popBackStack()

        lv_back.visibility = View.GONE

    }

    override fun onBackPressed() {
        val fragments = supportFragmentManager.backStackEntryCount
        Log.d("Tag","Check fragment : "+fragments)
        lv_back.visibility = View.GONE
        if (fragments > 0) {
            val manager = getSupportFragmentManager()
            val trans = manager.beginTransaction()
            trans.remove(FragmentOnServiceDetail())
            trans.commit()
            manager.popBackStack()

            return
        }else{

        }
    }
}

