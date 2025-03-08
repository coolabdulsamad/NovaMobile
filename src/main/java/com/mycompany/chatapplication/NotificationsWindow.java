/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationsWindow extends JPanel {
    private DefaultListModel<String> notificationModel;
    private String currentUser;
    
    public NotificationsWindow(String username) {
        this.currentUser = username;
        setSize(350, 450);
        setLayout(new BorderLayout());
        
        notificationModel = new DefaultListModel<>();
        JList<String> notificationList = new JList<>(notificationModel);
        notificationList.setFont(new Font("Arial", Font.PLAIN, 14));
        add(new JScrollPane(notificationList), BorderLayout.CENTER);
        
        loadNotifications();
    }
    
    private void loadNotifications() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
            String sql = "SELECT notification_id, notification_type, message, status FROM Notifications WHERE user_id = (SELECT user_id FROM Users WHERE username=?) ORDER BY created_at DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, currentUser);
            ResultSet rs = stmt.executeQuery();
            
            List<String> notifications = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("notification_id");
                String type = rs.getString("notification_type");
                String message = rs.getString("message");
                String status = rs.getString("status");
                
                String formattedNotification = formatNotification(id, type, message, status);
                notifications.add(formattedNotification);
                
                if (status.equals("Unread")) {
                    markAsRead(conn, id);
                }
            }
            
            notificationModel.clear();
            for (int i = 0; i < notifications.size(); i++) {
                notificationModel.addElement((i + 1) + ". " + notifications.get(i));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading notifications: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String formatNotification(int id, String type, String message, String status) {
        String prefix;
        switch (type) {
            case "friend_request":
                prefix = "[Friend Request] ";
                break;
            case "message_received":
                prefix = "[New Message] ";
                break;
            case "group_invite":
                prefix = "[Group Invite] ";
                break;
            default:
                prefix = "[Notification] ";
        }
        
        return prefix + message + (status.equals("Unread") ? " (NEW)" : "");
    }
    
    private void markAsRead(Connection conn, int notificationId) {
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE Notifications SET status='Read' WHERE notification_id=?")) {
            stmt.setInt(1, notificationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

