/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mphpayroll;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private static final String ACCOUNTS_FILE = "C:\\Users\\Sid\\Desktop\\Java Projects\\MPHPayroll\\MPHPayroll\\MPHPayroll\\src\\user_accounts.csv";
    private Map<String, User> users;

    public AuthService() {
        users = new HashMap<>();
        ensureAccountsFileExists();
        loadUsers();
    }

    private void ensureAccountsFileExists() {
        try {
            java.nio.file.Path path = java.nio.file.Paths.get(ACCOUNTS_FILE);
            if (!java.nio.file.Files.exists(path)) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(ACCOUNTS_FILE))) {
                    writer.println("username,password_hash");
                }
            }
        } catch (IOException e) {
            System.err.println("Error ensuring accounts file exists: " + e.getMessage());
        }
    }

    private void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    if (!line.trim().equals("username,password_hash")) {
                        System.err.println("Warning: user_accounts.csv header is not as expected. Expected 'username,password_hash'");
                    }
                    isHeader = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.put(parts[0].trim(), new User(parts[0].trim(), parts[1].trim()));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading user accounts: " + e.getMessage());
        }
    }

    private void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ACCOUNTS_FILE))) {
            writer.println("username,password_hash");
            for (User user : users.values()) {
                writer.println(user.getUsername() + "," + user.getPassword());
            }
        } catch (IOException e) {
            System.err.println("Error saving user accounts: " + e.getMessage());
        }
    }

    public boolean authenticate(String username, String password) {
        User user = users.get(username);
        if (user == null) {
            return false;
        }
        String hashedPasswordInput = hashPassword(password);
        return hashedPasswordInput != null && hashedPasswordInput.equals(user.getPassword());
    }

    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        String hashedPassword = hashPassword(password);
        if (hashedPassword == null) {
            return false;
        }
        users.put(username, new User(username, hashedPassword));
        saveUsers();
        return true;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("SHA-256 algorithm not found: " + e.getMessage());
            return null;
        }
    }
}