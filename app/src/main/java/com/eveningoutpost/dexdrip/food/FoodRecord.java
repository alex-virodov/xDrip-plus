package com.eveningoutpost.dexdrip.food;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.eveningoutpost.dexdrip.Models.Treatments;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class FoodRecord {
    private final File imageFile;
    private Bitmap thumbnail = null;
    private final ArrayList<Treatments> treatments = new ArrayList<>();

    private FoodRecord(File imageFile) {
        this.imageFile = imageFile;
    }

    public Bitmap getThumbnail() {
        if (!imageFile.exists()) {
            return null;
        }
        if (thumbnail == null) {
            File thumbnailFile = FoodImageDirectory.getOrGenerateThumbnailForImage(imageFile);
            this.thumbnail = BitmapFactory.decodeFile(thumbnailFile.getAbsolutePath());
        }
        return thumbnail;
    }

    public static void addTreatment(Map<File, FoodRecord> foodRecordMap, Treatments treatment) {
        File imageFile = FoodImageDirectory.getFoodImageFileFromTreatmentNote(treatment.notes);
        if (imageFile != null) {
            FoodRecord foodRecord = foodRecordMap.get(imageFile);
            if (foodRecord == null) {
                foodRecord = new FoodRecord(imageFile);
                foodRecordMap.put(imageFile, foodRecord);
            }
            foodRecord.treatments.add(treatment);
        }
    }

    public ArrayList<Treatments> getTreatments() {
        return treatments;
    }

    public File getImageFile() {
        return imageFile;
    }
}
