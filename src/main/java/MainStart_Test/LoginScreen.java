/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainStart_Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginScreen::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Makeup Booking - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 500);
        frame.setLocationRelativeTo(null);
        
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Left Panel (Icon/Logo)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(255, 182, 193)); // Soft Pink background
        leftPanel.setPreferredSize(new Dimension(350, 500));
        JLabel iconLabel = new JLabel(new ImageIcon("C:\\Users\\coola\\OneDrive\\Documents\\NetBeansProjects\\ChatApplication\\src\\main\\resources\\icons\\novamobile.jpg")); // Change 'icon.png' to your actual icon file
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        leftPanel.add(iconLabel, BorderLayout.CENTER);
        
        // Right Panel (Login Form)
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        
        JLabel titleLabel = new JLabel("Welcome Back!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(255, 105, 180)); // Hot Pink
        rightPanel.add(titleLabel, gbc);
        
        gbc.gridy++;
        JTextField emailField = new JTextField(30);
        emailField.setBorder(BorderFactory.createTitledBorder("Email"));
        rightPanel.add(emailField, gbc);
        
        gbc.gridy++;
        JPasswordField passwordField = new JPasswordField(30);
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        rightPanel.add(passwordField, gbc);
        
        gbc.gridy++;
        gbc.gridwidth = 1;
        JCheckBox showPassword = new JCheckBox("Show Password");
        showPassword.setBackground(Color.WHITE);
        showPassword.setForeground(new Color(255, 105, 180));
        showPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (showPassword.isSelected()) {
                    passwordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('*');
                }
            }
        });
        rightPanel.add(showPassword, gbc);
        
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(255, 105, 180)); // Hot Pink
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(200, 45));
        rightPanel.add(loginButton, gbc);
        
        gbc.gridy++;
        JLabel signUpLabel = new JLabel("Don't have an account? Sign up");
        signUpLabel.setForeground(new Color(255, 105, 180));
        signUpLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpLabel.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            frame.dispose(); // Close login window
            SignupScreen.main(null); // Open signup screen
        }
        });
        rightPanel.add(signUpLabel, gbc);
        
        // Add Panels to Main Panel
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }
}


