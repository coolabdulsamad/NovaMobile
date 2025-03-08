/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import Application_Connector.db.DatabaseHelper;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ProfilePage extends JPanel {
    private JTextField usernameField, firstNameField, lastNameField, emailField, countryField, phoneNumberField, dobField;
    private JTextArea bioField;
    private JLabel profileImageLabel, friendsCountLabel;
    private JButton saveButton, uploadImageButton;
    private int userId;

        public ProfilePage(int userId) {
        this.userId = userId;
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        profileImageLabel = new JLabel() {
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fillOval(0, 0, getWidth(), getHeight());
        g2d.setClip(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
        super.paintComponent(g2d);
    }
};
profileImageLabel.setPreferredSize(new Dimension(100, 100));
profileImageLabel.setOpaque(true);
profileImageLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
profileImageLabel.setHorizontalAlignment(JLabel.CENTER);


        uploadImageButton = new JButton("Change Photo");
        uploadImageButton.addActionListener(e -> uploadProfileImage());

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(profileImageLabel, gbc);

        gbc.gridy = 1;
        add(uploadImageButton, gbc);

        // Username
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(new JLabel("Username:"), gbc);
        usernameField = new JTextField(15);
        usernameField.setEditable(false);
        usernameField.setBorder(null);
        gbc.gridx = 2;
        //gbc.anchor = GridBagConstraints.WEST;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(usernameField, gbc);

        // Bio
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(new JLabel("Country:"), gbc);
        countryField = new JTextField(15);
          countryField.setBorder(BorderFactory.createEmptyBorder());
        countryField.addFocusListener(new FocusAdapter() {
    @Override
    public void focusGained(FocusEvent e) {
        countryField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }
    @Override
    public void focusLost(FocusEvent e) {
        countryField.setBorder(BorderFactory.createEmptyBorder());
    }
});
        gbc.gridx = 2;
        add(countryField, gbc);

        // User details
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(new JLabel("First Name:"), gbc);
        firstNameField = new JTextField(15);
         firstNameField.setBorder(BorderFactory.createEmptyBorder());
        firstNameField.addFocusListener(new FocusAdapter() {
    @Override
    public void focusGained(FocusEvent e) {
        firstNameField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }
    @Override
    public void focusLost(FocusEvent e) {
        firstNameField.setBorder(BorderFactory.createEmptyBorder());
    }
});
        gbc.gridx = 2;
        add(firstNameField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        add(new JLabel("Last Name:"), gbc);
        lastNameField = new JTextField(15);
        lastNameField.setBorder(BorderFactory.createEmptyBorder());
        lastNameField.addFocusListener(new FocusAdapter() {
    @Override
    public void focusGained(FocusEvent e) {
        lastNameField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }
    @Override
    public void focusLost(FocusEvent e) {
        lastNameField.setBorder(BorderFactory.createEmptyBorder());
    }
});
        gbc.gridx = 2;
        add(lastNameField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        add(new JLabel("Email:"), gbc);
        emailField = new JTextField(20);
        emailField.setBorder(BorderFactory.createEmptyBorder());
        emailField.addFocusListener(new FocusAdapter() {
    @Override
    public void focusGained(FocusEvent e) {
        emailField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }
    @Override
    public void focusLost(FocusEvent e) {
        emailField.setBorder(BorderFactory.createEmptyBorder());
    }
});

        gbc.gridx = 2;
        add(emailField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        add(new JLabel("Phone Number:"), gbc);
        phoneNumberField = new JTextField(15);
        phoneNumberField.setBorder(BorderFactory.createEmptyBorder());
        phoneNumberField.addFocusListener(new FocusAdapter() {
    @Override
    public void focusGained(FocusEvent e) {
        phoneNumberField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }
    @Override
    public void focusLost(FocusEvent e) {
        phoneNumberField.setBorder(BorderFactory.createEmptyBorder());
    }
});
        gbc.gridx = 2;
        add(phoneNumberField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        add(new JLabel("Bio:"), gbc);
        bioField = new JTextArea(3, 15);
        bioField.setBorder(BorderFactory.createEmptyBorder());
        bioField.setLineWrap(true);
        bioField.setWrapStyleWord(true);
        
        JScrollPane bioScrollPane = new JScrollPane(bioField);
        bioScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


        bioField.addFocusListener(new FocusAdapter() {
    @Override
    public void focusGained(FocusEvent e) {
        bioField.setBorder(BorderFactory.createLineBorder(Color.BLUE));
    }
    @Override
    public void focusLost(FocusEvent e) {
        bioField.setBorder(BorderFactory.createEmptyBorder());
    }
});
        gbc.gridx = 2;
        add(bioScrollPane, gbc);

        gbc.gridx = 1;
        gbc.gridy = 9;
        add(new JLabel("Date of Birth:"), gbc);
        dobField = new JTextField(15);
        dobField.setBorder(BorderFactory.createEmptyBorder());
        dobField.addFocusListener(new FocusAdapter() {
    @Override
    public void focusGained(FocusEvent e) {
        dobField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }
    @Override
    public void focusLost(FocusEvent e) {
        dobField.setBorder(BorderFactory.createEmptyBorder());
    }
});
        gbc.gridx = 2;
        add(dobField, gbc);
        

        // Friends Count
        gbc.gridx = 1;
        gbc.gridy = 10;
        add(new JLabel("Friends:"), gbc);
        friendsCountLabel = new JLabel();
        gbc.gridx = 2;
        add(friendsCountLabel, gbc);

        // Save Button
        saveButton = new JButton("Save Changes");
        saveButton.setBackground(Color.BLUE);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> saveProfileDetails());

        gbc.gridy = 11;
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        add(saveButton, gbc);

        // Load User Data
        loadUserProfile(userId);
    }

     public void loadUserProfile(int userId) {
        DatabaseHelper dbHelper = new DatabaseHelper();
        User user = dbHelper.getUserProfile(userId);

        if (user != null) {
            usernameField.setText(user.getUsername());
            countryField.setText(user.getCountry());
            dobField.setText(user.getDob());
            firstNameField.setText(user.getFirstName());
            lastNameField.setText(user.getLastName());
            emailField.setText(user.getEmail());
            bioField.setText(user.getBio());
            phoneNumberField.setText(user.getPhoneNumber());
            friendsCountLabel.setText(String.valueOf(user.getFriendsCount()));

            // ✅ Load and display profile image
            if (user.getProfileImage() != null) {
                try {
                    ByteArrayInputStream bis = new ByteArrayInputStream(user.getProfileImage());
                    BufferedImage img = ImageIO.read(bis);
                    if (img != null) {
                        profileImageLabel.setIcon(new ImageIcon(img.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
                    }
                } catch (Exception e) {
                    profileImageLabel.setText("Image Error");
                }
            } else {
                profileImageLabel.setText("No Image");
            }
        }

        // Refresh UI
        revalidate();
        repaint();
        SwingUtilities.updateComponentTreeUI(this);
    }



    private void uploadProfileImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                byte[] profileImageData = Files.readAllBytes(file.toPath());
                DatabaseHelper dbHelper = new DatabaseHelper();
                dbHelper.updateProfileImage(userId, profileImageData);

                // Update UI
                profileImageLabel.setIcon(new ImageIcon(new ImageIcon(profileImageData).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

private void saveProfileDetails() {
    String country = countryField.getText();
    String firstName = firstNameField.getText();
    String lastName = lastNameField.getText();
    String email = emailField.getText();
    String phoneNumber = phoneNumberField.getText();
    String bio = bioField.getText();
    String dob = dobField.getText();

    // Validate email
    if (!isValidEmail(email)) {
        JOptionPane.showMessageDialog(this, "Invalid email format! Please enter a valid email.");
        return;
    }

    // Validate phone number
    if (!isValidPhoneNumber(phoneNumber)) {
        JOptionPane.showMessageDialog(this, "Invalid phone number! It should be exactly 10 digits.");
        return;
    }

    DatabaseHelper dbHelper = new DatabaseHelper();
    boolean success = dbHelper.updateUserProfile(userId, firstName, lastName, email, bio, dob, country, phoneNumber);

    if (success) {
        JOptionPane.showMessageDialog(this, "Profile updated successfully!");
    } else {
        JOptionPane.showMessageDialog(this, "Error updating profile.");
    }
}

// Helper method to validate email
private static boolean isValidEmail(String email) {
    String emailRegex = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    return Pattern.matches(emailRegex, email);
}

// Helper method to validate phone number
private static boolean isValidPhoneNumber(String phoneNumber) {
    String phoneRegex = "^\\d{10}$"; // Adjust the regex as needed
    return Pattern.matches(phoneRegex, phoneNumber);
}

}
