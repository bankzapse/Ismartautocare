package com.ismartautocare;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AllAdapterListViewHistoryDetail extends ArrayAdapter<ItemOfAll> {

    private Context context;
    int resourceId;
    ArrayList<ItemOfAll> gettype;

    private class ViewHolder{
        TextView ttv_name_services,tv_count,tv_view_price;
        ImageView image_remove_services;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public AllAdapterListViewHistoryDetail(Context context, int resourceId, ArrayList<ItemOfAll> gettype) {
        super(context, resourceId, gettype);
        this.context = context;
        this.resourceId = resourceId;
        this.gettype = gettype;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SharedPreferences sharedPref = getContext().getSharedPreferences("pump_charge", 0);

        final ItemOfAll object = getItem(position);
        AllAdapterListViewHistoryDetail.ViewHolder  holder = null;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
        if(convertView == null){
            convertView = inflater.inflate(resourceId, parent, false);
            holder = new AllAdapterListViewHistoryDetail.ViewHolder();

            holder.ttv_name_services = convertView.findViewById(R.id.tv_name_services);
            holder.tv_count = convertView.findViewById(R.id.tv_count);
            holder.tv_view_price = convertView.findViewById(R.id.tv_view_price);
            holder.image_remove_services = convertView.findViewById(R.id.image_remove_services);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

//        Log.d("Tag","object desc : "+object.getDesc());
        holder.tv_count.setText(object.getCode());
        holder.ttv_name_services.setText(object.getDesc());
        holder.tv_view_price.setText(object.getPrice());

        holder.image_remove_services.setVisibility(View.GONE);

        return convertView;
    }


}
