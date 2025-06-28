/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mphpayroll;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EmployeeManagementFrame extends JFrame {
    private Map<String, Employee> employees;
    private AttendanceRecord attendanceRecord;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JTextField empNumberField, lastNameField, firstNameField; // These are now primarily for search/display
    private JButton viewButton, addButton, updateButton, deleteButton, refreshButton;

    private static final String EMPLOYEES_CSV_PATH = "C:\\Users\\Sid\\Desktop\\Java Projects\\MPHPayroll\\MPHPayroll\\MPHPayroll\\src\\MotorPH_employee_details.csv";
    private static final String ATTENDANCE_CSV_PATH = "C:\\Users\\Sid\\Desktop\\Java Projects\\MPHPayroll\\MPHPayroll\\MPHPayroll\\src\\attendance_record.csv";

    public EmployeeManagementFrame() {
        super("Welcome to MotorPH Payroll Hub - The Filipino's Choice!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        loadData();

        initComponents();
        refreshEmployeeTable();
        updateButtonStates();
    }

    private void loadData() {
        try {
            employees = DataLoader.loadEmployees(EMPLOYEES_CSV_PATH);
            attendanceRecord = new AttendanceRecord();
            DataLoader.loadAttendance(attendanceRecord, ATTENDANCE_CSV_PATH);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading employee data or attendance records: " + e.getMessage() + "\nPlease ensure CSV files are in the correct path.", "Loading Error", JOptionPane.ERROR_MESSAGE);
            employees = new HashMap<>();
            attendanceRecord = new AttendanceRecord();
        }
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Employee Number:"));
        empNumberField = new JTextField(10);
        empNumberField.setEditable(false); // Make non-editable
        searchPanel.add(empNumberField);
        searchPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField(10);
        lastNameField.setEditable(false); // Make non-editable
        searchPanel.add(lastNameField);
        searchPanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField(10);
        firstNameField.setEditable(false); // Make non-editable
        searchPanel.add(firstNameField);
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton);
        topPanel.add(searchPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{
                "Employee #", "Last Name", "First Name", "SSS Number", "PhilHealth Number", "TIN", "Pag-IBIG Number"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        topPanel.add(scrollPane, BorderLayout.CENTER);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        employeeTable.setRowSorter(sorter);

        sorter.setComparator(0, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                try {
                    int num1 = Integer.parseInt(s1);
                    int num2 = Integer.parseInt(s2);
                    return Integer.compare(num1, num2);
                } catch (NumberFormatException e) {
                    return s1.compareTo(s2);
                }
            }
        });

        add(topPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        viewButton = new JButton("View Details");
        addButton = new JButton("New Employee");
        updateButton = new JButton("Update Selected");
        deleteButton = new JButton("Delete Selected");
        refreshButton = new JButton("Refresh Table");

        buttonPanel.add(viewButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addListeners();
    }

    private void addListeners() {
        viewButton.addActionListener(e -> viewEmployeeDetails());
        addButton.addActionListener(e -> openNewEmployeeForm());
        updateButton.addActionListener(e -> openEditEmployeeDialog());
        deleteButton.addActionListener(e -> deleteSelectedEmployee());
        refreshButton.addActionListener(e -> {
            loadData();
            refreshEmployeeTable();
            JOptionPane.showMessageDialog(this, "Employee data refreshed.", "Refresh", JOptionPane.INFORMATION_MESSAGE);
            employeeTable.clearSelection();
        });

        employeeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    displaySelectedEmployeeFields();
                    updateButtonStates();
                }
            }
        });

        JButton searchButton = (JButton)((JPanel) (((JPanel) getContentPane().getComponent(0)).getComponent(0))).getComponent(6);
        searchButton.addActionListener(e -> filterAndSearchEmployee());
        empNumberField.addActionListener(e -> filterAndSearchEmployee());
        lastNameField.addActionListener(e -> filterAndSearchEmployee());
        firstNameField.addActionListener(e -> filterAndSearchEmployee());
    }

    private void updateButtonStates() {
        boolean rowSelected = employeeTable.getSelectedRow() != -1;
        updateButton.setEnabled(rowSelected);
        deleteButton.setEnabled(rowSelected);
        viewButton.setEnabled(rowSelected);
    }

    private void filterAndSearchEmployee() {
        String empNumber = empNumberField.getText().trim();
        String lastName = lastNameField.getText().trim().toLowerCase();
        String firstName = firstNameField.getText().trim().toLowerCase();

        if (!empNumber.isEmpty() && lastName.isEmpty() && firstName.isEmpty()) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(empNumber)) {
                    employeeTable.setRowSelectionInterval(i, i);
                    break;
                }
            }
        } else {
            TableRowSorter<?> sorter = (TableRowSorter<?>) employeeTable.getRowSorter();
            if (sorter == null) {
                return;
            }

            List<RowFilter<Object, Object>> filters = new ArrayList<>();

            if (!empNumber.isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + empNumber, 0));
            }
            if (!lastName.isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + lastName, 1));
            }
            if (!firstName.isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + firstName, 2));
            }

            if (filters.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.andFilter(filters));
            }
            employeeTable.clearSelection();
        }
    }


    private void refreshEmployeeTable() {
        tableModel.setRowCount(0);
        List<Employee> employeeList = new ArrayList<>(employees.values());

        employeeList.sort(Comparator.comparing(Employee::getEmployeeNumber, (s1, s2) -> {
            try {
                int num1 = Integer.parseInt(s1);
                int num2 = Integer.parseInt(s2);
                return Integer.compare(num1, num2);
            } catch (NumberFormatException e) {
                return s1.compareTo(s2);
            }
        }));

        for (Employee emp : employeeList) {
            tableModel.addRow(new Object[]{
                emp.getEmployeeNumber(),
                emp.getLastName(),
                emp.getFirstName(),
                emp.getSssNumber(),
                emp.getPhilhealthNumber(),
                emp.getTin(),
                emp.getPagibigNumber()
            });
        }
        updateButtonStates();
    }

    private void displaySelectedEmployeeFields() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = employeeTable.convertRowIndexToModel(selectedRow);
            String empNum = (String) tableModel.getValueAt(modelRow, 0);
            Employee selectedEmp = employees.get(empNum);

            if (selectedEmp != null) {
                empNumberField.setText(selectedEmp.getEmployeeNumber());
                lastNameField.setText(selectedEmp.getLastName());
                firstNameField.setText(selectedEmp.getFirstName());
            }
        } else {
            empNumberField.setText("");
            lastNameField.setText("");
            firstNameField.setText("");
        }
    }

    private void viewEmployeeDetails() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to view details.", "No Employee Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = employeeTable.convertRowIndexToModel(selectedRow);
        String employeeNumber = (String) tableModel.getValueAt(modelRow, 0);
        Employee selectedEmployee = employees.get(employeeNumber);

        if (selectedEmployee != null) {
            new EmployeeDetailsFrame(selectedEmployee, attendanceRecord).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Employee data not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generateNextEmployeeNumber() {
        Optional<Integer> maxEmpNum = employees.keySet().stream()
            .map(s -> {
                try {
                    return Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    return 0;
                }
            })
            .max(Comparator.naturalOrder());

        int nextNum = maxEmpNum.orElse(0) + 1;
        return String.valueOf(nextNum);
    }

    private void openNewEmployeeForm() {
        String nextEmpNum = generateNextEmployeeNumber();
        new NewEmployeeFormFrame(this, nextEmpNum).setVisible(true);
    }

    public void addNewEmployeeToCSV(Employee newEmployee) {
        if (employees.containsKey(newEmployee.getEmployeeNumber())) {
            JOptionPane.showMessageDialog(this, "Employee with this number already exists. This should not happen if auto-generating.", "Duplicate Employee", JOptionPane.ERROR_MESSAGE);
            return;
        }

        employees.put(newEmployee.getEmployeeNumber(), newEmployee);
        writeAllEmployeesToCSV();
        refreshEmployeeTable();
        JOptionPane.showMessageDialog(this, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        employeeTable.clearSelection();
    }

    private void openEditEmployeeDialog() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to update.", "No Employee Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = employeeTable.convertRowIndexToModel(selectedRow);
        String employeeNumber = (String) tableModel.getValueAt(modelRow, 0);
        Employee employeeToEdit = employees.get(employeeNumber);

        if (employeeToEdit != null) {
            EditEmployeeDialog dialog = new EditEmployeeDialog(this, employeeToEdit);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Selected employee data not found. This might indicate data inconsistency.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateEmployeeAndRefresh(String originalEmployeeNumber, Employee updatedEmployee) {
        if (!originalEmployeeNumber.equals(updatedEmployee.getEmployeeNumber())) {
            if (employees.containsKey(updatedEmployee.getEmployeeNumber())) {
                JOptionPane.showMessageDialog(this, "The new Employee Number '" + updatedEmployee.getEmployeeNumber() + "' already exists for another employee. Update cancelled.", "Duplicate Employee Number", JOptionPane.ERROR_MESSAGE);
                return;
            }
            employees.remove(originalEmployeeNumber);
        }
        employees.put(updatedEmployee.getEmployeeNumber(), updatedEmployee);

        writeAllEmployeesToCSV();
        refreshEmployeeTable();
        employeeTable.clearSelection();
    }


    private void deleteSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to delete.", "No Employee Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = employeeTable.convertRowIndexToModel(selectedRow);
        String employeeNumberToDelete = (String) tableModel.getValueAt(modelRow, 0);
        String employeeName = (String) tableModel.getValueAt(modelRow, 2) + " " + (String) tableModel.getValueAt(modelRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete employee " + employeeName + " (#" + employeeNumberToDelete + ")?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            employees.remove(employeeNumberToDelete);
            writeAllEmployeesToCSV();
            refreshEmployeeTable();
            JOptionPane.showMessageDialog(this, "Employee deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            employeeTable.clearSelection();
        }
    }

    private void writeAllEmployeesToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EMPLOYEES_CSV_PATH))) {
            writer.println("Employee #,Last Name,First Name,Birthday,Address,Phone Number,SSS #,Philhealth #,TIN,Pag-IBIG #,Status,Position,Immediate Supervisor,Basic Salary,Rice Subsidy,Phone Allowance,Clothing Allowance,Gross Semi-monthly Rate,Hourly Rate");
            for (Employee emp : employees.values()) {
                writer.printf("%s,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%.2f,%.2f,%.2f,%.2f,%.2f,%.2f%n",
                        emp.getEmployeeNumber(),
                        emp.getLastName(),
                        emp.getFirstName(),
                        emp.getBirthday(),
                        emp.getAddress(),
                        emp.getPhoneNumber(),
                        emp.getSssNumber(),
                        emp.getPhilhealthNumber(),
                        emp.getTin(),
                        emp.getPagibigNumber(),
                        emp.getStatus(),
                        emp.getPosition(),
                        emp.getImmediateSupervisor(),
                        emp.getBasicSalary(),
                        emp.getRiceSubsidy(),
                        emp.getPhoneAllowance(),
                        emp.getClothingAllowance(),
                        emp.getGrossSemiMonthlyRate(),
                        emp.getHourlyRate()
                );
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving employee data: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}