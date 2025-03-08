/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ManageFriendRequests extends JFrame {
    private String username;

    public ManageFriendRequests(String username) {
        this.username = username;

        // Set up the frame
        setTitle("Manage Friend Requests");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        // Set the icon for the frame (you can change the path to your icon)
        setIconImage(new ImageIcon("C:\\Users\\coola\\Documents\\NetBeansProjects\\ChatApplication\\src\\main\\resources\\icons\\novamobile.jpg").getImage()); // Update the icon path

        // Create table model and table
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Request ID");
        tableModel.addColumn("Friend Username");
        tableModel.addColumn("Action");

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Load friend requests from the database
        loadFriendRequests(tableModel);

        // Add Accept and Reject buttons to the table
        table.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox(), tableModel, username));

        // Add components to the frame
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadFriendRequests(DefaultTableModel tableModel) {
        String url = "jdbc:sqlite:chatting_app.db";
        String query = """
                SELECT f.id AS request_id, u.username AS friend_username
                FROM Friends f
                JOIN Users u ON f.user_id = u.user_id
                WHERE f.friend_id = (SELECT user_id FROM Users WHERE username = ?)
                AND f.status = 'Pending';
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int requestId = rs.getInt("request_id");
                String friendUsername = rs.getString("friend_username");
                tableModel.addRow(new Object[]{requestId, friendUsername, "Accept/Reject"});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageFriendRequests("JohnDoe").setVisible(true));
    }
}