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

public class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private boolean clicked;
    private DefaultTableModel tableModel;
    private String username;
    private int selectedRow; // Keep track of the row being edited

    public ButtonEditor(JCheckBox checkBox, DefaultTableModel tableModel, String username) {
        super(checkBox);
        this.tableModel = tableModel;
        this.username = username;

        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        clicked = true;
        selectedRow = row; // Save the row being edited
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (clicked) {
            int requestId = (int) tableModel.getValueAt(selectedRow, 0); // Get request ID from the selected row
            String friendUsername = (String) tableModel.getValueAt(selectedRow, 1);

            int option = JOptionPane.showOptionDialog(button, "Accept or Reject Friend Request from " + friendUsername + "?",
                    "Manage Friend Request", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, new String[]{"Accept", "Reject"}, "Accept");

            // Update the friend request status in the database
            updateFriendRequestStatus(requestId, option == JOptionPane.YES_OPTION ? "Accepted" : "Rejected");
            tableModel.removeRow(selectedRow); // Remove the processed row from the table
        }
        clicked = false;
        return label;
    }

    private void updateFriendRequestStatus(int requestId, String status) {
        String url = "jdbc:sqlite:chatting_app.db";
        String query = "UPDATE Friends SET status = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, requestId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}