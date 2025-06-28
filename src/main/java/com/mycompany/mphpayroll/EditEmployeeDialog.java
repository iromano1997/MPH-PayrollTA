/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mphpayroll;

import javax.swing.*;
import java.awt.*;

public class EditEmployeeDialog extends JDialog {
    private Employee employee;
    private EmployeeManagementFrame parentFrame;
    private String originalEmployeeNumber;

    private JTextField empNumberField;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField sssNumberField;
    private JTextField philhealthNumberField;
    private JTextField tinField;
    private JTextField pagibigNumberField;

    private JButton saveButton;
    private JButton cancelButton;

    public EditEmployeeDialog(EmployeeManagementFrame parentFrame, Employee employee) {
        super(parentFrame, "Edit Employee Details: " + employee.getFullName(), true);
        this.parentFrame = parentFrame;
        this.employee = employee;
        this.originalEmployeeNumber = employee.getEmployeeNumber();

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(450, 400);
        setLocationRelativeTo(parentFrame);
        setLayout(new BorderLayout(10, 10));

        initComponents();
        populateFields();
        addListeners();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        addField(formPanel, gbc, "Employee Number:", empNumberField = new JTextField(), row++);
        addField(formPanel, gbc, "Last Name:", lastNameField = new JTextField(), row++);
        addField(formPanel, gbc, "First Name:", firstNameField = new JTextField(), row++);
        addField(formPanel, gbc, "SSS Number:", sssNumberField = new JTextField(), row++);
        addField(formPanel, gbc, "PhilHealth Number:", philhealthNumberField = new JTextField(), row++);
        addField(formPanel, gbc, "TIN:", tinField = new JTextField(), row++);
        addField(formPanel, gbc, "Pag-IBIG Number:", pagibigNumberField = new JTextField(), row++);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("Save Changes");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String labelText, JTextField textField, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.0;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        textField.setPreferredSize(new Dimension(150, 25));
        panel.add(textField, gbc);
    }

    private void populateFields() {
        empNumberField.setText(employee.getEmployeeNumber());
        lastNameField.setText(employee.getLastName());
        firstNameField.setText(employee.getFirstName());
        sssNumberField.setText(employee.getSssNumber());
        philhealthNumberField.setText(employee.getPhilhealthNumber());
        tinField.setText(employee.getTin());
        pagibigNumberField.setText(employee.getPagibigNumber());
    }

    private void addListeners() {
        saveButton.addActionListener(e -> saveEmployeeDetails());
        cancelButton.addActionListener(e -> dispose());
    }

    private void saveEmployeeDetails() {
        String newEmpNumber = empNumberField.getText().trim();
        String newLastName = lastNameField.getText().trim();
        String newFirstName = firstNameField.getText().trim();
        String newSssNumber = sssNumberField.getText().trim();
        String newPhilhealthNumber = philhealthNumberField.getText().trim();
        String newTin = tinField.getText().trim();
        String newPagibigNumber = pagibigNumberField.getText().trim();

        if (newEmpNumber.isEmpty() || newLastName.isEmpty() || newFirstName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Employee Number, Last Name, and First Name are required.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        employee.setEmployeeNumber(newEmpNumber);
        employee.setLastName(newLastName);
        employee.setFirstName(newFirstName);
        employee.setSssNumber(newSssNumber);
        employee.setPhilhealthNumber(newPhilhealthNumber);
        employee.setTin(newTin);
        employee.setPagibigNumber(newPagibigNumber);

        parentFrame.updateEmployeeAndRefresh(originalEmployeeNumber, employee);

        JOptionPane.showMessageDialog(this, "Employee details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose(); // Close
    }
}