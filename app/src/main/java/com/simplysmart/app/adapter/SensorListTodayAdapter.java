package com.simplysmart.app.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.simplysmart.app.R;
import com.simplysmart.app.config.AppConstant;
import com.simplysmart.lib.model.sensor.SensorItem;
import com.simplysmart.lib.model.sensor.SensorList;

/**
 * Created by shailendrapsp on 6/12/16.
 */

public class SensorListTodayAdapter extends BaseAdapter {

    private SensorList sensorList;
    private Context mContext;
    private FragmentManager fragmentManager;
    private Typeface textTypeface;

    public SensorListTodayAdapter(SensorList sensorList, Context mContext, FragmentManager fragmentManager) {
        this.sensorList = sensorList;
        this.mContext = mContext;
        this.fragmentManager = fragmentManager;
        if (mContext == null) return;
        textTypeface = Typeface.createFromAsset(mContext.getAssets(), AppConstant.FONT_EUROSTILE_REGULAR_MID);
    }

    @Override
    public int getCount() {
        return sensorList.getData().size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final SensorItem sensorItem = sensorList.getData().get(position);
        String date = "";
        String unicode = "";
        Holder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sensor_list, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        if (sensorItem.getSensor_name() != null && !sensorItem.getSensor_name().equals("")) {
            holder.sensorName.setText(sensorItem.getSensor_name());
        } else {
            holder.sensorName.setText("-");
        }

        if (sensorItem.isActive()) {
            holder.sensorStatus.setText("Active");
            holder.sensorStatus.setTextColor(mContext.getResources().getColor(R.color.bw_color_green));
        } else if (sensorItem.isInactive()) {
            holder.sensorStatus.setText("Inactive");
            holder.sensorStatus.setTextColor(mContext.getResources().getColor(R.color.bw_color_yellow));
        } else if (sensorItem.isUnreachable()) {
            holder.sensorStatus.setText("Unreachable");
            holder.sensorStatus.setTextColor(mContext.getResources().getColor(R.color.bw_color_red));
        }

        if (sensorItem.getCumulative_today() != null && !sensorItem.getCumulative_today().equals("")) {
            if (sensorItem.getUnit() != null) {
                if (sensorItem.getUnit().contains(";")) {
                    unicode = "&#x" + sensorItem.getUnit();
                    holder.cumulativeValue.setText(sensorItem.getCumulative_today() + " " + Html.fromHtml(unicode));
                } else {
                    holder.cumulativeValue.setText(sensorItem.getCumulative_today() + " " + sensorItem.getUnit());
                }
            } else {
                holder.cumulativeValue.setText(sensorItem.getCumulative_today());
            }
        } else {
            holder.cumulativeValue.setText("----");
        }

//        holder.sensorName.setTypeface(textTypeface);
//        holder.cumulativeValue.setTypeface(textTypeface);

        return convertView;
    }

    private class Holder {
        TextView sensorName, cumulativeValue;
        TextView sensorStatus;

        Holder(View itemView) {
            this.sensorName = (TextView) itemView.findViewById(R.id.sensorName);
            this.sensorStatus = (TextView) itemView.findViewById(R.id.sensorStatus);
            this.cumulativeValue = (TextView) itemView.findViewById(R.id.cumulativeValue);
        }
    }

    public SensorList getSensorListData() {
        return sensorList;
    }
}
