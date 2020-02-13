package com.eveningoutpost.dexdrip.food;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.activeandroid.query.Select;
import com.eveningoutpost.dexdrip.BaseAppCompatActivity;
import com.eveningoutpost.dexdrip.Home;
import com.eveningoutpost.dexdrip.Models.Treatments;
import com.eveningoutpost.dexdrip.R;
import com.eveningoutpost.dexdrip.food.FoodDatesFragment;
import com.eveningoutpost.dexdrip.food.FoodListFragment;
import com.eveningoutpost.dexdrip.food.FoodRecord;
import com.eveningoutpost.dexdrip.xdrip;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FoodPhotoActivity2 extends BaseAppCompatActivity {
    private FoodListFragment foodListFragment;
    private FoodDatesFragment foodDatesFragment;
    private FoodRecord currentFoodRecord = null;

    @Override
    protected void onResume() {
        xdrip.checkForcedEnglish(xdrip.getAppContext());
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        xdrip.checkForcedEnglish(xdrip.getAppContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        List<Treatments> treatments = Treatments.latest(100);

        Map<File, FoodRecord> foodRecordMap = new LinkedHashMap<>();
        for (Treatments treatment : treatments) {
            FoodRecord.addTreatment(foodRecordMap, treatment);
        }

        foodListFragment = new FoodListFragment(this, foodRecordMap);
        foodDatesFragment = new FoodDatesFragment(this);
        final Button addButton = findViewById(R.id.add_button);
        final EditText noteEditText = findViewById(R.id.food_note_edit_text);
        addButton.setVisibility(View.INVISIBLE); // TODO: Enabled is better, but color is same now.
        noteEditText.setVisibility(View.INVISIBLE);

        foodListFragment.onSelected((FoodRecord foodRecord) -> {
            currentFoodRecord = foodRecord;
            foodDatesFragment.setTreatments(foodRecord.getTreatments());
            addButton.setVisibility(View.VISIBLE);
            noteEditText.setVisibility(View.VISIBLE);
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

    public void clickAdd(View view) {
        long timestamp = 0;
        if (getIntent().hasExtra("timestamp")) {
            timestamp = getIntent().getExtras().getLong("timestamp");
        }

        Treatments.create_note("Food photo " + Uri.fromFile(currentFoodRecord.getImageFile()), timestamp, /*position=*/-1);
        finish();
    }
}
