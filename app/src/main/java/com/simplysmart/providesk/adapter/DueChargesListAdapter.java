package com.simplysmart.providesk.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.simplysmart.providesk.R;
import com.simplysmart.lib.model.bill.DueCharge;

import java.util.ArrayList;

public class DueChargesListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<DueCharge> dueCharges;

    public DueChargesListAdapter(Context context, ArrayList<DueCharge> values) {
        this.mContext = context;
        this.dueCharges = values;
    }

    @Override
    public int getCount() {
        return dueCharges.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.custom_list_row_bill_due_charges, parent, false);

            holder = new Holder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.value = (TextView) convertView.findViewById(R.id.value);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.name.setText(dueCharges.get(position).getName());
        holder.value.setText(dueCharges.get(position).getDue_amount());

        return convertView;
    }

    private static class Holder {
        private TextView name;
        private TextView value;
    }
}
