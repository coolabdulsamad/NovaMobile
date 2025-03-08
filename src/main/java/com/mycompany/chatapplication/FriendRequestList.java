/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.*;
import javax.sound.sampled.*;

public class FriendRequestList extends JPanel {
    private JPanel panel;
    private String currentUser;
    private ImageIcon defaultProfileIcon;
    private Timer refreshTimer;
    private int lastRequestCount = 0; // Track number of requests
    private String username;
    private JPanel suggestedPanel;
    private JLabel placeholderMessage;
    private JPanel historyPanel;
    private JPanel pendingRequestsPanel;

private Dashboard parentDashboard; // Store reference to Dashboard
private Dashboard dashboard;

public FriendRequestList(String username, Dashboard dashboard) {
    this.currentUser = username;
    this.parentDashboard = dashboard;

    startAutoRefresh();
    
    // Set the main layout
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(1000, 600)); // Adjusted for better spacing

    // Create the left panel for pending requests
    JPanel pendingRequestsPanel = new JPanel();
    pendingRequestsPanel.setLayout(new BoxLayout(pendingRequestsPanel, BoxLayout.Y_AXIS));
    pendingRequestsPanel.setBackground(Color.WHITE);

    JScrollPane pendingScroll = new JScrollPane(pendingRequestsPanel);
    pendingScroll.setPreferredSize(new Dimension(300, 600));
    pendingScroll.setBorder(BorderFactory.createTitledBorder("Pending Friend Requests"));

    // ✅ Middle Panel (Summary, Suggested Friends, Placeholder)
    JPanel middlePanel = new JPanel();
    middlePanel.setLayout(new BorderLayout());
    middlePanel.setPreferredSize(new Dimension(350, 600));
    middlePanel.setBackground(Color.WHITE);
    middlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

    // Summary Panel
    JPanel summaryPanel = createSummaryPanel();
    middlePanel.add(summaryPanel, BorderLayout.NORTH);

    // Suggested Friends
    JPanel suggestedFriendsPanel = new JPanel();
    suggestedFriendsPanel.setLayout(new BoxLayout(suggestedFriendsPanel, BoxLayout.Y_AXIS));
    suggestedFriendsPanel.setBorder(BorderFactory.createTitledBorder("Friends Suggestion!"));
    middlePanel.add(new JScrollPane(suggestedFriendsPanel), BorderLayout.CENTER);
    
    
    // Placeholder Message
    JLabel placeholderMessage = new JLabel("No new requests. Find new friends!");
    placeholderMessage.setHorizontalAlignment(SwingConstants.CENTER);
    placeholderMessage.setForeground(Color.GRAY);
    placeholderMessage.setFont(new Font("Arial", Font.ITALIC, 14));
    middlePanel.add(placeholderMessage, BorderLayout.SOUTH);

    // Right panel for history
    JPanel historyPanel = new JPanel();
    historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
    historyPanel.setBackground(new Color(173, 216, 230)); // Light Blue

    JScrollPane historyScroll = new JScrollPane(historyPanel);
    historyScroll.setPreferredSize(new Dimension(300, 600));
    historyScroll.setBorder(BorderFactory.createTitledBorder("Request History"));

    // Add all panels to the main layout
    add(pendingScroll, BorderLayout.WEST);
    add(middlePanel, BorderLayout.CENTER);
    add(historyScroll, BorderLayout.EAST);

    // ✅ Fetch pending requests, history, and suggested friends
    fetchPendingRequests(pendingRequestsPanel);
    fetchRequestHistory(historyPanel);
    fetchSuggestedFriends(suggestedFriendsPanel, placeholderMessage); 
    
        // UI Updates
    revalidate();
    repaint();
}


