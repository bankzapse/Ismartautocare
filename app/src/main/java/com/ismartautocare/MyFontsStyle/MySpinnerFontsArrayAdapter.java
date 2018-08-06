package com.ismartautocare.MyFontsStyle;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by AndroidDeveloper on 9/27/16 AD.
 */
public class MySpinnerFontsArrayAdapter extends ArrayAdapter {

    public MySpinnerFontsArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public TextView getView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/ThaiSansNeue-Black.otf");
        v.setTypeface(tf);
        return v;
    }

    public TextView getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/ThaiSansNeue-Black.otf");
        v.setTypeface(tf);
        return v;
    }

}
