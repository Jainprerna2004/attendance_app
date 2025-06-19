package com.ssgb.easyattendance.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssgb.easyattendance.R;
import com.ssgb.easyattendance.realm.Class_Names;
import com.ssgb.easyattendance.viewholders.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ClassListAdapter extends RecyclerView.Adapter<ViewHolder> implements Filterable {

    private final Activity mActivity;
    List<Class_Names> mList;
    List<Class_Names> mListFull; // Full list for filtering

    public ClassListAdapter(List<Class_Names> list, Activity context) {
        mActivity = context;
        mList = list;
        mListFull = new ArrayList<>(list); // Copy the full list
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_adapter, parent, false);
        return new ViewHolder(itemView, mActivity, mList);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final Class_Names temp = mList.get(position);
        holder.class_name.setText(temp.getName_class());
        holder.subject_name.setText(temp.getName_subject());
        holder.total_students.setText("");

        switch (temp.getPosition_bg()) {
            case "0":
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_paleblue);
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_1);
                break;
            case "1":
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_green);
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_2);
                break;
            case "2":
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_yellow);
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_3);
                break;
            case "3":
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_palegreen);
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_4);
                break;
            case "4":
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_paleorange);
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_5);
                break;
            case "5":
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_white);
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_6);
                holder.subject_name.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.text_color_secondary));
                holder.class_name.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.text_color_secondary));
                holder.total_students.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.text_color_secondary));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return classFilter;
    }

    private Filter classFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Class_Names> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Class_Names item : mListFull) {
                    if (item.getName_class().toLowerCase().contains(filterPattern) ||
                        item.getName_subject().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mList.clear();
            mList.addAll((List<Class_Names>) results.values);
            notifyDataSetChanged();
        }
    };

    public void updateList(List<Class_Names> newList) {
        mList = newList;
        mListFull = new ArrayList<>(newList);
        notifyDataSetChanged();
    }
}
