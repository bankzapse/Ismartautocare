package com.ismartautocare;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

//import com.samart.thailandcheckin.imageloader.ImageLoader;

/**
 * Created by thananan on 3/28/2016 AD.
 */
public class AllAdapterListView extends ArrayAdapter<Item> {

    private Context context;
    int resourceId;
    ArrayList<Item> gettype;
    TextView tv_total,tv_dis,tv_final,tv_service_end;
    String type_dis;
    ImageView image_dis;

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

    public AllAdapterListView(Context context, int resourceId, ArrayList<Item> gettype,TextView tv_total,TextView tv_dis,TextView tv_final, String type_dis,ImageView image_dis,TextView tv_service_end) {
        super(context, resourceId, gettype);
        this.context = context;
        this.resourceId = resourceId;
        this.gettype = gettype;
        this.tv_total = tv_total;
        this.tv_dis = tv_dis;
        this.tv_final = tv_final;
        this.type_dis = type_dis;
        this.image_dis = image_dis;
        this.tv_service_end = tv_service_end;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SharedPreferences sharedPref = getContext().getSharedPreferences("pump_charge", 0);

        final Item object = getItem(position);
        ViewHolder  holder = null;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
        if(convertView == null){
            convertView = inflater.inflate(resourceId, parent, false);
            holder = new ViewHolder();

            holder.ttv_name_services = convertView.findViewById(R.id.tv_name_services);
            holder.tv_count = convertView.findViewById(R.id.tv_count);
            holder.tv_view_price = convertView.findViewById(R.id.tv_view_price);
            holder.image_remove_services = convertView.findViewById(R.id.image_remove_services);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.tv_count.setText(String.valueOf(position+1));
        holder.ttv_name_services.setText(object.getDesc());
        holder.tv_view_price.setText(object.getPrice().toString());

        holder.image_remove_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettype.remove(position);
                notifyDataSetChanged();
                FragmentNewServices fn = new FragmentNewServices();
                fn.UpdateSumPrice(gettype,tv_total, tv_dis, tv_final,type_dis,image_dis, tv_service_end);
            }
        });

        return convertView;
    }

}
