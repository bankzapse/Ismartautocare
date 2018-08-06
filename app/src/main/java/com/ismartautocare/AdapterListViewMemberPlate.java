package com.ismartautocare;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.gson.JsonObject;
import com.llollox.androidtoggleswitch.widgets.ToggleSwitch;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by programmer on 2/5/18.
 */

public class AdapterListViewMemberPlate extends ArrayAdapter<Item> {

    private Context context;
    int resourceId;
    ArrayList<Item> gettype;
    String brand,province;

    private class ViewHolder{
        TextView tv_plate_number,tv_province;
        ImageView image_brand;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public AdapterListViewMemberPlate(Context context, int resourceId, ArrayList<Item> gettype) {
        super(context, resourceId, gettype);
        this.context = context;
        this.resourceId = resourceId;
        this.gettype = gettype;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SharedPreferences sharedPref = getContext().getSharedPreferences("pump_charge", 0);

        final Item object = getItem(position);
        AdapterListViewMemberPlate.ViewHolder holder = null;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
        if(convertView == null){
            convertView = inflater.inflate(resourceId, parent, false);
            holder = new AdapterListViewMemberPlate.ViewHolder();

            holder.tv_plate_number = convertView.findViewById(R.id.tv_plate_number);
            holder.tv_province = convertView.findViewById(R.id.tv_province);
            holder.image_brand = convertView.findViewById(R.id.image_brand);

            convertView.setTag(holder);
        }else{
            holder = (AdapterListViewMemberPlate.ViewHolder)convertView.getTag();
        }

        try {
            JSONObject object_car_model = new JSONObject(String.valueOf(object.getCar_models()));
            brand = object_car_model.getString("brand");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.tv_plate_number.setText(object.getPlate());
        holder.tv_province.setText(object.getProvince());

        Glide.with(context)
                .load(context.getResources().getString(R.string.base_url)+"carLogo/"+brand+".png")
                .error(R.drawable.bmw)
                .into(holder.image_brand);

//        try {
//            InstanceID instanceID = InstanceID.getInstance(this);
//
//            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
//                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
//
//            Log.i("Tag", "GCM Registration Token: " + token);
//
//        }catch (Exception e) {
//            Log.d("Tag", "Failed to complete token refresh", e);
//        }

        return convertView;
    }

}
