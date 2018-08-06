package com.ismartautocare

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.TextView
import com.ismartautocare.service.ApiUtils
import com.ismartautocare.service.SOService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by programmer on 10/10/17.
 */
class FragmentHistory : Fragment() {

    lateinit var lv:ListView
    lateinit var v:View
    lateinit var show_fragment: FrameLayout
    lateinit var sharedPref: SharedPreferences
    lateinit var access_token: String
    lateinit var itemslist: ArrayList<ItemOfAll>
    lateinit var tv_not_data: TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        v = inflater!!.inflate(R.layout.fragment_history, container, false)

        sharedPref = activity.getSharedPreferences("isac", 0)
        access_token = sharedPref.getString("access_token", "")

        lv = v.findViewById<View>(R.id.lv) as ListView
        tv_not_data = v.findViewById(R.id.tv_not_data)

        lv.onItemClickListener = AdapterView.OnItemClickListener { adv, v, position, arg3 ->
            // TODO Auto-generated method stub
            Log.d("TagClick", "Click at position" + position)

            val args = Bundle()
            args.putSerializable("object", itemslist[position])
            val toFragment = FragmentHistoryDetail()
            toFragment.arguments = args

            val fm = fragmentManager
            val ft = fm.beginTransaction()
            ft.replace(R.id.show_fragment, toFragment, "fragment_history_detail")
            ft.addToBackStack(FragmentTransaction.TRANSIT_FRAGMENT_FADE.toString())
            ft.commit()

            (activity as ServiceActivity).lv_back.visibility = View.VISIBLE

        }

        callServiceHistory()

        return v
    }

    fun callServiceHistory() {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDateandTime = sdf.format(Date())
        Log.d("Tag", "currentDateandTime : " + currentDateandTime)

        val myService: SOService
        myService = ApiUtils.getSOService()
        myService.getOnServiceHistory("application/x-www-form-urlencoded", "Bearer " + access_token,currentDateandTime).enqueue(object : Callback<SOResponseOnService> {

            override fun onResponse(call: Call<SOResponseOnService>, response1: Response<SOResponseOnService>) {
                if (response1.isSuccessful) {
                    itemslist = ArrayList()
//                    Log.d("Tag","itemslist size : "+itemslist.size)
                    Log.d("Tag","response1 : "+response1.body()!!.result.size)
                    if(response1.body()!!.result.size>0){
                        itemslist = response1.body()!!.result as ArrayList<ItemOfAll>
                        Log.d("Tag", "Response response1 body : " + response1.body()!!.result)
                        val adapter = ArrayAdapterListviewOnserviceHistory(activity, R.layout.custom_listview_history, itemslist)
                        lv.adapter = adapter
                        tv_not_data.visibility = View.GONE
                    }else{
                        tv_not_data.visibility = View.VISIBLE
                    }
                } else {
                    Log.d("Tag", "Response error : " + response1.code())
                }
            }

            override fun onFailure(call: Call<SOResponseOnService>, t: Throwable) {
                Log.d("Tag", "Response call : " + call)
                Log.d("Tag", "Response t : " + t.message)
            }
        })
    }
}