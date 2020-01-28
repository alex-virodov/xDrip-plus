package com.eveningoutpost.dexdrip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.eveningoutpost.dexdrip.Models.DateUtil;
import com.eveningoutpost.dexdrip.Models.Treatments;
import com.eveningoutpost.dexdrip.UtilityModels.UndoRedo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FoodPhotoActivity extends BaseAppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    File pendingImageFile = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (pendingImageFile == null) {
            // TODO: need runtime permission!
            pendingImageFile = createImageFile();
            Log.d("xxav", "pendingImageFile=" + pendingImageFile);
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                Uri photoURI = Uri.fromFile(pendingImageFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

        Log.d("xxav", "onResume intent = " + getIntent());
        if (getIntent() != null) {
            Log.d("xxav", "onResume intent action = " + getIntent().getAction());
            Log.d("xxav", "onResume intent extra = " + getIntent().getExtras());
            if (getIntent().getExtras() != null) {
                Log.d("xxav", "onResume intent testextra = " + getIntent().getExtras().getString("testextra"));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("xxav", "onActivityResult intent = " + getIntent());
        Log.d("xxav", "onActivityResult data = " + data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Treatments.create_note("Food photo " + Uri.fromFile(pendingImageFile), /*timestamp=*/0, /*position=*/-1);

            // TODO: Doesn't seem to work - not in gallery on emulator.
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(pendingImageFile);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);

            Intent intent = new Intent(this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private File createImageFile() {
        try {
            // TODO: Need runtime permission!
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "xDripFood_" + timeStamp + "_";
            // TODO: Deprecated on Q
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            return image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
