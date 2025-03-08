/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class UserProfile extends JFrame {
    private int userId;
    private JLabel profileImageLabel, usernameLabel, emailLabel, nameLabel, dobLabel, countryLabel, phoneLabel, bioLabel, statusLabel;
    private JButton chatButton, removeButton;
    private ImageIcon defaultProfileImage;
    private Dashboard parentDashboard; // Store reference to Dashboard
    


    public UserProfile(int userId, Dashboard dashboard) {
        this.userId = userId;
        this.parentDashboard = dashboard; // Store reference to Dashboard
        this.defaultProfileImage = new ImageIcon("resources/icons/user.png"); // Default image

        setTitle("User Profile");
        setSize(400, 500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Set the icon for the frame (you can change the path to your icon)
        setIconImage(new ImageIcon("C:\\Users\\coola\\Documents\\NetBeansProjects\\ChatApplication\\src\\main\\resources\\icons\\novamobile.jpg").getImage()); // Update the icon path

        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setBackground(Color.WHITE);
        profilePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Profile Image
        profileImageLabel = new JLabel();
        profileImageLabel.setPreferredSize(new Dimension(100, 100));
        profileImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // User Details
        usernameLabel = new JLabel();
        emailLabel = new JLabel();
        nameLabel = new JLabel();
        dobLabel = new JLabel();
        countryLabel = new JLabel();
        phoneLabel = new JLabel();
        bioLabel = new JLabel();
        statusLabel = new JLabel();

        usernameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        emailLabel.setForeground(Color.GRAY);
        bioLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        // Buttons
        chatButton = new JButton("Chat");
        chatButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        chatButton.setBackground(new Color(30, 144, 255));
        chatButton.setForeground(Color.WHITE);
        /// ✅ FIXED UserProfile.java (Ensuring correct user ID is passed)
chatButton.addActionListener(e -> {
    if (parentDashboard != null) {
        try {
            String selectedUsername = usernameLabel.getText().replace("@", "").trim();
            int selectedUserId = this.userId; // This should be the friend's ID
            int loggedInUserId = getCurrentUserId(); // Get logged-in user ID

            System.out.println("DEBUG: In UserProfile - Selected Friend: " + selectedUsername + 
                               " (Friend ID: " + selectedUserId + "), Logged-in User ID: " + loggedInUserId);

            if (loggedInUserId == -1) {
                JOptionPane.showMessageDialog(this, "Error: Could not retrieve logged-in user ID.");
                return;
            }

            parentDashboard.switchToChatWindow(selectedUsername, selectedUserId, loggedInUserId);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }
});









        removeButton = new JButton("Remove Friend");
        removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeButton.setBackground(Color.RED);
        removeButton.setForeground(Color.WHITE);

        // Load user details
        loadUserDetails();

        // Add components
        profilePanel.add(profileImageLabel);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        profilePanel.add(usernameLabel);
        profilePanel.add(emailLabel);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        profilePanel.add(nameLabel);
        profilePanel.add(dobLabel);
        profilePanel.add(countryLabel);
        profilePanel.add(phoneLabel);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        profilePanel.add(bioLabel);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        profilePanel.add(statusLabel);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        profilePanel.add(chatButton);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        profilePanel.add(removeButton);

        add(profilePanel, BorderLayout.CENTER);
        setVisible(true);

        // Button actions
        chatButton.addActionListener(e -> openChat());
        removeButton.addActionListener(e -> removeFriend());
    }

    private void loadUserDetails() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
            String sql = "SELECT username, email, first_name, last_name, dob, country, phone_number, bio, profile_image, online_status " +
                         "FROM Users WHERE user_id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usernameLabel.setText("@" + rs.getString("username"));
                emailLabel.setText(rs.getString("email"));
                nameLabel.setText("Name: " + rs.getString("first_name") + " " + rs.getString("last_name"));
                dobLabel.setText("DOB: " + rs.getString("dob"));
                countryLabel.setText("Country: " + rs.getString("country"));
                phoneLabel.setText("Phone: " + rs.getString("phone_number"));
                bioLabel.setText("Bio: " + rs.getString("bio"));

                // Profile Image
                byte[] profileImageBytes = rs.getBytes("profile_image");
                profileImageLabel.setIcon(getCircularImage(profileImageBytes));

                // Online Status
                String onlineStatus = rs.getString("online_status");
                boolean isOnline = onlineStatus.equals("Online");
                statusLabel.setText(isOnline ? "Online" : "Offline");
                statusLabel.setForeground(isOnline ? Color.GREEN : Color.RED);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ImageIcon getCircularImage(byte[] imageBytes) {
        try {
            BufferedImage originalImage;
            if (imageBytes != null) {
                originalImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
            } else {
                originalImage = ImageIO.read(getClass().getResource("resources/icons/user.png"));
            }

            int diameter = 100;
            BufferedImage circularImage = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = circularImage.createGraphics();
            g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, diameter, diameter));
            g2.drawImage(originalImage, 0, 0, diameter, diameter, null);
            g2.dispose();

            return new ImageIcon(circularImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultProfileImage;
    }

    private void openChat() {
        JOptionPane.showMessageDialog(this, "Opening chat with " + usernameLabel.getText());
    }

    private void removeFriend() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
            String sql = "DELETE FROM Friends WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, getCurrentUserId());
            stmt.setInt(2, userId);
            stmt.setInt(3, userId);
            stmt.setInt(4, getCurrentUserId());
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Friend removed successfully.");
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

private int getCurrentUserId() throws SQLException {
    String currentUsername = getCurrentUsername();
    if (currentUsername == null || currentUsername.isEmpty()) {
        System.out.println("ERROR: No username found in getCurrentUserId()");
        return -1;  // Prevent query failure
    }

    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
        String sql = "SELECT user_id FROM Users WHERE username = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, currentUsername);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            int userId = rs.getInt("user_id");
            System.out.println("DEBUG: Retrieved Logged-in User ID: " + userId);
            return userId;
        } else {
            System.out.println("ERROR: No user found in DB for username: " + currentUsername);
            return -1;
        }
    }
}



    private String getCurrentUsername() {
    if (parentDashboard != null) {
        String username = parentDashboard.getLoggedInUsername();
        System.out.println("DEBUG: Fetched username from Dashboard: " + username);
        return username;
    }
    System.out.println("ERROR: parentDashboard is NULL in UserProfile!");
    return null;
}
    
}