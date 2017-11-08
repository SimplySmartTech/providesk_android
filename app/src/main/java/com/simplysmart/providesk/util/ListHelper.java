package com.simplysmart.providesk.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by shailendrapsp on 14/9/16.
 */
public class ListHelper {
    public static void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {

            return;
        }
        // set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        // setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
    }

    public static void getCustomListSize(ListView listView,int pixels){
        ListAdapter adapter = listView.getAdapter();
        if(adapter == null){
            return;
        }
        View listItem;
        int totalHeight = 0;

        for(int i=0;i<adapter.getCount();i++){
            listItem = adapter.getView(i,null,listView);
            listItem.measure(0,0);
            totalHeight+=(listItem.getMeasuredHeight()+pixels);
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
    }

}
