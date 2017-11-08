package com.simplysmart.providesk.activity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.simplysmart.providesk.R;
import com.simplysmart.providesk.custom.TouchImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by shailendrapsp on 30/11/16.
 */

public class ImageViewActivity extends BaseActivity {

    private TouchImageView touchImageView;
    private String mPreviousPhotoPath;
    private TextView errorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_view);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (getIntent() != null && getIntent().getExtras() != null) {
            mPreviousPhotoPath = getIntent().getStringExtra("photoPath");
        }

        touchImageView = (TouchImageView) findViewById(R.id.viewImage);
        errorLayout = (TextView) findViewById(R.id.errorLayout);

        setPic(touchImageView, mPreviousPhotoPath);
    }

    @Override
    protected int getStatusBarColor() {
        return 0;
    }

    private void setPic(ImageView view, String imageUrl) {
        Picasso.with(ImageViewActivity.this).load(imageUrl)
                .placeholder(R.drawable.loading_border)
                .noFade()
                .fit().centerInside()
                .error(R.drawable.loading_border).into(view);
    }
}
