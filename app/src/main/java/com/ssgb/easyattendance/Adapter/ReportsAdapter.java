package com.ssgb.easyattendance.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssgb.easyattendance.R;
import com.ssgb.easyattendance.Reports_Detail_Activity;
import com.ssgb.easyattendance.realm.Attendance_Reports;

import java.util.List;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ViewHolder> {
    private List<Attendance_Reports> mList;
    private Context context;

    public ReportsAdapter(Context context, List<Attendance_Reports> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void updateList(List<Attendance_Reports> newList) {
        this.mList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reports_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Attendance_Reports report = mList.get(position);
        holder.date.setText(report.getDate());
        holder.studentName.setText(report.getClassname());
        holder.attendanceStatus.setText(report.getSubjName());
        
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Reports_Detail_Activity.class);
            intent.putExtra("date", report.getDate());
            intent.putExtra("classname", report.getClassname());
            intent.putExtra("subjectname", report.getSubjName());
            intent.putExtra("room_ID", report.getClass_id());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, studentName, attendanceStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            studentName = itemView.findViewById(R.id.student_name);
            attendanceStatus = itemView.findViewById(R.id.attendance_status);
        }
    }
}
