package com.simplysmart.providesk.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.simplysmart.providesk.R;
import com.simplysmart.providesk.util.ParseDateFormat;
import com.simplysmart.lib.model.notification.Notification;

import java.util.ArrayList;

public class NotificationListAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Notification> notificationLists;

    public NotificationListAdapter(Context context, ArrayList<Notification> notificationLists) {
        this.mContext = context;
        this.notificationLists = notificationLists;
    }

    @Override
    public int getCount() {
        return (notificationLists.size());
    }

    @Override
    public Object getItem(int arg0) {
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.custom_list_row_notification_child, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.notificationSubject = (TextView) convertView.findViewById(R.id.notificationSubject);
            viewHolder.notificationDesc = (TextView) convertView.findViewById(R.id.notificationDesc);
            viewHolder.notificationTime = (TextView) convertView.findViewById(R.id.notificationTime);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        try {
            viewHolder.notificationTime.setText(ParseDateFormat.dateFormat(mContext, notificationLists.get(position).getCreated_at().trim()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (notificationLists.get(position).getSub_category() != null && !notificationLists.get(position).getSub_category().equalsIgnoreCase("")) {
            viewHolder.notificationSubject.setText(notificationLists.get(position).getSub_category());
        } else {
            viewHolder.notificationSubject.setText(notificationLists.get(position).getCategory());
        }

        viewHolder.notificationDesc.setText(notificationLists.get(position).getSubject());

        if (notificationLists.get(position).isUnread()) {
            viewHolder.notificationSubject.setTypeface(null, Typeface.BOLD);
            viewHolder.notificationDesc.setTypeface(null, Typeface.BOLD);
            viewHolder.notificationTime.setTypeface(null, Typeface.BOLD);

            viewHolder.notificationSubject.setTextColor(ContextCompat.getColor(mContext, R.color.bw_color_black));
            viewHolder.notificationDesc.setTextColor(ContextCompat.getColor(mContext, R.color.bw_color_dark_gray));
            viewHolder.notificationTime.setTextColor(ContextCompat.getColor(mContext, R.color.bw_color_dark_gray));

        } else {
            viewHolder.notificationSubject.setTypeface(null, Typeface.NORMAL);
            viewHolder.notificationDesc.setTypeface(null, Typeface.NORMAL);
            viewHolder.notificationTime.setTypeface(null, Typeface.NORMAL);

            viewHolder.notificationSubject.setTextColor(ContextCompat.getColor(mContext, R.color.bw_color_black));
            viewHolder.notificationDesc.setTextColor(ContextCompat.getColor(mContext, R.color.bw_color_dark_gray));
            viewHolder.notificationTime.setTextColor(ContextCompat.getColor(mContext, R.color.bw_color_dark_gray));
        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView notificationSubject, notificationDesc, notificationTime;
    }
}