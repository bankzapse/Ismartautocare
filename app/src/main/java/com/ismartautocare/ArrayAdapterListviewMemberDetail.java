package com.ismartautocare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by programmer on 3/6/18.
 */

public class ArrayAdapterListviewMemberDetail extends ArrayAdapter<ItemMember> {

    private Context context;
    int resourceId;
    ArrayList<ItemMember> gettype;
    String plate,province,by;

    private class ViewHolder{
        TextView tv_plate_number,tv_province,tv_served;
        ImageView img_logo_list;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public ArrayAdapterListviewMemberDetail(Context context, int resourceId, ArrayList<ItemMember> gettype) {
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

            holder.tv_plate_number = convertView.findViewById(R.id.tv_plate_number);
            holder.tv_province = convertView.findViewById(R.id.tv_province);
//            holder.tv_served = convertView.findViewById(R.id.tv_served);

//            holder.tv_station_address = convertView.findViewById(R.id.tv_station_address);
//            holder.tv_number = convertView.findViewById(R.id.tv_number);
//            holder.img_logo_list = convertView.findViewById(R.id.img_logo_list);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

//        try {
//            JSONObject object_plate = new JSONObject(gettype.get(position).getPlate().toString());
//            JSONObject object_served = new JSONObject(gettype.get(position).getServed().toString());
//            plate = object_plate.getString("plate");
//            province = object_plate.getString("province");
//            by = object_served.getString("by");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        holder.tv_plate_number.setText(plate);
//        holder.tv_province.setText(province);
//        holder.tv_served.setText("Served by max");

        return convertView;
    }

}
