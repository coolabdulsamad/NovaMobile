package com.mycompany.chatapplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.SwingUtilities;


public class Dashboard extends JFrame {
    private int userId;  // Store logged-in user's ID
    private String username;  // Store logged-in user's name
    private JPanel contentPanel; // Right-side panel that updates dynamically
    private CardLayout cardLayout; // For switching views
    private java.util.List<JLabel> sidebarLabels = new java.util.ArrayList<>();
    private FriendsList friendsList;
    private String currentUser; // ✅ Stores the logged-in username
    private static final ImageIcon defaultProfileIcon = new ImageIcon("default_profile.png");
    private JPanel userDetailsPanel;
    private JPanel currentPanel; // Keep track of the currently displayed panel
    private ChatWindow chatWindow; // Declare chatWindow instance
    private int loggedInUserId;
    private int chatUserId;
    private String loggedInUsername;
    private JPanel sidebarPanel;

    private JCheckBox darkModeToggle;
private JComboBox<String> fontSizeDropdown;
private JComboBox<String> themeColorDropdown;
private JCheckBox friendRequestToggle;
private JCheckBox messageNotificationToggle;
private JCheckBox soundToggle;


    

public Dashboard(String username) {
    this.username = username;
    this.userId = fetchUserId(username);
    this.currentUser = username; // ✅ Set the logged-in user
    this.loggedInUsername = username;
    System.out.println("DEBUG: Dashboard initialized with username: " + loggedInUsername);

    setTitle("Dashboard");
    setSize(1000, 1000);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    contentPanel = new JPanel(new CardLayout());  // ✅ Initialize contentPanel before applying settings
   

    
            URL iconURL = MainTab.class.getClassLoader().getResource("icons/novamobile.jpg");
        if (iconURL != null) {
            setIconImage(new ImageIcon(iconURL).getImage());
        } else {
            System.err.println("Icon image not found.");
        }
        
    // Create Sidebar Panel (Left Panel) - Initially small
    JPanel sidebarPanel = new JPanel();
    sidebarPanel.setLayout(new GridBagLayout()); // For centered buttons
    sidebarPanel.setPreferredSize(new Dimension(60, getHeight())); // Compact width
    sidebarPanel.setBackground(new Color(255, 255, 255)); // Dark theme

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(10, 5, 10, 5);

    // Create the Main Content Panel (Right Panel)
    cardLayout = new CardLayout();
    contentPanel = new JPanel(cardLayout);

    // Add different pages to the content panel
    contentPanel.add(createHomePanel(), "home");
    contentPanel.add(createChatPanel(), "chat");
    contentPanel.add(createFriendsPanel(), "friends");
    contentPanel.add(createProfilePanel(), "profile");
    contentPanel.add(createNotificationsPanel(), "notifications");

    // Create Sidebar Buttons with Icons Only
    JButton chatButton = createSidebarIconButton("C:\\Users\\coola\\OneDrive\\Documents\\NetBeansProjects\\ChatApplication\\src\\main\\resources\\icons\\chat.png", "chat", gbc, sidebarPanel, false);
    JButton friendsButton = createSidebarIconButton("C:\\Users\\coola\\OneDrive\\Documents\\NetBeansProjects\\ChatApplication\\src\\main\\resources\\icons\\friends.png", "friends", gbc, sidebarPanel, false);
    JButton profileButton = createSidebarIconButton("icons/profile.png", "profile", gbc, sidebarPanel, true);
    JButton notificationsButton = createSidebarIconButton("C:\\Users\\coola\\OneDrive\\Documents\\NetBeansProjects\\ChatApplication\\src\\main\\resources\\icons\\notifications.png", "notifications", gbc, sidebarPanel, false);
    JButton logoutButton = createSidebarIconButton("C:\\Users\\coola\\OneDrive\\Documents\\NetBeansProjects\\ChatApplication\\src\\main\\resources\\icons\\logout.png", "logout", gbc, sidebarPanel, false);
    


    ImageIcon expandIcon = new ImageIcon("C:\\Users\\coola\\OneDrive\\Documents\\NetBeansProjects\\ChatApplication\\src\\main\\resources\\icons\\expand.png");

// ✅ Resize the expand icon to match other icons (24x24)
Image img = expandIcon.getImage();
Image newImg = img.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
expandIcon = new ImageIcon(newImg);

JButton expandButton = new JButton(expandIcon);
expandButton.setContentAreaFilled(false);
expandButton.setBorderPainted(false);
expandButton.setFocusPainted(false);
expandButton.setPreferredSize(new Dimension(40, 40)); // Keep button small

expandButton.addActionListener(e -> toggleSidebarWidth(sidebarPanel));

    gbc.gridy++;
    sidebarPanel.add(expandButton, gbc);

    // Add everything to the main frame
    add(sidebarPanel, BorderLayout.WEST);
    add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
        cardLayout.show(contentPanel, "home"); // ✅ Show home panel by default
        
        Timer timer = new Timer(5000, e -> {
    if (friendsList != null) {
        friendsList.refreshFriendsList();
    }
});
timer.start();

    }

