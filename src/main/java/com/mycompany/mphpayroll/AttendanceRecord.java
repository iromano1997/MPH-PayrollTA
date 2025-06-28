/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mphpayroll;


import java.util.HashMap;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

public class AttendanceRecord {
    private Map<String, Map<String, String[]>> attendanceData;

    public AttendanceRecord() {
        this.attendanceData = new HashMap<>();
    }

    public void addAttendance(String empNumber, String date, String timeIn, String timeOut) {
        attendanceData.computeIfAbsent(empNumber, k -> new HashMap<>()).put(date, new String[]{timeIn, timeOut});
    }

    public Map<String, String[]> getAttendanceForEmployee(String empNumber) {
        return attendanceData.getOrDefault(empNumber, Collections.emptyMap());
    }

    public Map<String, String[]> getAttendanceInRange(String empNumber, Date startDate, Date endDate) {
        Map<String, String[]> filteredAttendance = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Map<String, String[]> employeeAttendance = attendanceData.get(empNumber);

        if (employeeAttendance != null) {
            for (Map.Entry<String, String[]> entry : employeeAttendance.entrySet()) {
                try {
                    Date recordDate = dateFormat.parse(entry.getKey());
                    if (!recordDate.before(startDate) && !recordDate.after(endDate)) {
                        filteredAttendance.put(entry.getKey(), entry.getValue());
                    }
                } catch (ParseException e) {
                    System.err.println("Error parsing date in attendance record: " + entry.getKey());
                }
            }
        }
        return filteredAttendance;
    }

}   