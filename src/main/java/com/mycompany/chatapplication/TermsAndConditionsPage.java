/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TermsAndConditionsPage extends JFrame {
    private JCheckBox termsAndConditionsCheckBox;
    private JButton signupButton;

    // Constructor accepting checkbox and button as parameters
    public TermsAndConditionsPage(JCheckBox termsAndConditionsCheckBox, JButton signupButton) {
        this.termsAndConditionsCheckBox = termsAndConditionsCheckBox;
        this.signupButton = signupButton;

        // Set up the frame
        setTitle("Terms and Conditions - Novamobile");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen
        setResizable(false); // Disable window resizing for consistency
        // Set the icon for the frame (you can change the path to your icon)
        setIconImage(new ImageIcon("C:\\Users\\coola\\Documents\\NetBeansProjects\\ChatApplication\\src\\main\\resources\\icons\\novamobile.jpg").getImage()); // Update the icon path

        // Create a panel to hold the content and style it
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(245, 245, 245)); // Light gray background
        add(contentPanel);

        // Add title label
        JLabel titleLabel = new JLabel("Terms and Conditions", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204)); // Blue color for the title
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        // Add a scrollable text area for the terms and conditions
        JTextArea textArea = new JTextArea();
        textArea.setText("Welcome to Novamobile!\n\nHere are the terms and conditions for using this chat application:\n\n"
                + "1. You agree to provide accurate and truthful information when signing up for the Novamobile service.\n"
                + "2. You agree not to share any illegal, harmful, or offensive content through the platform.\n"
                + "3. Novamobile is not responsible for any content shared between users.\n"
                + "4. You agree to respect other users' privacy and not to share their personal information without consent.\n"
                + "5. You must be at least 13 years of age to use Novamobile. If under 18, parental consent is required.\n"
                + "6. Novamobile may monitor conversations for safety and quality purposes, but will not interfere unless necessary.\n"
                + "7. You agree not to engage in any spamming, phishing, or other malicious activities.\n"
                + "8. Novamobile is not responsible for any interruptions, delays, or outages in the service.\n"
                + "9. You are solely responsible for the security of your account, including your password.\n"
                + "10. Novamobile reserves the right to suspend or terminate any account that violates these terms.\n"
                + "11. By using the platform, you consent to Novamobile collecting certain data, as described in our privacy policy.\n"
                + "12. You agree not to reverse engineer, decompile, or otherwise attempt to access the source code of Novamobile.\n"
                + "13. Novamobile is not responsible for any third-party links or external content that you may access through the platform.\n"
                + "14. You agree to notify Novamobile immediately if you suspect unauthorized access to your account.\n"
                + "15. Novamobile reserves the right to modify these terms at any time, and you will be notified of any changes.\n\n"
                + "Please read and accept the terms and conditions before proceeding.");
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setBackground(new Color(255, 255, 255)); // White background for the text area
        textArea.setCaretColor(Color.BLACK); // Black caret
        JScrollPane scrollPane = new JScrollPane(textArea);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Create a footer panel to hold the Accept and Cancel buttons
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(245, 245, 245)); // Light gray background for footer panel
        contentPanel.add(footerPanel, BorderLayout.SOUTH); // Add it to the main content panel

        // Create the Accept and Cancel buttons
        JButton agreeButton = new JButton("Accept");
        agreeButton.setFont(new Font("Arial", Font.BOLD, 14));
        agreeButton.setBackground(new Color(0, 204, 102)); // Green for agree
        agreeButton.setForeground(Color.WHITE);
        footerPanel.add(agreeButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBackground(new Color(255, 102, 102)); // Red for cancel
        cancelButton.setForeground(Color.WHITE);
        footerPanel.add(cancelButton);

        // Action listeners for Accept and Cancel buttons
        agreeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                termsAndConditionsCheckBox.setSelected(true); // Automatically tick the checkbox
                signupButton.setEnabled(true); // Enable the Signup button when accepted
                dispose(); // Close the Terms and Conditions page
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                termsAndConditionsCheckBox.setSelected(false); // Untick the checkbox
                signupButton.setEnabled(false); // Disable the Signup button when cancelled
                dispose(); // Close the Terms and Conditions page
            }
        });
    }
}
