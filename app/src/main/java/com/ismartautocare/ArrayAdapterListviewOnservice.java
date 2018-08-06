package com.ismartautocare;

import android.content.Context;
import android.graphics.Color;
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
 * Created by AndroidDeveloper on 8/3/17 AD.
 */
public class ArrayAdapterListviewOnservice extends ArrayAdapter<ItemOfOnservice> {

    private Context context;
    int resourceId;
    ArrayList<ItemOfOnservice> gettype;
    String plate,province,by,brand,time;

    private class ViewHolder{
        TextView tv_plate_number,tv_province,tv_served,tv_finish,tv_rno;
        ImageView img_brand,image_finish,image_check_status;
        RoundCornerProgressBar progress_bar_start,progress_bar_finish;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public ArrayAdapterListviewOnservice(Context context, int resourceId, ArrayList<ItemOfOnservice> gettype) {
        super(context, resourceId, gettype);
        this.context = context;
        this.resourceId = resourceId;
        this.gettype = gettype;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ItemOfOnservice object = getItem(position);
        ViewHolder  holder = null;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
        if(convertView == null){
            convertView = inflater.inflate(resourceId, parent, false);
            holder = new ViewHolder();

            holder.img_brand = convertView.findViewById(R.id.img_brand);
            holder.tv_plate_number = convertView.findViewById(R.id.tv_plate_number);
            holder.tv_province = convertView.findViewById(R.id.tv_province);
            holder.tv_served = convertView.findViewById(R.id.tv_served);
            holder.image_finish = convertView.findViewById(R.id.image_finish);
            holder.tv_finish = convertView.findViewById(R.id.tv_finish);
            holder.progress_bar_start = convertView.findViewById(R.id.progress_bar_start);
            holder.image_check_status = convertView.findViewById(R.id.image_check_status);
            holder.progress_bar_finish = convertView.findViewById(R.id.progress_bar_finish);
            holder.tv_rno = convertView.findViewById(R.id.tv_rno);

//            holder.tv_station_address = convertView.findViewById(R.id.tv_station_address);
//            holder.tv_number = convertView.findViewById(R.id.tv_number);
//            holder.img_logo_list = convertView.findViewById(R.id.img_logo_list);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        try {
            JSONObject object_plate = new JSONObject(gettype.get(position).getPlate().toString());
            JSONObject object_served = new JSONObject(gettype.get(position).getServed().toString());
            JSONObject object_model = new JSONObject(object_plate.getString("model"));
            brand = object_model.getString("brand");

            plate = object_plate.getString("plate");
            province = object_plate.getString("province");
            by = object_served.getString("by");
            time = object_served.getString("time");

            String check = gettype.get(position).getChecked().toString();
            if (check != null && (!TextUtils.equals(check ,"null")) && (!TextUtils.isEmpty(check))){
                Log.d("Tag", "not isEmpty");
//                return; // or break, continue, throw
                holder.image_finish.setImageResource(R.drawable.finish_1x_yellow);
                holder.progress_bar_start.setProgress(1000);
                holder.image_check_status.setImageResource(R.drawable.confirm_2x_re);
                holder.progress_bar_finish.setProgress(500);
            }else{
                holder.image_finish.setImageResource(R.drawable.finish_1x);
                holder.tv_finish.setTextColor(context.getResources().getColor(R.color.white));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Glide.with(context)
                .load(context.getResources().getString(R.string.base_url)+"carLogo/"+brand+".png")
                .error(R.drawable.bmw)
                .into(holder.img_brand);

        holder.tv_plate_number.setText(plate);
        holder.tv_province.setText(province);
        holder.tv_served.setText(context.getString(R.string.on_service_served_by)+"\n"+by);
        holder.tv_rno.setText("#"+gettype.get(position).getRno());

//        Log.d("Tag","convertStringToData : "+convertStringToData(time));
//        Log.d("Tag","TimeCuurent : "+TimeCuurent());

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
