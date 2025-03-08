/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class MessagePage extends JFrame {
    private String username; // Logged-in user's username
    private JComboBox<String> friendList;
    private JTextArea chatArea;
    private JTextField messageField;

    public MessagePage(String username) {
        this.username = username;

        // Frame setup
        setTitle("Messages");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        // Set the icon for the frame (you can change the path to your icon)
        setIconImage(new ImageIcon("C:\\Users\\coola\\Documents\\NetBeansProjects\\ChatApplication\\src\\main\\resources\\icons\\novamobile.jpg").getImage()); // Update the icon path

        // Layout setup
        setLayout(new BorderLayout());

        // Friend List Panel
        JPanel friendPanel = new JPanel();
        friendPanel.setLayout(new BorderLayout());
        JLabel friendLabel = new JLabel("Select a Friend:");
        friendList = new JComboBox<>(getFriendListFromDB());
        friendPanel.add(friendLabel, BorderLayout.NORTH);
        friendPanel.add(friendList, BorderLayout.CENTER);

        // Chat Area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane chatScroll = new JScrollPane(chatArea);

        // Message Input Panel
        JPanel messagePanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        messagePanel.add(messageField, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.EAST);

        // Add components to frame
        add(friendPanel, BorderLayout.NORTH);
        add(chatScroll, BorderLayout.CENTER);
        add(messagePanel, BorderLayout.SOUTH);

        // Load chat history when a friend is selected
        friendList.addActionListener(e -> loadChatHistory());
        
        // Send button action
        sendButton.addActionListener(e -> sendMessage());
    }

    // Fetch friends from the database
    private String[] getFriendListFromDB() {
        ArrayList<String> friends = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db");
             PreparedStatement stmt = conn.prepareStatement("""
                     SELECT U.username
                     FROM Friends F
                     JOIN Users U ON F.friend_id = U.user_id
                     WHERE F.user_id = (SELECT user_id FROM Users WHERE username = ?)
                     AND F.status = 'Accepted'
                     """)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                friends.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends.toArray(new String[0]);
    }

    // Load chat history with a selected friend
    private void loadChatHistory() {
        chatArea.setText(""); // Clear chat area
        String selectedFriend = (String) friendList.getSelectedItem();
        if (selectedFriend == null) return;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db");
             PreparedStatement stmt = conn.prepareStatement("""
                     SELECT sender_id, receiver_id, message_text, timestamp
                     FROM Messages
                     WHERE (sender_id = (SELECT user_id FROM Users WHERE username = ?)
                     AND receiver_id = (SELECT user_id FROM Users WHERE username = ?))
                     OR (sender_id = (SELECT user_id FROM Users WHERE username = ?)
                     AND receiver_id = (SELECT user_id FROM Users WHERE username = ?))
                     ORDER BY timestamp ASC
                     """)) {
            stmt.setString(1, username);
            stmt.setString(2, selectedFriend);
            stmt.setString(3, selectedFriend);
            stmt.setString(4, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String message = rs.getString("message_text");
                String timestamp = rs.getString("timestamp");
                String sender = rs.getInt("sender_id") == getUserId(username) ? "You" : selectedFriend;
                chatArea.append(sender + ": " + message + " (" + timestamp + ")\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Send a message to a selected friend
    private void sendMessage() {
        String selectedFriend = (String) friendList.getSelectedItem();
        String message = messageField.getText().trim();

        if (selectedFriend == null || message.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a friend and type a message.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db");
             PreparedStatement stmt = conn.prepareStatement("""
                     INSERT INTO Messages (sender_id, receiver_id, message_text)
                     VALUES ((SELECT user_id FROM Users WHERE username = ?),
                             (SELECT user_id FROM Users WHERE username = ?), ?)
                     """)) {
            stmt.setString(1, username);
            stmt.setString(2, selectedFriend);
            stmt.setString(3, message);
            stmt.executeUpdate();

            // Update chat area
            chatArea.append("You: " + message + " (Just now)\n");
            messageField.setText(""); // Clear input field
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to get user ID from the database
    private int getUserId(String username) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db");
             PreparedStatement stmt = conn.prepareStatement("""
                     SELECT user_id FROM Users WHERE username = ?
                     """)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // User not found
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MessagePage("JohnDoe").setVisible(true));
    }
}