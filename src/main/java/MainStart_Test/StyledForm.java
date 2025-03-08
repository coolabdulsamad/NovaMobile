/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainStart_Test;

import javax.swing.*;
import java.awt.*;

public class StyledForm {
    public static void main(String[] args) {
        // Frame and Panel Setup
        JFrame frame = new JFrame("Styled Registration Form");
        JPanel panel = new JPanel();
        panel.setLayout(null); // Using absolute positioning
        panel.setBackground(Color.WHITE);

        // Set frame properties
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.add(panel);
        frame.setLocationRelativeTo(null); // Centering the frame

        // Create ComboBoxes for Date of Birth
        JLabel dobLabel = new JLabel("Date of Birth:");
        dobLabel.setBounds(50, 130, 100, 25);
        dobLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JComboBox<String> monthComboBox = new JComboBox<>();
        monthComboBox.addItem("January");
        monthComboBox.addItem("February");
        monthComboBox.addItem("March");
        monthComboBox.addItem("April");
        monthComboBox.addItem("May");
        monthComboBox.addItem("June");
        monthComboBox.addItem("July");
        monthComboBox.addItem("August");
        monthComboBox.addItem("September");
        monthComboBox.addItem("October");
        monthComboBox.addItem("November");
        monthComboBox.addItem("December");
        monthComboBox.setSelectedItem("January");
        monthComboBox.setBounds(150, 130, 90, 30);
        monthComboBox.setBackground(new Color(255, 255, 255)); // White background
        monthComboBox.setForeground(new Color(50, 50, 50)); // Dark text color
        monthComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        monthComboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JComboBox<String> dayComboBox = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            dayComboBox.addItem(String.valueOf(i));
        }
        dayComboBox.setSelectedItem("1");
        dayComboBox.setBounds(250, 130, 50, 30);
        dayComboBox.setBackground(new Color(255, 255, 255)); 
        dayComboBox.setForeground(new Color(50, 50, 50));
        dayComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        dayComboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JComboBox<String> yearComboBox = new JComboBox<>();
        for (int i = 1900; i <= 2023; i++) {
            yearComboBox.addItem(String.valueOf(i));
        }
        yearComboBox.setSelectedItem("2000");
        yearComboBox.setBounds(310, 130, 70, 30);
        yearComboBox.setBackground(new Color(255, 255, 255));
        yearComboBox.setForeground(new Color(50, 50, 50));
        yearComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        yearComboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // Create ComboBox for Country
        JLabel countryLabel = new JLabel("Country:");
        countryLabel.setBounds(50, 170, 100, 25);
        countryLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JComboBox<String> countryComboBox = new JComboBox<>();
        countryComboBox.addItem("USA");
        countryComboBox.addItem("Canada");
        countryComboBox.addItem("India");
        countryComboBox.addItem("UK");
        countryComboBox.addItem("Australia");
        countryComboBox.addItem("Germany");
        countryComboBox.addItem("France");
        countryComboBox.addItem("Nigeria");
        countryComboBox.setSelectedItem("Nigeria");
        countryComboBox.setBounds(150, 170, 200, 30);
        countryComboBox.setBackground(new Color(255, 255, 255));
        countryComboBox.setForeground(new Color(50, 50, 50));
        countryComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        countryComboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // Create ComboBox for Country Code and TextField for Phone Number
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setBounds(50, 210, 100, 25);
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JComboBox<String> countryCodeComboBox = new JComboBox<>();
        countryCodeComboBox.addItem("+1");
        countryCodeComboBox.addItem("+44");
        countryCodeComboBox.addItem("+91");
        countryCodeComboBox.addItem("+61");
        countryCodeComboBox.addItem("+234");
        countryCodeComboBox.setSelectedItem("+234");
        countryCodeComboBox.setBounds(150, 210, 80, 30);
        countryCodeComboBox.setBackground(new Color(255, 255, 255));
        countryCodeComboBox.setForeground(new Color(50, 50, 50));
        countryCodeComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        countryCodeComboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JTextField phoneNumberField = new JTextField();
        phoneNumberField.setBounds(240, 210, 160, 30);
        phoneNumberField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        phoneNumberField.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneNumberField.setBackground(new Color(255, 255, 255));
        phoneNumberField.setForeground(new Color(50, 50, 50));

        // Add all components to panel
        panel.add(dobLabel);
        panel.add(monthComboBox);
        panel.add(dayComboBox);
        panel.add(yearComboBox);
        panel.add(countryLabel);
        panel.add(countryComboBox);
        panel.add(phoneLabel);
        panel.add(countryCodeComboBox);
        panel.add(phoneNumberField);

        // Display the frame
        frame.setVisible(true);
    }
}

