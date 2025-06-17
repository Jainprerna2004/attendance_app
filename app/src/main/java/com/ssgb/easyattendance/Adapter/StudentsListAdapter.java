package com.ssgb.easyattendance.Adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssgb.easyattendance.R;
import com.ssgb.easyattendance.realm.Students_List;
import com.ssgb.easyattendance.viewholders.ViewHolder_students;

import java.util.List;

public class StudentsListAdapter extends RecyclerView.Adapter<ViewHolder_students> {

    private final Activity mActivity;
    List<Students_List> mList;
    String stuID, mroomID;

    public StudentsListAdapter(List<Students_List> list, Activity context, String roomID, String extraClick) {
        mActivity = context;
        mList = list;
        mroomID = roomID;
    }

    @NonNull
    @Override
    public ViewHolder_students onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_attendance_adapter, parent, false);
        return new ViewHolder_students(itemView, mActivity, mList, mroomID);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_students holder, final int position) {
        Students_List temp = mList.get(position);
        holder.student_name.setText(temp.getName_student());
        holder.student_regNo.setText(temp.getRegNo_student());


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        stuID = temp.getRegNo_student();
        String value = preferences.getString(stuID, null);
        if (value==null){

        }else {
            if (value.equals("Present")) {
                holder.radioButton_present.setChecked(true);
            } else {
                holder.radioButton_absent.setChecked(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

}
