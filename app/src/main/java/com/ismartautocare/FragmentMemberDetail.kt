package com.ismartautocare

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ismartautocare.service.ApiUtils
import com.ismartautocare.service.SOService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by programmer on 2/23/18.
 */
class FragmentMemberDetail : Fragment() {

    lateinit var lv:ListView
    lateinit var v:View
    lateinit var sharedPref: SharedPreferences
    lateinit var access_token: String
    lateinit var itemslist: ArrayList<ItemMember>
    lateinit var tv_not_data: TextView
    lateinit var item: ItemMember
    lateinit var tv_driver_name: TextView
    lateinit var tv_driver_phon: TextView
    lateinit var image_member_car_size: ImageView
    lateinit var size: String
    lateinit var tv_date: TextView
    lateinit var tv_issuedfrom: TextView
    lateinit var tv_issuedby: TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var v = inflater!!.inflate(R.layout.fragment_member_detail, container, false) as ViewGroup

        sharedPref = activity.getSharedPreferences("isac", 0)
        access_token = sharedPref.getString("access_token", "")

        Element(v)

        val args = arguments
        item = args.getSerializable("object") as ItemMember

        tv_not_data.visibility = View.GONE

        tv_driver_name.text = item.getName()

//        Log.d("Tag", "getPlates : " + item.getPlates().toString())
//        try {
//            val object_plate = JSONObject(item.getPlates().toString())
//
//            var member_tel = object_plate.getString("contact")
//            var member_model = object_plate.getString("model")
//            val object_model = JSONObject(member_model)
//
//            size = object_model.getString("size")
//            if (size!!.equals("S", ignoreCase = true)) {
//                image_member_car_size.setImageResource(R.drawable.car_type_s_1x)
//            } else if (size!!.equals("M", ignoreCase = true)) {
//                image_member_car_size.setImageResource(R.drawable.car_type_m_1x)
//            } else if (size!!.equals("L", ignoreCase = true)) {
//                image_member_car_size.setImageResource(R.drawable.car_type_l_1x)
//            } else if (size!!.equals("XL", ignoreCase = true)) {
//                image_member_car_size.setImageResource(R.drawable.car_type_xl_1x)
//            } else if (size!!.equals("XXL", ignoreCase = true)) {
//                image_member_car_size.setImageResource(R.drawable.car_type_xxl_1x)
//            } else if (size!!.equals("SPORT", ignoreCase = true)) {
//                image_member_car_size.setImageResource(R.drawable.car_type_sport_1x)
//            } else if (size!!.equals("SUPER", ignoreCase = true)) {
//                image_member_car_size.setImageResource(R.drawable.car_type_supert_1x)
//            }
//
//            tv_driver_phon.text = member_tel
////            tv_date.text = convertStringToData(item.getCreated())
////            tv_issuedfrom.text = item.getIssuedfrom()
////            tv_issuedby.text = item.getIssuedby()
//
//        }catch (e : Exception){
//        }
//
////        callServiceMemberHistory()
//
//        lv.onItemClickListener = AdapterView.OnItemClickListener { adv, v, position, arg3 ->
//            // TODO Auto-generated method stub
//            Log.d("TagClick", "Click at position" + position)
//
//            val args = Bundle()
//            args.putSerializable("object", itemslist[position])
//            val toFragment = FragmentHistoryDetail()
//            toFragment.arguments = args
//
//            val fm = fragmentManager
//            val ft = fm.beginTransaction()
//            ft.replace(R.id.show_fragment, toFragment, "fragment_history_detail")
//            ft.addToBackStack(FragmentTransaction.TRANSIT_FRAGMENT_FADE.toString())
//            ft.commit()
//
//            (activity as ServiceActivity).lv_back.visibility = View.VISIBLE
//
//        }

        return v
    }

    fun Element(v: ViewGroup) {
        tv_not_data = v.findViewById(R.id.tv_not_data)
        lv = v.findViewById(R.id.lv)

        tv_driver_name = v.findViewById(R.id.tv_driver_name)
        tv_driver_phon = v.findViewById(R.id.tv_driver_phon)
        image_member_car_size = v.findViewById(R.id.image_member_car_size)
        tv_date = v.findViewById(R.id.tv_date)
        tv_issuedfrom = v.findViewById(R.id.tv_issuedfrom)
        tv_issuedby = v.findViewById(R.id.tv_issuedby)
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

//    private fun callServiceMemberHistory() {
//        val myService: SOService
//        myService = ApiUtils.getSOService()
//        myService.getMemberHistory("application/x-www-form-urlencoded", "Bearer " + access_token,item.getId()).enqueue(object : Callback<SOResponseOnService> {
//
//            override fun onResponse(call: Call<SOResponseOnService>, response1: Response<SOResponseOnService>) {
//                if (response1.isSuccessful) {
//                    itemslist = ArrayList()
//                    if(response1.body()!!.result.size>0){
//                        itemslist = response1.body()!!.result as java.util.ArrayList<ItemMember>
//                        Log.d("Tag", "Response response1 body member : " + response1.body()!!.result)
//
//                        val adapter = ArrayAdapterListviewMemberDetail(activity, R.layout.custom_listview_history, itemslist)
//                        lv.adapter = adapter
//                        tv_not_data.visibility = View.GONE
//                    }else{
//                        tv_not_data.visibility = View.VISIBLE
//                    }
//
//                } else {
//                    Log.d("Tag", "Response error : " + response1.code())
//                }
//            }
//
//            override fun onFailure(call: Call<SOResponseOnService>, t: Throwable) {
//                Log.d("Tag", "Response call : " + call)
//                Log.d("Tag", "Response t : " + t.message)
//            }
//        })
//    }

}
