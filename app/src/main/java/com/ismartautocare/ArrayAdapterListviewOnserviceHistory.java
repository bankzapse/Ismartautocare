package com.ismartautocare;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by programmer on 3/6/18.
 */

public class ArrayAdapterListviewOnserviceHistory extends ArrayAdapter<ItemOfAll> {

    private Context context;
    int resourceId;
    ArrayList<ItemOfAll> gettype;
    String plate,province,by,date_time,brand;

    private class ViewHolder{
        TextView tv_plate_number,tv_province,tv_price,tv_service,tv_ref,tv_date_time;
        ImageView img_logo_list,img_brand;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public ArrayAdapterListviewOnserviceHistory(Context context, int resourceId, ArrayList<ItemOfAll> gettype) {
        super(context, resourceId, gettype);
        this.context = context;
        this.resourceId = resourceId;
        this.gettype = gettype;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ItemOfAll object = getItem(position);
        ViewHolder  holder = null;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
        if(convertView == null){
            convertView = inflater.inflate(resourceId, parent, false);
            holder = new ViewHolder();

            holder.tv_plate_number = convertView.findViewById(R.id.tv_plate_number);
            holder.tv_province = convertView.findViewById(R.id.tv_province);
            holder.tv_price = convertView.findViewById(R.id.tv_price);
            holder.tv_service = convertView.findViewById(R.id.tv_service);
            holder.tv_ref = convertView.findViewById(R.id.tv_ref);
            holder.tv_date_time = convertView.findViewById(R.id.tv_date_time);
            holder.img_brand = convertView.findViewById(R.id.img_brand);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        StringBuilder sb = new StringBuilder();
        try {
            JSONObject object_plate = new JSONObject(gettype.get(position).getPlate().toString());
            JSONObject object_released = new JSONObject(gettype.get(position).getReleased().toString());
            JSONObject object_model = new JSONObject(object_plate.getString("model"));

            plate = object_plate.getString("plate");
            province = object_plate.getString("province");
            date_time = object_released.getString("time");
            brand = object_model.getString("brand");

            Gson gson = new Gson() ;
            JSONArray json = new JSONArray(gson.toJson(gettype.get(position).getServices()).toString());
            Log.d("Tag","json : "+ json);

            for (int i = 0 ; i < json.length(); i++){
                JSONObject getcode = json.getJSONObject(i);
                sb.append(getcode.getString("code"));
                sb.append(" ");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.tv_plate_number.setText(plate);
        holder.tv_province.setText(province);
        holder.tv_service.setText(sb);
        holder.tv_price.setMaxEms(10);
        holder.tv_price.setText(gettype.get(position).getTotal());
        holder.tv_ref.setText("#"+gettype.get(position).getRno());
        holder.tv_date_time.setText(convertStringToData(date_time)+"\n"+convertStringToTime(date_time));

        Glide.with(context)
                .load(context.getResources().getString(R.string.base_url)+"carLogo/"+brand+".png")
                .error(R.drawable.bmw)
                .into(holder.img_brand);

        return convertView;
    }

    public String convertStringToData(String stringData){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");//yyyy-MM-dd'T'HH:mm:ss
//        val output = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        Date data = null;
        try {
            data = sdf.parse(stringData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output.format(data);
    }

    public String convertStringToTime(String stringData){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");//yyyy-MM-dd'T'HH:mm:ss
//        val output = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        SimpleDateFormat output = new SimpleDateFormat("HH:mm");
        Date data = null;
        try {
            data = sdf.parse(stringData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output.format(data);
    }

}
