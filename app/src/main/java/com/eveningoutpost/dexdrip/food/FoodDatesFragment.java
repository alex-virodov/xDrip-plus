package com.eveningoutpost.dexdrip.food;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.eveningoutpost.dexdrip.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FoodDatesFragment {
    RecyclerView foodDatesList;
    FoodDatesListAdapter adapter = new FoodDatesListAdapter();

    public FoodDatesFragment(Activity activity) {
        foodDatesList = activity.findViewById(R.id.food_dates_list);
        foodDatesList.setHasFixedSize(true);
        foodDatesList.setLayoutManager(new LinearLayoutManager(activity));
        foodDatesList.setAdapter(adapter);
    }

    public void setTreatments(ArrayList<Treatment> treatments) {
        adapter.setTreatments(treatments);
    }

    static class FoodDatesListViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;
        public int position;

        public FoodDatesListViewHolder(FoodDatesListAdapter foodDatesListAdapter, @NonNull View itemView) {
            super(itemView);
            dateTextView = (TextView) itemView;

            dateTextView.setOnClickListener(view -> {
                foodDatesListAdapter.onSelected(position);
            });
        }
    }

    static class FoodDatesListAdapter extends RecyclerView.Adapter<FoodDatesListViewHolder> {
        private static final ArrayList<Treatment> EMPTY_TREATMENTS = new ArrayList<>();
        private ArrayList<Treatment> treatments = EMPTY_TREATMENTS;
        int selected = -1;
        private static final SimpleDateFormat FORMATTER =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public FoodDatesListAdapter() {
        }

        @NonNull
        @Override
        public FoodDatesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listitem_food_dates, parent, false);
            return new FoodDatesListViewHolder(this, view);
        }

        @Override
        public void onBindViewHolder(@NonNull FoodDatesListViewHolder holder, int position) {
            Treatment treatment = treatments.get(position);
            Date date = new Date(treatment.timestamp);
            holder.dateTextView.setText(FORMATTER.format(date));
            holder.position = position;
        }

        @Override
        public int getItemCount() {
            return treatments.size();
        }

        public void onSelected(int position) {
            int oldSelected = selected;
            selected = position;
            if (oldSelected != -1) {
                notifyItemChanged(oldSelected);
            }
            notifyItemChanged(selected);
        }

        public void setTreatments(ArrayList<Treatment> treatments) {
            this.treatments = treatments;
            notifyDataSetChanged();
        }
    }
}
