package com.ssgb.easyattendance.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssgb.easyattendance.R;
import com.ssgb.easyattendance.realm.Attendance_Students_List;

import java.util.List;

public class Reports_Detail_Adapter extends RecyclerView.Adapter<Reports_Detail_Adapter.ViewHolder> {
    private List<Attendance_Students_List> mList;
    private Context context;

    public Reports_Detail_Adapter(Context context, List<Attendance_Students_List> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void updateList(List<Attendance_Students_List> newList) {
        this.mList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reports_detail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Attendance_Students_List student = mList.get(position);
        holder.name.setText(student.getStudentName());
        holder.regNo.setText(student.getStudentRollNo());
        holder.attendance.setText(student.getAttendance());
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, regNo, attendance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            regNo = itemView.findViewById(R.id.reg_no);
            attendance = itemView.findViewById(R.id.attendance);
        }
    }
}