    private boolean sidebarExpanded = false;

private void toggleSidebarWidth(JPanel sidebarPanel) {
    if (sidebarExpanded) {
        sidebarPanel.setPreferredSize(new Dimension(60, getHeight())); // Shrink
        for (JLabel label : sidebarLabels) {
            label.setVisible(false); // Hide text
        }
    } else {
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight())); // Expand
        for (JLabel label : sidebarLabels) {
            label.setVisible(true); // Show text
        }
    }
    sidebarExpanded = !sidebarExpanded;
    sidebarPanel.revalidate();
    sidebarPanel.repaint();
}

public void loginUser(String username) {
    this.loggedInUsername = username;  // Store the logged-in user's username
    System.out.println("DEBUG: User logged in as " + loggedInUsername);
}



public String getLoggedInUsername() {
    System.out.println("DEBUG: Retrieving loggedInUsername from Dashboard: " + loggedInUsername);
    return loggedInUsername;
}





    // Method to create Sidebar Buttons
// Method to create Sidebar Buttons (Supports Profile Image from DB)
private JButton createSidebarIconButton(String iconPath, String panelName, GridBagConstraints gbc, JPanel sidebarPanel, boolean isProfile) {
    ImageIcon icon;

    if (isProfile) {
        // ✅ Fetch the profile image from the database for the Profile button
        icon = getProfileIcon(userId);
    } else {
        // ✅ Use the normal icon for other buttons
        icon = new ImageIcon(iconPath);
    }

    // ✅ Resize the icon to 24x24
    Image img = icon.getImage();
    Image newImg = img.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
    icon = new ImageIcon(newImg);

    JButton button = new JButton(icon);
    button.setContentAreaFilled(false);
    button.setBorderPainted(false);
    button.setFocusPainted(false);
    button.setPreferredSize(new Dimension(40, 40));

    // ✅ Set Tooltip (Shows when hovering over the button)
    button.setToolTipText(panelName.substring(0, 1).toUpperCase() + panelName.substring(1));

    button.addActionListener(e -> {
    if (panelName.equals("logout")) {
        logout();
    } else if (panelName.equals("settings")) {
        cardLayout.show(contentPanel, "settings");
    } else {
        cardLayout.show(contentPanel, panelName);
    }
});


    gbc.gridy++;
    sidebarPanel.add(button, gbc);

    // ✅ Create Label (Initially Hidden)
    JLabel label = new JLabel(panelName.substring(0, 1).toUpperCase() + panelName.substring(1));
    label.setForeground(new Color(7, 94, 84)); // WhatsApp dark greenish-blue
    label.setVisible(false); // Start hidden
    sidebarLabels.add(label); // Store for later updates

    gbc.gridx = 1;
    sidebarPanel.add(label, gbc);
    gbc.gridx = 0; // Reset for next row

    return button;
}


private JPanel createChatPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    JLabel chatLabel = new JLabel("Chat Window", SwingConstants.CENTER);
    chatLabel.setFont(new Font("Arial", Font.BOLD, 20));
    //panel.add(chatLabel, BorderLayout.NORTH);

    // Now ChatWindow is a JPanel, so we can add it directly!
    ChatWindow chatPanel = new ChatWindow(username, userId, this);
    panel.add(chatPanel, BorderLayout.CENTER);

    return panel;
}

