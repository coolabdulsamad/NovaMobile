/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Custom_Code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CustomTitleBar extends JFrame {
    private int posX = 0, posY = 0; // Variables for dragging functionality

    public CustomTitleBar(String title, String iconPath, int width, int height) {
        // Remove default title bar
        setUndecorated(true);
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Set custom icon if provided
        if (iconPath != null && !iconPath.isEmpty()) {
            setIconImage(new ImageIcon(iconPath).getImage());
        }

        // Create a custom title bar panel
        JPanel titleBar = new JPanel();
        titleBar.setBackground(new Color(0, 123, 255)); // Set title bar color
        titleBar.setLayout(new BorderLayout());
        titleBar.setPreferredSize(new Dimension(width, 40)); // Set fixed height for the title bar

        // Add icon to the title bar (if provided)
        JLabel iconLabel = new JLabel();
        if (iconPath != null && !iconPath.isEmpty()) {
            iconLabel.setIcon(new ImageIcon(iconPath));
            iconLabel.setPreferredSize(new Dimension(35, 35));
        }

        // Add title text
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        buttonPanel.setOpaque(false);

        // Create minimize button
        JButton minimizeButton = new JButton("—");
        customizeButton(minimizeButton, new Color(255, 204, 0));
        minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED));

        // Create close button
        JButton closeButton = new JButton("X");
        customizeButton(closeButton, new Color(220, 20, 60));
        closeButton.addActionListener(e -> System.exit(0));

        // Add buttons to the panel
        buttonPanel.add(minimizeButton);
        buttonPanel.add(closeButton);

        // Add components to the title bar
        if (iconPath != null && !iconPath.isEmpty()) {
            titleBar.add(iconLabel, BorderLayout.WEST);
        }
        titleBar.add(titleLabel, BorderLayout.CENTER);
        titleBar.add(buttonPanel, BorderLayout.EAST);

        // Make title bar draggable
        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                posX = e.getX();
                posY = e.getY();
            }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                setLocation(e.getXOnScreen() - posX, e.getYOnScreen() - posY);
            }
        });

        // Add the custom title bar to the frame
        add(titleBar, BorderLayout.NORTH);

        // This will make sure the content area is below the title bar
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout()); // Use BorderLayout for content
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Method to customize buttons
    private void customizeButton(JButton button, Color color) {
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(40, 30));
    }
}
