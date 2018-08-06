package com.ismartautocare

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.util.ArrayList

/**
 * Created by programmer on 1/31/18.
 */
class CustomListViewServicesListViewAdapter(context: Context, users: ArrayList<Item>) : ArrayAdapter<Item>(context, R.layout.custom_services, users) {

    lateinit var tv_name_services: TextView

    private class ViewHolder
    //        TextView home;


    override fun getViewTypeCount(): Int {

        return count
    }

    override fun getItemViewType(position: Int): Int {

        return position
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val list_item = getItem(position)

        var convertView = convertView

        // Get the data item for this position
        //        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        val viewHolder: ViewHolder // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            convertView = inflater.inflate(R.layout.custom_services, parent, false)
            tv_name_services = convertView.findViewById(R.id.tv_name_services)

            //            viewHolder.name = (TextView) convertView.findViewById(R.id.tvName);
            //            viewHolder.home = (TextView) convertView.findViewById(R.id.tvHome);
            // Cache the viewHolder object inside the fresh view
            convertView!!.tag = viewHolder
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = convertView.tag as ViewHolder
        }

        tv_name_services.text = list_item.desc

        return convertView
    }
}