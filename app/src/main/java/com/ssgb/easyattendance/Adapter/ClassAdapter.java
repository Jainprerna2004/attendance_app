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
import com.ssgb.easyattendance.database.entities.ClassNames;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {
    private List<ClassNames> classList;
    private Context context;

    public ClassAdapter(Context context, List<ClassNames> classList) {
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
        ClassNames classItem = classList.get(position);
        holder.className.setText(classItem.getClass_name());
        holder.subjectName.setText(classItem.getSubject_name());
        holder.teacherName.setText(classItem.getTeacher_name());
        holder.roomNo.setText(classItem.getRoom_no());
        holder.time.setText(classItem.getTime());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ClassDetail_Activity.class);
            intent.putExtra("class_id", classItem.getClass_id());
            intent.putExtra("class_name", classItem.getClass_name());
            intent.putExtra("subject_name", classItem.getSubject_name());
            intent.putExtra("teacher_name", classItem.getTeacher_name());
            intent.putExtra("room_no", classItem.getRoom_no());
            intent.putExtra("time", classItem.getTime());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return classList != null ? classList.size() : 0;
    }

    public void updateList(List<ClassNames> newList) {
        this.classList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView className, subjectName, teacherName, roomNo, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.class_name);
            subjectName = itemView.findViewById(R.id.subject_name);
            teacherName = itemView.findViewById(R.id.teacher_name);
            roomNo = itemView.findViewById(R.id.room_no);
            time = itemView.findViewById(R.id.time);
        }
    }
} 