/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import Application_Connector.db.DatabaseHelper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Pattern;



public class SignupPage {

    public static JPanel createSignUpPanel(){  
        
        
        // Left panel for login form
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(450, 650));
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
       
        // Logo
        JLabel logoLabel = new JLabel("NovaMobile", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setBounds(100, 10, 250, 40);
        logoLabel.setForeground(new Color(0, 123, 255));
        panel.add(logoLabel);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Please Kindly Register for our App.", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setBounds(100, 50, 250, 30);
        subtitleLabel.setForeground(Color.GRAY);
        panel.add(subtitleLabel);

        // Create JLabel for Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 80, 80, 25);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        userLabel.setForeground(new Color(0, 123, 255));
        panel.add(userLabel);
        
        // Create a panel to hold the email field and the icon
        JPanel userPanel = new JPanel();
        userPanel.setLayout(null); // Absolute positioning
        userPanel.setBounds(50, 100, 330, 30);
        userPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        userPanel.setBackground(Color.WHITE);
        panel.add(userPanel);

        // Load and resize email icon
        ImageIcon userIcon = new ImageIcon(
            new ImageIcon(LoginPage.class.getResource("/icons/user.png")) // Path to your email icon
                .getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH)
        );

        // Create the email field
        JTextField userText = new JTextField(20);
        userText.setBounds(35, 5, 290, 20); // Leave space for the icon
        userText.setFont(new Font("Arial", Font.PLAIN, 14));
        userText.setBorder(null); // Remove default border
        userPanel.add(userText);

        // Add the email icon inside the text field
        JLabel userIconLabel = new JLabel(userIcon);
        userIconLabel.setBounds(8, 6, 20, 20); // Adjust position inside panel
        userPanel.add(userIconLabel);

        // Create JLabel for Email
                JLabel emailLabel = new JLabel("Email:");
                emailLabel.setBounds(50, 140, 80, 25);
                emailLabel.setFont(new Font("Arial", Font.PLAIN, 13));
                emailLabel.setForeground(new Color(0, 123, 255));
                panel.add(emailLabel);

            // Create a panel to hold the email field and the icon
        JPanel emailPanel = new JPanel();
        emailPanel.setLayout(null); // Absolute positioning
        emailPanel.setBounds(50, 160, 330, 30);
        emailPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        emailPanel.setBackground(Color.WHITE);
        panel.add(emailPanel);

