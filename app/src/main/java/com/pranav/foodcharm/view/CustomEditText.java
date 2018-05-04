package com.pranav.foodcharm.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.pranav.foodcharm.R;

/**
 * Created by wedigtech 32 on 10/21/2015.
 */
public class CustomEditText extends EditText {

    public CustomEditText(Context context) {
        super(context);
        init(null);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);

            String fontName = a.getString(R.styleable.CustomTextView_fontName);

            Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
            setTypeface(myTypeface);

            a.recycle();

        }
    }

   /* @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
           *//* ((Activity)getContext()).finish();*//*
            Utilities.hideKeyboard(getContext());
            ((Activity)getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }*/
  /* @Override
   public boolean onKeyPreIme(int keyCode, KeyEvent event)
   {
       if(keyCode == KeyEvent.KEYCODE_BACK)
       {
           clearFocus();
       }
       return super.onKeyPreIme(keyCode, event);
   }*/
}
