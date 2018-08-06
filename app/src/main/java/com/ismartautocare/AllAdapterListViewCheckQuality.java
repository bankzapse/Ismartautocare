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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class AllAdapterListViewCheckQuality  extends ArrayAdapter<ItemServicePrice> {

    private Context context;
    int resourceId;
    ArrayList<ItemServicePrice> gettype;
    String size,check_all;
    ImageView image_qc_check,image_check_all;
    ArrayList<Integer> list_int = new ArrayList<>();

    private class ViewHolder{
        TextView ttv_name_services,tv_count;
        ImageView check_services;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public AllAdapterListViewCheckQuality(Context context, int resourceId, ArrayList<ItemServicePrice> gettype,String size_,String check_all,ImageView image_qc_check,ImageView image_check_all) {
        super(context, resourceId, gettype);
        this.context = context;
        this.resourceId = resourceId;
        this.gettype = gettype;
        this.size = size_;
        this.check_all = check_all;
        this.image_qc_check = image_qc_check;
        this.image_check_all = image_check_all;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ItemServicePrice object = getItem(position);
        AllAdapterListViewCheckQuality.ViewHolder holder = null;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
        if(convertView == null){
            convertView = inflater.inflate(resourceId, parent, false);
            holder = new AllAdapterListViewCheckQuality.ViewHolder();

            holder.ttv_name_services = convertView.findViewById(R.id.tv_name_services);
            holder.tv_count = convertView.findViewById(R.id.tv_count);
            holder.check_services = convertView.findViewById(R.id.check_services);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.tv_count.setText(String.valueOf(position+1));
        holder.ttv_name_services.setText(object.desc);


        final FragmentOnServiceDetail fd = new FragmentOnServiceDetail();
        if(check_all.equalsIgnoreCase("All")){
            holder.check_services.setImageResource(R.drawable.check_2x);
            fd.CheckDiableCheck(image_qc_check);
        }else{
            final ViewHolder finalHolder = holder;
            holder.check_services.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list_int.add(position);
                    Log.d("Tag","list_int : "+ list_int);

                    finalHolder.check_services.setImageResource(R.drawable.check_2x);
                    finalHolder.check_services.setClickable(false);
                    if(gettype.size() == list_int.size()){
                        image_check_all.setImageResource(R.drawable.check_all_2x_true);
                        fd.CheckDiableCheck(image_qc_check);
                    }
                }
            });
        }

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
