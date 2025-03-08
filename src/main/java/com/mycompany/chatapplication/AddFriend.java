/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import javax.swing.*;
import java.awt.*;

public class AddFriend extends JPanel {
    private JPanel suggestedFriendsPanel, searchPanel, userDetailsPanel;

    public AddFriend() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(900, 500)); // Adjusted for perfect layout

        // **Left Panel: Suggested Friends**
        suggestedFriendsPanel = new JPanel();
        suggestedFriendsPanel.setLayout(new BoxLayout(suggestedFriendsPanel, BoxLayout.Y_AXIS));
        suggestedFriendsPanel.setBackground(Color.WHITE);
        suggestedFriendsPanel.setBorder(BorderFactory.createTitledBorder("Suggested Friends"));
        suggestedFriendsPanel.setPreferredSize(new Dimension(250, 500));

        // **Middle Panel: Search & Results**
        searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createTitledBorder("Find Friends"));
        searchPanel.setPreferredSize(new Dimension(400, 500));

        // **Right Panel: User Details / Illustration**
        userDetailsPanel = new JPanel(new BorderLayout());
        userDetailsPanel.setBackground(Color.WHITE);
        userDetailsPanel.setBorder(BorderFactory.createTitledBorder("User Details"));
        userDetailsPanel.setPreferredSize(new Dimension(250, 500));

        // Add all sections to the main layout
        add(suggestedFriendsPanel, BorderLayout.WEST);
        add(searchPanel, BorderLayout.CENTER);
        add(userDetailsPanel, BorderLayout.EAST);
    }

    // **Run this to test the layout**
    public static void main(String[] args) {
        JFrame frame = new JFrame("Find Friends");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 500);
        frame.add(new AddFriend());
        frame.setVisible(true);
    }
}

