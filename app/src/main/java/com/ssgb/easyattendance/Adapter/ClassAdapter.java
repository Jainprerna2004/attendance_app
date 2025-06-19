package com.ssgb.easyattendance.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssgb.easyattendance.ClassDetail_Activity;
import com.ssgb.easyattendance.R;
import com.ssgb.easyattendance.realm.Class_Names;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {
    private List<Class_Names> classList;
    private Context context;

    public ClassAdapter(Context context, List<Class_Names> classList) {
        this.context = context;
        this.classList = classList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.class_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Class_Names classItem = classList.get(position);
        holder.className.setText(classItem.getName_class());
        holder.subjectName.setText(classItem.getName_subject());
        holder.totalStudents.setText("students: 0"); // You can update this with actual count later

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ClassDetail_Activity.class);
            intent.putExtra("class_id", classItem.getId());
            intent.putExtra("class_name", classItem.getName_class());
            intent.putExtra("subject_name", classItem.getName_subject());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return classList != null ? classList.size() : 0;
    }

    public void updateList(List<Class_Names> newList) {
        this.classList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView className, subjectName, totalStudents;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.className_adapter);
            subjectName = itemView.findViewById(R.id.subjectName_adapter);
            totalStudents = itemView.findViewById(R.id.totalStudents_adapter);
        }
    }
} 