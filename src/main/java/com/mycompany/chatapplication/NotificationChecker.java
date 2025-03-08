/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class NotificationChecker extends Thread {
    private String username;

    public NotificationChecker(String username) {
        this.username = username;
    }

    @Override
    public void run() {
        while (true) {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
                String sql = "SELECT notification_id, message FROM Notifications WHERE user_id = (SELECT user_id FROM Users WHERE username=?) AND status = 'Unread'";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int notificationId = rs.getInt("notification_id");
                    String message = rs.getString("message");

                    // Play notification sound
                    playNotificationSound();

                    // Show pop-up notification
                    SwingUtilities.invokeLater(() -> 
                        JOptionPane.showMessageDialog(null, message, "New Notification", JOptionPane.INFORMATION_MESSAGE));
                    
                    // Mark notification as Read
                    markNotificationAsRead(conn, notificationId);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Wait for 5 seconds before checking again
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void playNotificationSound() {
        try {
            File soundFile = new File("sounds/notification.wav");
            if (!soundFile.exists()) {
                System.err.println("Sound file not found: " + soundFile.getAbsolutePath());
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.setFramePosition(0); // Reset clip position before playing
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void markNotificationAsRead(Connection conn, int notificationId) {
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE Notifications SET status = 'Read' WHERE notification_id = ?")) {
            stmt.setInt(1, notificationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