public void refreshFriendsPanel() {
    if (friendsList != null) {
        friendsList.refreshFriendsList(); // ✅ Reloads the Friends List
    }
}


private JPanel createFriendsPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    // ✅ Create Tabs
    JTabbedPane tabbedPane = new JTabbedPane();

    // ✅ Friends Tab
    JPanel friendsTab = new JPanel(new BorderLayout());
    friendsList = new FriendsList(username, this); // ✅ Store the Friends List instance
    friendsTab.add(friendsList, BorderLayout.CENTER);

    tabbedPane.addTab("Friends", friendsTab);

    // ✅ Friend Requests Tab
    JPanel requestsTab = new JPanel(new BorderLayout());
    JButton refreshRequestsButton = new JButton("Refresh Requests");
    refreshRequestsButton.addActionListener(e -> {
        requestsTab.removeAll();
        requestsTab.add(refreshRequestsButton, BorderLayout.NORTH);
        requestsTab.add(new FriendRequestList(username, this), BorderLayout.CENTER);
        requestsTab.revalidate();
        requestsTab.repaint();
    });
    requestsTab.add(refreshRequestsButton, BorderLayout.NORTH);
    requestsTab.add(new FriendRequestList(username, this), BorderLayout.CENTER);
    tabbedPane.addTab("Requests", requestsTab);
//    
//    
//Timer refreshTimer = new Timer(5000, e -> refreshRequestsButton.doClick()); // Refresh every 5 sec
//refreshTimer.start();


    // ✅ Find Friends Tab (New Layout)
JPanel addFriendTab = new JPanel(new BorderLayout());

// **Left Panel: Suggested Friends**
JPanel suggestedFriendsPanel = new JPanel();
suggestedFriendsPanel.setLayout(new BoxLayout(suggestedFriendsPanel, BoxLayout.Y_AXIS));
suggestedFriendsPanel.setBackground(Color.WHITE);
suggestedFriendsPanel.setBorder(BorderFactory.createTitledBorder("Suggested Friends"));
suggestedFriendsPanel.setPreferredSize(new Dimension(250, 500));

JPanel suggestedFriendsContainer = new JPanel();
suggestedFriendsContainer.setLayout(new BoxLayout(suggestedFriendsContainer, BoxLayout.Y_AXIS));
suggestedFriendsContainer.setBackground(Color.WHITE);

JScrollPane scrollPane = new JScrollPane(suggestedFriendsContainer);
scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
scrollPane.setPreferredSize(new Dimension(250, 500)); // Keep size fixed

suggestedFriendsPanel.add(scrollPane);
fetchSuggestedFriends(suggestedFriendsContainer);


// **Middle Panel: Search & Results**
JPanel searchPanel = new JPanel(new BorderLayout());
searchPanel.setBackground(Color.WHITE);
searchPanel.setBorder(BorderFactory.createTitledBorder("Find Friends"));
searchPanel.setPreferredSize(new Dimension(400, 500));

JTextField searchField = new JTextField();
searchField.setPreferredSize(new Dimension(300, 30));

searchField.setText("Search..."); // placeholder text

searchField.addFocusListener(new FocusAdapter() {
    @Override
    public void focusGained(FocusEvent e) {
        if (searchField.getText().equals("Search...")) {
            searchField.setText("");
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (searchField.getText().isEmpty()) {
            searchField.setText("Search...");
        }
    }
});


JButton searchButton = new JButton("🔍");
searchButton.setFont(new Font("Apple Color Emoji", Font.BOLD, 15));
searchButton.setPreferredSize(new Dimension(70, 30));
searchButton.setFocusPainted(false);

// **Search Bar Layout**
JPanel searchBar = new JPanel(new BorderLayout());
searchBar.add(searchField, BorderLayout.CENTER);
searchBar.add(searchButton, BorderLayout.EAST);

JPanel searchResultsPanel = new JPanel();
searchResultsPanel.setLayout(new BoxLayout(searchResultsPanel, BoxLayout.Y_AXIS));
searchResultsPanel.setBackground(Color.WHITE);
searchResultsPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10)); // ✅ Top gap

