package com.ismartautocare

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.text.Editable
import android.text.TextWatcher
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
import java.util.ArrayList

/**
 * Created by programmer on 10/11/17.
 */

class FragmentMember : Fragment() {

    lateinit var lv:ListView
    lateinit var v:View
    lateinit var show_fragment: FrameLayout
    lateinit var sharedPref: SharedPreferences
    lateinit var access_token: String
    lateinit var itemslist: ArrayList<ItemMember>
    lateinit var tv_not_data: TextView
    lateinit var et_keywords: EditText


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var v = inflater!!.inflate(R.layout.fragment_member, container, false) as ViewGroup

        sharedPref = activity.getSharedPreferences("isac", 0)
        access_token = sharedPref.getString("access_token", "")

        Element(v)

        tv_not_data.visibility = View.GONE

        lv.onItemClickListener = AdapterView.OnItemClickListener { adv, v, position, arg3 ->
            // TODO Auto-generated method stub
            Log.d("TagClick", "Click at position" + position)
            Log.d("TagClick", "Click at price" + itemslist[position])

            val args = Bundle()
            args.putSerializable("object", itemslist[position])
            val toFragment = FragmentMemberDetail()
            toFragment.arguments = args

            val fm = fragmentManager
            val ft = fm.beginTransaction()
            ft.replace(R.id.show_fragment, toFragment, "fragment_member_detail")
            ft.addToBackStack(FragmentTransaction.TRANSIT_FRAGMENT_FADE.toString())
            ft.commit()

            (activity as ServiceActivity).lv_back.visibility = View.VISIBLE

        }

        callService("")

        et_keywords.addTextChangedListener(Watcher)

        return v
    }

    fun Element(v: ViewGroup) {

        lv = v.findViewById<View>(R.id.lv) as ListView
        show_fragment = v.findViewById(R.id.show_fragment)
        tv_not_data = v.findViewById(R.id.tv_not_data)
        et_keywords = v.findViewById(R.id.et_keywords)

    }



    private val Watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//            if (s.length == 0) {
//                callService("")
//            } else {
////                lv_box1.alpha = 0.5f
////                lv_box2.visibility = View.INVISIBLE
////                lv_search.visibility = View.VISIBLE
//                callService(s.toString())
//            }
            callService(s.toString())
        }

        override fun afterTextChanged(s: Editable) {

        }
    }

    private fun callService(s : String) {
        val myService: SOService
        myService = ApiUtils.getSOService()
        myService.getMemberPlateMember("application/x-www-form-urlencoded", "Bearer " + access_token,s).enqueue(object : Callback<SOAnswersMember> {

            override fun onResponse(call: Call<SOAnswersMember>, response1: Response<SOAnswersMember>) {
                if (response1.isSuccessful) {
                    itemslist = ArrayList()
                    if(response1.body()!!.result.size>0){
                        itemslist = response1.body()!!.result as java.util.ArrayList<ItemMember>
                        Log.d("Tag", "Response response1 body member : " + response1.body()!!.result)

                        val adapter = ArrayAdapterListviewMember(activity, R.layout.custom_listview_member, itemslist)
                        lv.adapter = adapter
                        tv_not_data.visibility = View.GONE
                        lv.visibility = View.VISIBLE
                        adapter.notifyDataSetChanged()
                    }else{
                        tv_not_data.visibility = View.VISIBLE
                        lv.visibility = View.GONE
                    }

                } else {
                    Log.d("Tag", "Response error : " + response1.code())
                }
            }

            override fun onFailure(call: Call<SOAnswersMember>, t: Throwable) {
                Log.d("Tag", "Response call : " + call)
                Log.d("Tag", "Response t : " + t.message)
            }
        })
    }

}
