package com.simplysmart.providesk.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.simplysmart.providesk.R;
import com.simplysmart.providesk.config.AppConstant;
import com.simplysmart.lib.model.walllet.Transaction;

import java.util.ArrayList;

public class TransactionListAdapter extends BaseAdapter {

    private final Context mContext;
    private ArrayList<Transaction> transactions;

    public TransactionListAdapter(Activity activity, ArrayList<Transaction> transactions) {
        this.mContext = activity;
        this.transactions = transactions;
    }

    @Override
    public int getCount() {
        return transactions.size();
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
            convertView = inflater.inflate(R.layout.custom_list_row_tranaction, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.transactionLogo = (TextView) convertView.findViewById(R.id.transactionLogo);
            viewHolder.transactionNote = (TextView) convertView.findViewById(R.id.transactionNote);
            viewHolder.transactionNumber = (TextView) convertView.findViewById(R.id.transactionNumber);
            viewHolder.transactionAmount = (TextView) convertView.findViewById(R.id.transactionAmount);
            viewHolder.transactionDate = (TextView) convertView.findViewById(R.id.transactionDate);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Typeface iconTypeface = Typeface.createFromAsset(mContext.getAssets(), AppConstant.FONT_BOTSWORTH);
        viewHolder.transactionLogo.setTypeface(iconTypeface);

        if (transactions.get(position).getNote() != null) {
            viewHolder.transactionNote.setText(transactions.get(position).getNote());
        } else if (transactions.get(position).getComment() != null) {
            viewHolder.transactionNote.setText(transactions.get(position).getComment());
        } else {
            viewHolder.transactionNote.setText("---");
        }

        if (transactions.get(position).getNet_amount() != null) {
            if (transactions.get(position).getTransaction_type().equalsIgnoreCase("credit")) {
                viewHolder.transactionAmount.setText("+ " + mContext.getResources().getString(R.string.Rs) + " " + transactions.get(position).getNet_amount());
                viewHolder.transactionAmount.setTextColor(mContext.getResources().getColor(R.color.bw_color_green));
            } else {
                viewHolder.transactionAmount.setText("- " + mContext.getResources().getString(R.string.Rs) + " " + transactions.get(position).getNet_amount());
                viewHolder.transactionAmount.setTextColor(mContext.getResources().getColor(R.color.bw_color_orange));
            }
        } else {
            viewHolder.transactionAmount.setText("");
        }

//        viewHolder.transactionAmount.setText(mContext.getResources().getString(R.string.Rs) + " " + transactions.get(position).getNet_amount());

        if (transactions.get(position).getTransaction_number() != null) {
            viewHolder.transactionNumber.setText("Ref. Number : " + transactions.get(position).getTransaction_number());
        } else {
            viewHolder.transactionNumber.setText("---");
        }

        if (transactions.get(position).getTransaction_at() != null) {
            viewHolder.transactionDate.setText(transactions.get(position).getTransaction_at());
        } else {
            viewHolder.transactionDate.setText("---");
        }

        return convertView;
    }

    static class ViewHolder {
        TextView transactionLogo, transactionAmount, transactionNumber, transactionNote, transactionDate;
    }

}