searchField.getDocument().addDocumentListener(new DocumentListener() {
    @Override
    public void insertUpdate(DocumentEvent e) {
        searchUsers(searchField.getText().trim(), searchResultsPanel);
    }
    @Override
    public void removeUpdate(DocumentEvent e) {
        searchUsers(searchField.getText().trim(), searchResultsPanel);
    }
    @Override
    public void changedUpdate(DocumentEvent e) {
        searchUsers(searchField.getText().trim(), searchResultsPanel);
    }
});

// **Manual Search when Clicking Search Button**
searchButton.addActionListener(e -> {
    String searchText = searchField.getText().trim();
    if (!searchText.isEmpty()) {
        searchUsers(searchText, searchResultsPanel); // ✅ Perform search
    } else {
        searchResultsPanel.removeAll(); // ✅ Clear results if empty
        searchResultsPanel.revalidate();
        searchResultsPanel.repaint();
    }
});



JScrollPane searchScrollPane = new JScrollPane(searchResultsPanel);
searchScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

searchPanel.add(searchBar, BorderLayout.NORTH);
searchPanel.add(searchScrollPane, BorderLayout.CENTER);

// ✅ Initialize userDetailsPanel before using it
userDetailsPanel = new JPanel(new BorderLayout());
userDetailsPanel.setBackground(Color.WHITE);
userDetailsPanel.setBorder(BorderFactory.createTitledBorder("User Details"));
userDetailsPanel.setPreferredSize(new Dimension(250, 500));

// ✅ Default message if no user is selected
JLabel detailsLabel = new JLabel("Select a user to view details.");
detailsLabel.setHorizontalAlignment(SwingConstants.CENTER);
userDetailsPanel.add(detailsLabel, BorderLayout.CENTER);



// **Add All Panels to the Layout**
addFriendTab.add(suggestedFriendsPanel, BorderLayout.WEST);
fetchSuggestedFriends(suggestedFriendsPanel); // ✅ Load suggested friends
addFriendTab.add(searchPanel, BorderLayout.CENTER);
addFriendTab.add(userDetailsPanel, BorderLayout.EAST);

// ✅ Replace the old Find Friends Tab with this
tabbedPane.addTab("Find Friends", addFriendTab);


    // ✅ Add Tabs to Panel
    panel.add(tabbedPane, BorderLayout.CENTER);

    return panel;
}

