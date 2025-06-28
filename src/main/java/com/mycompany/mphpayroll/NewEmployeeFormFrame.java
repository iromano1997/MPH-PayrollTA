/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mphpayroll;

import javax.swing.*;
import java.awt.*;

public class NewEmployeeFormFrame extends JFrame {
    private EmployeeManagementFrame parentFrame;
    private JTextField empNumberField, lastNameField, firstNameField;
    private JTextField sssField, philhealthField, tinField, pagibigField;
    private JButton submitButton;

    private String generatedEmpNumber;

    public NewEmployeeFormFrame(EmployeeManagementFrame parent, String generatedEmpNumber) {
        super("Add New Employee (Fields with asterisk (*) are important.)");
        this.parentFrame = parent;
        this.generatedEmpNumber = generatedEmpNumber;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        initComponents();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        empNumberField = new JTextField(15);
        empNumberField.setText(generatedEmpNumber);
        empNumberField.setEditable(false);
        empNumberField.setBackground(Color.LIGHT_GRAY);


        lastNameField = new JTextField(15);
        firstNameField = new JTextField(15);
        sssField = new JTextField(15);
        philhealthField = new JTextField(15);
        tinField = new JTextField(15);
        pagibigField = new JTextField(15);

        formPanel.add(new JLabel("Employee Number:"));
        formPanel.add(empNumberField);
        formPanel.add(new JLabel("*Last Name:"));
        formPanel.add(lastNameField);
        formPanel.add(new JLabel("*First Name:"));
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("*SSS No.:"));
        formPanel.add(sssField);
        formPanel.add(new JLabel("*PhilHealth No.:"));
        formPanel.add(philhealthField);
        formPanel.add(new JLabel("*TIN:"));
        formPanel.add(tinField);
        formPanel.add(new JLabel("*Pag-IBIG No.:"));
        formPanel.add(pagibigField);

        add(formPanel, BorderLayout.CENTER);

        submitButton = new JButton("Add Employee");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(submitButton);
        add(buttonPanel, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> addNewEmployee());
    }

    private void addNewEmployee() {
        String empNumber = empNumberField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String sss = sssField.getText().trim();
        String philhealth = philhealthField.getText().trim();
        String tin = tinField.getText().trim();
        String pagibig = pagibigField.getText().trim();

        if (empNumber.isEmpty() || lastName.isEmpty() || firstName.isEmpty() ||
            sss.isEmpty() || philhealth.isEmpty() || tin.isEmpty() || pagibig.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields (Employee Number, Names, SSS, PhilHealth, TIN, Pag-IBIG).", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Employee newEmployee = new Employee(empNumber, firstName, lastName,
                                                 "N/A", "N/A", "N/A", sss, philhealth, tin, pagibig, "Regular", "N/A", "N/A",
                                                 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

            parentFrame.addNewEmployeeToCSV(newEmployee);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}