/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainStart_Test;

// Importing necessary packages
import javax.swing.*;
import java.awt.*;

public class RegistrationForm {
    public static void main(String[] args) {
        // Frame and Panel Setup
        JFrame frame = new JFrame("Registration Form");
        JPanel panel = new JPanel();
        panel.setLayout(null); // Using absolute positioning
        panel.setBackground(Color.WHITE);

        // Set frame properties
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.add(panel);
        frame.setLocationRelativeTo(null); // Centering the frame

        // Create First Name Label and Field
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setBounds(50, 50, 100, 25);
        JTextField firstNameField = new JTextField();
        firstNameField.setBounds(150, 50, 200, 30);
        firstNameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // Create Last Name Label and Field
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setBounds(50, 90, 100, 25);
        JTextField lastNameField = new JTextField();
        lastNameField.setBounds(150, 90, 200, 30);
        lastNameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // Create Date of Birth Label and ComboBoxes
        JLabel dobLabel = new JLabel("Date of Birth:");
        dobLabel.setBounds(50, 130, 100, 25);

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

        JComboBox<String> dayComboBox = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            dayComboBox.addItem(String.valueOf(i));
        }
        dayComboBox.setSelectedItem("1");
        dayComboBox.setBounds(250, 130, 50, 30);

        JComboBox<String> yearComboBox = new JComboBox<>();
        for (int i = 1900; i <= 2023; i++) {
            yearComboBox.addItem(String.valueOf(i));
        }
        yearComboBox.setSelectedItem("2000");
        yearComboBox.setBounds(310, 130, 70, 30);

        // Create Country Label and ComboBox
        JLabel countryLabel = new JLabel("Country:");
        countryLabel.setBounds(50, 170, 100, 25);
        JComboBox<String> countryComboBox = new JComboBox<>();
        countryComboBox.addItem("USA");
        countryComboBox.addItem("Canada");
        countryComboBox.addItem("India");
        countryComboBox.addItem("UK");
        countryComboBox.addItem("Australia");
        countryComboBox.addItem("Germany");
        countryComboBox.addItem("France");
        countryComboBox.addItem("Nigeria");
        countryComboBox.setSelectedItem("USA");
        countryComboBox.setBounds(150, 170, 200, 30);

        // Create Phone Number Label, ComboBox for Country Code, and TextField for Phone Number
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setBounds(50, 210, 100, 25);

        JComboBox<String> countryCodeComboBox = new JComboBox<>();
        countryCodeComboBox.addItem("+1");
        countryCodeComboBox.addItem("+44");
        countryCodeComboBox.addItem("+91");
        countryCodeComboBox.addItem("+61");
        countryCodeComboBox.addItem("+234");
        countryCodeComboBox.setSelectedItem("+234");
        countryCodeComboBox.setBounds(150, 210, 80, 30);

        JTextField phoneNumberField = new JTextField();
        phoneNumberField.setBounds(240, 210, 160, 30);
        phoneNumberField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // Add components to the panel
        panel.add(firstNameLabel);
        panel.add(firstNameField);
        panel.add(lastNameLabel);
        panel.add(lastNameField);
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
