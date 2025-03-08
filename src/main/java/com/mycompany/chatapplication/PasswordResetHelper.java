/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import static com.sun.glass.ui.Cursor.setVisible;
import java.awt.GridLayout;
import java.security.SecureRandom;
import java.sql.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class PasswordResetHelper {

    private static final String DB_URL = "jdbc:sqlite:chatting_app.db";
    private static final String DB_USER = "yourdbuser";
    private static final String DB_PASS = "yourdbpassword";

    // Generate a secure random token
    public static String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return bytesToHex(bytes);
    }

    // Save the token to the database
    public static void saveToken(String email, String token) throws SQLException {
        long expirationTime = System.currentTimeMillis() + (60 * 60 * 1000); // 1 hour expiration time
        Timestamp expiresAt = new Timestamp(expirationTime);

        String query = "INSERT INTO password_reset_tokens (email, token, expires_at) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, token);
            stmt.setTimestamp(3, expiresAt);
            stmt.executeUpdate();
        }
    }

    // Utility method to convert bytes to a hexadecimal string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
    
    // Verify the token from the database
    public static boolean verifyToken(String token) throws SQLException {
        String query = "SELECT email, expires_at FROM password_reset_tokens WHERE token = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Timestamp expiresAt = rs.getTimestamp("expires_at");
                if (System.currentTimeMillis() < expiresAt.getTime()) {
                    return true; // Token is valid
                }
            }
            return false; // Token is invalid or expired
        }
    }
    
    // Update the password for the user
    public static boolean updatePassword(String email, String newPassword) throws SQLException {
        String query = "UPDATE users SET password = ? WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newPassword); // Hash the password before storing it in a real application
            stmt.setString(2, email);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0; // If update was successful
        }
    }

    // Optionally, you can delete the reset token after use to prevent re-use
    public static void deleteToken(String token) throws SQLException {
        String query = "DELETE FROM password_reset_tokens WHERE token = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, token);
            stmt.executeUpdate();
        }
    }
    
    public class ResetPasswordPage extends JFrame {

    private JTextField tokenField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    public ResetPasswordPage(String token) {
        setTitle("Reset Password");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tokenField = new JTextField(token);
        tokenField.setEditable(false);
        
        newPasswordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        
        JButton resetButton = new JButton("Reset Password");
        resetButton.addActionListener(e -> handlePasswordReset(token));
        
        setLayout(new GridLayout(4, 2));
        add(new JLabel("Token:"));
        add(tokenField);
        add(new JLabel("New Password:"));
        add(newPasswordField);
        add(new JLabel("Confirm Password:"));
        add(confirmPasswordField);
        add(resetButton);
        
        setVisible(true);
    }

    private void handlePasswordReset(String token) {
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        try {
            if (newPassword.equals(confirmPassword)) {
                // Verify token
                boolean isTokenValid = PasswordResetHelper.verifyToken(token);
                if (isTokenValid) {
                    // Update the password
                    String email = getEmailFromToken(token); // Retrieve the email from the token if needed
                    boolean isPasswordUpdated = PasswordResetHelper.updatePassword(email, newPassword);
                    if (isPasswordUpdated) {
                        // Delete the token
                        PasswordResetHelper.deleteToken(token);
                        JOptionPane.showMessageDialog(this, "Password reset successful!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to update the password.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid or expired token.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Passwords do not match. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred. Please try again later.");
        }
    }

    private String getEmailFromToken(String token) {
        // Retrieve the email from the token stored in the database
        // For simplicity, this can be added to the token table or retrieved from the token data itself
        return "user@example.com"; // Placeholder: implement actual logic
    }
}

}

