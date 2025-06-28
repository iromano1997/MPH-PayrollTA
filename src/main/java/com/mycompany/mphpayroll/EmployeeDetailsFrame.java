/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mphpayroll;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;     

public class EmployeeDetailsFrame extends JFrame {
    private Employee employee;
    private AttendanceRecord attendanceRecord;

    private JLabel empNumberLabel, nameLabel, birthdayLabel, basicSalaryLabel, hourlyRateLabel;
    private JLabel sssLabel, philhealthLabel, tinLabel, pagibigLabel;
    private JComboBox<String> monthComboBox, yearComboBox;
    private JLabel totalHoursLabel, grossSalaryLabel, deductionsLabel, netSalaryLabel;
    private JButton computeButton;

    public EmployeeDetailsFrame(Employee employee, AttendanceRecord attendanceRecord) {
        super("Employee Details: " + employee.getFullName());
        this.employee = employee;
        this.attendanceRecord = attendanceRecord;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(620, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initComponents();
        displayEmployeeDetails();
    }

    private void initComponents() {
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        addLabelAndField(detailsPanel, gbc, "Employee Number:", empNumberLabel = new JLabel(), row++);
        addLabelAndField(detailsPanel, gbc, "Name:", nameLabel = new JLabel(), row++);
        addLabelAndField(detailsPanel, gbc, "Birthday:", birthdayLabel = new JLabel(), row++);
        addLabelAndField(detailsPanel, gbc, "SSS No.:", sssLabel = new JLabel(), row++);
        addLabelAndField(detailsPanel, gbc, "PhilHealth No.:", philhealthLabel = new JLabel(), row++);
        addLabelAndField(detailsPanel, gbc, "TIN:", tinLabel = new JLabel(), row++);
        addLabelAndField(detailsPanel, gbc, "Pag-IBIG No.:", pagibigLabel = new JLabel(), row++);
        addLabelAndField(detailsPanel, gbc, "Basic Salary:", basicSalaryLabel = new JLabel(), row++);
        addLabelAndField(detailsPanel, gbc, "Hourly Rate:", hourlyRateLabel = new JLabel(), row++);

        add(detailsPanel, BorderLayout.NORTH);

        JPanel payrollPanel = new JPanel(new GridBagLayout());
        payrollPanel.setBorder(BorderFactory.createTitledBorder("Payroll Calculation"));
        GridBagConstraints pGbc = new GridBagConstraints();
        pGbc.insets = new Insets(4, 4, 4, 4);
        pGbc.anchor = GridBagConstraints.WEST;

        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        monthComboBox = new JComboBox<>(months);

        List<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int y = currentYear - 2; y <= currentYear + 1; y++) {
            years.add(String.valueOf(y));
        }
        yearComboBox = new JComboBox<>(years.toArray(new String[0]));
        yearComboBox.setSelectedItem(String.valueOf(currentYear));

        pGbc.gridx = 0; pGbc.gridy = 0; payrollPanel.add(new JLabel("Month:"), pGbc);
        pGbc.gridx = 1; pGbc.gridy = 0; payrollPanel.add(monthComboBox, pGbc);
        pGbc.gridx = 2; pGbc.gridy = 0; payrollPanel.add(new JLabel("Year:"), pGbc);
        pGbc.gridx = 3; pGbc.gridy = 0; payrollPanel.add(yearComboBox, pGbc);

        computeButton = new JButton("Compute");
        pGbc.gridx = 0; pGbc.gridy = 1; pGbc.gridwidth = 4; pGbc.anchor = GridBagConstraints.CENTER;
        payrollPanel.add(computeButton, pGbc);

        row = 2;
        pGbc.gridwidth = 1; pGbc.anchor = GridBagConstraints.WEST;
        addLabelAndField(payrollPanel, pGbc, "Total Hours Worked:", totalHoursLabel = new JLabel(), row++);
        addLabelAndField(payrollPanel, pGbc, "Gross Salary:", grossSalaryLabel = new JLabel(), row++);
        addLabelAndField(payrollPanel, pGbc, "Total Deductions:", deductionsLabel = new JLabel(), row++);
        addLabelAndField(payrollPanel, pGbc, "Net Salary:", netSalaryLabel = new JLabel(), row++);

        add(payrollPanel, BorderLayout.CENTER);

        computeButton.addActionListener(e -> computeAndDisplaySalary());
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String labelText, JLabel valueLabel, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(valueLabel, gbc);
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
    }

