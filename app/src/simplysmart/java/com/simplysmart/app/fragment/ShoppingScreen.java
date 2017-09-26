package com.simplysmart.app.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simplysmart.app.R;
import com.simplysmart.app.model.ItemData;
import com.simplysmart.app.adapter.ShoppingScreenAdapter;

import java.util.ArrayList;

/**
 * Created by shekhar on 23/03/17.
 */
public class ShoppingScreen extends BaseFragment {


    private ArrayList<ItemData> res;
    private View rootView;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_shopping_screen, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        ShoppingScreenAdapter adapter = new ShoppingScreenAdapter(getItems(), getActivity());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public int getHeaderColor() {
        return ContextCompat.getColor(_activity, R.color.bw_color_yellow);
    }

    @Override
    public String getHeaderTitle() {
        return "Shopping";
    }

    public ArrayList<ItemData> getItems() {
        res = new ArrayList<>();
        ItemData item = new ItemData(R.drawable.ic_grocery, "Grocery", null);
        res.add(item);
        item = new ItemData(R.drawable.ic_milk, "Milk/Newspaper", null);
        res.add(item);
        item = new ItemData(R.drawable.ic_medicine, "Medicines", null);
        res.add(item);
        item = new ItemData(R.drawable.ic_handyman, "Plumber/Electrician", null);
        res.add(item);
        item = new ItemData(R.drawable.ic_internet, "Internet Service", null);
        res.add(item);
        item = new ItemData(R.drawable.ic_dth, "DTH Service", null);
        res.add(item);

        return res;
    }

}

