package com.eveningoutpost.dexdrip.food;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.eveningoutpost.dexdrip.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

// TODO: actual fragment
public class FoodListFragment {
    public interface SelectionListener {
        void selected(FoodRecord foodRecord);
    }

    private final FoodListAdapter adapter;
    private final RecyclerView foodList;
    private SelectionListener selectionListener = foodRecord -> {};

    public FoodListFragment(Activity activity, Map<File, FoodRecord> foodRecordMap) {
        foodList = activity.findViewById(R.id.food_list);
        foodList.setHasFixedSize(true);
        foodList.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new FoodListAdapter(foodRecordMap);
        foodList.setAdapter(adapter);
    }


    public void onSelected(SelectionListener selectionListener) {
        this.selectionListener = selectionListener;
    }

    class FoodListViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnailImageView;
        public CardView cardView;
        public FrameLayout selectedFrameLayout;
        public int position = -1;

        public FoodListViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            thumbnailImageView = itemView.findViewById(R.id.food_item_thumbnail);
            selectedFrameLayout = itemView.findViewById(R.id.food_item_selected); // TODO: Must be a better way to do this!

            cardView.setOnClickListener(view -> {
                adapter.onSelected(position);
            });
        }
    }

    class FoodListAdapter extends RecyclerView.Adapter<FoodListViewHolder> {
        private final ArrayList<FoodRecord> foodRecords = new ArrayList<>();
        int selected = -1;

        public FoodListAdapter(Map<File, FoodRecord> foodRecordMap) {
            foodRecords.addAll(foodRecordMap.values());
            // TODO: sort...
        }

        @NonNull
        @Override
        public FoodListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listitem_food, parent, false);
            return new FoodListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FoodListViewHolder holder, int position) {
            FoodRecord foodRecord = foodRecords.get(position);
            Bitmap thumbnail = foodRecord.getThumbnail();
            if (thumbnail != null) {
                holder.thumbnailImageView.setImageBitmap(thumbnail);
            } else {
                // TODO: proper missing image icon
                holder.thumbnailImageView.setImageResource(R.drawable.ic_launcher);
            }
            holder.position = position;
            if (position == selected) {
                holder.selectedFrameLayout.setVisibility(View.VISIBLE);
            } else {
                holder.selectedFrameLayout.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return foodRecords.size();
        }

        public void onSelected(int position) {
            int oldSelected = selected;
            selected = position;
            if (oldSelected != -1) {
                notifyItemChanged(oldSelected);
            }
            notifyItemChanged(selected);
            selectionListener.selected(foodRecords.get(position));
        }
    }
}