        // Load and resize email icon
        ImageIcon emailIcon = new ImageIcon(
            new ImageIcon(LoginPage.class.getResource("/icons/mail.png")) // Path to your email icon
                .getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH)
        );

        // Create the email field
        JTextField emailText = new JTextField();
        emailText.setBounds(35, 5, 290, 20); // Leave space for the icon
        emailText.setFont(new Font("Arial", Font.PLAIN, 14));
        emailText.setBorder(null); // Remove default border
        emailPanel.add(emailText);

        // Add the email icon inside the text field
        JLabel emailIconLabel = new JLabel(emailIcon);
        emailIconLabel.setBounds(8, 6, 20, 20); // Adjust position inside panel
        emailPanel.add(emailIconLabel);

         // Create JLabel for Password
                JLabel passwordLabel = new JLabel("Password:");
                passwordLabel.setBounds(50, 210, 80, 25);
                passwordLabel.setFont(new Font("Arial", Font.PLAIN, 13));
                passwordLabel.setForeground(new Color(0, 123, 255));
                panel.add(passwordLabel);

        // Create a panel to hold the password field and the icon
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(null); // Absolute positioning
        passwordPanel.setBounds(50, 230, 330, 30);
        passwordPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        passwordPanel.setBackground(Color.WHITE);
        panel.add(passwordPanel);

        // Initialize the password field inside the panel
        JPasswordField passwordText = new JPasswordField();
        passwordText.setBounds(32, 5, 275, 20); // Position the field inside the panel, leaving space for the icon
        passwordText.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font for the password field
        passwordText.setBorder(null); // Remove default border to make space for the icon
        passwordPanel.add(passwordText); // Add the password field to the panel

        // Load and resize the padlock icon for password field (16x16)
        ImageIcon padlockIcon = new ImageIcon(
            new ImageIcon(LoginPage.class.getResource("/icons/padlock.png"))
                .getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH) // Resize the icon to 16x16
        );

        // Create the label for the padlock icon inside the password field
        JLabel padlockIconLabel = new JLabel(padlockIcon);
        padlockIconLabel.setBounds(8, 6, 20, 20); // Position the padlock icon inside the text field (left side)
        passwordPanel.add(padlockIconLabel);

        // Load and resize icons
        ImageIcon showIcon = new ImageIcon(
            new ImageIcon(LoginPage.class.getResource("/icons/show.png"))
                .getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)
        );

        ImageIcon hideIcon = new ImageIcon(
            new ImageIcon(LoginPage.class.getResource("/icons/hide.png"))
                .getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)
        );

        // Create the show/hide password icon inside the text field
        JLabel showPasswordLabel = new JLabel(showIcon);
        showPasswordLabel.setBounds(309, 6, 20, 20); // Adjust inside position
        passwordPanel.add(showPasswordLabel);

        // Add a mouse listener to toggle password visibility
        showPasswordLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (passwordText.getEchoChar() == (char) 0) {
                    passwordText.setEchoChar('*');
                    showPasswordLabel.setIcon(showIcon);
                } else {
                    passwordText.setEchoChar((char) 0);
                    showPasswordLabel.setIcon(hideIcon);
                }
            }
        });

        JLabel fnameLabel = new JLabel("FirstName:");
        fnameLabel.setBounds(50, 270, 80, 25);
        fnameLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        fnameLabel.setForeground(new Color(0, 123, 255));
        panel.add(fnameLabel);

        // Create a panel to hold the email field and the icon
        JPanel fnamePanel = new JPanel();
        fnamePanel.setLayout(null); // Absolute positioning
        fnamePanel.setBounds(50, 290, 330, 30);
        fnamePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        fnamePanel.setBackground(Color.WHITE);
        panel.add(fnamePanel);

        // Create text fields for First Name, Last Name
        JTextField firstNameField = new JTextField();
        firstNameField.setBounds(5, 5, 315, 20);
        firstNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        firstNameField.setBorder(null); // Remove default border
        fnamePanel.add(firstNameField);
        
        JLabel lnameLabel = new JLabel("LastName:");
        lnameLabel.setBounds(50, 330, 80, 25);
        lnameLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        lnameLabel.setForeground(new Color(0, 123, 255));
        panel.add(lnameLabel);

        // Create a panel to hold the email field and the icon
        JPanel lnamePanel = new JPanel();
        lnamePanel.setLayout(null); // Absolute positioning
        lnamePanel.setBounds(50, 350, 330, 30);
        lnamePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        lnamePanel.setBackground(Color.WHITE);
        panel.add(lnamePanel);

        // Create text fields for First Name, Last Name
        JTextField lastNameField = new JTextField();
        lastNameField.setBounds(5, 5, 315, 20);
        lastNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        lastNameField.setBorder(null); // Remove default border
        lnamePanel.add(lastNameField);
        
        
        // Create ComboBoxes for Date of Birth
        JLabel dobLabel = new JLabel("Date of Birth:");
        dobLabel.setBounds(50, 400, 100, 25);
        dobLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        dobLabel.setForeground(new Color(0, 123, 255));

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
        monthComboBox.setBounds(130, 400, 90, 30);
        monthComboBox.setBackground(new Color(255, 255, 255)); // White background
        monthComboBox.setForeground(new Color(50, 50, 50)); // Dark text color
        monthComboBox.setFont(new Font("Arial", Font.PLAIN, 13));
        monthComboBox.setBorder(BorderFactory.createLineBorder(new Color(0, 123, 255), 1));

        JComboBox<String> dayComboBox = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            dayComboBox.addItem(String.valueOf(i));
        }
        dayComboBox.setSelectedItem("1");
        dayComboBox.setBounds(240, 400, 50, 30);
        dayComboBox.setBackground(new Color(255, 255, 255)); 
        dayComboBox.setForeground(new Color(50, 50, 50));
        dayComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        dayComboBox.setBorder(BorderFactory.createLineBorder(new Color(0, 123, 255), 1));

        JComboBox<String> yearComboBox = new JComboBox<>();
        for (int i = 1900; i <= 2023; i++) {
            yearComboBox.addItem(String.valueOf(i));
        }
        yearComboBox.setSelectedItem("2000");
        yearComboBox.setBounds(310, 400, 70, 30);
        yearComboBox.setBackground(new Color(255, 255, 255));
        yearComboBox.setForeground(new Color(50, 50, 50));
        yearComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        yearComboBox.setBorder(BorderFactory.createLineBorder(new Color(0, 123, 255), 1));

        // Create ComboBox for Country
        JLabel countryLabel = new JLabel("Country:");
        countryLabel.setBounds(50, 440, 100, 25);
        countryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        countryLabel.setForeground(new Color(0, 123, 255));

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
        countryComboBox.setBounds(130, 440, 250, 30);
        countryComboBox.setBackground(new Color(255, 255, 255));
        countryComboBox.setForeground(new Color(50, 50, 50));
        countryComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        countryComboBox.setBorder(BorderFactory.createLineBorder(new Color(0, 123, 255), 1));

        // Create ComboBox for Country Code and TextField for Phone Number
        JLabel phoneLabel = new JLabel("Phone No.:");
        phoneLabel.setBounds(50, 490, 100, 25);
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneLabel.setForeground(new Color(0, 123, 255));

        JComboBox<String> countryCodeComboBox = new JComboBox<>();
        countryCodeComboBox.addItem("+1");
        countryCodeComboBox.addItem("+44");
        countryCodeComboBox.addItem("+91");
        countryCodeComboBox.addItem("+61");
        countryCodeComboBox.addItem("+234");
        countryCodeComboBox.setSelectedItem("+234");
        countryCodeComboBox.setBounds(130, 490, 80, 25);
        countryCodeComboBox.setBackground(new Color(255, 255, 255));
        countryCodeComboBox.setForeground(new Color(50, 50, 50));
        countryCodeComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        countryCodeComboBox.setBorder(BorderFactory.createLineBorder(new Color(0, 123, 255), 1));

        JTextField phoneNumberField = new JTextField();
        phoneNumberField.setBounds(215, 490, 165, 25);
        phoneNumberField.setBorder(BorderFactory.createLineBorder(new Color(0, 123, 255), 1));
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
        