private void fetchRequestHistory(JPanel historyPanel) {
    SwingWorker<Void, JPanel> worker = new SwingWorker<>() {
        @Override
        protected Void doInBackground() {
            historyPanel.removeAll(); // Clear previous history

            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
                int userId = getUserId(conn, currentUser);
                String query = "SELECT u.user_id, u.username, u.profile_image, f.sent_at, f.created_at, f.status " +
                               "FROM Users u " +
                               "JOIN Friends f ON (u.user_id = f.friend_id AND f.user_id = ?) " + // You sent a request
                               "   OR (u.user_id = f.user_id AND f.friend_id = ?) " + // You accepted/rejected a request
                               "WHERE f.status IN ('Accepted', 'Rejected') " +
                               "ORDER BY f.created_at DESC"; // Show latest first

                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, userId);
                stmt.setInt(2, userId);
                ResultSet rs = stmt.executeQuery();

                int count = 1; // Start numbering from 1

                while (rs.next()) {
                    int friendId = rs.getInt("user_id");
                    String friendUsername = rs.getString("username");
                    byte[] profileImageBytes = rs.getBytes("profile_image");
                    String sentAt = rs.getString("sent_at");
                    String createdAt = rs.getString("created_at");
                    String status = rs.getString("status");

                    // ✅ Pass the numbering to the UI
                    JPanel historyPanelItem = createHistoryPanel(count, friendId, friendUsername, profileImageBytes, sentAt, createdAt, status);
                    publish(historyPanelItem);
                    count++; // Increment number for next request
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void process(java.util.List<JPanel> chunks) {
            for (JPanel historyItem : chunks) {
                historyPanel.add(historyItem);
            }
            historyPanel.revalidate();
            historyPanel.repaint();
        }
    };
    worker.execute();
}




private JPanel createHistoryPanel(int number, int friendId, String friendUsername, byte[] profileImageBytes, String sentAt, String createdAt, String status) {
    JPanel historyPanelItem = new JPanel();
    historyPanelItem.setLayout(new BorderLayout());
    historyPanelItem.setPreferredSize(new Dimension(320, 70));
    historyPanelItem.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
    historyPanelItem.setBackground(Color.WHITE);
    historyPanelItem.setMaximumSize(new Dimension(320, 70));

    // Profile Image
    JLabel profileLabel = new JLabel();
    profileLabel.setPreferredSize(new Dimension(50, 50));
    profileLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

    if (profileImageBytes != null) {
        ImageIcon profileIcon = new ImageIcon(profileImageBytes);
        Image img = profileIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        profileLabel.setIcon(new ImageIcon(img));
    } else {
        profileLabel.setIcon(defaultProfileIcon);
    }

    // User Info Panel
    JPanel userInfoPanel = new JPanel();
    userInfoPanel.setLayout(new GridBagLayout());
    userInfoPanel.setBackground(Color.WHITE);
    userInfoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;

    JLabel numberLabel = new JLabel(number + ". "); // Add numbering
    numberLabel.setForeground(Color.BLACK);
    numberLabel.setFont(new Font("Arial", Font.BOLD, 14));

    JLabel usernameLabel = new JLabel(friendUsername);
    usernameLabel.setForeground(new Color(0, 0, 139)); // Dark Blue
    usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));

    JLabel sentAtLabel = new JLabel("Sent: " + sentAt);
    sentAtLabel.setFont(new Font("Arial", Font.ITALIC, 10));
    sentAtLabel.setForeground(Color.GRAY);

    JLabel createdAtLabel = new JLabel("Status: " + status + " | " + createdAt);
    createdAtLabel.setFont(new Font("Arial", Font.BOLD, 10));
    createdAtLabel.setForeground(status.equals("Accepted") ? new Color(0, 128, 0) : new Color(220, 20, 60)); // Green or Red

    JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    topRow.setBackground(Color.WHITE);
    topRow.add(numberLabel);
    topRow.add(usernameLabel);

    userInfoPanel.add(topRow, gbc);
    gbc.gridy++;
    userInfoPanel.add(sentAtLabel, gbc);
    gbc.gridy++;
    userInfoPanel.add(createdAtLabel, gbc);

    // Layout
    historyPanelItem.add(profileLabel, BorderLayout.WEST);
    historyPanelItem.add(userInfoPanel, BorderLayout.CENTER);

    return historyPanelItem;
}




