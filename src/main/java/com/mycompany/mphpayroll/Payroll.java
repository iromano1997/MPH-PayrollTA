/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mphpayroll;

import java.io.IOException;
import java.util.*;

public class Payroll {
    public static void main(String[] args) {
        try {
            Map<String, Employee> employees = DataLoader.loadEmployees("C:\\Users\\Sid\\Desktop\\Java Projects\\MPHPayroll\\MPHPayroll\\MPHPayroll\\src\\MotorPH_employee_details.csv");
            AttendanceRecord attendance = new AttendanceRecord();
            DataLoader.loadAttendance(attendance, "C:\\Users\\Sid\\Desktop\\Java Projects\\MPHPayroll\\MPHPayroll\\MPHPayroll\\src\\attendance_record.csv");

            new Menu(employees, attendance).showMenu();
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }
}                