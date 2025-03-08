/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import javax.imageio.ImageIO;

public class FriendsList extends JPanel {
    private JPanel friendsPanel;
    private String currentUser;
    private ImageIcon defaultProfileImage;
    private Dashboard dashboard;
    private Dashboard parentDashboard; // Store reference to Dashboard

    public FriendsList(String username, Dashboard dashboard) {
        this.currentUser = username;
        this.parentDashboard = dashboard; // Store Dashboard reference
        this.defaultProfileImage = new ImageIcon("C:\\Users\\coola\\Documents\\NetBeansProjects\\ChatApplication\\src\\main\\resources\\icons\\user.png"); // Default profile image

        //setTitle("My Friends");
        setSize(400, 500);
        setLayout(new BorderLayout());
        //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Set the icon for the frame (you can change the path to your icon)
        //setIconImage(new ImageIcon("C:\\Users\\coola\\Documents\\NetBeansProjects\\ChatApplication\\src\\main\\resources\\icons\\novamobile.jpg").getImage()); // Update the icon path
        
        friendsPanel = new JPanel();
        friendsPanel.setLayout(new BoxLayout(friendsPanel, BoxLayout.Y_AXIS));
        friendsPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(friendsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        loadFriends();
        setVisible(true);
    }
    
    public void refreshFriendsList() {
    loadFriends(); // Reload the list from the database
}


    private void loadFriends() {
        friendsPanel.removeAll(); 

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
            String sql = "SELECT Users.user_id, Users.username, Users.profile_image " +
                         "FROM Friends " +
                         "JOIN Users ON Friends.friend_id = Users.user_id " +
                         "WHERE Friends.user_id = (SELECT user_id FROM Users WHERE username=?) " +
                         "AND Friends.status = 'Accepted' " +
                         "UNION " +
                         "SELECT Users.user_id, Users.username, Users.profile_image " +
                         "FROM Friends " +
                         "JOIN Users ON Friends.user_id = Users.user_id " +
                         "WHERE Friends.friend_id = (SELECT user_id FROM Users WHERE username=?) " +
                         "AND Friends.status = 'Accepted'";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, currentUser);
            stmt.setString(2, currentUser);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String username = rs.getString("username");
                byte[] profileImageBytes = rs.getBytes("profile_image");

                addFriendToList(userId, username, profileImageBytes);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        friendsPanel.revalidate();
        friendsPanel.repaint();
    }

    private void addFriendToList(int userId, String username, byte[] profileImageBytes) {
        JPanel friendPanel = new JPanel(new BorderLayout());
        friendPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(173, 216, 230), 2), // Light blue border
                BorderFactory.createEmptyBorder(10, 10, 10, 10) // Padding inside panel
        ));
        friendPanel.setBackground(Color.WHITE);
        friendPanel.setMaximumSize(new Dimension(360, 70));
        friendPanel.setPreferredSize(new Dimension(360, 70));
        friendPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Profile Image (Circular)
        JLabel profileImageLabel = new JLabel();
        profileImageLabel.setPreferredSize(new Dimension(50, 50));
        profileImageLabel.setIcon(getCircularImage(profileImageBytes));

        // Username Label
        JLabel usernameLabel = new JLabel(username);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Remove Friend Button
        JButton removeButton = new JButton("Remove");
        removeButton.setForeground(Color.RED);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeFriend(userId);
            }
        });

        // Click to view profile
        friendPanel.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        if (parentDashboard != null) {
            new UserProfile(userId, parentDashboard);  // ✅ Correctly pass Dashboard
        } else {
            System.out.println("ERROR: parentDashboard is NULL in FriendsList!");
        }
    }
});


        // Layout
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(Color.WHITE);
        textPanel.add(usernameLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(removeButton);

        friendPanel.add(profileImageLabel, BorderLayout.WEST);
        friendPanel.add(textPanel, BorderLayout.CENTER);
        friendPanel.add(buttonPanel, BorderLayout.EAST);

        friendsPanel.add(friendPanel);
        friendsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between friends
    }

    private ImageIcon getCircularImage(byte[] imageBytes) {
    try {
        BufferedImage originalImage;

        // Check if imageBytes is NULL or empty
        if (imageBytes != null && imageBytes.length > 0) {
            originalImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
        } else {
            System.out.println("Profile image is NULL, using default image.");
            
            // Load the default image from resources
            InputStream defaultImageStream = getClass().getResourceAsStream("/icons/user.png");
            if (defaultImageStream == null) {
                System.err.println("Error: Default profile image not found.");
                return new ImageIcon(); // Return an empty icon as a fallback
            }
            originalImage = ImageIO.read(defaultImageStream);
        }

        if (originalImage == null) {
            System.err.println("Error: Failed to load profile image.");
            return new ImageIcon(); // Return an empty icon
        }

        int diameter = 50; 
        BufferedImage circularImage = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circularImage.createGraphics();
        g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, diameter, diameter));
        g2.drawImage(originalImage, 0, 0, diameter, diameter, null);
        g2.dispose();

        return new ImageIcon(circularImage);
    } catch (IOException e) {
        e.printStackTrace();
        return new ImageIcon(); // Return an empty icon if an error occurs
    }
}

    private void removeFriend(int friendId) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
            String sql = "DELETE FROM Friends WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, getUserId(currentUser));
            stmt.setInt(2, friendId);
            stmt.setInt(3, friendId);
            stmt.setInt(4, getUserId(currentUser));
            stmt.executeUpdate();

            loadFriends(); // Refresh the list
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getUserId(String username) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
            String sql = "SELECT user_id FROM Users WHERE username=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        }
        return -1;
    }
}