private void displayUserDetails(int userId) {
    userDetailsPanel.removeAll(); // ✅ Clear previous content

    SwingWorker<Void, Void> worker = new SwingWorker<>() {
        private String username, fullName, email, phone, country, bio, onlineStatus;
        private int friendCount;
        private byte[] profileImageBytes;

        @Override
        protected Void doInBackground() {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
                String query = "SELECT username, first_name || ' ' || last_name AS full_name, email, phone_number, country, bio, online_status, " +
                               "(SELECT COUNT(*) FROM Friends WHERE Friends.user_id = Users.user_id OR Friends.friend_id = Users.user_id) AS friend_count, " +
                               "profile_image FROM Users WHERE user_id = ?";
                
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    username = rs.getString("username");
                    fullName = rs.getString("full_name");
                    email = rs.getString("email");
                    phone = rs.getString("phone_number");
                    country = rs.getString("country");
                    bio = rs.getString("bio") != null ? rs.getString("bio") : "No bio available.";
                    onlineStatus = rs.getString("online_status");
                    friendCount = rs.getInt("friend_count");
                    profileImageBytes = rs.getBytes("profile_image");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void done() {
            userDetailsPanel.setLayout(new BorderLayout());

            // **Main Container Panel**
            JPanel container = new JPanel();
            container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
            container.setBackground(Color.WHITE);
            container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // ✅ Spacing

            // **Profile Image**
            JLabel profileLabel = new JLabel();
            profileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            profileLabel.setPreferredSize(new Dimension(100, 100)); // ✅ Bigger size
            if (profileImageBytes != null) {
                ImageIcon profileIcon = new ImageIcon(profileImageBytes);
                Image img = profileIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                profileLabel.setIcon(new ImageIcon(img));
            } else {
                profileLabel.setIcon(defaultProfileIcon);
            }

            // **User Info Labels**
            JLabel nameLabel = new JLabel(fullName);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16)); // ✅ Bigger font
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel usernameLabel = new JLabel("@" + username);
            usernameLabel.setFont(new Font("Arial", Font.ITALIC, 13));
            usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            usernameLabel.setForeground(Color.DARK_GRAY);

            JLabel emailLabel = new JLabel("📧 " + email);
            emailLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 12));
            emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel phoneLabel = new JLabel("📞 " + phone);
            phoneLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 12));
            phoneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel countryLabel = new JLabel("🌍 " + country);
            countryLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 12));
            countryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel statusLabel = new JLabel(onlineStatus.equals("Online") ? "🟢 Online" : "🔴 Offline");
            statusLabel.setFont(new Font("Apple Color Emoji", Font.BOLD, 13));
            statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            statusLabel.setForeground(onlineStatus.equals("Online") ? new Color(0, 128, 0) : new Color(220, 20, 60));

            JLabel friendCountLabel = new JLabel(friendCount + " friends");
            friendCountLabel.setFont(new Font("Arial", Font.BOLD, 12));
            friendCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // **Bio Section (Scrollable)**
            JTextArea bioTextArea = new JTextArea(bio);
            bioTextArea.setWrapStyleWord(true);
            bioTextArea.setLineWrap(true);
            bioTextArea.setEditable(false);
            bioTextArea.setFont(new Font("Arial", Font.ITALIC, 12));
            bioTextArea.setBackground(new Color(245, 245, 245)); // ✅ Light gray background
            bioTextArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JScrollPane bioScroll = new JScrollPane(bioTextArea);
            bioScroll.setPreferredSize(new Dimension(200, 60)); // ✅ Fixed size
            bioScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            bioScroll.setBorder(BorderFactory.createTitledBorder("Bio"));

            // **Add Friend Button**
            JButton addFriendButton = new JButton("➕ Add Friend");
            addFriendButton.setBackground(new Color(30, 144, 255)); // ✅ Professional blue color
            addFriendButton.setForeground(Color.WHITE);
            addFriendButton.setFont(new Font("Apple Color Emoji", Font.BOLD, 12));
            addFriendButton.setPreferredSize(new Dimension(150, 30));
            addFriendButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            addFriendButton.addActionListener(e -> sendFriendRequest(username));

            // **Add Components to Container**
            container.add(profileLabel);
            container.add(Box.createVerticalStrut(10));
            container.add(nameLabel);
            container.add(usernameLabel);
            container.add(Box.createVerticalStrut(10));
            container.add(emailLabel);
            container.add(phoneLabel);
            container.add(countryLabel);
            container.add(Box.createVerticalStrut(10));
            container.add(statusLabel);
            container.add(friendCountLabel);
            container.add(Box.createVerticalStrut(15));
            container.add(bioScroll);
            container.add(Box.createVerticalStrut(10));
            container.add(addFriendButton);

            userDetailsPanel.removeAll();
            userDetailsPanel.add(container, BorderLayout.CENTER);
            userDetailsPanel.revalidate();
            userDetailsPanel.repaint();
        }
    };
    worker.execute();
}




