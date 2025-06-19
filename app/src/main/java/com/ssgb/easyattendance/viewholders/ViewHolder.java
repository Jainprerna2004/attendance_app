package com.ssgb.easyattendance.viewholders;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.ssgb.easyattendance.ClassDetail_Activity;
import com.ssgb.easyattendance.MainActivity;
import com.ssgb.easyattendance.R;
import com.ssgb.easyattendance.realm.AppDatabase;
import com.ssgb.easyattendance.realm.Class_Names;
import com.ssgb.easyattendance.realm.ClassNamesDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

public class ViewHolder extends RecyclerView.ViewHolder {

    public final TextView class_name;
    public final TextView subject_name;
    public final TextView total_students;
    public ImageView imageView_bg;
    public RelativeLayout frameLayout;
    public CardView cardView;

    public Activity mActivity;
    List<Class_Names> mList;
    private AppDatabase db;
    private ExecutorService executorService;

    public ViewHolder(@NonNull final View itemView, Activity MainActivity, List<Class_Names> list) {
        super(itemView);

        class_name = itemView.findViewById(R.id.className_adapter);
        subject_name = itemView.findViewById(R.id.subjectName_adapter);
        imageView_bg = itemView.findViewById(R.id.imageClass_adapter);
        frameLayout = itemView.findViewById(R.id.frame_bg);
        cardView = itemView.findViewById(R.id.cardView_adapter);
        total_students = itemView.findViewById(R.id.totalStudents_adapter);

        mActivity = MainActivity;
        mList = list;
        db = AppDatabase.getInstance(mActivity.getApplicationContext());
        executorService = Executors.newSingleThreadExecutor();

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ClassDetail_Activity.class);
                intent.putExtra("theme", mList.get(getAdapterPosition()).getPosition_bg());
                intent.putExtra("className", mList.get(getAdapterPosition()).getName_class());
                intent.putExtra("subjectName", mList.get(getAdapterPosition()).getName_subject());
                intent.putExtra("classroom_ID", mList.get(getAdapterPosition()).getId());
                Pair<View, String> p1 = Pair.<View, String>create(cardView, "ExampleTransition");
                ActivityOptionsCompat optionsCompat = makeSceneTransitionAnimation(mActivity, p1);
                view.getContext().startActivity(intent, optionsCompat.toBundle());
            }
        });

        // Add long press to delete
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDeleteConfirmationDialog(getAdapterPosition());
                return true;
            }
        });
    }

    private void showDeleteConfirmationDialog(final int position) {
        if (position < 0 || position >= mList.size()) return;
        
        Class_Names classToDelete = mList.get(position);
        
        View dialogView = View.inflate(mActivity, R.layout.dialog_delete_class, null);
        TextView classNameText = dialogView.findViewById(R.id.class_name_text);
        TextView subjectNameText = dialogView.findViewById(R.id.subject_name_text);
        classNameText.setText(classToDelete.getName_class());
        subjectNameText.setText(classToDelete.getName_subject());

        android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(mActivity)
            .setView(dialogView)
            .setPositiveButton("Delete", (dialogInterface, which) -> {
                deleteClass(position);
            })
            .setNegativeButton("Cancel", null)
            .show();

        // Set button text colors after the dialog is shown
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(android.graphics.Color.parseColor("#FF6B6B"));
        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(android.graphics.Color.parseColor("#333333"));
    }

    private void deleteClass(final int position) {
        if (position < 0 || position >= mList.size()) return;
        
        final Class_Names classToDelete = mList.get(position);
        
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Delete the class
                    db.classNamesDao().delete(classToDelete);
                    
                    // Delete associated students
                    db.studentsListDao().deleteByClassId(classToDelete.getId());
                    
                    // Delete associated attendance reports
                    db.attendanceReportsDao().deleteByClassId(classToDelete.getId());
                    
                    // Delete associated attendance students list
                    db.attendanceStudentsListDao().deleteByClassId(classToDelete.getId());
                    
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mActivity, "Class deleted successfully", Toast.LENGTH_SHORT).show();
                            // Remove from the list and notify adapter
                            mList.remove(position);
                            if (mActivity instanceof MainActivity) {
                                ((MainActivity) mActivity).refreshClassesList();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mActivity, "Error deleting class", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
