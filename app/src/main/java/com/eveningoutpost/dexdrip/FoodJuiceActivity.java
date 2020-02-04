package com.eveningoutpost.dexdrip;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.eveningoutpost.dexdrip.Models.DateUtil;
import com.eveningoutpost.dexdrip.Models.JoH;
import com.eveningoutpost.dexdrip.Models.Treatments;
import com.eveningoutpost.dexdrip.UtilityModels.UndoRedo;

import java.util.Date;
import java.util.UUID;

public class FoodJuiceActivity extends BaseAppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        createJuiceNote();

        Intent intent = new Intent(this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    static void createJuiceNote() {
        boolean noteOnly = true;
        if (noteOnly) {
            Treatments.create_note("Juice", /*timestamp=*/0, /*position=*/-1);
        } else {
            int juiceCarbs = 25; // TODO: configurable.
            // TODO: check for duplicate / too close submissions.

            Treatments treatment = new Treatments();

            treatment.eventType = "Juice";
            treatment.carbs = juiceCarbs;
            treatment.insulin = 0;
            treatment.timestamp = new Date().getTime();
            treatment.created_at = DateUtil.toISOString(treatment.timestamp);
            treatment.uuid = UUID.randomUUID().toString();
            treatment.enteredBy = Treatments.XDRIP_TAG + " notification";
            treatment.save();
            Treatments.pushTreatmentSync(treatment);
            UndoRedo.addUndoTreatment(treatment.uuid);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
