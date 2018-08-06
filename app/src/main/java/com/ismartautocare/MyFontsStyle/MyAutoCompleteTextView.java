package com.ismartautocare.MyFontsStyle;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * Created by AndroidDeveloper on 10/17/16 AD.
 */
public class MyAutoCompleteTextView extends AutoCompleteTextView {
    public MyAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyAutoCompleteTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
//        this.setTextSize(25);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/ThaiSansNeue-Black.otf");
        setTypeface(tf);
    }

}

