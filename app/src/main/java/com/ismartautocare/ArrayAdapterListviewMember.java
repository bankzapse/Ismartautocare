package com.ismartautocare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by programmer on 2/23/18.
 */

public class ArrayAdapterListviewMember extends ArrayAdapter<ItemMember> {

    private Context context;
    int resourceId;
    ArrayList<ItemMember> gettype;
    String get_size,by,get_code,type;

    private class ViewHolder{
        TextView tv_plate_number,tv_province,tv_served,tv_code,tv_date_time,tv_address,tv_type,tv_name;
        ImageView image_size;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public ArrayAdapterListviewMember(Context context, int resourceId, ArrayList<ItemMember> gettype) {
        super(context, resourceId, gettype);
        this.context = context;
        this.resourceId = resourceId;
        this.gettype = gettype;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ItemMember object = getItem(position);
        ViewHolder  holder = null;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
        if(convertView == null){
            convertView = inflater.inflate(resourceId, parent, false);
            holder = new ViewHolder();

            holder.image_size = convertView.findViewById(R.id.image_size);
            holder.tv_code = convertView.findViewById(R.id.tv_code);
            holder.tv_date_time = convertView.findViewById(R.id.tv_date_time);
            holder.tv_address = convertView.findViewById(R.id.tv_address);
            holder.tv_type = convertView.findViewById(R.id.tv_type);
            holder.tv_name = convertView.findViewById(R.id.tv_name);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

//        try {
//            JSONObject object_plate = new JSONObject(gettype.get(position).getMember_type().toString());
////            JSONObject object_plate = new JSONObject(gettype.get(position).getMember_type().toString());
////            JSONObject object_served = new JSONObject(gettype.get(position).getServed().toString());
////            plate = object_plate.getString("plate");
////            province = object_plate.getString("province");
////            by = object_served.getString("by");
//            type = object_plate.getString("type");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        get_code = gettype.get(position).getCode();
        get_size = gettype.get(position).getSize();
//
        holder.tv_code.setText(gettype.get(position).getCode());
        holder.image_size.setImageResource(R.drawable.car_type_l_1x);
        holder.tv_name.setText(gettype.get(position).getName());

        if (get_size.equals("S")) {
            holder.image_size.setImageResource(R.drawable.car_type_s_1x);
        }else if(get_size.equals("M")){
            holder.image_size.setImageResource(R.drawable.car_type_m_1x);
        }else if(get_size.equals("L")){
            holder.image_size.setImageResource(R.drawable.car_type_l_1x);
        }else if(get_size.equals("XL")){
            holder.image_size.setImageResource(R.drawable.car_type_xl_1x);
        }else if(get_size.equals("XXL")){
            holder.image_size.setImageResource(R.drawable.car_type_xxl_1x);
        }else if(get_size.equals("SPORT")){
            holder.image_size.setImageResource(R.drawable.car_type_sport_1x);
        }else if(get_size.equals("SUPER")){
            holder.image_size.setImageResource(R.drawable.car_type_super_1x);
        }else{
            holder.image_size.setImageResource(R.drawable.car_type_1x);
        }
//
        holder.tv_date_time.setText(convertStringToData(gettype.get(position).getCreated()));
        holder.tv_address.setText(gettype.get(position).getAddress());
//        holder.tv_type.setText(type);
//        holder.tv_plate_number.setText(plate);
//        holder.tv_province.setText(province);
//        holder.tv_served.setText("Served by "+by);

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

}
