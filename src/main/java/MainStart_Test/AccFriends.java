/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainStart_Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.*;

import com.mycompany.chatapplication.MainTab;
import java.awt.GridLayout;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AccFriends extends JPanel {
    private JTextField friendUsernameField;
    private JButton sendRequestButton;
    private String currentUser;

    public AccFriends(String username) {
        this.currentUser = username;

        //setTitle("Add Friend");
        setSize(300, 150);
        setLayout(new GridLayout(3, 1));
        //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Set the icon for the frame (you can change the path to your icon)
        
        URL iconURL = MainTab.class.getClassLoader().getResource("icons/novamobile.jpg");
if (iconURL != null) {
    //setIconImage(new ImageIcon(iconURL).getImage());
} else {
    System.err.println("Icon image not found.");
}
        //setIconImage(new ImageIcon("C:\\Users\\coola\\Documents\\NetBeansProjects\\ChatApplication\\src\\main\\resources\\icons\\novamobile.jpg").getImage()); // Update the icon path

        friendUsernameField = new JTextField();
        sendRequestButton = new JButton("Send Request");

        sendRequestButton.addActionListener(e -> sendFriendRequest());

        add(new JLabel("Enter Friend's Username:"));
        add(friendUsernameField);
        add(sendRequestButton);
        
        setVisible(true);
    }

    private void sendFriendRequest() {
        String friendUsername = friendUsernameField.getText().trim();

        if (friendUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a username.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
            // Get user IDs
            int userId = getUserId(conn, currentUser);
            int friendId = getUserId(conn, friendUsername);

            if (friendId == -1) {
                JOptionPane.showMessageDialog(this, "User not found!");
                return;
            }

            // Check if request already exists
            String checkQuery = "SELECT * FROM Friends WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, friendId);
            checkStmt.setInt(3, friendId);
            checkStmt.setInt(4, userId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Friend request already exists!");
            } else {
                // Insert friend request
                String insertQuery = "INSERT INTO Friends (user_id, friend_id, status) VALUES (?, ?, 'Pending')";
                PreparedStatement stmt = conn.prepareStatement(insertQuery);
                stmt.setInt(1, userId);
                stmt.setInt(2, friendId);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Friend request sent!");
                
                // Insert notification for the receiver
                String notifyQuery = "INSERT INTO Notifications (user_id, notification_type, message, status) VALUES (?, 'friend_request', ?, 'Unread')";
                PreparedStatement notifyStmt = conn.prepareStatement(notifyQuery);
                notifyStmt.setInt(1, friendId);
                notifyStmt.setString(2, currentUser + " sent you a friend request.");
                notifyStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error!");
        }
    }

    private int getUserId(Connection conn, String username) throws SQLException {
        String query = "SELECT user_id FROM Users WHERE username = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        return rs.next() ? rs.getInt("user_id") : -1;
    }
}

