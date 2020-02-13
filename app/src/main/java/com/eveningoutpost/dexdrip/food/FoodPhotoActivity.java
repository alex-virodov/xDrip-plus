package com.eveningoutpost.dexdrip.food;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.eveningoutpost.dexdrip.BaseAppCompatActivity;
import com.eveningoutpost.dexdrip.Home;
import com.eveningoutpost.dexdrip.Models.Treatments;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FoodPhotoActivity extends BaseAppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final PermissionHelper.Permission[] PERMISSIONS_REQUIRED = {
            PermissionHelper.Permission.WRITE_EXTERNAL_STORAGE,
            PermissionHelper.Permission.CAMERA
    };

    File pendingImageFile = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (pendingImageFile == null) {
            if (!PermissionHelper.hasAllPermissions(this, PERMISSIONS_REQUIRED)) {
                if (PermissionHelper.shouldShowRationale(this, PERMISSIONS_REQUIRED)) {
                    Toast.makeText(this,
                            "Camera and External Storage permissions are needed to run this functionality",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                // After permission request completes, the activity will resume again.
                PermissionHelper.requestPermissions(this, PERMISSIONS_REQUIRED);
                return;
            }

            pendingImageFile = createImageFile();
            Log.d("xxav", "pendingImageFile=" + pendingImageFile);
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                Uri photoURI = Uri.fromFile(pendingImageFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("xxav", "onActivityResult intent = " + getIntent());
        Log.d("xxav", "onActivityResult data = " + data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            long timestamp = 0;
            if (getIntent().hasExtra("timestamp")) {
                timestamp = getIntent().getExtras().getLong("timestamp");
            }

            Treatments.create_note("Food photo " + Uri.fromFile(pendingImageFile), timestamp, /*position=*/-1);

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(pendingImageFile);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
        }
        Intent intent = new Intent(this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private File createImageFile() {
        try {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "xDripFood_" + timeStamp + "_";
            // TODO: Deprecated on Q. Works because targetsdk is 23 now.
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File photoDir = new File(storageDir, "xDripFood");
            if (!photoDir.exists()) {
                photoDir.mkdirs();
            }
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    photoDir      /* directory */
            );
            return image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
