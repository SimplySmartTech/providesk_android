package com.simplysmart.app.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simplysmart.app.R;
import com.simplysmart.app.config.AppConstant;

/**
 * Created by chandrashekhar on 14/8/15.
 * Custom Button View
 */
public class CustomButtonView extends LinearLayout{

    private TextView iconView;
    private TextView captionView;
    private Typeface iconTypeface;
//    private Typeface textTypeface;


    public CustomButtonView(Context context) {
        super(context);
    }

    public CustomButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context,AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomButtonView, 0, 0);
         String icon,caption;

        int iconPadding;
        int captionPadding;
        int iconTextSize;
        int captionTextSize;
        boolean isSelected;
        try {
            icon = a.getString(R.styleable.CustomButtonView_text_icon);
            caption = a.getString(R.styleable.CustomButtonView_text_caption);
            iconPadding = a.getInteger(R.styleable.CustomButtonView_icon_padding, 0);
            captionPadding = a.getInteger(R.styleable.CustomButtonView_caption_padding, 0);
            iconTextSize = a.getInteger(R.styleable.CustomButtonView_icon_text_size, getResources().getInteger(R.integer.icon_text_size));
            captionTextSize = a.getInteger(R.styleable.CustomButtonView_caption_text_size, getResources().getInteger(R.integer.caption_text_size));
            isSelected =a.getBoolean(R.styleable.CustomButtonView_selected,false);

        } finally {
            a.recycle();
        }

        captionView = new TextView(context);
        iconView = new TextView(context);

        iconView.setTextSize(iconTextSize);
        iconView.setPadding(iconPadding, iconPadding, iconPadding, iconPadding);
        iconTypeface = Typeface.createFromAsset(context.getAssets(), AppConstant.FONT_BOTSWORTH);

        captionView.setTextSize(captionTextSize);
        captionView.setPadding(captionPadding, captionPadding, captionPadding, captionPadding);
//        textTypeface = Typeface.createFromAsset(context.getAssets(), AppConstant.FONT_EUROSTILE_REGULAR_MID);

        setIcon(icon);
        setCaption(caption);

        setSelected(isSelected);

        addView(iconView);
        addView(captionView);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
    }

    public void setIcon(String icon) {
        iconView.setTypeface(iconTypeface);
        iconView.setText(icon);
    }

    public void setSelectedTextColor(int color){
        iconView.setTextColor(color);
        captionView.setTextColor(color);

    }

    public void setCaption(String caption){
//        captionView.setTypeface(textTypeface);
        captionView.setText(caption);
    }

    public String getCaption(){
        return captionView.getText().toString();
    }

    public void setSelected(boolean isSelected){

        if (isSelected){
            setSelectedTextColor(getResources().getColor(R.color.bw_color_red));
            iconView.setBackgroundResource(R.drawable.circle_red_border);
        }else{
            setSelectedTextColor(getResources().getColor(R.color.bw_color_black));
            iconView.setBackgroundResource(R.drawable.circle_black_border);
        }
    }

}
