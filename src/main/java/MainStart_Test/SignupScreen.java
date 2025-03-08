/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainStart_Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignupScreen {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignupScreen::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Makeup Booking - Signup");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 650);
        frame.setLocationRelativeTo(null);
        
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Left Panel (Icon/Logo)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(255, 182, 193)); // Soft Pink background
        leftPanel.setPreferredSize(new Dimension(350, 650));
        JLabel iconLabel = new JLabel(new ImageIcon("C:\\Users\\coola\\OneDrive\\Documents\\NetBeansProjects\\ChatApplication\\src\\main\\resources\\icons\\novamobile.jpg")); // Change 'icon.png' to your actual icon file
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        leftPanel.add(iconLabel, BorderLayout.CENTER);
        
        // Right Panel (Signup Form)
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel titleLabel = new JLabel("Create an Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(255, 105, 180)); // Hot Pink
        rightPanel.add(titleLabel, gbc);
        
        gbc.gridy++;
        JTextField nameField = new JTextField(30);
        nameField.setBorder(BorderFactory.createTitledBorder("Full Name"));
        rightPanel.add(nameField, gbc);
        
        gbc.gridy++;
        JTextField emailField = new JTextField(30);
        emailField.setBorder(BorderFactory.createTitledBorder("Email"));
        rightPanel.add(emailField, gbc);
        
        gbc.gridy++;
        JPasswordField passwordField = new JPasswordField(30);
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        rightPanel.add(passwordField, gbc);
        
        gbc.gridy++;
        JPasswordField confirmPasswordField = new JPasswordField(30);
        confirmPasswordField.setBorder(BorderFactory.createTitledBorder("Confirm Password"));
        rightPanel.add(confirmPasswordField, gbc);
        
        gbc.gridy++;
        JTextField secretKeyField = new JTextField(30);
        secretKeyField.setBorder(BorderFactory.createTitledBorder("Secret Key (Optional for Admins)"));
        rightPanel.add(secretKeyField, gbc);
        
        gbc.gridy++;
        JCheckBox showPassword = new JCheckBox("Show Password");
        showPassword.setBackground(Color.WHITE);
        showPassword.setForeground(new Color(255, 105, 180));
        showPassword.setFocusPainted(false);
        showPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (showPassword.isSelected()) {
                    passwordField.setEchoChar((char) 0);
                    confirmPasswordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('*');
                    confirmPasswordField.setEchoChar('*');
                }
            }
        });
        gbc.gridwidth = 1;
        rightPanel.add(showPassword, gbc);
        gbc.gridwidth = 2;
        
        gbc.gridy++;
        JButton signupButton = new JButton("Sign Up");
        signupButton.setBackground(new Color(255, 105, 180)); // Hot Pink
        signupButton.setForeground(Color.WHITE);
        signupButton.setFont(new Font("Arial", Font.BOLD, 14));
        signupButton.setPreferredSize(new Dimension(200, 45));
        rightPanel.add(signupButton, gbc);
        
        gbc.gridy++;
        JLabel loginLabel = new JLabel("Already have an account? Login");
        loginLabel.setForeground(new Color(255, 105, 180));
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                frame.dispose(); // Close signup window
                LoginScreen.main(null); // Open login screen
            }
        });
        rightPanel.add(loginLabel, gbc);
        
        // Add Panels to Main Panel
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
