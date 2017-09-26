package com.simplysmart.app.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.simplysmart.app.R;
import com.simplysmart.app.config.AppConstant;
import com.simplysmart.app.model.Items;

import java.util.ArrayList;


public class DrawerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Items> navDrawerItems;
    private Typeface textTypeface;

    public DrawerAdapter(Context context, ArrayList<Items> navDrawerItems) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
        textTypeface = Typeface.createFromAsset(context.getAssets(), AppConstant.FONT_EUROSTILE_REGULAR_MID);
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.side_navigation__list_item, parent, false);
        }

        TextView image = (TextView) convertView.findViewById(R.id.item_icon);
        TextView text = (TextView) convertView.findViewById(R.id.item_text);

        Typeface iconTypeface = Typeface.createFromAsset(context.getAssets(), AppConstant.FONT_BOTSWORTH);
        image.setTypeface(iconTypeface);

        image.setText(Html.fromHtml(navDrawerItems.get(position).getIcon()));
        text.setText(navDrawerItems.get(position).getTitle());

        text.setTypeface(textTypeface);

        if (position == 6) {
            image.setTextSize(35);
        }

        // set font to layout widgets
//		Typeface typeFace = CustomFontsLoader.getTypeface(context, 1);
//		if (typeFace != null) {
//			text.setTypeface(typeFace);
//		}

        return convertView;
    }
}
