package com.ismartautocare;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AllAdapterGridView extends ArrayAdapter<String> {

    private Context context;
    int resourceId;
    ArrayList<String> gettype,responseList_model_services_code;
    ImageView image_dis;

    private class ViewHolder{
        TextView tv_id_service,tv_name_service;
        ImageView image_remove_services;
        LinearLayout lv_gv;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public AllAdapterGridView(Context context, int resourceId, ArrayList<String> gettype, ArrayList<String> responseList_model_services_code_) {
        super(context, resourceId, gettype);
        this.context = context;
        this.resourceId = resourceId;
        this.gettype = gettype;
        this.responseList_model_services_code = responseList_model_services_code_;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SharedPreferences sharedPref = getContext().getSharedPreferences("pump_charge", 0);

//        final Item object = getItem(position);
        AllAdapterGridView.ViewHolder holder = null;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
        if(convertView == null){
            convertView = inflater.inflate(resourceId, parent, false);
            holder = new AllAdapterGridView.ViewHolder();

            holder.lv_gv = convertView.findViewById(R.id.lv_gv);
            holder.tv_id_service = convertView.findViewById(R.id.tv_id_service);
            holder.tv_name_service = convertView.findViewById(R.id.tv_name_service);

            convertView.setTag(holder);
        }else{
            holder = (AllAdapterGridView.ViewHolder)convertView.getTag();
        }

//        Log.d("Tag","gettype : "+gettype.get(position));

//        if (position == mSelectedItem) {
//            // set your color
//        }


        holder.tv_id_service.setText(responseList_model_services_code.get(position));
        holder.tv_name_service.setText(gettype.get(position));

        return convertView;
    }

}
