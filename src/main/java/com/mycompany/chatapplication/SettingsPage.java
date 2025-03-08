/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class SettingsPage {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Settings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 450);
        frame.setLayout(new BorderLayout());
        // Set the icon for the frame (you can change the path to your icon)
        
        URL iconURL = MainTab.class.getClassLoader().getResource("icons/novamobile.jpg");
if (iconURL != null) {
    frame.setIconImage(new ImageIcon(iconURL).getImage());
} else {
    System.err.println("Icon image not found.");
}
        frame.setIconImage(new ImageIcon("C:\\Users\\coola\\Documents\\NetBeansProjects\\ChatApplication\\src\\main\\resources\\icons\\novamobile.jpg").getImage()); // Update the icon path
        
        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(7, 1, 5, 5));
        buttonPanel.setPreferredSize(new Dimension(220, frame.getHeight()));
        buttonPanel.setBackground(new Color(240, 240, 240));
        
        JButton accountSettings = new JButton("Account Settings");
        JButton generalSettings = new JButton("General Settings");
        JButton personalization = new JButton("Personalization");
        JButton notificationSettings = new JButton("Notification Settings");
        JButton storageSettings = new JButton("Storage Settings");
        JButton helpSupport = new JButton("Help & Support");
        JButton securitySettings = new JButton("Security Settings");
        
        JButton[] buttons = {accountSettings, generalSettings, personalization, notificationSettings, storageSettings, helpSupport, securitySettings};
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        JLabel contentLabel = new JLabel("Select an option from the left", SwingConstants.CENTER);
        contentLabel.setFont(new Font("Arial", Font.BOLD, 16));
        contentPanel.add(contentLabel, BorderLayout.CENTER);
        
        for (JButton button : buttons) {
            button.setFocusPainted(false);
            button.setBackground(Color.WHITE);
            button.setFont(new Font("Arial", Font.PLAIN, 14));
            button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (button == accountSettings) {
                        contentPanel.removeAll();
                        JPanel accountPanel = new JPanel();
                        accountPanel.setLayout(new GridLayout(4, 2, 10, 10));
                        accountPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                        
                        JLabel firstNameLabel = new JLabel("First Name:");
                        JTextField firstNameField = new JTextField();
                        JLabel lastNameLabel = new JLabel("Last Name:");
                        JTextField lastNameField = new JTextField();
                        JLabel phoneLabel = new JLabel("Phone Number:");
                        JTextField phoneField = new JTextField();
                        JButton changePasswordButton = new JButton("Change Password");
                        JButton deleteAccountButton = new JButton("Delete Account");
                        
                        accountPanel.add(firstNameLabel);
                        accountPanel.add(firstNameField);
                        accountPanel.add(lastNameLabel);
                        accountPanel.add(lastNameField);
                        accountPanel.add(phoneLabel);
                        accountPanel.add(phoneField);
                        accountPanel.add(changePasswordButton);
                        accountPanel.add(deleteAccountButton);
                        
                        contentPanel.add(accountPanel, BorderLayout.CENTER);
                        contentPanel.revalidate();
                        contentPanel.repaint();
                    } else {
                        contentLabel.setText(button.getText());
                        contentPanel.removeAll();
                        contentPanel.add(contentLabel, BorderLayout.CENTER);
                        contentPanel.revalidate();
                        contentPanel.repaint();
                    }
                }
            });
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(220, 220, 220));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(Color.WHITE);
                }
            });
            buttonPanel.add(button);
        }
        
        // Adding components to frame
        frame.add(buttonPanel, BorderLayout.WEST);
        frame.add(contentPanel, BorderLayout.CENTER);
        
        frame.setVisible(true);
    }
}
