package com.ismartautocare.MyFontsStyle;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * Created by AndroidDeveloper on 9/27/16 AD.
 */
public class MyCheckboxFonts extends CheckBox {


    public MyCheckboxFonts(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyCheckboxFonts(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyCheckboxFonts(Context context) {
        super(context);
        init();

    }

    private void init() {
//        this.setTextSize(25);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/ThaiSansNeue-Black.otf");
        setTypeface(tf);
    }

}
