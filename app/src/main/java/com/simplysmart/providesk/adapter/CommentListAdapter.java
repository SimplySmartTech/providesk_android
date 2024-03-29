package com.simplysmart.providesk.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simplysmart.providesk.R;
import com.simplysmart.providesk.config.GlobalData;
import com.simplysmart.providesk.util.ParseDateFormat;
import com.simplysmart.lib.model.helpdesk.ComplaintChat;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ComplaintChat> complaintChats;

    public CommentListAdapter(Context context, ArrayList<ComplaintChat> complaintChats) {
        this.mContext = context;
        this.complaintChats = complaintChats;
    }

    @Override
    public int getCount() {
        return complaintChats.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.custom_list_row_complaint_comment, parent, false);

            viewHolder.botsworth_chat_bubble = (RelativeLayout) convertView.findViewById(R.id.botsworth_chat_bubble);
            viewHolder.resident_chat_bubble = (RelativeLayout) convertView.findViewById(R.id.resident_chat_bubble);
            viewHolder.system_chat_bubble = (RelativeLayout) convertView.findViewById(R.id.system_chat_bubble);

            viewHolder.botsworthComment = (TextView) convertView.findViewById(R.id.txt_comment_botswoth);
            viewHolder.botsworthTime = (TextView) convertView.findViewById(R.id.txt_time_botswoth);

            viewHolder.residentComment = (TextView) convertView.findViewById(R.id.txt_comment_resident);
            viewHolder.residentTime = (TextView) convertView.findViewById(R.id.txt_time_resident);

            viewHolder.systemComment = (TextView) convertView.findViewById(R.id.systemComment);
            viewHolder.systemTime = (TextView) convertView.findViewById(R.id.systemTime);

            viewHolder.activityImage = (ImageView) convertView.findViewById(R.id.activityImage);
            viewHolder.activityImageResident = (ImageView) convertView.findViewById(R.id.activityImageResident);

            viewHolder.loadingImage = (LinearLayout) convertView.findViewById(R.id.loadingImage);
            viewHolder.loadingImageResident = (LinearLayout) convertView.findViewById(R.id.loadingImageResident);

            viewHolder.inputPhoto = (CircleImageView) convertView.findViewById(R.id.inputPhoto);
            viewHolder.nameOther = (TextView) convertView.findViewById(R.id.nameOther);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.activityImage.setTag(position);
        viewHolder.activityImageResident.setTag(position);

        if (complaintChats.get(position).getResource() == null) {

            viewHolder.system_chat_bubble.setVisibility(View.VISIBLE);
            viewHolder.resident_chat_bubble.setVisibility(View.GONE);
            viewHolder.botsworth_chat_bubble.setVisibility(View.GONE);

            if (complaintChats.get(position).getText() != null) {
                viewHolder.systemComment.setText(Html.fromHtml(complaintChats.get(position).getText()));
            } else {
                viewHolder.systemComment.setText("----");
            }

            try {
                viewHolder.systemTime.setText(ParseDateFormat.dateFormat(mContext, complaintChats.get(position).getCreated_at()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else if (complaintChats.get(position).getResource().getId().equalsIgnoreCase(GlobalData.getInstance().getResidentId())) {

            viewHolder.resident_chat_bubble.setVisibility(View.VISIBLE);
            viewHolder.botsworth_chat_bubble.setVisibility(View.GONE);
            viewHolder.system_chat_bubble.setVisibility(View.GONE);

            if (complaintChats.get(position).getText() != null) {
                viewHolder.residentComment.setText(Html.fromHtml(complaintChats.get(position).getText()));
            } else {
                viewHolder.residentComment.setText("----");
            }

            try {
                viewHolder.residentTime.setText(ParseDateFormat.dateFormat(mContext, complaintChats.get(position).getCreated_at()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (complaintChats.get(position).getImage_url() != null && !complaintChats.get(position).getImage_url().equalsIgnoreCase("")) {
                viewHolder.activityImageResident.setVisibility(View.VISIBLE);
                viewHolder.loadingImageResident.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(complaintChats.get(position).getImage_url())
                        .error(R.drawable.loading_border).into(viewHolder.activityImageResident);
            } else {
                viewHolder.activityImageResident.setVisibility(View.GONE);
                viewHolder.loadingImageResident.setVisibility(View.GONE);
            }

        } else {

            viewHolder.botsworth_chat_bubble.setVisibility(View.VISIBLE);
            viewHolder.resident_chat_bubble.setVisibility(View.GONE);
            viewHolder.system_chat_bubble.setVisibility(View.GONE);

            if (complaintChats.get(position).getText() != null) {
                viewHolder.botsworthComment.setText(Html.fromHtml(complaintChats.get(position).getText()));
            } else {
                viewHolder.botsworthComment.setText("----");
            }
            try {
                viewHolder.botsworthTime.setText(ParseDateFormat.dateFormat(mContext, complaintChats.get(position).getCreated_at()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (complaintChats.get(position).getImage_url() != null && !complaintChats.get(position).getImage_url().equalsIgnoreCase("")) {
                viewHolder.activityImage.setVisibility(View.VISIBLE);
                viewHolder.loadingImage.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(complaintChats.get(position).getImage_url())
                        .error(R.drawable.loading_border).into(viewHolder.activityImage);
            } else {
                viewHolder.activityImage.setVisibility(View.GONE);
                viewHolder.loadingImage.setVisibility(View.GONE);
            }

            viewHolder.nameOther.setText(complaintChats.get(position).getResource().getName());

            if (complaintChats.get(position).getResource().getProfile_photo_url() != null
                    && !complaintChats.get(position).getResource().getProfile_photo_url().equalsIgnoreCase("")) {
                Picasso.with(mContext)
                        .load(complaintChats.get(position).getResource().getProfile_photo_url())
                        .placeholder(R.drawable.default_avatar_user)
                        .error(R.drawable.default_avatar_user)
                        .into(viewHolder.inputPhoto);
            } else {
                Picasso.with(mContext)
                        .load(R.drawable.default_avatar_user)
                        .placeholder(R.drawable.default_avatar_user)
                        .error(R.drawable.default_avatar_user)
                        .into(viewHolder.inputPhoto);
            }
        }

        return convertView;
    }

    private static class ViewHolder {
        private RelativeLayout botsworth_chat_bubble, resident_chat_bubble, system_chat_bubble;
        private TextView botsworthComment, residentComment, systemComment;
        private TextView botsworthTime, residentTime, systemTime;

        private ImageView activityImage, activityImageResident;
        private LinearLayout loadingImageResident, loadingImage;

        private TextView nameOther;
        private CircleImageView inputPhoto;

    }

    public void addData(ArrayList<ComplaintChat> data) {
        complaintChats.addAll(data);
    }

    public ArrayList<ComplaintChat> getData() {
        return complaintChats;
    }

}