private void searchUsers(String searchText, JPanel searchResultsPanel) {
    searchResultsPanel.removeAll(); // ✅ Clear previous results

    JLabel resultCountLabel = new JLabel("Searching...");
    resultCountLabel.setFont(new Font("Arial", Font.BOLD, 12));
    resultCountLabel.setForeground(Color.DARK_GRAY);
    resultCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
    searchResultsPanel.add(resultCountLabel); // ✅ Add label at the top

    SwingWorker<Void, JPanel> worker = new SwingWorker<>() {
        private int userCount = 0;
        private final List<JPanel> resultPanels = new ArrayList<>();

        @Override
        protected Void doInBackground() {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
                String query = "SELECT user_id, username, profile_image, " +
                               "(SELECT COUNT(*) FROM Friends WHERE Friends.user_id = Users.user_id OR Friends.friend_id = Users.user_id) AS friend_count " +
                               "FROM Users WHERE username LIKE ? AND user_id <> ?";
                
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, "%" + searchText + "%");
                stmt.setInt(2, getUserId(conn, currentUser)); // ✅ Exclude current user
                
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    userCount++;
                    JPanel userPanel = createSearchResultPanel(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getBytes("profile_image"),
                        rs.getInt("friend_count")
                    );
                    resultPanels.add(userPanel);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void done() {
            searchResultsPanel.removeAll(); // ✅ Clear results
            if (userCount > 0) {
                resultCountLabel.setText("Users Found: " + userCount);
                searchResultsPanel.add(resultCountLabel);
                searchResultsPanel.add(Box.createVerticalStrut(10)); // ✅ Space between label and results
                for (JPanel userPanel : resultPanels) {
                    searchResultsPanel.add(userPanel);
                    searchResultsPanel.add(Box.createVerticalStrut(5)); // ✅ Space between users
                }
            } else {
                resultCountLabel.setText("No user found.");
                searchResultsPanel.add(resultCountLabel); // ✅ Show only "No user found" if no results
            }

            searchResultsPanel.revalidate();
            searchResultsPanel.repaint();
        }
    };
    worker.execute();
}




private JPanel createSearchResultPanel(int userId, String username, byte[] profileImageBytes, int friendCount) {
    // **Main User Panel (Increased Width)**
    JPanel userPanel = new JPanel(new BorderLayout());
    userPanel.setPreferredSize(new Dimension(600, 55)); // ✅ Wider Panel
    userPanel.setMaximumSize(new Dimension(600, 55)); 
    userPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), 
        BorderFactory.createEmptyBorder(5, 5, 5, 5)
    )); 
    userPanel.setBackground(Color.WHITE);
    
userPanel.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        displayUserDetails(userId);
    }
});



    // **Profile Image**
    JLabel profileLabel = new JLabel();
    profileLabel.setPreferredSize(new Dimension(50, 50)); // ✅ Bigger Profile Picture
    profileLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5)); 

    if (profileImageBytes != null) {
        ImageIcon profileIcon = new ImageIcon(profileImageBytes);
        Image img = profileIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        profileLabel.setIcon(new ImageIcon(img));
    } else {
        profileLabel.setIcon(defaultProfileIcon);
    }

    // **User Info Panel (Username & Friend Count)**
    JPanel userInfoPanel = new JPanel(new GridLayout(2, 1));
    userInfoPanel.setBackground(Color.WHITE);
    userInfoPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0)); 

    JLabel usernameLabel = new JLabel(username);
    usernameLabel.setForeground(new Color(0, 0, 139)); // **Dark Blue**
    usernameLabel.setFont(new Font("Arial", Font.BOLD, 14)); // ✅ Bigger Font

    JLabel friendCountLabel = new JLabel(friendCount + " friends");
    friendCountLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // ✅ Increased Font
    friendCountLabel.setForeground(Color.GRAY);

    userInfoPanel.add(usernameLabel);
    userInfoPanel.add(friendCountLabel);

    // **Add Friend Button**
    JButton addFriendButton = new JButton("➕ Add");
    addFriendButton.setBackground(new Color(30, 144, 255)); 
    addFriendButton.setForeground(Color.WHITE);
    addFriendButton.setFont(new Font("Apple Color Emoj", Font.BOLD, 12)); // ✅ Bigger Button Font
    addFriendButton.setPreferredSize(new Dimension(80, 30)); // ✅ Wider Button
    addFriendButton.setFocusPainted(false);
    addFriendButton.addActionListener(e -> sendFriendRequest(username));

    // **Hover effect for button**
    addFriendButton.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            addFriendButton.setBackground(new Color(0, 102, 204)); 
        }
        public void mouseExited(java.awt.event.MouseEvent evt) {
            addFriendButton.setBackground(new Color(30, 144, 255)); 
        }
    });

    // **Final Layout**
    userPanel.add(profileLabel, BorderLayout.WEST);
    userPanel.add(userInfoPanel, BorderLayout.CENTER);
    userPanel.add(addFriendButton, BorderLayout.EAST);

    return userPanel;
}