// Create the checkbox for terms and conditions
JCheckBox termsAndConditionsCheckBox = new JCheckBox("By clicking this, I have agreed to the");
termsAndConditionsCheckBox.setFont(new Font("Arial", Font.PLAIN, 11));
termsAndConditionsCheckBox.setBounds(50, 530, 202, 15);
termsAndConditionsCheckBox.setBackground(null);
termsAndConditionsCheckBox.setEnabled(false); // Initially disabled
panel.add(termsAndConditionsCheckBox);

// Create JButton for Signup
JButton signupButton = new JButton("Signup");
signupButton.setBounds(50, 555, 330, 40);
signupButton.setBackground(new Color(0, 123, 255));
signupButton.setForeground(Color.WHITE);
signupButton.setFont(new Font("Arial", Font.BOLD, 16));
signupButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
signupButton.setFocusPainted(false);
signupButton.setEnabled(false); // Initially disabled
panel.add(signupButton);

// Add a mouse listener to open the terms and conditions page
JLabel termsAndConditionsLink = new JLabel("Terms and Conditions.");
termsAndConditionsLink.setFont(new Font("Arial", Font.PLAIN, 11));
termsAndConditionsLink.setForeground(new Color(0, 123, 255));
termsAndConditionsLink.setBounds(252, 530, 180, 15);
termsAndConditionsLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
panel.add(termsAndConditionsLink);

// Add mouse listener to open the terms and conditions page
termsAndConditionsLink.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        // Now pass the termsAndConditionsCheckBox and signupButton properly
        TermsAndConditionsPage termsAndConditionsPage = new TermsAndConditionsPage(termsAndConditionsCheckBox, signupButton);
        termsAndConditionsPage.setVisible(true);
    }
    @Override
    public void mouseEntered(MouseEvent e) {
                termsAndConditionsLink.setForeground(Color.BLUE); // Change color on mouse hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                termsAndConditionsLink.setForeground(new Color(0, 123, 255)); // Revert color on mouse exit
            }
});


