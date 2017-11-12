package com.simplysmart.providesk.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simplysmart.lib.model.categories.v2.SubCategory;
import com.simplysmart.providesk.R;
import com.simplysmart.providesk.activity.AddNewComplaintActivity;
import com.simplysmart.providesk.activity.ComplaintDetailScreenActivity;
import com.simplysmart.providesk.config.AppConstant;
import com.simplysmart.providesk.config.GlobalData;
import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.common.CloudinaryImage;
import com.simplysmart.lib.common.DebugLog;
import com.simplysmart.lib.config.NetworkUtilities;
import com.simplysmart.lib.model.common.CommonResponse;
import com.simplysmart.lib.model.helpdesk.NewComplaint;
import com.simplysmart.lib.model.login.Unit;
import com.simplysmart.lib.request.CreateRequest;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shekhar on 4/8/15.
 */

public class NewComplaintDetailPhone extends BaseFragment {

    private Activity _activity;
    private TextView complaintButton, requestButton;
    private Spinner subCategorySpinner;
    private ImageView imageOne;
    private ArrayList<SubCategory> subCategories;
    private ArrayList<Unit> units;
    private ArrayList<String> imageUrlList = new ArrayList<>();
    private Typeface textTypeface;
    private String category_id, category_name, sub_category_id, unit_id;
    private String complaintType;
    private EditText editComment;
    private View view;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this._activity = activity;
        textTypeface = Typeface.createFromAsset(_activity.getAssets(), AppConstant.FONT_EUROSTILE_REGULAR_MID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_complaint_detail, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeView();
    }

    @Override
    protected int getHeaderColor() {
        return ContextCompat.getColor(getActivity(), R.color.bw_color_red);
    }

