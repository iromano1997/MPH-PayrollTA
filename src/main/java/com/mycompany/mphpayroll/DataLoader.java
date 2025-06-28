/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mphpayroll;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DataLoader {

    public static Map<String, Employee> loadEmployees(String filePath) throws IOException {
        Map<String, Employee> employees = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (parts.length >= 19) {
                    try {
                        String employeeNumber = parts[0].trim();
                        String lastName = parts[1].trim().replaceAll("\"", "");
                        String firstName = parts[2].trim().replaceAll("\"", "");
                        String birthday = parts[3].trim().replaceAll("\"", "");
                        String address = parts[4].trim().replaceAll("\"", "");
                        String phoneNumber = parts[5].trim().replaceAll("\"", "");
                        String sssNumber = parts[6].trim().replaceAll("\"", "");
                        String philhealthNumber = parts[7].trim().replaceAll("\"", "");
                        String tin = parts[8].trim().replaceAll("\"", "");
                        String pagibigNumber = parts[9].trim().replaceAll("\"", "");
                        String status = parts[10].trim().replaceAll("\"", "");
                        String position = parts[11].trim().replaceAll("\"", "");
                        String immediateSupervisor = parts[12].trim().replaceAll("\"", "");
                        double basicSalary = Double.parseDouble(parts[13].trim().replaceAll("\"", ""));
                        double riceSubsidy = Double.parseDouble(parts[14].trim().replaceAll("\"", ""));
                        double phoneAllowance = Double.parseDouble(parts[15].trim().replaceAll("\"", ""));
                        double clothingAllowance = Double.parseDouble(parts[16].trim().replaceAll("\"", ""));
                        double grossSemiMonthlyRate = Double.parseDouble(parts[17].trim().replaceAll("\"", ""));
                        double hourlyRate = Double.parseDouble(parts[18].trim().replaceAll("\"", ""));

                        Employee employee = new Employee(employeeNumber, firstName, lastName, birthday,
                                address, phoneNumber, sssNumber, philhealthNumber, tin, pagibigNumber, status,
                                position, immediateSupervisor, basicSalary, riceSubsidy, phoneAllowance,
                                clothingAllowance, grossSemiMonthlyRate, hourlyRate);
                        employees.put(employeeNumber, employee);
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping line due to number format error: " + line + " - " + e.getMessage());
                    }
                } else {
                    System.err.println("Skipping line due to incorrect number of columns: " + line);
                }
            }
        }
        return employees;
    }

    public static void loadAttendance(AttendanceRecord attendance, String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            DecimalFormat df = new DecimalFormat("00");

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String employeeNumber = data[0].trim();
                    String date = data[1].trim();
                    String timeIn = data[2].trim();
                    String timeOut = data[3].trim();
                    attendance.addAttendance(employeeNumber, date, timeIn, timeOut);
                }
            }
        }
    }
}