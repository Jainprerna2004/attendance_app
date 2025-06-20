package com.ssgb.easyattendance.viewholders;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ssgb.easyattendance.R;
import com.ssgb.easyattendance.realm.Attendance_Students_List;

import java.util.List;

public class ViewHolder_reports_detail extends RecyclerView.ViewHolder {

    public TextView namE;
    public TextView regNo;
    public TextView status;

    public CardView circle;

    public Activity mActivity;
    List<Attendance_Students_List> mList;

    public ViewHolder_reports_detail(@NonNull final View itemView, Activity MainActivity, List<Attendance_Students_List> list) {
        super(itemView);

        namE = itemView.findViewById(R.id.student_name_report_detail_adapter);
        regNo = itemView.findViewById(R.id.student_regNo_report_detail_adapter);
        status = itemView.findViewById(R.id.status_report_detail_adapter);
        circle = itemView.findViewById(R.id.cardView_report_detail_adapter);


        mActivity = MainActivity;
        mList = list;

    }

}