private void fetchSuggestedFriends(JPanel suggestedFriendsPanel) {
    SwingWorker<Void, JPanel> worker = new SwingWorker<>() {
        @Override
        protected Void doInBackground() {
            suggestedFriendsPanel.removeAll(); // Clear previous results
            JPanel container = new JPanel();
            container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
            container.setBackground(Color.WHITE);

            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
                int userId = getUserId(conn, currentUser);

                // ✅ Exclude already added friends and pending requests
                String query = "SELECT user_id, username, profile_image FROM Users " +
                               "WHERE user_id NOT IN (" +
                               "   SELECT friend_id FROM Friends WHERE user_id = ? " +
                               "   UNION " +
                               "   SELECT user_id FROM Friends WHERE friend_id = ?" +
                               ") AND user_id <> ? ORDER BY RANDOM() LIMIT 5";

                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, userId);
                stmt.setInt(2, userId);
                stmt.setInt(3, userId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    JPanel userPanel = createUserPanel(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getBytes("profile_image")
                    );
                    container.add(userPanel);
                    container.add(Box.createVerticalStrut(5)); // **Spacing between items**
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            JScrollPane scrollPane = new JScrollPane(container);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());

            suggestedFriendsPanel.add(scrollPane);
            return null;
        }

        @Override
        protected void process(List<JPanel> chunks) {
            suggestedFriendsPanel.revalidate();
            suggestedFriendsPanel.repaint();
        }
    };
    worker.execute();
}


private JPanel createUserPanel(int userId, String username, byte[] profileImageBytes) {
    // **Main User Panel (Border Layout)**
    JPanel userPanel = new JPanel(new BorderLayout());
    userPanel.setPreferredSize(new Dimension(230, 60)); // **Fixed size**
    userPanel.setMaximumSize(new Dimension(230, 60)); // **Prevents shrinking**
    userPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), 
        BorderFactory.createEmptyBorder(5, 5, 5, 5)
    )); // **Padding inside**
    userPanel.setBackground(Color.WHITE);
    
userPanel.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        displayUserDetails(userId);
    }
});



    // **Profile Image**
    JLabel profileLabel = new JLabel();
    profileLabel.setPreferredSize(new Dimension(45, 45)); // **Fixed size**
    profileLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10)); 

    if (profileImageBytes != null) {
        ImageIcon profileIcon = new ImageIcon(profileImageBytes);
        Image img = profileIcon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        profileLabel.setIcon(new ImageIcon(img));
    } else {
        profileLabel.setIcon(defaultProfileIcon);
    }

    // **User Info Panel (Username + Button)**
    JPanel userInfoPanel = new JPanel(new GridLayout(2, 1)); // **Two rows**
    userInfoPanel.setBackground(Color.WHITE);
    userInfoPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5)); 

    JLabel usernameLabel = new JLabel(username);
    usernameLabel.setForeground(new Color(0, 0, 139)); // **Dark Blue**
    usernameLabel.setFont(new Font("Arial", Font.BOLD, 12));

    // **Add Friend Button (Fixed Size)**
    JButton addFriendButton = new JButton("➕ Add");
    addFriendButton.setBackground(new Color(30, 144, 255)); // **Blue**
    addFriendButton.setForeground(Color.WHITE);
    addFriendButton.setFont(new Font("Apple Color Emoj", Font.BOLD, 10));
    addFriendButton.setPreferredSize(new Dimension(60, 25)); // **Fixed button size**
    addFriendButton.setFocusPainted(false);
    addFriendButton.addActionListener(e -> sendFriendRequest(username));

    // **Hover effect for button**
    addFriendButton.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            addFriendButton.setBackground(new Color(0, 102, 204)); // **Darker Blue on Hover**
        }
        public void mouseExited(java.awt.event.MouseEvent evt) {
            addFriendButton.setBackground(new Color(30, 144, 255)); // **Normal Blue**
        }
    });

    userInfoPanel.add(usernameLabel);
    userInfoPanel.add(addFriendButton);

    // **Final Layout**
    userPanel.add(profileLabel, BorderLayout.WEST);
    userPanel.add(userInfoPanel, BorderLayout.CENTER);

    return userPanel;
}



