package com.simplysmart.app.fragment;

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
import com.simplysmart.app.R;
import com.simplysmart.app.custom.CustomButtonView;
import com.simplysmart.lib.model.categories.CategoriesResponse;
import com.simplysmart.lib.model.categories.CategoryField;
import com.simplysmart.lib.model.categories.SubCategories;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by shekhar on 4/8/15.
 */
public class CreateNewComplaintPhone extends BaseFragment {

    private Activity _activity;
    private View rootView;
    private HashMap<String, ArrayList<SubCategories>> hashMap;

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
        return ContextCompat.getColor(getActivity(), R.color.bg_color);
    }

    @Override
    protected String getHeaderTitle() {
        return _activity.getString(R.string.title_helpdesk);
    }

    private void initializeView() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                CategoriesResponse categoriesResponse;
                SharedPreferences preferences = _activity.getSharedPreferences("CategoryInfo", Context.MODE_PRIVATE);
                String category_string = preferences.getString("category_details", "");
                hashMap = new HashMap<>();
                Gson gson = new Gson();
                int i = 0;

                categoriesResponse = gson.fromJson(category_string, CategoriesResponse.class);
                ArrayList<CategoryField> categoryLists = categoriesResponse.getData().getComplaint();
                int categories_size = categoryLists.size();
                for (i = 0; i < categories_size; i++) {
                    CategoryField categoryField = categoryLists.get(i);
                    CustomButtonView customButtonView = (CustomButtonView) rootView.findViewWithTag(categoryField.getName());
                    customButtonView.setTag(categoryField.getId());
                    hashMap.put(categoryField.getId(), categoryField.getSub_categories());
                }
            }
        };

        new Thread(runnable).start();
        CustomButtonView buttonElectrical = (CustomButtonView) rootView.findViewById(R.id.btn_electrical);
        CustomButtonView buttonWater = (CustomButtonView) rootView.findViewById(R.id.btn_water);
        CustomButtonView buttonOther = (CustomButtonView) rootView.findViewById(R.id.btn_other);

        if (previousId != null) {
            previousId.setSelected(true);
        }
        buttonElectrical.setOnClickListener(buttonClick);
        buttonWater.setOnClickListener(buttonClick);
        buttonOther.setOnClickListener(buttonClick);
    }

    private final View.OnClickListener buttonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String caption = switchIconSelection(v, previousId);

            String category_id = (String) v.getTag();
            ArrayList<SubCategories> subCategories = hashMap.get(category_id);

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