    @Override
    protected String getHeaderTitle() {
        return _activity.getString(R.string.title_helpdesk);
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(_activity.getApplicationContext()).registerReceiver(onCaptureImage, new IntentFilter("image_captured"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(_activity.getApplicationContext()).unregisterReceiver(onCaptureImage);
    }

    private void initializeView() {

        complaintType = "Complaint";

        complaintButton = (TextView) _activity.findViewById(R.id.btn_complaint);
        requestButton = (TextView) _activity.findViewById(R.id.btn_request);
        TextView category_logo = (TextView) _activity.findViewById(R.id.category_logo);
        TextView unit_logo = (TextView) _activity.findViewById(R.id.unit_logo);
        TextView buttonImage = (TextView) _activity.findViewById(R.id.btn_img);
        TextView buttonSubmit = (TextView) _activity.findViewById(R.id.btn_submit);
        editComment = (EditText) _activity.findViewById(R.id.edit_comment);

        final Spinner unitSpinner = (Spinner) _activity.findViewById(R.id.unit_spinner);
        subCategorySpinner = (Spinner) _activity.findViewById(R.id.subcategory_spinner);
        imageOne = (ImageView) _activity.findViewById(R.id.img_one);
        ImageView imageTwo = (ImageView) _activity.findViewById(R.id.img_two);
        TextView imageCount = (TextView) _activity.findViewById(R.id.img_count);

        Typeface iconTypeface = Typeface.createFromAsset(_activity.getAssets(), AppConstant.FONT_BOTSWORTH);
        category_logo.setTypeface(iconTypeface);
        unit_logo.setTypeface(iconTypeface);
        buttonImage.setTypeface(iconTypeface);

        category_logo.setText(_activity.getString(R.string.icon_electricity));
        unit_logo.setText(_activity.getString(R.string.icon_myflat));
        buttonImage.setText(_activity.getString(R.string.icon_photo));

        complaintButton.setTypeface(textTypeface);
        requestButton.setTypeface(textTypeface);
        buttonSubmit.setTypeface(textTypeface);
        editComment.setTypeface(textTypeface);

        complaintButton.setOnClickListener(complaintClick);
        requestButton.setOnClickListener(requestClick);
        buttonImage.setOnClickListener(captureImageClick);

        Gson gson = new Gson();

        Bundle bundle = getArguments();
        String params_subcategories = bundle.getString(CreateNewComplaintPhone.KEY_SUBCATEGORIES);
        category_id = bundle.getString(CreateNewComplaintPhone.KEY_CATEGORY_ID);
        category_name = bundle.getString(CreateNewComplaintPhone.KEY_CATEGORY_NAME);

        subCategories = gson.fromJson(params_subcategories, new TypeToken<ArrayList<SubCategory>>() {
        }.getType());

        sub_category_id = (subCategories != null ? subCategories.get(0).getId() : null);

        ArrayAdapter<SubCategory> list = new ArrayAdapter<>(getActivity(), R.layout.custom_item_unit_spinner, subCategories);

        if (subCategories == null) return;

        list.setDropDownViewResource(R.layout.custom_item_unit_spinner);
        subCategorySpinner.setAdapter(list);

        subCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sub_category_id = subCategories.get(subCategorySpinner.getSelectedItemPosition()).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        units = GlobalData.getInstance().getUnits();
        ArrayList<String> unitNames = new ArrayList<>();

        for (Unit unit : units) {
            unitNames.add(unit.getInfo());
        }
        ArrayAdapter<String> listUnit = new ArrayAdapter<>(getActivity(), R.layout.custom_item_unit_spinner, unitNames);
        unitSpinner.setAdapter(listUnit);

        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unit_id = units.get(unitSpinner.getSelectedItemPosition()).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (NetworkUtilities.isInternet(_activity)) {
                    showActivitySpinner();
                    createComplaintRequest();
                } else {
                    showSnackBar(view, _activity.getString(R.string.error_no_internet_connection));
                }
            }
        });
    }

    private void createComplaintRequest() {

        if (!editComment.getText().toString().trim().equalsIgnoreCase("")) {

            NewComplaint complaint = new NewComplaint();
            complaint.setOf_type(complaintType);
            complaint.setCategory_id(category_id);
            complaint.setSub_category_id(sub_category_id);
            complaint.setUnit_id(unit_id);

            if (category_name.equalsIgnoreCase("Others") || complaint.getOf_type().equalsIgnoreCase("Request")) {
                complaint.setSub_category_id(null);
            }

            complaint.setResident_id(GlobalData.getInstance().getResidentId());
            complaint.setDescription(editComment.getText().toString());
            complaint.setAssets(imageUrlList);

            CreateRequest.getInstance().createNewComplaint(complaint, new ApiCallback<CommonResponse>() {
                @Override
                public void onSuccess(CommonResponse response) {
                    dismissActivitySpinner();
                    showSnackBar(view, "Complaint created.");
                    Intent newIntent = new Intent(_activity, ComplaintDetailScreenActivity.class);
                    newIntent.putExtra("complaint_id", response.getId());
                    _activity.startActivity(newIntent);
                    _activity.finish();
                }

                @Override
                public void onFailure(String error) {
                    dismissActivitySpinner();
                    DebugLog.d("Error [" + error + "]");
                    showSnackBar(view, "Failed Complaint creation.");
                }
            });
        } else {
            showSnackBar(view, "Please add description of your complaint.");
        }
    }

    private final View.OnClickListener complaintClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            complaintButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bw_color_red));
            requestButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_gray));
            subCategorySpinner.setVisibility(View.VISIBLE);
            complaintType = "Complaint";
        }
    };

    private final View.OnClickListener requestClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            complaintButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bw_color_dark_gray));
            requestButton.setBackgroundColor(ContextCompat.getColor(_activity, R.color.bw_color_red));
            if (!category_name.equalsIgnoreCase("Others")) {
                subCategorySpinner.setVisibility(View.INVISIBLE);
            }
            complaintType = "Request";
        }
    };

    private final View.OnClickListener captureImageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AddNewComplaintActivity.CustomDialog();
        }
    };

    private final BroadcastReceiver onCaptureImage = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String path = intent.getStringExtra(AddNewComplaintActivity.KEY_EXTRA_DATA);
            DebugLog.i("Capture Image Path" + path);
            imageOne.setVisibility(View.VISIBLE);

            String transformation_params = "w_" + 80 + "," + "h_" + 80 + "," + "c_fit";
            String transformed_path = CloudinaryImage.transform(path, transformation_params);

            DebugLog.i("Transformed Image Path" + transformed_path);
            Picasso.with(getActivity()).load(transformed_path).placeholder(R.drawable.loading_border).error(R.drawable.loading_border).into(imageOne);
            if (!imageUrlList.contains(path)) imageUrlList.add(path);
        }
    };

}
