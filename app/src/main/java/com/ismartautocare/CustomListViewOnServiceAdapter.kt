package com.ismartautocare

import android.content.Context
import android.widget.BaseAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import java.util.ArrayList

/**
 * Created by programmer on 10/10/17.
 */

class CustomListViewOnServiceAdapter(context: Context, users: ArrayList<String>) : ArrayAdapter<String>(context, R.layout.custom_listview_on_service, users) {
    // View lookup cache
    private class ViewHolder//        TextView name;
    //        TextView home;

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        // Get the data item for this position
        //        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        val viewHolder: ViewHolder // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            convertView = inflater.inflate(R.layout.custom_listview_on_service, parent, false)
            //            viewHolder.name = (TextView) convertView.findViewById(R.id.tvName);
            //            viewHolder.home = (TextView) convertView.findViewById(R.id.tvHome);
            // Cache the viewHolder object inside the fresh view
            convertView!!.tag = viewHolder
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = convertView.tag as ViewHolder
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        //        viewHolder.name.setText(user.name);
        //        viewHolder.home.setText(user.hometown);
        // Return the completed view to render on screen
        return convertView
    }
}