// Add action listener to the Signup button
signupButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Get the input values
        String username = userText.getText();
        String email = emailText.getText();
        String password = new String(passwordText.getPassword());
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String country = (String) countryComboBox.getSelectedItem();
        String dob = monthComboBox.getSelectedItem() + " " + dayComboBox.getSelectedItem() + ", " + yearComboBox.getSelectedItem(); // Format: MM DD, YYYY

        // Validate inputs
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty() || country == null || dob == null) {
            JOptionPane.showMessageDialog(panel, "All fields are required.");
            return;
        }

        // Validate username
        if (username.length() < 6 || !Pattern.matches("^[a-zA-Z0-9_.-]*$", username)) {
            JOptionPane.showMessageDialog(panel, "Username must be at least 6 characters and contain only letters, numbers, or '_', '.', '-'.");

            return;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(panel, "Please enter a valid email address.");
            return;
        }

        // Validate password
        if (!isValidPassword(password)) {
            JOptionPane.showMessageDialog(panel, "Password must be 8-12 characters long and include at least one special character.");
            return;
        }

        // Validate phone number format
        if (!isValidPhoneNumber(phoneNumber)) {
            JOptionPane.showMessageDialog(panel, "Please enter a valid phone number.");
            return;
        }

        // Call DatabaseHelper to check if user exists
        DatabaseHelper dbHelper = new DatabaseHelper();

        if (dbHelper.doesUserExist(username, email)) {
            JOptionPane.showMessageDialog(panel, "Username or Email already exists. Please try another.");
            return;
        }

        // Save the user in the database
        boolean isSuccess = dbHelper.signup(username, email, password, firstName, lastName, dob, country, phoneNumber);

        if (isSuccess) {
            JOptionPane.showMessageDialog(panel, "Signup successful for user: " + username);
            // Close the signup page after successful signup
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(panel);
            if (parentFrame != null) {
                parentFrame.dispose();
            }
        } else {
            JOptionPane.showMessageDialog(panel, "Signup failed. Please try again.");
        }
    }
});
        
        // Create ComboBox for Country Code and TextField for Phone Number
        JLabel loginLabel = new JLabel("Already have an account? ");
        loginLabel.setBounds(120, 600, 170, 20);
        loginLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        loginLabel.setForeground(Color.GRAY);
        panel.add(loginLabel);
        
        JLabel login2Label = new JLabel("Login", SwingConstants.CENTER);
        login2Label.setBounds(265, 600, 50, 20);
        login2Label.setFont(new Font("Arial", Font.PLAIN, 14));
        login2Label.setForeground(Color.BLACK);
        login2Label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        login2Label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                login2Label.setForeground(Color.BLUE); // Change color on mouse hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                login2Label.setForeground(Color.BLACK); // Revert color on mouse exit
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                MainTab2.frame.dispose();
//                Main.main(new String[0]);

            }
        });

        
        panel.add(login2Label);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(450, 650));
        rightPanel.setBackground(Color.WHITE);        
        rightPanel.setLayout(new BorderLayout());      

        // Array of image paths
        String[] imagePaths = {
            "C:\\Users\\coola\\Downloads\\undraw_chatting_2b1g.png",
            "C:\\Users\\coola\\Downloads\\undraw_chatting_1.png",
            "C:\\Users\\coola\\Downloads\\undraw_chatting_2.png",
            "C:\\Users\\coola\\Downloads\\undraw_chatting_3.png",
            "C:\\Users\\coola\\Downloads\\undraw_chatting_4.png",
            "C:\\Users\\coola\\Downloads\\undraw_chatting_5.png",
            "C:\\Users\\coola\\Downloads\\undraw_chatting_6.png",
            "C:\\Users\\coola\\Downloads\\undraw_chatting_7.png",
            "C:\\Users\\coola\\Downloads\\undraw_chatting_8.png",
            "C:\\Users\\coola\\Downloads\\undraw_chatting_9.png",
            "C:\\Users\\coola\\Downloads\\undraw_chatting_10.png",
            "C:\\Users\\coola\\Downloads\\undraw_chatting_11.png",
            "C:\\Users\\coola\\Downloads\\undraw_chatting_12.png",
            "C:\\Users\\coola\\Downloads\\undraw_chatting_13.png",
            "C:\\Users\\coola\\Downloads\\undraw_chatting_14.png",
        };

        // Create a JLabel for the illustration image
        final JLabel illustrationLabel = new JLabel();
        illustrationLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create a Timer to change images every 3 seconds (3000 ms)
        int interval = 3000; // Change image every 3 seconds
        Timer timer = new Timer(interval, new ActionListener() {
            int currentIndex = 0;

    @Override
    public void actionPerformed(ActionEvent e) {
        // Load the next image in the array
        ImageIcon icon = new ImageIcon(imagePaths[currentIndex]);
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(650, 650, Image.SCALE_SMOOTH); // Resize image
        icon.setImage(newImage);
        illustrationLabel.setIcon(icon);

        // Move to the next image, loop back to the first one when at the end
        currentIndex = (currentIndex + 1) % imagePaths.length;
        }
        });

        // Start the timer to begin changing images
        timer.start();

        // Add the illustration label to the right panel
        rightPanel.add(illustrationLabel, BorderLayout.CENTER);

                JPanel finalPanel = new JPanel();
        finalPanel.setLayout(new BorderLayout());
        finalPanel.add(panel, BorderLayout.WEST);
        finalPanel.add(rightPanel, BorderLayout.EAST);

        return finalPanel;
    }

    // Helper method to validate email
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(emailRegex, email);
    }

    // Helper method to validate password
    private static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,12}$";
        return Pattern.matches(passwordRegex, password);
    }

    // Helper method to validate phone number
    private static boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^\\d{10}$"; // Adjust the regex as needed
        return Pattern.matches(phoneRegex, phoneNumber);
    }
    
    
}    




// // Create a link to the privacy policy page
//        JLabel privacyPolicyLink = new JLabel("Privacy Policy");
//        privacyPolicyLink.setFont(new Font("Arial", Font.PLAIN, 12));
//        privacyPolicyLink.setForeground(Color.BLUE);
//        privacyPolicyLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//
//        // Add a mouse listener to the link
//        privacyPolicyLink.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                // Open the privacy policy page
//                PrivacyPolicyPage privacyPolicyPage = new PrivacyPolicyPage();
//                privacyPolicyPage.setVisible(true);
//            }
//        });
