/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mphpayroll;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Menu {
    private Scanner scanner;
    private Map<String, Employee> employees;
    private AttendanceRecord attendance;
    private PayrollCalculator payrollCalculator;

    public Menu(Map<String, Employee> employees, AttendanceRecord attendance) {
        this.scanner = new Scanner(System.in);
        this.employees = employees;
        this.attendance = attendance;
        this.payrollCalculator = new PayrollCalculator();
    }

    public Scanner getScanner() {
        return scanner;
    }

    public Map<String, Employee> getEmployees() {
        return employees;
    }

    public AttendanceRecord getAttendance() {
        return attendance;
    }

    public PayrollCalculator getPayrollCalculator() {
        return payrollCalculator;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public void setEmployees(Map<String, Employee> employees) {
        this.employees = employees;
    }

    public void setAttendance(AttendanceRecord attendance) {
        this.attendance = attendance;
    }

    public void setPayrollCalculator(PayrollCalculator payrollCalculator) {
        this.payrollCalculator = payrollCalculator;
    }

    public void showMenu() {
        int choice;
        do {
            System.out.println("\nWelcome to MotorPH Payroll Hub Menu:");
            System.out.print("\n");
            System.out.println("1. Display Employee Information");
            System.out.println("2. Compute Hours Worked");
            System.out.println("3. Compute Gross Salary");
            System.out.println("4. Compute Net Salary");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine();
                processChoice(choice);
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                choice = 0;
            }
        } while (choice != 5);
        scanner.close();
    }

    private void processChoice(int choice) {
        switch (choice) {
            case 1:
                displayEmployeeInfo();
                break;
            case 2:
                computeHoursWorked();
                break;
            case 3:
                computeGrossSalary();
                break;
            case 4:
                computeNetSalary();
                break;
            case 5:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    private void displayEmployeeInfo() {
        System.out.print("\n");
        System.out.print("Enter Employee Number: ");
        String empNumber = scanner.nextLine().trim();
        Employee emp = employees.get(empNumber);

        if (emp != null) {
            System.out.println("\nEmployee Details:");
            System.out.println("Employee Number: " + emp.getEmployeeNumber());
            System.out.println("Full Name: " + emp.getFullName());
            System.out.println("Birthday: " + emp.getBirthday());
            System.out.printf("Basic Salary: PHP %.2f%n", emp.getBasicSalary());
            System.out.printf("Hourly Rate: PHP %.2f%n", emp.getHourlyRate());
        } else {
            System.out.println("Employee not found.");
        }
    }

    private void computeHoursWorked() {
        System.out.print("\n");
        System.out.print("Enter employee number: ");
        String empNumber = scanner.nextLine();

        System.out.print("Enter start date (MM/dd/yyyy): ");
        String startDateStr = scanner.nextLine();
        System.out.print("Enter end date (MM/dd/yyyy): ");
        String endDateStr = scanner.nextLine();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);

            Map<String, String[]> attendanceData =
                    attendance.getAttendanceInRange(empNumber, startDate, endDate);

            if (attendanceData.isEmpty()) {
                System.out.println("No attendance records found for this period.");
                return;
            }

            long totalMinutes = 0;

            for (Map.Entry<String, String[]> entry : attendanceData.entrySet()) {
                long minutes = calculateTimeDifferenceMinutes(
                        entry.getValue()[0], entry.getValue()[1]);
                totalMinutes += minutes;
                System.out.printf("Date: %s, Hours: %s%n",
                        entry.getKey(), formatTimeDifference(minutes));
            }

            System.out.printf("Total Hours: %s%n", formatTimeDifference(totalMinutes));
        } catch (ParseException e) {
            System.out.println("Invalid date format.");
        }
    }

    private void computeGrossSalary() {
        System.out.print("\n");
        System.out.print("Enter employee number: ");
        String empNumber = scanner.nextLine();
        Employee emp = employees.get(empNumber);

        if (emp == null) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.print("Enter start date (MM/dd/yyyy): ");
        String startDateStr = scanner.nextLine();
        System.out.print("Enter end date (MM/dd/yyyy): ");
        String endDateStr = scanner.nextLine();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);

            Map<String, String[]> attendanceData =
                    attendance.getAttendanceInRange(empNumber, startDate, endDate);

            if (attendanceData.isEmpty()) {
                System.out.println("No attendance records found for this period.");
                return;
            }

            long totalMinutes = 0;
            for (Map.Entry<String, String[]> entry : attendanceData.entrySet()) {
                totalMinutes += calculateTimeDifferenceMinutes(
                        entry.getValue()[0], entry.getValue()[1]);
            }

            double totalHours = totalMinutes / 60.0;
            double grossSalary = totalHours * emp.getHourlyRate();
            System.out.printf("Gross salary for %s: PHP %.2f%n", emp.getFullName(), grossSalary);
        } catch (ParseException e) {
            System.out.println("Invalid date format.");
        }
    }

    private void computeNetSalary() {
        System.out.print("\n");
        System.out.print("Enter employee number: ");
        String empNumber = scanner.nextLine();
        Employee emp = employees.get(empNumber);

        if (emp == null) {
            System.out.println("Employee not found.");
            return;
        }

        double basicSalary = emp.getBasicSalary();
        PayrollCalculator payrollCalculator = new PayrollCalculator();
        double totalDeductions = payrollCalculator.calculateTotalDeductions(basicSalary);
        double taxableIncome = basicSalary - totalDeductions;
        double withholdingTax = PayrollCalculator.calculateWithholdingTax(taxableIncome);
        double netSalary = taxableIncome - withholdingTax;

        System.out.println("\nNet Salary Calculation:");
        System.out.printf("Basic Salary: PHP %.2f%n", basicSalary);
        System.out.printf("Total Deductions: PHP %.2f%n", totalDeductions);
        System.out.printf("Taxable Income: PHP %.2f%n", taxableIncome);
        System.out.printf("Withholding Tax: PHP %.2f%n", withholdingTax);
        System.out.printf("Net Salary: PHP %.2f%n", netSalary);
    }

    long calculateTimeDifferenceMinutes(String logIn, String logOut) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Date timeIn = format.parse(logIn);
            Date timeOut = format.parse(logOut);
            return (timeOut.getTime() - timeIn.getTime()) / (60 * 1000);
        } catch (ParseException e) {
            return -1;
        }
    }

    String formatTimeDifference(long minutes) {
        return (minutes < 0) ? "Invalid" :
                String.format("%d:%02d", minutes / 60, minutes % 60);
    }
}