private void fetchPendingRequests(JPanel pendingRequestsPanel) {
    SwingWorker<Void, JPanel> worker = new SwingWorker<>() {
        @Override
        protected Void doInBackground() {
            pendingRequestsPanel.removeAll(); // Clear old requests

            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
                int userId = getUserId(conn, currentUser);
                String query = "SELECT u.user_id, u.username, u.profile_image, f.sent_at " +
                               "FROM Users u " +
                               "JOIN Friends f ON u.user_id = f.user_id " +
                               "WHERE f.friend_id = ? AND f.status = 'Pending'";

                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int friendId = rs.getInt("user_id");
                    String friendUsername = rs.getString("username");
                    byte[] profileImageBytes = rs.getBytes("profile_image");
                    String sentAt = rs.getString("sent_at");

                    JPanel requestPanel = createRequestPanel(friendId, friendUsername, profileImageBytes, sentAt);
                    
                    // 🔹 Wrap request panels in a small container to avoid layout issues
                    JPanel container = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
                    container.setBackground(Color.WHITE);
                    container.add(requestPanel);

                    publish(container);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void process(java.util.List<JPanel> chunks) {
            for (JPanel requestPanel : chunks) {
                pendingRequestsPanel.add(requestPanel);
            }
            pendingRequestsPanel.revalidate();
            pendingRequestsPanel.repaint();
        }
    };
    worker.execute();
}


private JPanel createRequestPanel(int friendId, String friendUsername, byte[] profileImageBytes, String sentAt) {
    // Main request panel (Rectangular card)
    JPanel requestPanel = new JPanel();
    requestPanel.setLayout(new BorderLayout());
    requestPanel.setPreferredSize(new Dimension(320, 80)); // Fixed width, increased height
    requestPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
    requestPanel.setBackground(Color.WHITE);
    requestPanel.setMaximumSize(new Dimension(320, 80)); // Prevent stretching

    // Profile Image
    JLabel profileLabel = new JLabel();
    profileLabel.setPreferredSize(new Dimension(50, 50));
    profileLabel.setBorder(BorderFactory.createEmptyBorder(7, 5, 7, 10)); // Add padding
    
    if (profileImageBytes != null) {
        ImageIcon profileIcon = new ImageIcon(profileImageBytes);
        Image img = profileIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        profileLabel.setIcon(new ImageIcon(img));
    } else {
        profileLabel.setIcon(defaultProfileIcon);
    }

    // User Info Panel (Username & Sent At)
    JPanel userInfoPanel = new JPanel();
    userInfoPanel.setLayout(new GridBagLayout()); // Center the text vertically
    userInfoPanel.setBackground(Color.WHITE);
    userInfoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add padding

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;

    JLabel usernameLabel = new JLabel(friendUsername);
    usernameLabel.setForeground(new Color(0, 0, 139)); // Dark Blue
    usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));

    JLabel sentAtLabel = new JLabel("Sent: " + sentAt);
    sentAtLabel.setFont(new Font("Arial", Font.ITALIC, 10));
    sentAtLabel.setForeground(Color.GRAY);

    userInfoPanel.add(usernameLabel, gbc);
    gbc.gridy++; // Move to the next row
    userInfoPanel.add(sentAtLabel, gbc);

    // Buttons Panel (Fixed Size)
    JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));
    buttonPanel.setBackground(Color.WHITE);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(25, 1, 25, 3)); // Padding around buttons

    JButton acceptButton = new JButton("Accept");
    acceptButton.setBackground(new Color(30, 144, 255)); // Blue
    acceptButton.setFont(new Font("Arial", Font.BOLD, 8));
    acceptButton.setForeground(Color.WHITE);
    acceptButton.setPreferredSize(new Dimension(65, 5)); // Fixed size
    acceptButton.setFocusable(false);
    acceptButton.addActionListener(e -> handleFriendRequest(friendId, "Accepted"));

    JButton rejectButton = new JButton("Reject");
    rejectButton.setBackground(new Color(220, 20, 60)); // Red
    rejectButton.setFont(new Font("Arial", Font.BOLD, 8));
    rejectButton.setForeground(Color.WHITE);
    rejectButton.setPreferredSize(new Dimension(65, 5)); // Fixed size
    rejectButton.setFocusable(false);
    rejectButton.addActionListener(e -> handleFriendRequest(friendId, "Rejected"));

    buttonPanel.add(acceptButton);
    buttonPanel.add(rejectButton);

    // Layout Management
    JPanel contentPanel = new JPanel(new BorderLayout());
    contentPanel.setBackground(Color.WHITE);
    contentPanel.add(profileLabel, BorderLayout.WEST);
    contentPanel.add(userInfoPanel, BorderLayout.CENTER);
    contentPanel.add(buttonPanel, BorderLayout.EAST);

    requestPanel.add(contentPanel, BorderLayout.CENTER);

    return requestPanel;
}




    private void fetchFriendRequests() {
        SwingWorker<Void, JPanel> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                panel.removeAll();
                int newRequestCount = 0;
                try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
                    int userId = getUserId(conn, currentUser);
                    String query = "SELECT u.user_id, u.username, u.profile_image FROM Users u " +
                                   "JOIN Friends f ON u.user_id = f.user_id " +
                                   "WHERE f.friend_id = ? AND f.status = 'Pending'";

                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setInt(1, userId);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        int friendId = rs.getInt("user_id");
                        String friendUsername = rs.getString("username");
                        byte[] profileImageBytes = rs.getBytes("profile_image");

                        JPanel requestPanel = createRequestPanel(friendId, friendUsername, profileImageBytes);
                        publish(requestPanel);
                        newRequestCount++;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // If new friend requests arrive, play sound and show notification
                if (newRequestCount > lastRequestCount) {
                    playNotificationSound();
                    showNotification("You have a new friend request!");
                }
                lastRequestCount = newRequestCount;

                return null;
            }

            @Override
            protected void process(java.util.List<JPanel> chunks) {
                for (JPanel requestPanel : chunks) {
                    panel.add(requestPanel);
                    panel.revalidate();
                    panel.repaint();
                }
            }
        };
        worker.execute();
    }

    private JPanel createRequestPanel(int friendId, String friendUsername, byte[] profileImageBytes) {
        JPanel requestPanel = new JPanel();
        requestPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        requestPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Load profile image or default image
        JLabel profileLabel = new JLabel();
        profileLabel.setPreferredSize(new Dimension(40, 40));
        if (profileImageBytes != null) {
            ImageIcon profileIcon = new ImageIcon(profileImageBytes);
            Image img = profileIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            profileLabel.setIcon(new ImageIcon(img));
        } else {
            profileLabel.setIcon(defaultProfileIcon);
        }

        // Click profile image to view full profile
        profileLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        profileLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new UserProfile(friendId, dashboard);
            }
        });

        JLabel usernameLabel = new JLabel(friendUsername);
        JButton acceptButton = new JButton("Accept");
        JButton rejectButton = new JButton("Reject");

        acceptButton.addActionListener(e -> handleFriendRequest(friendId, "Accepted"));
        rejectButton.addActionListener(e -> handleFriendRequest(friendId, "Rejected"));

        requestPanel.add(profileLabel);
        requestPanel.add(usernameLabel);
        requestPanel.add(acceptButton);
        requestPanel.add(rejectButton);

        return requestPanel;
    }

    private void handleFriendRequest(int friendId, String status) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
            int userId = getUserId(conn, currentUser);
            String query = "UPDATE Friends SET status = ? WHERE user_id = ? AND friend_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, status);
            stmt.setInt(2, friendId);
            stmt.setInt(3, userId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Friend request " + status);
            
if (parentDashboard != null) {
    parentDashboard.refreshFriendsPanel(); // ✅ Refresh the Friends List
}


            
            fetchFriendRequests();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getUserId(Connection conn, String username) throws SQLException {
        String query = "SELECT user_id FROM Users WHERE username = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        return rs.next() ? rs.getInt("user_id") : -1;
    }

    private void startAutoRefresh() {
        refreshTimer = new Timer(5000, (ActionEvent e) -> fetchFriendRequests());
        refreshTimer = new Timer(5000, (ActionEvent e) -> fetchSuggestedFriends(suggestedPanel, placeholderMessage));
        refreshTimer = new Timer(5000, (ActionEvent e) -> fetchRequestHistory(historyPanel));
        refreshTimer.start();
    }

    private void playNotificationSound() {
        try {
            File soundFile = new File("sounds/notification.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

private void showNotification(String message) {
    if (SystemTray.isSupported()) {
        try {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage("resources/icons/user.png"); // Use your default image
            TrayIcon trayIcon = new TrayIcon(image, "Friend Request");
            trayIcon.setImageAutoSize(true);
            tray.add(trayIcon);

            trayIcon.displayMessage("New Friend Request", message, MessageType.INFO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    } else {
        System.out.println("System tray not supported!");
    }
}

private JPanel createSummaryPanel() {
    JPanel panel = new JPanel(new GridLayout(1, 3, 10, 0)); // Three columns
    panel.setBackground(Color.WHITE);
    panel.setBorder(BorderFactory.createTitledBorder("Request Summary"));

    JLabel pendingLabel = new JLabel("Pending: 0", SwingConstants.CENTER);
    JLabel acceptedLabel = new JLabel("Accepted: 0", SwingConstants.CENTER);
    JLabel rejectedLabel = new JLabel("Rejected: 0", SwingConstants.CENTER);

    pendingLabel.setFont(new Font("Arial", Font.BOLD, 14));
    acceptedLabel.setFont(new Font("Arial", Font.BOLD, 14));
    rejectedLabel.setFont(new Font("Arial", Font.BOLD, 14));

    panel.add(pendingLabel);
    panel.add(acceptedLabel);
    panel.add(rejectedLabel);

    // ✅ Update values dynamically
    updateSummaryPanel(pendingLabel, acceptedLabel, rejectedLabel);
    return panel;
}

private void updateSummaryPanel(JLabel pendingLabel, JLabel acceptedLabel, JLabel rejectedLabel) {
    SwingWorker<Void, Void> worker = new SwingWorker<>() {
        @Override
        protected Void doInBackground() {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
                int userId = getUserId(conn, currentUser);
                
                // Count Pending Requests
                String pendingQuery = "SELECT COUNT(*) FROM Friends WHERE friend_id = ? AND status = 'Pending'";
                PreparedStatement pendingStmt = conn.prepareStatement(pendingQuery);
                pendingStmt.setInt(1, userId);
                ResultSet pendingRs = pendingStmt.executeQuery();
                int pendingCount = pendingRs.next() ? pendingRs.getInt(1) : 0;

                // Count Accepted Requests
                String acceptedQuery = "SELECT COUNT(*) FROM Friends WHERE (user_id = ? OR friend_id = ?) AND status = 'Accepted'";
                PreparedStatement acceptedStmt = conn.prepareStatement(acceptedQuery);
                acceptedStmt.setInt(1, userId);
                acceptedStmt.setInt(2, userId);
                ResultSet acceptedRs = acceptedStmt.executeQuery();
                int acceptedCount = acceptedRs.next() ? acceptedRs.getInt(1) : 0;

                // Count Rejected Requests
                String rejectedQuery = "SELECT COUNT(*) FROM Friends WHERE (user_id = ? OR friend_id = ?) AND status = 'Rejected'";
                PreparedStatement rejectedStmt = conn.prepareStatement(rejectedQuery);
                rejectedStmt.setInt(1, userId);
                rejectedStmt.setInt(2, userId);
                ResultSet rejectedRs = rejectedStmt.executeQuery();
                int rejectedCount = rejectedRs.next() ? rejectedRs.getInt(1) : 0;

                // Update UI
                pendingLabel.setText("Pending: " + pendingCount);
                acceptedLabel.setText("Accepted: " + acceptedCount);
                rejectedLabel.setText("Rejected: " + rejectedCount);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    };
    worker.execute();
}

private void fetchSuggestedFriends(JPanel suggestedPanel, JLabel placeholderMessage) {
    SwingWorker<Void, JPanel> worker = new SwingWorker<>() {
        @Override
        protected Void doInBackground() {
            suggestedPanel.removeAll(); // Clear existing suggestions

            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
                int userId = getUserId(conn, currentUser);
                
                // ✅ Exclude already accepted friends & fetch additional details
                String query = "SELECT u.user_id, u.username, u.profile_image, u.first_name, u.last_name, " +
                               "u.email, u.phone_number, u.country, u.bio, u.online_status, " +
                               "(SELECT COUNT(*) FROM Friends WHERE user_id = u.user_id OR friend_id = u.user_id) AS friends_count " +
                               "FROM Users u " +
                               "WHERE u.user_id NOT IN (" +
                               "   SELECT friend_id FROM Friends WHERE user_id = ? AND status = 'Accepted' " +
                               "   UNION " +
                               "   SELECT user_id FROM Friends WHERE friend_id = ? AND status = 'Accepted' " +
                               ") " +
                               "AND u.user_id <> ? ORDER BY RANDOM() LIMIT 5";

                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, userId);
                stmt.setInt(2, userId);
                stmt.setInt(3, userId);
                ResultSet rs = stmt.executeQuery();

                boolean hasSuggestions = false;
                while (rs.next()) {
                    hasSuggestions = true;
                    JPanel userPanel = createSuggestedUserPanel(
                        rs.getInt("user_id"), rs.getString("username"), rs.getBytes("profile_image"),
                        rs.getString("first_name") + " " + rs.getString("last_name"), rs.getString("email"),
                        rs.getString("phone_number"), rs.getString("country"), rs.getString("bio"),
                        rs.getString("online_status"), rs.getInt("friends_count")
                    );
                    publish(userPanel);
                }

                placeholderMessage.setVisible(!hasSuggestions);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void process(java.util.List<JPanel> chunks) {
            for (JPanel userPanel : chunks) {
                suggestedPanel.add(userPanel);
            }
            suggestedPanel.revalidate();
            suggestedPanel.repaint();
        }
    };
    worker.execute();
}



private JPanel createSuggestedUserPanel(int userId, String username, byte[] profileImageBytes,
                                        String fullName, String email, String phone, String country,
                                        String bio, String onlineStatus, int friendsCount) {
    // **Main Panel (Fixed Size)**
    JPanel userPanel = new JPanel(new BorderLayout());
    userPanel.setPreferredSize(new Dimension(480, 190)); // **Increased size for clarity**
    userPanel.setMaximumSize(new Dimension(480, 190)); // **Stops shrinking**
    userPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), 
            BorderFactory.createEmptyBorder(8, 8, 8, 8))); // **Padding**
    userPanel.setBackground(Color.WHITE);

    
    // **Profile Image (Fixed Size)**
    JLabel profileLabel = new JLabel();
    profileLabel.setPreferredSize(new Dimension(55, 55)); // **Bigger Image**
    profileLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15)); // **Padding**
    if (profileImageBytes != null) {
        ImageIcon profileIcon = new ImageIcon(profileImageBytes);
        Image img = profileIcon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
        profileLabel.setIcon(new ImageIcon(img));
    } else {
        profileLabel.setIcon(defaultProfileIcon);
    }

    // **User Info Panel (Username, Full Name, and Details)**
    JPanel userInfoPanel = new JPanel(new GridLayout(3, 1)); // **3 Rows: Username, Full Name, Details**
    userInfoPanel.setBackground(Color.WHITE);

    JLabel usernameLabel = new JLabel("   "+username);
    usernameLabel.setForeground(new Color(0, 0, 139)); // **Dark Blue**
    usernameLabel.setFont(new Font("Arial", Font.BOLD, 12));

    JLabel fullNameLabel = new JLabel(fullName);
    fullNameLabel.setFont(new Font("Arial", Font.PLAIN, 11));
    fullNameLabel.setForeground(Color.DARK_GRAY);

    JLabel detailsLabel = new JLabel("   "+friendsCount + " friends ");
    detailsLabel.setFont(new Font("Arial", Font.PLAIN, 10));
    detailsLabel.setForeground(Color.GRAY);
    
    JLabel emailLabel = new JLabel(email);
    emailLabel.setFont(new Font("Arial", Font.PLAIN, 10));
    emailLabel.setForeground(Color.GRAY);

    JLabel phoneLabel = new JLabel("   "+phone + " | " + country);
    phoneLabel.setFont(new Font("Arial", Font.PLAIN, 10));
    phoneLabel.setForeground(Color.GRAY);

    userInfoPanel.add(usernameLabel);
    userInfoPanel.add(fullNameLabel);
    userInfoPanel.add(detailsLabel);
    userInfoPanel.add(emailLabel);
    userInfoPanel.add(phoneLabel);

    // **Status Indicator (Top Right)**
    JLabel statusLabel = new JLabel(onlineStatus.equals("Online") ? "🟢 Online" : "🔴 Offline");
    statusLabel.setFont(new Font("Arial", Font.PLAIN, 9));
    statusLabel.setForeground(onlineStatus.equals("Online") ? new Color(0, 128, 0) : new Color(220, 20, 60));

    // **"Add Friend" Button (Bottom Right)**
    JButton addFriendButton = new JButton("➕ Add");
    addFriendButton.setBackground(new Color(30, 144, 255)); // **Blue**
    addFriendButton.setForeground(Color.WHITE);
    addFriendButton.setFont(new Font("Arial", Font.BOLD, 10)); // **Neat Font**
    addFriendButton.setPreferredSize(new Dimension(65, 25)); 
    addFriendButton.setFocusPainted(false);

    // **Hover effect for button**
    addFriendButton.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            addFriendButton.setBackground(new Color(0, 102, 204)); // **Darker Blue on Hover**
        }
        public void mouseExited(java.awt.event.MouseEvent evt) {
            addFriendButton.setBackground(new Color(30, 144, 255)); // **Normal Blue**
        }
    });

    addFriendButton.addActionListener(e -> sendFriendRequest(userId));

    // **Right Panel (Status on Top, Button on Bottom)**
    JPanel rightPanel = new JPanel(new BorderLayout());
    rightPanel.setBackground(Color.WHITE);
    rightPanel.add(statusLabel, BorderLayout.NORTH);
    rightPanel.add(addFriendButton, BorderLayout.SOUTH);

    // **Main Row Layout (Profile Image + Details + Right Panel)**
    JPanel topContentPanel = new JPanel(new BorderLayout());
    topContentPanel.setBackground(Color.WHITE);
    topContentPanel.add(profileLabel, BorderLayout.WEST);
    topContentPanel.add(userInfoPanel, BorderLayout.CENTER);
    topContentPanel.add(rightPanel, BorderLayout.EAST);

    // **Bio Panel (Scrollable)**
    JPanel bioPanel = new JPanel(new BorderLayout());
    bioPanel.setBackground(new Color(240, 240, 240)); // **Light Gray Background**
    bioPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // **Padding Inside**
    bioPanel.setPreferredSize(new Dimension(270, 100)); // **Fixed Bio Panel Height**

    JTextArea bioTextArea = new JTextArea();
    bioTextArea.setFont(new Font("Arial", Font.ITALIC, 10));
    bioTextArea.setForeground(Color.DARK_GRAY);
    bioTextArea.setWrapStyleWord(true);
    bioTextArea.setLineWrap(true);
    bioTextArea.setEditable(false);
    bioTextArea.setOpaque(false);

    if (bio != null && !bio.isEmpty()) {
        bioTextArea.setText(bio);
    } else {
        bioTextArea.setText("No bio available.");
        bioTextArea.setForeground(Color.LIGHT_GRAY);
    }

    JScrollPane bioScrollPane = new JScrollPane(bioTextArea);
    bioScrollPane.setBorder(null);
    bioScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    bioScrollPane.setPreferredSize(new Dimension(270, 40)); // **Ensures Bio is Scrollable**

    bioPanel.add(bioScrollPane, BorderLayout.CENTER);

    // **Final Layout (Top Content + Scrollable Bio)**
    JPanel contentPanel = new JPanel(new BorderLayout());
    contentPanel.setBackground(Color.WHITE);
    contentPanel.add(topContentPanel, BorderLayout.NORTH);
    contentPanel.add(bioPanel, BorderLayout.SOUTH);

    userPanel.add(contentPanel, BorderLayout.CENTER);
    return userPanel;
}





private void sendFriendRequest(int friendId) {
    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
        int userId = getUserId(conn, currentUser);

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
            String insertQuery = "INSERT INTO Friends (user_id, friend_id, status, sent_at) VALUES (?, ?, 'Pending', CURRENT_TIMESTAMP)";
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


}