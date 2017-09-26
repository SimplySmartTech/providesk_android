package com.simplysmart.app.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.simplysmart.lib.callback.ApiCallback;
import com.simplysmart.lib.model.categories.CategoriesResponse;
import com.simplysmart.lib.model.categories.v2.CategoryResponse;
import com.simplysmart.lib.request.CreateRequest;

/**
 * Created by shekhar on 10/28/15.
 */
public class FetchCategories extends IntentService {

    public FetchCategories() {
        super(FetchCategories.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        fetchCategories();
        fetchCategoriesV2();
    }

    private void fetchCategories() {

        CreateRequest.getInstance().fetchCategories(new ApiCallback<CategoriesResponse>() {
            @Override
            public void onSuccess(CategoriesResponse response) {

                Gson gson = new Gson();
                String data = gson.toJson(response);
                SharedPreferences categoryInfo = getSharedPreferences("CategoryInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor preferencesEditor = categoryInfo.edit();
                preferencesEditor.putString("category_details", data);
                preferencesEditor.apply();
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    private void fetchCategoriesV2() {

        CreateRequest.getInstance().fetchCategoriesV2(new ApiCallback<CategoryResponse>() {
            @Override
            public void onSuccess(CategoryResponse response) {

                Gson gson = new Gson();
                String data = gson.toJson(response);
                SharedPreferences categoryInfo = getSharedPreferences("CategoryInfoV2", Context.MODE_PRIVATE);
                SharedPreferences.Editor preferencesEditor = categoryInfo.edit();
                preferencesEditor.putString("category_details_v2", data);
                preferencesEditor.apply();
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }
}
