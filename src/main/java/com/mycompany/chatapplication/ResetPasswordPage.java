/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import Application_Connector.db.DatabaseHelper;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Application_Connector.db.PasswordUtils;
import java.util.regex.Pattern;

public class ResetPasswordPage extends JFrame {
    private JTextField tokenField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton resetPasswordButton;

    public ResetPasswordPage() {
        setTitle("Reset Password - Novamobile");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel tokenLabel = new JLabel("Enter Token:");
        tokenLabel.setBounds(30, 30, 100, 25);
        add(tokenLabel);

        tokenField = new JTextField();
        tokenField.setBounds(150, 30, 200, 25);
        add(tokenField);

        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setBounds(30, 80, 100, 25);
        add(newPasswordLabel);

        newPasswordField = new JPasswordField();
        newPasswordField.setBounds(150, 80, 200, 25);
        add(newPasswordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds(30, 130, 150, 25);
        add(confirmPasswordLabel);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(150, 130, 200, 25);
        add(confirmPasswordField);

        resetPasswordButton = new JButton("Reset Password");
        resetPasswordButton.setBounds(130, 180, 150, 30);
        add(resetPasswordButton);

        resetPasswordButton.addActionListener(e -> resetPassword());

        setLocationRelativeTo(null);
    }

            public void cleanExpiredTokens(Connection connection) {
    String cleanupQuery = "DELETE FROM ResetTokens WHERE expires_at <= CURRENT_TIMESTAMP";
    try (PreparedStatement statement = connection.prepareStatement(cleanupQuery)) {
        int affectedRows = statement.executeUpdate();
        System.out.println(affectedRows + " expired tokens deleted.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    private void resetPassword() {
        String token = tokenField.getText().trim();
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String password = new String(newPasswordField.getPassword());

        if (token.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            return;
        }
        
         // Validate password
        if (!isValidPassword(password)) {
            JOptionPane.showMessageDialog(this, "Password must be 8-12 characters long and include at least one special character.");
            return;
        }

        try (Connection connection = DatabaseHelper.connect()) {
            cleanExpiredTokens(connection);

            String tokenQuery = "SELECT user_id FROM ResetTokens WHERE token = ? AND expires_at > CURRENT_TIMESTAMP";
            PreparedStatement tokenStatement = connection.prepareStatement(tokenQuery);
            tokenStatement.setString(1, token);
            ResultSet tokenResult = tokenStatement.executeQuery();

            if (!tokenResult.next()) {
                JOptionPane.showMessageDialog(this, "Invalid or expired token!");
                return;
            }

            int userId = tokenResult.getInt("user_id");

            // Hash the new password using BCrypt
            String hashedPassword = PasswordUtils.hashPassword(newPassword);

            String updatePasswordQuery = "UPDATE Users SET password = ? WHERE user_id = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updatePasswordQuery);
            updateStatement.setString(1, hashedPassword);
            updateStatement.setInt(2, userId);

            int rowsUpdated = updateStatement.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Password reset successfully!");

                // Clean up the token after successful reset
                String deleteTokenQuery = "DELETE FROM ResetTokens WHERE token = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteTokenQuery);
                deleteStatement.setString(1, token);
                deleteStatement.executeUpdate();

                dispose(); // Close the window after successful reset
            } else {
                JOptionPane.showMessageDialog(this, "Failed to reset password.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }
    
    // Helper method to validate password
    private static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,12}$";
        return Pattern.matches(passwordRegex, password);
    }
}


