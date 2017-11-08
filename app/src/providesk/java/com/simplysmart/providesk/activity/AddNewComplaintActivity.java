package com.simplysmart.providesk.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.simplysmart.providesk.R;
import com.simplysmart.providesk.aws.AWSConstants;
import com.simplysmart.providesk.aws.Util;
import com.simplysmart.providesk.fragment.CreateNewComplaintPhone;
import com.simplysmart.lib.common.DebugLog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by shekhar on 4/8/15.
 * This AddNewComplaintActivity offer create new complaint functionality.
 */
public class AddNewComplaintActivity extends BaseActivity {

    private static final int REQUEST_TAKE_PHOTO = 1;
    public final static int REQUEST_GALLARY_PHOTO = 2;
    private static final String INTENT_IMAGE_CAPTURED = "image_captured";
    public static final String KEY_EXTRA_DATA = "image_data";
    private static Context context;
    private static String mCurrentPhotoPath;

    private TransferUtility transferUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_complaint);

        context = AddNewComplaintActivity.this;

        transferUtility = Util.getTransferUtility(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Help desk");

        Fragment fragment = new CreateNewComplaintPhone();
        getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

    @Override
    protected int getStatusBarColor() {
        return 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportActionBar().show();
                    getFragmentManager().popBackStack();
                } else {
                    super.onBackPressed();
                }
        }
        return true;
    }

    public static void CustomDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_custom_gallery);
        dialog.setTitle(context.getResources().getString(R.string.txt_capture_image_selection));

        LinearLayout lLayoutCameraDialog = (LinearLayout) dialog.findViewById(R.id.lLayoutCameraDialog);
        LinearLayout lLayoutGalleryDialog = (LinearLayout) dialog.findViewById(R.id.lLayoutGalleryDialog);
        LinearLayout lLayoutRemoveDialog = (LinearLayout) dialog.findViewById(R.id.lLayoutRemoveDialog);

        lLayoutCameraDialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
                dialog.dismiss();
            }
        });

        lLayoutGalleryDialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                ((Activity) context).startActivityForResult(pickPhoto, REQUEST_GALLARY_PHOTO);
                dialog.dismiss();
            }
        });

        lLayoutRemoveDialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private static void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                ((Activity) context).startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setPic(mCurrentPhotoPath);

        } else if (requestCode == REQUEST_GALLARY_PHOTO && resultCode == RESULT_OK) {
            Uri photoUri = data.getData();
            if (photoUri != null) {
                try {
                    //We get the file path from the media info returned by the content resolver
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(photoUri, filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = 0;
                        columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String filePath = null;
                        filePath = cursor.getString(columnIndex);
                        cursor.close();

                        setPic(filePath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void setPic(final String photoPath) {
        beginUpload(compressImage(photoPath));
    }

    private void beginUpload(final String photoPath) {

        DebugLog.d("Local photo url:" + photoPath);
        String filePath = photoPath;
        try {
            try {
                File file = new File(filePath);
                TransferObserver observer = transferUtility.upload(
                        AWSConstants.BUCKET_NAME,
                        AWSConstants.PATH_FOLDER + file.getName(),
                        file, CannedAccessControlList.PublicRead);

                showActivitySpinner();
                observer.setTransferListener(new UploadListener(file.getName()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String message = (String) msg.obj;

            Intent i = new Intent(INTENT_IMAGE_CAPTURED);
            i.putExtra(KEY_EXTRA_DATA, message);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
        }
    };

    private class UploadListener implements TransferListener {

        private String fileName;

        UploadListener(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public void onError(int id, Exception e) {
            Log.e("AAA", "Error during upload: " + id, e);
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d("AAA", String.format("onProgressChanged: %d, total: %d, current: %d", id, bytesTotal, bytesCurrent));
        }

        @Override
        public void onStateChanged(int id, TransferState newState) {
            Log.d("AAA", "onStateChanged: " + id + ", " + newState);

            if (newState == TransferState.COMPLETED) {

                dismissActivitySpinner();
                String url = AWSConstants.S3_URL + AWSConstants.BUCKET_NAME + "/" + AWSConstants.PATH_FOLDER + fileName;
                DebugLog.d("URL :::: " + url);

                Message msg = Message.obtain();
                msg.obj = url;
                msg.setTarget(handler);
                msg.sendToTarget();
            }
        }
    }

}
