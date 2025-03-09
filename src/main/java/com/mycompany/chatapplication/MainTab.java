/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import Application_Connector.db.DatabaseHelper;
import MainStart_Test.TypingEffectDemo;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.prefs.Preferences;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

public class MainTab {
    public static void main(String[] args) {
            if (GraphicsEnvironment.isHeadless()) {
        System.out.println("Running in headless mode — skipping GUI.");
        return; // Exit if running in headless mode (like on Render)
    }
        
        SwingUtilities.invokeLater(() -> {
            if (!attemptAutoLogin()) {  // If auto-login fails, show Welcome Screen
                showWelcomeScreen();
            }
        });

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db");
             Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA journal_mode=WAL;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Auto-login method (returns true if login succeeds)
    private static boolean attemptAutoLogin() {
        Preferences prefs = Preferences.userNodeForPackage(MainTab.class);
        String savedEmail = prefs.get("rememberedEmail", "");
        String savedPassword = prefs.get("rememberedPassword", "");

        if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
            DatabaseHelper dbHelper = new DatabaseHelper();
            String userId = dbHelper.validateLogin(savedEmail, savedPassword);

            if (userId != null) {
                int loggedInUserId = Integer.parseInt(userId);
                dbHelper.updateOnlineStatus(loggedInUserId, "Online");

                String username = dbHelper.getUsernameById(loggedInUserId);
                //JOptionPane.showMessageDialog(null, "Auto-login successful!");

                new Dashboard(username).setVisible(true);
                UserSession.setCurrentUsername(loggedInUserId, username);
                return true;  // Auto-login successful, no need to show Welcome Screen
            }
        }
        return false;  // Auto-login failed, show Welcome Screen
    }

    // Show the Welcome Screen (MainTab)
    private static void showWelcomeScreen() {
        JFrame frame = new JFrame("NovaMobile");
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        frame.getContentPane().setBackground(new Color(225, 233, 241));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        URL iconURL = MainTab.class.getClassLoader().getResource("icons/novamobile.jpg");
        if (iconURL != null) {
            frame.setIconImage(new ImageIcon(iconURL).getImage());
        } else {
            System.err.println("Icon image not found.");
        }

        JPanel typingPanel = TypingEffectDemo.createTypingEffectPanel(frame);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        frame.add(typingPanel, gbc);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}




// Window.setResizable(false)