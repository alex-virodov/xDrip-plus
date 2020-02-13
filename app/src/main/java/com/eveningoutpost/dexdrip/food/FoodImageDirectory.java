package com.eveningoutpost.dexdrip.food;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FoodImageDirectory {
    private static final File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    private static final File imageDirectory = new File(storageDir, "xDripFood");
    private static final File thumbnailDirectory = new File(imageDirectory, ".thumbnails");

    public static File getOrGenerateThumbnailForImage(File imageFile) {
        File thumbnailFile = new File(thumbnailDirectory, imageFile.getName());
        if (!thumbnailFile.exists()) {
            Bitmap image = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            Bitmap thumbnail = Bitmap.createScaledBitmap(image, 192, 192, true);
            image.recycle();
            thumbnailFile.getParentFile().mkdirs();

            try {
                FileOutputStream out = new FileOutputStream(thumbnailFile);
                thumbnail.compress(Bitmap.CompressFormat.PNG, 85, out);
                out.close();
                thumbnail.recycle();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return thumbnailFile;
    }

    public static @Nullable
    File getFoodImageFileFromTreatmentNote(String note) {
        int firstFileUrl = note.indexOf("file://");
        int firstJpgExt = note.indexOf(".jpg");

        if (firstFileUrl != -1 && firstJpgExt != -1) {
            String fileUrlString = note.substring(firstFileUrl, firstJpgExt + ".jpg".length());
            return new File(Uri.parse(fileUrlString).getPath());
        } else {
            return null;
        }
    }
}
