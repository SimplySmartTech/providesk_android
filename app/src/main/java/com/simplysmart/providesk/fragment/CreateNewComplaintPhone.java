package com.simplysmart.providesk.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.simplysmart.lib.model.categories.v2.Category;
import com.simplysmart.lib.model.categories.v2.CategoryResponse;
import com.simplysmart.lib.model.categories.v2.SubCategory;
import com.simplysmart.providesk.R;
import com.simplysmart.providesk.custom.CustomButtonView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by shekhar on 4/8/15.
 */
public class CreateNewComplaintPhone extends BaseFragment {

    private Activity _activity;
    private View rootView;
    private HashMap<String, ArrayList<SubCategory>> hashMap;

    public static final String KEY_CATEGORY_NAME = "category_name";
    public static final String KEY_CATEGORY_ID = "category_id";
    public static final String KEY_SUBCATEGORIES = "subcategories";

    private View previousId;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this._activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_complaint_selection, container, false);
        return rootView;
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

    private void initializeView() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                CategoryResponse categoriesResponse;
                SharedPreferences preferences = _activity.getSharedPreferences("CategoryInfo", Context.MODE_PRIVATE);
                String category_string = preferences.getString("category_details", "");
                hashMap = new HashMap<>();
                Gson gson = new Gson();
                int i = 0;

                categoriesResponse = gson.fromJson(category_string, CategoryResponse.class);
                ArrayList<Category> categoryLists = categoriesResponse.getCategories();
                int categories_size = categoryLists.size();
                for (i = 0; i < categories_size; i++) {
                    Category categoryField = categoryLists.get(i);
                    CustomButtonView customButtonView = (CustomButtonView) rootView.findViewWithTag(categoryField.getName());
                    customButtonView.setTag(categoryField.getId());
                    hashMap.put(categoryField.getId(), categoryField.getSub_categories());
                }
            }
        };

        new Thread(runnable).start();
        CustomButtonView buttonElectrical = (CustomButtonView) rootView.findViewById(R.id.btn_electrical);
        CustomButtonView buttonCivilWork = (CustomButtonView) rootView.findViewById(R.id.btn_civilwork);
        CustomButtonView buttonWater = (CustomButtonView) rootView.findViewById(R.id.btn_water);
        CustomButtonView buttonCableTv = (CustomButtonView) rootView.findViewById(R.id.btn_cabletv);
        CustomButtonView buttonCitizen = (CustomButtonView) rootView.findViewById(R.id.btn_citizen_billing);
        CustomButtonView buttonInfra = (CustomButtonView) rootView.findViewById(R.id.btn_infra);
        CustomButtonView buttonIT = (CustomButtonView) rootView.findViewById(R.id.btn_it);
        CustomButtonView buttonOther = (CustomButtonView) rootView.findViewById(R.id.btn_other);

        if (previousId != null) {
            previousId.setSelected(true);
        }
        buttonElectrical.setOnClickListener(buttonClick);
        buttonCivilWork.setOnClickListener(buttonClick);
        buttonWater.setOnClickListener(buttonClick);
        buttonCableTv.setOnClickListener(buttonClick);
        buttonCitizen.setOnClickListener(buttonClick);
        buttonInfra.setOnClickListener(buttonClick);
        buttonIT.setOnClickListener(buttonClick);
        buttonOther.setOnClickListener(buttonClick);
    }

    private final View.OnClickListener buttonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String caption = switchIconSelection(v, previousId);

            String category_id = (String) v.getTag();
            ArrayList<SubCategory> subCategories = hashMap.get(category_id);

            Gson gson = new Gson();
            String params_subcategories = gson.toJson(subCategories);

            Fragment fragment = new NewComplaintDetailPhone();
            Bundle bundle = new Bundle();
            bundle.putString(KEY_CATEGORY_NAME, caption);
            bundle.putString(KEY_CATEGORY_ID, category_id);
            bundle.putString(KEY_SUBCATEGORIES, params_subcategories);
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
        }
    };

    private String switchIconSelection(View selected, View previousId) {

        if (previousId != null) {
            previousId.setSelected(false);
        }
        selected.setSelected(true);
        this.previousId = selected;

        return ((CustomButtonView) selected).getCaption();
    }

}
