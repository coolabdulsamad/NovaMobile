/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import Application_Connector.db.DatabaseHelper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ForgotPasswordRequestPage extends JFrame {
    private JTextField emailField;
    private JButton submitButton;

    public ForgotPasswordRequestPage() {
        setTitle("Forgot Password");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        emailField = new JTextField();
        emailField.setBorder(BorderFactory.createTitledBorder("Enter Your Registered Email"));

        submitButton = new JButton("Submit");

        panel.add(emailField);
        panel.add(submitButton);

        add(panel, BorderLayout.CENTER);

        // Button click action
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText().trim();
                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter your email address.");
                } else {
                    checkEmailInDatabase(email);
                }
            }
        });

        setLocationRelativeTo(null);
    }

    private void checkEmailInDatabase(String email) {
        try (Connection connection = DatabaseHelper.connect()) {
            String query = "SELECT user_id FROM Users WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                JOptionPane.showMessageDialog(this, "Email found! User ID: " + userId);
                // Proceed to Step 3 (Token Generation) - We will handle this next.
                generateResetToken(userId, email);
            } else {
                JOptionPane.showMessageDialog(this, "Email not found. Please try again.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
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

private void generateResetToken(int userId, String email) {
    String token = TokenGenerator.generateToken();
    
    

    
    try (Connection connection = DatabaseHelper.connect()) {
        cleanExpiredTokens(connection);
        String query = "INSERT INTO ResetTokens (user_id, token, expires_at) VALUES (?, ?, datetime('now', '+1 hour'))";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        statement.setString(2, token);
        int rowsInserted = statement.executeUpdate();

        if (rowsInserted > 0) {
            // Send email
            boolean emailSent = EmailSender.sendEmail(email, token);
            if (emailSent) {
                JOptionPane.showMessageDialog(this, "Reset token generated and sent to your email: " + email);
                this.dispose();
                ResetPasswordPage ResetPasswordPage = new ResetPasswordPage();
                ResetPasswordPage.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Token generated but failed to send email.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to generate reset token.");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
    }
}



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ForgotPasswordRequestPage().setVisible(true);
        });
    }
}


