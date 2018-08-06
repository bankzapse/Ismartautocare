package com.ismartautocare;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by programmer on 3/5/18.
 */

public class AllAdapterListViewOnServiceDetail extends ArrayAdapter<ItemServicePrice> {

    private Context context;
    int resourceId;
    ArrayList<ItemServicePrice> gettype;
    String size;

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

    public AllAdapterListViewOnServiceDetail(Context context, int resourceId, ArrayList<ItemServicePrice> gettype,String size_) {
        super(context, resourceId, gettype);
        this.context = context;
        this.resourceId = resourceId;
        this.gettype = gettype;
        this.size = size_;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ItemServicePrice object = getItem(position);
        AllAdapterListViewOnServiceDetail.ViewHolder holder = null;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
        if(convertView == null){
            convertView = inflater.inflate(resourceId, parent, false);
            holder = new AllAdapterListViewOnServiceDetail.ViewHolder();

            holder.ttv_name_services = convertView.findViewById(R.id.tv_name_services);
            holder.tv_count = convertView.findViewById(R.id.tv_count);
            holder.tv_view_price = convertView.findViewById(R.id.tv_view_price);
            holder.image_remove_services = convertView.findViewById(R.id.image_remove_services);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.tv_count.setText(String.valueOf(position+1));
        holder.ttv_name_services.setText(object.desc);

        try {
            JSONObject object_price = new JSONObject(gettype.get(position).getPrice().toString());
            holder.tv_view_price.setText(object_price.getString(size));
        } catch (JSONException e) {
//            e.printStackTrace();
        }

        holder.image_remove_services.setVisibility(View.GONE);

        return convertView;
    }

    public String TimeCuurent() {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm");
//        Date data = new Date();
//        try {
//            dateformat.format(c.time);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        return dateformat.format(c.getTime());
    }

    public String convertStringToData(String stringData) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");//yyyy-MM-dd'T'HH:mm:ss
        //        val output = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        SimpleDateFormat output = new SimpleDateFormat("HH:mm");
        Date data = new Date();
        try {
            data = sdf.parse(stringData);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return output.format(data);
    }

}

//        extends ArrayAdapter<String> {
//
//    private Context context;
//    int resourceId;
//    ArrayList<String> gettype;
//    String size;
//
//    private class ViewHolder{
//        TextView ttv_name_services,tv_count,tv_view_price;
//        ImageView image_remove_services;
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        Log.d("Tag","gettype size : "+ gettype.size());
//        return gettype.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }
//
//    public AllAdapterListViewOnServiceDetail(Context context, int resourceId, ArrayList<String> gettype_,String size_) {
//        super(context, resourceId);
//        this.context = context;
//        this.resourceId = resourceId;
//        this.gettype = gettype_;
//        this.size = size_;
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//
////        final ItemServicePrice object = getItem(position);
//        ViewHolder holder = null;
//        LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
//        if(convertView == null){
//            convertView = inflater.inflate(resourceId, null);
//
//            holder = new ViewHolder();
//
//            holder.ttv_name_services = convertView.findViewById(R.id.tv_name_services);
//            holder.tv_count = convertView.findViewById(R.id.tv_count);
//            holder.tv_view_price = convertView.findViewById(R.id.tv_view_price);
//            holder.image_remove_services = convertView.findViewById(R.id.image_remove_services);
//
//            convertView.setTag(holder);
//        }else{
//            holder = (ViewHolder)convertView.getTag();
//        }
//
//        Log.d("Tag","getDesc : "+gettype.get(position));
////
//        holder.tv_count.setText(String.valueOf(position+1));
////        holder.ttv_name_services.setText(gettype.get(position).getDesc());
//
//        try {
//            JSONObject object_price = new JSONObject(gettype.get(position).getPrice().toString());
//            if (object_price.getString("S").equalsIgnoreCase(size)){
//                holder.tv_view_price.setText(object_price.getString("S"));
//            } else if (object_price.getString("M").equalsIgnoreCase(size)) {
//                holder.tv_view_price.setText(object_price.getString("M"));
//            } else if (object_price.getString("L").equalsIgnoreCase(size)) {
//                holder.tv_view_price.setText(object_price.getString("L"));
//            } else if (object_price.getString("XL").equalsIgnoreCase(size)) {
//                holder.tv_view_price.setText(object_price.getString("XL"));
//            } else if (object_price.getString("XXL").equalsIgnoreCase(size)) {
//                holder.tv_view_price.setText(object_price.getString("XXL"));
//            } else if (object_price.getString("SPORT").equalsIgnoreCase(size)) {
//                holder.tv_view_price.setText(object_price.getString("SPORT"));
//            } else if (object_price.getString("SUPER").equalsIgnoreCase(size)) {
//                holder.tv_view_price.setText(object_price.getString("SUPER"));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        holder.image_remove_services.setVisibility(View.GONE);
//
//        return convertView;
//    }
//
//
//}