private void sendFriendRequest(String friendUsername) {
    if (friendUsername.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Enter a username.");
        return;
    }

    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
        int userId = getUserId(conn, username);
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
            notifyStmt.setString(2, username + " sent you a friend request.");
            notifyStmt.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Database error!");
    }
}


private int getUserId(Connection conn, String username) throws SQLException {
    String sql = "SELECT user_id FROM Users WHERE username=?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("user_id");
        }
    }
    return -1; // Return -1 if user is not found
}


private JPanel createProfilePanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    JLabel profileLabel = new JLabel("Profile Page", SwingConstants.CENTER);
    profileLabel.setFont(new Font("Arial", Font.BOLD, 20));
    panel.add(profileLabel, BorderLayout.NORTH);

    // Load Profile Page inside the panel
    ProfilePage profilePage = new ProfilePage(userId);
    panel.add(profilePage, BorderLayout.CENTER);

    return panel;
}

private JPanel createNotificationsPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    JLabel notificationsLabel = new JLabel("Notifications", SwingConstants.CENTER);
    notificationsLabel.setFont(new Font("Arial", Font.BOLD, 20));
    panel.add(notificationsLabel, BorderLayout.NORTH);

    // Load Notifications inside the panel
    NotificationsWindow notificationsWindow = new NotificationsWindow(username);
    panel.add(notificationsWindow, BorderLayout.CENTER);

    return panel;
}


    // Logout
    private void logout() {
        updateLastSeenTime(userId);
        updateUserStatusOnLogout(username);
        JOptionPane.showMessageDialog(this, "Logged out successfully.");
        new LoginPage().showLoginScreen();
        dispose();
    }
    
    private void updateUserStatusOnLogout(String username) {
    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
        String query = "UPDATE Users SET online_status = 'Offline', last_seen = CURRENT_TIMESTAMP WHERE username = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    
    // Method to update the last_seen time for a user when they log out
private void updateLastSeenTime(int userId) {
    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
        String sql = "UPDATE Users SET last_seen = CURRENT_TIMESTAMP WHERE user_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, userId);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    
    // Method to retrieve the current user's ID based on the username
private int getCurrentUserId(String username) {
    int userId = -1;
    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db")) {
        String sql = "SELECT user_id FROM Users WHERE username = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            userId = rs.getInt("user_id");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return userId;
}

// Fetch userId from the database
private int fetchUserId(String username) {
    int id = -1; // Default to -1 if not found
    String sql = "SELECT user_id FROM Users WHERE username = ?";

    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db");
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            id = rs.getInt("user_id"); // Get user ID from database
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return id;
}

private JPanel createHomePanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));

    panel.add(welcomeLabel, BorderLayout.CENTER);
    return panel;
}
    
private ImageIcon getProfileIcon(int userId) {
    String sql = "SELECT profile_image FROM Users WHERE user_id = ?";

    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db");
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            byte[] imageBytes = rs.getBytes("profile_image");
            if (imageBytes != null) {
                ImageIcon icon = new ImageIcon(imageBytes);

                // ✅ Resize to 24x24 like other icons
                Image img = icon.getImage();
                Image newImg = img.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                return new ImageIcon(newImg);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // ✅ If no profile image, return a default profile icon
    return new ImageIcon("icons/default_profile.png");
}

public void switchToChatWindow(String username, int chatUserId, int loggedInUserId) {
    System.out.println("DEBUG: In Dashboard - Switching to chat with " + username + 
                       " (Chat User ID: " + chatUserId + "), Logged-in User ID: " + loggedInUserId);

    if (chatWindow != null) {
        contentPanel.remove(chatWindow);
    }

    chatWindow = new ChatWindow(username, userId, this);
    contentPanel.add(chatWindow, "chat");
    cardLayout.show(contentPanel, "chat");

    revalidate();
    repaint();
}

    public static void main(String[] args) {
        new Dashboard("TestUser").setVisible(true);
        SwingUtilities.invokeLater(() -> new Dashboard("TestUser"));
    }
    

}
