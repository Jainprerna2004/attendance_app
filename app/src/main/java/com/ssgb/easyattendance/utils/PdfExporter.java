package com.ssgb.easyattendance.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.kernel.geom.PageSize;
import com.ssgb.easyattendance.realm.AppDatabase;
import com.ssgb.easyattendance.realm.AttendanceStudentsListDao;
import com.ssgb.easyattendance.realm.Attendance_Students_List;
import com.ssgb.easyattendance.realm.StudentsListDao;
import com.ssgb.easyattendance.realm.Students_List;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PdfExporter {
    private static final String TAG = "PdfExporter";
    private Context context;
    private AppDatabase db;
    private ExecutorService executorService;

    public PdfExporter(Context context) {
        this.context = context;
        this.db = AppDatabase.getInstance(context);
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public interface PdfExportCallback {
        void onSuccess(String filePath);
        void onError(String error);
    }

    public void exportMonthlyReport(String classId, String className, String subjectName, 
                                   int year, int month, PdfExportCallback callback) {
        executorService.execute(() -> {
            try {
                String filePath = generateMonthlyReport(classId, className, subjectName, year, month);
                callback.onSuccess(filePath);
            } catch (Exception e) {
                Log.e(TAG, "Error generating PDF: " + e.getMessage(), e);
                callback.onError("Error generating PDF: " + e.getMessage());
            }
        });
    }

    public void exportMonthlyReportToUri(String classId, String className, String subjectName,
                                         int year, int month, Uri uri, PdfExportCallback callback) {
        executorService.execute(() -> {
            try {
                OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
                generateMonthlyReportToStream(classId, className, subjectName, year, month, outputStream);
                callback.onSuccess("PDF saved to selected location.");
            } catch (Exception e) {
                Log.e(TAG, "Error generating PDF: " + e.getMessage(), e);
                callback.onError("Error generating PDF: " + e.getMessage());
            }
        });
    }

    private String generateMonthlyReport(String classId, String className, String subjectName, 
                                       int year, int month) throws Exception {
        // Create file name
        String fileName = String.format("Attendance_%s_%s_%d_%02d.pdf", 
            className.replace(" ", "_"), subjectName.replace(" ", "_"), year, month + 1);
        
        // Use app-specific external storage directory (doesn't require permissions on Android 11+)
        File appDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Reports");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        File pdfFile = new File(appDir, fileName);

        // Initialize PDF writer
        PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A3.rotate());

        // Get data for the month
        List<String> dates = getDatesForMonth(year, month);
        List<Students_List> students = getStudentsForClass(classId);
        Map<String, Map<String, String>> attendanceData = getAttendanceDataForMonth(classId, year, month);

        // Create header
        Paragraph header = new Paragraph(String.format("Monthly Attendance Report\n%s - %s\n%s", 
            className, subjectName, getMonthYearString(year, month)))
            .setTextAlignment(TextAlignment.CENTER)
            .setFontSize(16)
            .setBold();
        document.add(header);

        // Create table
        Table table = createAttendanceTable(students, dates, attendanceData);
        document.add(table);

        // Add summary
        addSummary(document, students.size(), dates.size(), attendanceData);

        document.close();
        return pdfFile.getAbsolutePath();
    }

    private void generateMonthlyReportToStream(String classId, String className, String subjectName,
                                               int year, int month, OutputStream outputStream) throws Exception {
        // Create file name (for header only)
        // ... rest of PDF generation logic, but use outputStream ...
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A3.rotate());

        // Get data for the month
        List<String> dates = getDatesForMonth(year, month);
        List<Students_List> students = getStudentsForClass(classId);
        Map<String, Map<String, String>> attendanceData = getAttendanceDataForMonth(classId, year, month);

        // Create header
        Paragraph header = new Paragraph(String.format("Monthly Attendance Report\n%s - %s\n%s",
            className, subjectName, getMonthYearString(year, month)))
            .setTextAlignment(TextAlignment.CENTER)
            .setFontSize(16)
            .setBold();
        document.add(header);

        // Create table
        Table table = createAttendanceTable(students, dates, attendanceData);
        document.add(table);

        // Add summary
        addSummary(document, students.size(), dates.size(), attendanceData);

        document.close();
    }

    private List<String> getDatesForMonth(int year, int month) {
        List<String> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        
        while (calendar.get(Calendar.MONTH) == month) {
            dates.add(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DATE, 1);
        }
        
        return dates;
    }

    private List<Students_List> getStudentsForClass(String classId) {
        List<Students_List> students = db.studentsListDao().getStudentsByClassId(classId);
        
        // Sort students by roll number
        Collections.sort(students, new Comparator<Students_List>() {
            @Override
            public int compare(Students_List s1, Students_List s2) {
                try {
                    int roll1 = Integer.parseInt(s1.getRegNo_student());
                    int roll2 = Integer.parseInt(s2.getRegNo_student());
                    return Integer.compare(roll1, roll2);
                } catch (NumberFormatException e) {
                    return s1.getRegNo_student().compareTo(s2.getRegNo_student());
                }
            }
        });
        
        return students;
    }

    private Map<String, Map<String, String>> getAttendanceDataForMonth(String classId, int year, int month) {
        Map<String, Map<String, String>> attendanceData = new HashMap<>();
        AttendanceStudentsListDao attendanceDao = db.attendanceStudentsListDao();
        
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        
        while (calendar.get(Calendar.MONTH) == month) {
            String date = dateFormat.format(calendar.getTime());
            String dateAndClassId = date + classId;
            
            List<Attendance_Students_List> dayAttendance = attendanceDao.getByDateAndClassId(dateAndClassId);
            
            for (Attendance_Students_List attendance : dayAttendance) {
                String studentId = attendance.getStudentRollNo();
                if (!attendanceData.containsKey(studentId)) {
                    attendanceData.put(studentId, new HashMap<>());
                }
                attendanceData.get(studentId).put(date, attendance.getAttendance());
            }
            
            calendar.add(Calendar.DATE, 1);
        }
        
        return attendanceData;
    }

    private Table createAttendanceTable(List<Students_List> students, List<String> dates, 
                                       Map<String, Map<String, String>> attendanceData) {
        // Calculate table width
        float[] columnWidths = new float[dates.size() + 2]; // +2 for student name and roll number
        columnWidths[0] = 2; // Student name
        columnWidths[1] = 1; // Roll number
        for (int i = 2; i < columnWidths.length; i++) {
            columnWidths[i] = 1; // Date columns
        }
        
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));
        table.setSkipFirstHeader(false);
        
        // Add header row
        table.addHeaderCell(createHeaderCell("Student Name"));
        table.addHeaderCell(createHeaderCell("Roll No"));
        
        for (String date : dates) {
            String day = date.substring(0, 2); // Only the day part, e.g., "01"
            table.addHeaderCell(createHeaderCell(day));
        }
        
        // Add data rows
        for (Students_List student : students) {
            table.addCell(createCell(student.getName_student()));
            table.addCell(createCell(student.getRegNo_student()));
            
            Map<String, String> studentAttendance = attendanceData.get(student.getRegNo_student());
            
            for (String date : dates) {
                String attendance = "-";
                if (studentAttendance != null && studentAttendance.containsKey(date)) {
                    attendance = studentAttendance.get(date).equals("Present") ? "P" : "A";
                }
                table.addCell(createCell(attendance));
            }
        }
        
        return table;
    }

    private Cell createHeaderCell(String text) {
        Cell cell = new Cell();
        cell.add(new Paragraph(text));
        cell.setBold();
        cell.setTextAlignment(TextAlignment.CENTER);
        cell.setBackgroundColor(com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY);
        return cell;
    }

    private Cell createCell(String text) {
        Cell cell = new Cell();
        cell.add(new Paragraph(text));
        cell.setTextAlignment(TextAlignment.CENTER);
        return cell;
    }

    private void addSummary(Document document, int totalStudents, int totalDays, 
                           Map<String, Map<String, String>> attendanceData) {
        document.add(new Paragraph("\n"));
        
        Paragraph summary = new Paragraph("Summary:")
            .setBold()
            .setFontSize(14);
        document.add(summary);
        
        document.add(new Paragraph("Total Students: " + totalStudents));
        document.add(new Paragraph("Total Days: " + totalDays));
        
        // Calculate attendance statistics
        int totalPresent = 0;
        int totalAbsent = 0;
        
        for (Map<String, String> studentAttendance : attendanceData.values()) {
            for (String attendance : studentAttendance.values()) {
                if (attendance.equals("Present")) {
                    totalPresent++;
                } else if (attendance.equals("Absent")) {
                    totalAbsent++;
                }
            }
        }
        
        document.add(new Paragraph("Total Present: " + totalPresent));
        document.add(new Paragraph("Total Absent: " + totalAbsent));
        
        if (totalPresent + totalAbsent > 0) {
            double attendancePercentage = (double) totalPresent / (totalPresent + totalAbsent) * 100;
            document.add(new Paragraph(String.format("Attendance Percentage: %.2f%%", attendancePercentage)));
        }
    }

    private String getMonthYearString(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        return monthFormat.format(calendar.getTime());
    }

    public void cleanup() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
} 