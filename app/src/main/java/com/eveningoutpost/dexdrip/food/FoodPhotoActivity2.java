package com.eveningoutpost.dexdrip.food;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.eveningoutpost.dexdrip.BaseAppCompatActivity;
import com.eveningoutpost.dexdrip.R;
import com.eveningoutpost.dexdrip.food.FoodDatesFragment;
import com.eveningoutpost.dexdrip.food.FoodListFragment;
import com.eveningoutpost.dexdrip.food.FoodRecord;
import com.eveningoutpost.dexdrip.food.Treatment;
import com.eveningoutpost.dexdrip.xdrip;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class FoodPhotoActivity2 extends BaseAppCompatActivity {
    FoodListFragment foodListFragment;
    FoodDatesFragment foodDatesFragment;

    @Override
    protected void onResume() {
        xdrip.checkForcedEnglish(xdrip.getAppContext());
        super.onResume();
    }

    private static final Treatment[] TEST_TREATMENTS = new Treatment[] {
            new Treatment("Food photo file:///storage/emulated/0/Pictures/xDripFood/xDripFood_20200204_124357_7405432031083100582.jpg", 1580849041499L),
            new Treatment("Prime 722  ? Rewind 722", 1581177257000L),
            new Treatment("Food photo file:///storage/emulated/0/Pictures/xDripFood/xDripFood_20200208_085123_1715361242884717933.jpg", 1581173100000L),
            new Treatment("Food photo file:///storage/emulated/0/Pictures/xDripFood/xDripFood_20200208_085150_7552276985565858757.jpg", 1581180719874L),
            new Treatment("Food photo file:///storage/emulated/0/Pictures/xDripFood/xDripFood_20200208_093242_5853572484031700840.jpg", 1581131400000L),
            new Treatment("Food photo file:///storage/emulated/0/Pictures/xDripFood/xDripFood_20200208_093242_5853572484031700840.jpg", 1581090200000L),
            new Treatment("Food photo file:///storage/emulated/0/Pictures/xDripFood/xDripFood_20200208_093242_5853572484031700841.jpg", 1581090200000L),
            new Treatment("Food photo file:///storage/emulated/0/Pictures/xDripFood/xDripFood_20200208_093242_5853572484031700842.jpg", 1581090200000L),
            new Treatment("Food photo file:///storage/emulated/0/Pictures/xDripFood/xDripFood_20200208_093242_5853572484031700843.jpg", 1581090200000L),
            new Treatment("Food photo file:///storage/emulated/0/Pictures/xDripFood/xDripFood_20200208_093242_5853572484031700844.jpg", 1581090200000L),
            new Treatment("Food photo file:///storage/emulated/0/Pictures/xDripFood/xDripFood_20200208_093242_5853572484031700845.jpg", 1581090200000L),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        xdrip.checkForcedEnglish(xdrip.getAppContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        Map<File, FoodRecord> foodRecordMap = new LinkedHashMap<>();
        for (Treatment treatment : TEST_TREATMENTS) {
            FoodRecord.addTreatment(foodRecordMap, treatment);
        }

        foodListFragment = new FoodListFragment(this, foodRecordMap);
        foodDatesFragment = new FoodDatesFragment(this);
        foodListFragment.onSelected((FoodRecord foodRecord) -> {
            foodDatesFragment.setTreatments(foodRecord.getTreatments());
        });
    }

    public void clickNewPhoto(View view) {
        Intent foodPhotoIntent = new Intent(this.getApplicationContext(), FoodPhotoActivity.class);
        foodPhotoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (getIntent().hasExtra("timestamp")) {
            long timestamp = getIntent().getExtras().getLong("timestamp");
            foodPhotoIntent.putExtra("timestamp", timestamp);
        }
        startActivity(foodPhotoIntent);
    }
}
