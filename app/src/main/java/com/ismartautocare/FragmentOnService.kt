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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

/**
 * Created by programmer on 10/9/17.
 */
class FragmentOnService : Fragment() {

    lateinit var lv:ListView
    lateinit var v:View
    lateinit var show_fragment: FrameLayout
    lateinit var sharedPref: SharedPreferences
    lateinit var access_token: String
    lateinit var itemslist: ArrayList<ItemOfOnservice>
    lateinit var tv_not_data: TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        v = inflater!!.inflate(R.layout.fragment_on_service, container, false)
        lv = v.findViewById<View>(R.id.lv) as ListView

        sharedPref = activity.getSharedPreferences("isac", 0)
        access_token = sharedPref.getString("access_token", "")

        show_fragment = v.findViewById<View>(R.id.show_fragment) as FrameLayout
        tv_not_data = v.findViewById(R.id.tv_not_data)
        tv_not_data.visibility = View.GONE

        lv.onItemClickListener = AdapterView.OnItemClickListener { adv, v, position, arg3 ->
            // TODO Auto-generated method stub

            val args = Bundle()
            args.putSerializable("object", itemslist[position])
            val toFragment = FragmentOnServiceDetail()
            toFragment.arguments = args

            val fm = fragmentManager
            val ft = fm.beginTransaction()
            ft.replace(R.id.show_fragment, toFragment, "fragment_on_service_detail")
            ft.addToBackStack(FragmentTransaction.TRANSIT_FRAGMENT_FADE.toString())
            ft.commit()

            (activity as ServiceActivity).lv_back.visibility = View.VISIBLE

        }

        callService()

        return v
    }

    fun callService() {

        val myService: SOService
        myService = ApiUtils.getSOService()
        myService.getOnServices("application/x-www-form-urlencoded", "Bearer " + access_token).enqueue(object : Callback<SOResponseOnServiceTest> {

            override fun onResponse(call: Call<SOResponseOnServiceTest>, response1: Response<SOResponseOnServiceTest>) {
                if (response1.isSuccessful) {
                    itemslist = ArrayList()

                    try {
                        if(response1.body()!!.result.size>0){
                            itemslist = response1.body()!!.result as ArrayList<ItemOfOnservice>
                            val adapter = ArrayAdapterListviewOnservice(activity, R.layout.custom_listview_on_service, itemslist)
                            lv.adapter = adapter
                            tv_not_data.visibility = View.GONE
                        }else{
                            tv_not_data.visibility = View.VISIBLE
                        }
                    }catch (e : Exception){
                    }
                } else {
                    Log.d("Tag", "Response error : " + response1.code())
                }
            }

            override fun onFailure(call: Call<SOResponseOnServiceTest>, t: Throwable) {
                Log.d("Tag", "Response call : " + call)
                Log.d("Tag", "Response t service : " + t.message)
            }
        })
    }

}


