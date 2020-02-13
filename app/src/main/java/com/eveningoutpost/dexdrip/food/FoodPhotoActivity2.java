package com.eveningoutpost.dexdrip.food;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.activeandroid.query.Select;
import com.eveningoutpost.dexdrip.BaseAppCompatActivity;
import com.eveningoutpost.dexdrip.Home;
import com.eveningoutpost.dexdrip.ImportedLibraries.dexcom.Dex_Constants;
import com.eveningoutpost.dexdrip.Models.Sensor;
import com.eveningoutpost.dexdrip.Models.Treatments;
import com.eveningoutpost.dexdrip.R;
import com.eveningoutpost.dexdrip.Services.DexCollectionService;
import com.eveningoutpost.dexdrip.UtilityModels.BgGraphBuilder;
import com.eveningoutpost.dexdrip.UtilityModels.ColorCache;
import com.eveningoutpost.dexdrip.UtilityModels.PersistentStore;
import com.eveningoutpost.dexdrip.UtilityModels.Pref;
import com.eveningoutpost.dexdrip.food.FoodDatesFragment;
import com.eveningoutpost.dexdrip.food.FoodListFragment;
import com.eveningoutpost.dexdrip.food.FoodRecord;
import com.eveningoutpost.dexdrip.xdrip;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PreviewLineChartView;

import static com.eveningoutpost.dexdrip.UtilityModels.ColorCache.getCol;

public class FoodPhotoActivity2 extends BaseAppCompatActivity {
    private FoodListFragment foodListFragment;
    private FoodDatesFragment foodDatesFragment;
    private FoodRecord currentFoodRecord = null;

    BgGraphBuilder bgGraphBuilder;
    LineChartView chart;

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

        chart = (LineChartView) findViewById(R.id.chart);
        chart.setBackgroundColor(getCol(ColorCache.X.color_home_chart_background));
        chart.setZoomType(ZoomType.HORIZONTAL);
        chart.getChartData().setAxisXTop(null);
        // chart.setOnValueTouchListener(bgGraphBuilder.getOnValueSelectTooltipListener(mActivity));
        // chart.setViewportCalculationEnabled(true);
        // chart.setViewportChangeListener(new Home.ChartViewPortListener());

        foodListFragment.onSelected((FoodRecord foodRecord) -> {
            currentFoodRecord = foodRecord;
            foodDatesFragment.setTreatments(foodRecord.getTreatments());
            addButton.setVisibility(View.VISIBLE);
            noteEditText.setVisibility(View.VISIBLE);

            Treatments currentTreatment = foodRecord.getTreatments().get(0);
            int HOUR = (60000 * 60);
            bgGraphBuilder = new BgGraphBuilder(this, /*start=*/currentTreatment.timestamp - 2 * HOUR, /*end=*/currentTreatment.timestamp + 6 * HOUR, 100, false, false);
            LineChartData lineChartData = bgGraphBuilder.lineData();
            ArrayList<PointValue> list = new ArrayList<>();
            list.add(new PointValue(currentTreatment.timestamp / BgGraphBuilder.FUZZER, 0));
            list.add(new PointValue(currentTreatment.timestamp / BgGraphBuilder.FUZZER, 200));
            lineChartData.getLines().add(new Line(list)
                    .setColor(0xffffffff)
                    .setStrokeWidth(2)
                    .setHasPoints(false)
                    .setReverseYAxis(true)
                    .setHasLines(true));
            chart.setLineChartData(lineChartData);
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