    private void displayEmployeeDetails() {
        empNumberLabel.setText(employee.getEmployeeNumber());
        nameLabel.setText(employee.getFullName());
        birthdayLabel.setText(employee.getBirthday());
        sssLabel.setText(employee.getSssNumber());
        philhealthLabel.setText(employee.getPhilhealthNumber());
        tinLabel.setText(employee.getTin());
        pagibigLabel.setText(employee.getPagibigNumber());
        basicSalaryLabel.setText(String.format("PHP %.2f", employee.getBasicSalary()));
        hourlyRateLabel.setText(String.format("PHP %.2f", employee.getHourlyRate()));
    }

    private void computeAndDisplaySalary() {
        if (attendanceRecord == null) {
            JOptionPane.showMessageDialog(this, "Attendance record not available.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int month = monthComboBox.getSelectedIndex();
        int year = Integer.parseInt(yearComboBox.getSelectedItem().toString());

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        Date startDate = cal.getTime();
        cal.set(year, month, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = cal.getTime();

        Map<String, String[]> monthlyAttendance = attendanceRecord.getAttendanceInRange(
                employee.getEmployeeNumber(), startDate, endDate
        );

        if (monthlyAttendance.isEmpty()) {
            totalHoursLabel.setText("N/A");
            grossSalaryLabel.setText("N/A");
            deductionsLabel.setText("N/A");
            netSalaryLabel.setText("N/A");
            JOptionPane.showMessageDialog(this, "No attendance records found for this period.", "No Records", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        long totalMinutes = 0;
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        for (Map.Entry<String, String[]> entry : monthlyAttendance.entrySet()) {
            try {
                Date timeIn = timeFormat.parse(entry.getValue()[0]);
                Date timeOut = timeFormat.parse(entry.getValue()[1]);
                long dailyMinutes = (timeOut.getTime() - timeIn.getTime()) / (60 * 1000);
                totalMinutes += dailyMinutes;
                
            } catch (ParseException e) {
                System.err.println("Error parsing time for " + employee.getEmployeeNumber() + " on " + entry.getKey() + ": " + e.getMessage());
            }
        }

        double totalHours = totalMinutes / 60.0;
        double grossSalary = totalHours * employee.getHourlyRate();

        PayrollCalculator payrollCalculator = new PayrollCalculator();
        double sssDeduction = payrollCalculator.calculateSSS(employee.getBasicSalary());
        double philhealthDeduction = payrollCalculator.calculatePhilHealth(employee.getBasicSalary());
        double pagibigDeduction = payrollCalculator.calculatePagIBIG(employee.getBasicSalary());
        double totalDeductions = sssDeduction + philhealthDeduction + pagibigDeduction;

        double taxableIncome = employee.getBasicSalary() - totalDeductions;
        double withholdingTax = PayrollCalculator.calculateWithholdingTax(taxableIncome);

        double netSalary = grossSalary - totalDeductions - withholdingTax;

        totalHoursLabel.setText(String.format("%.2f hours", totalHours));
        grossSalaryLabel.setText(String.format("PHP %.2f", grossSalary));
        deductionsLabel.setText(String.format("PHP %.2f (SSS: %.2f, PhilHealth: %.2f, PagIbig: %.2f)", totalDeductions, sssDeduction, philhealthDeduction, pagibigDeduction));
        netSalaryLabel.setText(String.format("PHP %.2f", netSalary));
    }
}