/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author coola
 */
public class MainTab2 {
        public static JFrame frame;
       public static void main(String[] args) {
        // Create JFrame
        frame = new JFrame("Login Page");
        frame.setSize(1200, 1200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout()); // Use GridBagLayout to center components
        frame.getContentPane().setBackground(new Color(225, 233, 241)); // Background color
        frame.setUndecorated(true);
        // Set the icon for the frame (you can change the path to your icon)
        
        URL iconURL = MainTab.class.getClassLoader().getResource("icons/novamobile.jpg");
if (iconURL != null) {
    frame.setIconImage(new ImageIcon(iconURL).getImage());
} else {
    System.err.println("Icon image not found.");
}
        
        frame.setIconImage(new ImageIcon("C:\\Users\\coola\\Documents\\NetBeansProjects\\ChatApplication\\src\\main\\resources\\icons\\novamobile.jpg").getImage()); // Update the icon path


        // Create Login Panel
        JPanel loginPanel = SignupPage.createSignUpPanel(); // Use a static method to create the login panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        //or
        //gbc.anchor = GridBagConstraints.NORTHWEST;
        //or
        //gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 0, 0, 0);

        // Add Login Panel to Frame
        frame.add(loginPanel, gbc);

        // Center the JFrame on the screen
        frame.setLocationRelativeTo(null);

        // Make JFrame visible
        frame.setVisible(true);
        
    }
}
