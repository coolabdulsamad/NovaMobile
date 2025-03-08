/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import javax.swing.*;

import Application_Connector.db.DatabaseHelper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class GroupDetailsWindow extends JDialog {
    private int groupId;
    private JPanel membersPanel;
    private JTextField groupNameField;
    private JLabel groupIconLabel;
    private byte[] groupIconBytes;
    private int loggedInUserId; // ✅ Store the logged-in user ID
    private ChatWindow chatWindow; // Store parent reference
    private ChatWindow parentChatWindow;
    private boolean isAdmin; // ✅ Declare at class level
    private JTextArea groupDescriptionArea; // ✅ Field to display and edit description
    private JButton saveDescriptionButton; // ✅ Button to save changes
    private JLabel pinnedMessageLabel;
    private JButton unpinMessageButton;

    
public GroupDetailsWindow(ChatWindow parent, int groupId, String groupName, byte[] groupIconBytes, int loggedInUserId) {
    super((JFrame) SwingUtilities.getWindowAncestor(parent), "Group Details", true);
    this.groupId = groupId;
    this.groupIconBytes = groupIconBytes;
    this.loggedInUserId = loggedInUserId;
    this.chatWindow = parent; // ✅ Store ChatWindow reference
    this.parentChatWindow = parent; // ✅ Store ChatWindow reference
    this.isAdmin = isCurrentUserAdmin(); // ✅ Initialize isAdmin properly
    
    URL iconURL = MainTab.class.getClassLoader().getResource("icons/novamobile.jpg");
if (iconURL != null) {
    setIconImage(new ImageIcon(iconURL).getImage());
} else {
    System.err.println("Icon image not found.");
}
    
    setSize(400, 500);
    setLayout(new BorderLayout());
    
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // ✅ Ensures proper disposal
        setLocationRelativeTo(parent);

    // ✅ Ensure reference is cleared on close
// ✅ Ensure reference is cleared on close
addWindowListener(new java.awt.event.WindowAdapter() {
    @Override
    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
        if (parent instanceof ChatWindow chatWindow) {
            chatWindow.groupDetailsWindow = null;
        }
    }
});


    boolean isAdmin = isCurrentUserAdmin();
    boolean isOwner = (loggedInUserId == getGroupOwnerId(groupId)); // ✅ Fix: Determine ownership

    // ✅ Header: Group Name & Icon
    JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    groupIconLabel = new JLabel();
    setGroupIcon(groupIconBytes);
    
    groupNameField = new JTextField(groupName, 20);
    JButton editGroupButton = new JButton("Save Changes");

    editGroupButton.addActionListener(e -> {
        String newGroupName = groupNameField.getText().trim();
        if (!newGroupName.isEmpty()) {
            updateGroupName(newGroupName);
        } else {
            JOptionPane.showMessageDialog(this, "Group name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    headerPanel.add(groupIconLabel);
    headerPanel.add(groupNameField);
    headerPanel.add(editGroupButton);
    

// ✅ Create a wrapper panel for description & members list
JPanel centerPanel = new JPanel(new BorderLayout());

// ✅ Pinned Message Panel
JPanel pinnedPanel = new JPanel(new BorderLayout());
pinnedPanel.setBorder(BorderFactory.createTitledBorder("📌 Pinned Message"));

pinnedMessageLabel = new JLabel("No pinned message");
pinnedMessageLabel.setFont(new Font("Arial", Font.BOLD, 14));
pinnedPanel.add(pinnedMessageLabel, BorderLayout.CENTER);

// ✅ Unpin Button (Only for Admins)
if (isAdmin) {
    unpinMessageButton = new JButton("Unpin");
    unpinMessageButton.addActionListener(e -> unpinMessage());
    pinnedPanel.add(unpinMessageButton, BorderLayout.EAST);
}

// ✅ Load pinned message from DB
loadPinnedMessage();

// ✅ Add it to the window
centerPanel.add(pinnedPanel, BorderLayout.NORTH);


// ✅ Group Description Panel
groupDescriptionArea = new JTextArea(4, 30);
groupDescriptionArea.setLineWrap(true);
groupDescriptionArea.setWrapStyleWord(true);
groupDescriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
groupDescriptionArea.setEditable(isAdmin); // Only admins can edit
JScrollPane descriptionScrollPane = new JScrollPane(groupDescriptionArea);
loadGroupDescription();

// Save button (only for admins)
if (isAdmin) {
    saveDescriptionButton = new JButton("Save Description");
    saveDescriptionButton.addActionListener(e -> updateGroupDescription());
}

// ✅ Panel for Description
JPanel descriptionPanel = new JPanel(new BorderLayout());
descriptionPanel.setBorder(BorderFactory.createTitledBorder("Group Description"));
descriptionPanel.add(descriptionScrollPane, BorderLayout.CENTER);

// If admin, add save button below the text area
if (isAdmin) {
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(saveDescriptionButton);
    descriptionPanel.add(buttonPanel, BorderLayout.SOUTH);
}

// ✅ Add description panel to NORTH
centerPanel.add(descriptionPanel, BorderLayout.NORTH);

// ✅ Members List Panel
membersPanel = new JPanel();
membersPanel.setLayout(new BoxLayout(membersPanel, BoxLayout.Y_AXIS));
JScrollPane membersScrollPane = new JScrollPane(membersPanel);
loadGroupMembers();

// ✅ Add members list to CENTER
centerPanel.add(membersScrollPane, BorderLayout.CENTER);

// ✅ Add centerPanel to the main layout
add(centerPanel, BorderLayout.CENTER);



    // ✅ Action Buttons
    JButton addMemberButton = new JButton("Add Member");
    addMemberButton.addActionListener(e -> addMemberToGroup());
    


// ✅ Action Buttons Panel (2 per row)
JPanel actionsPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // Rows auto-adjust, 2 buttons per row
actionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding for better UI


// Add buttons with spacing
actionsPanel.add(addMemberButton);

    if (isAdmin){
    JButton deleteAllMessagesButton = new JButton("Delete All Messages");

    
    deleteAllMessagesButton.addActionListener(e -> deleteAllGroupMessages());
    
    actionsPanel.add(deleteAllMessagesButton);
    }

if (!isAdmin) { // ✅ Show "Exit Group" button only for non-admins
    JButton exitGroupButton = new JButton("Exit Group");
    exitGroupButton.setForeground(Color.RED);
    
    exitGroupButton.addActionListener(e -> exitGroup());

    actionsPanel.add(exitGroupButton);
//    actionsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // ✅ Space between buttons
    
    setSize(500, 500);
}


if (isAdmin) {
    JButton generateInviteButton = new JButton("Generate Invite Link");
    generateInviteButton.addActionListener(e -> generateInviteLink());
    actionsPanel.add(generateInviteButton);
    setSize(700, 600);
}

    if (isAdmin) {
        JButton copyButton = new JButton("Copy Invite Link");
        copyButton.addActionListener(e -> copyInviteLinkToClipboard());
        actionsPanel.add(copyButton);
    }
    
    // Add Delete Group button only for owner
if (isOwner) {
    JButton deleteGroupButton = new JButton("Delete Group");
    deleteGroupButton.setForeground(Color.RED);
    deleteGroupButton.setFont(new Font("Arial", Font.BOLD, 12));
    deleteGroupButton.addActionListener(e -> deleteGroup());
    
    actionsPanel.add(deleteGroupButton);
    actionsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // ✅ Add padding for delete button
}

    // ✅ Fill empty spaces to maintain 2-column layout
int totalButtons = actionsPanel.getComponentCount();
if (totalButtons % 2 != 0) {
    actionsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add empty space for alignment
}
    
    add(headerPanel, BorderLayout.NORTH);
//    add(membersScrollPane, BorderLayout.CENTER);
    add(actionsPanel, BorderLayout.SOUTH);

    setLocationRelativeTo(parent);

    // ✅ Restrict Editing for Non-Admins
    groupNameField.setEditable(isAdmin);
    editGroupButton.setEnabled(isAdmin);
    groupIconLabel.setEnabled(isAdmin);
    
    // ✅ Allow only admins to change the group icon
if (isAdmin) {
    groupIconLabel.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            changeGroupIcon();
        }
    });
} else {
    groupIconLabel.setEnabled(false); // Gray out the icon for non-admins
}

    
     setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Automatically dispose when the window is closed
     
    setVisible(true);
}



    private void setGroupIcon(byte[] iconBytes) {
    if (iconBytes != null) {
        ImageIcon icon = new ImageIcon(iconBytes);
        Image image = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        groupIconLabel.setIcon(new ImageIcon(image));
    } else {
        groupIconLabel.setText("🧑‍🤝‍🧑");
        groupIconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
    }

//    // ✅ Add Click Event to Change Icon
//    groupIconLabel.addMouseListener(new MouseAdapter() {
//        @Override
//        public void mouseClicked(MouseEvent e) {
//            changeGroupIcon();
//        }
//    });
}

    private void changeGroupIcon() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "png", "jpeg"));

    int result = fileChooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();

        // ✅ Convert the file to a byte array
        try (FileInputStream fis = new FileInputStream(selectedFile)) {
            byte[] imageBytes = fis.readAllBytes();

            // ✅ Save to Database
            updateGroupIcon(imageBytes);

            // ✅ Update UI
            setGroupIcon(imageBytes);
            //JOptionPane.showMessageDialog(this, "Group icon updated successfully!");
            
            showNotification("✅ Group icon updated!");

        } catch (IOException e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(this, "Failed to update group icon!", "Error", JOptionPane.ERROR_MESSAGE);
            showNotification("❌ Failed to update group icon!");
        }
    }
}

    private void updateGroupIcon(byte[] imageBytes) {
    String sql = "UPDATE Groups SET icon = ? WHERE group_id = ?";

    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setBytes(1, imageBytes);
        pstmt.setInt(2, groupId);
        pstmt.executeUpdate();
        
        showNotification("✅ Group icon updated!");
    } catch (SQLException e) {
        e.printStackTrace();
        showNotification("❌ Failed to update group icon!");
    }
}


    // ✅ Load Group Members
private void loadGroupMembers() {
    membersPanel.removeAll();

    int ownerId = getGroupOwnerId(groupId); // Get the group owner
    boolean isAdmin = isCurrentUserAdmin();
    boolean isOwner = (loggedInUserId == ownerId); // Check if the current user is the owner

    String sql = """
        SELECT u.user_id, u.username,
               CASE WHEN a.user_id IS NOT NULL OR g.created_by = u.user_id THEN 'Admin' ELSE 'Member' END AS role
        FROM GroupMembers gm
        JOIN Users u ON gm.user_id = u.user_id
        LEFT JOIN GroupAdmins a ON gm.group_id = a.group_id AND gm.user_id = a.user_id
        JOIN Groups g ON gm.group_id = g.group_id
        WHERE gm.group_id = ?
    """;

    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, groupId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            int memberId = rs.getInt("user_id");
            String memberName = rs.getString("username");
            String role = rs.getString("role");

            // ✅ Use GridBagLayout or FlowLayout for proper alignment
            JPanel memberPanel = new JPanel();
            memberPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10)); // Add horizontal and vertical spacing

            JLabel nameLabel = new JLabel(memberName + " (" + role + ")");
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Consistent font size

            JButton removeButton = new JButton("Remove");
            // ✅ Only admins can remove members
            if (isAdmin) {
                removeButton.addActionListener(e -> removeMemberFromGroup(memberId));
            } else {
                removeButton.setEnabled(false);
            }
            
            JButton promoteButton = new JButton("Make Admin");
            JButton demoteButton = new JButton("Remove Admin");

            // ✅ Remove button logic for Admin
            if (role.equals("Admin")) {
                removeButton.setEnabled(false);
                removeButton.setText("Admin");

                // ✅ Only the owner can remove admins
                if (isOwner && memberId != ownerId) {
                    demoteButton.addActionListener(e -> removeAdmin(memberId));
                    memberPanel.add(demoteButton);
                }
            } else {
                removeButton.addActionListener(e -> removeMemberFromGroup(memberId));

                if (isAdmin) {
                    promoteButton.addActionListener(e -> promoteToAdmin(memberId));
                    memberPanel.add(promoteButton);
                }
            }

            // ✅ Organize components properly with spacing
            memberPanel.add(nameLabel);
            memberPanel.add(removeButton);

            // Add to the members list
            membersPanel.add(memberPanel);
        }

        membersPanel.revalidate();
        membersPanel.repaint();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}




private void removeAdmin(int memberId) {
    String sql = "DELETE FROM GroupAdmins WHERE group_id = ? AND user_id = ?";

    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, groupId);
        pstmt.setInt(2, memberId);
        pstmt.executeUpdate();

        //JOptionPane.showMessageDialog(this, "User removed as admin!");
        showNotification("✅ User removed as admin!");
        
        loadGroupMembers(); // ✅ Refresh UI
    } catch (SQLException e) {
        e.printStackTrace();
        //JOptionPane.showMessageDialog(this, "Failed to remove admin!", "Error", JOptionPane.ERROR_MESSAGE);
        showNotification("❌ Failed to remove admin!");
    }
}

private int getGroupOwnerId(int groupId) {
    String sql = "SELECT created_by FROM Groups WHERE group_id = ?";

    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, groupId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            int ownerId = rs.getInt("created_by");
            System.out.println("Group Owner ID (from DB): " + ownerId); // ✅ Debugging
            return ownerId;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return -1; // Default value if not found
}




private void promoteToAdmin(int memberId) {
    String sql = "INSERT INTO GroupAdmins (group_id, user_id) VALUES (?, ?)";

    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, groupId);
        pstmt.setInt(2, memberId);
        pstmt.executeUpdate();

        JOptionPane.showMessageDialog(this, "User promoted to Admin!");
        loadGroupMembers(); // Refresh UI
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to promote user!", "Error", JOptionPane.ERROR_MESSAGE);
    }
}



 private int getGroupAdminId(int groupId) {
    String sql = "SELECT created_by FROM Groups WHERE group_id = ?";
    
    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, groupId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("created_by"); // ✅ Return the admin's user ID
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return -1; // Default (should not happen)
}


    // ✅ Remove Member
    private void removeMemberFromGroup(int memberId) {
        String sql = "DELETE FROM GroupMembers WHERE group_id = ? AND user_id = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, groupId);
            pstmt.setInt(2, memberId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Member removed!");
            loadGroupMembers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Add Member
private void addMemberToGroup() {
    String username = JOptionPane.showInputDialog(this, "Enter Username to Add:");
    if (username == null || username.trim().isEmpty()) {
        return;
    }

    int newMemberId = getUserIdByUsername(username);
    if (newMemberId == -1) {
        JOptionPane.showMessageDialog(this, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String sql = "INSERT INTO GroupMembers (group_id, user_id) VALUES (?, ?)";

    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, groupId);
        pstmt.setInt(2, newMemberId);
        pstmt.executeUpdate();

        JOptionPane.showMessageDialog(this, "Member added successfully!");
        loadGroupMembers();
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to add member!", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private int getUserIdByUsername(String username) {
    String sql = "SELECT user_id FROM Users WHERE username = ?";
    
    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("user_id");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return -1; // User not found
}


    // ✅ Delete All Messages
   private void deleteAllGroupMessages() {
       boolean isAdmin = isCurrentUserAdmin();

    if (!isAdmin) {
        JOptionPane.showMessageDialog(this, "Only admins can delete messages!", "Access Denied", JOptionPane.ERROR_MESSAGE);
        return;
    }
       
    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Delete All Messages", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) return;

    String sql = "DELETE FROM GroupMessages WHERE group_id = ?";

    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, groupId);
        pstmt.executeUpdate();

        JOptionPane.showMessageDialog(this, "All messages deleted!");

        // ✅ Auto-reload chat messages after deletion
        if (getParent() instanceof ChatWindow chatWindow) {
            chatWindow.clearChatMessages();
            chatWindow.loadGroupMessages(groupId);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    
    private void updateGroupName(String newGroupName) {
    String sql = "UPDATE Groups SET group_name = ? WHERE group_id = ?";

    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, newGroupName);
        pstmt.setInt(2, groupId);
        pstmt.executeUpdate();

        //JOptionPane.showMessageDialog(this, "Group name updated successfully!");
        showNotification("✅ Group name updated!");

    } catch (SQLException e) {
        e.printStackTrace();
        //JOptionPane.showMessageDialog(this, "Failed to update group name!", "Error", JOptionPane.ERROR_MESSAGE);
        showNotification("❌ Failed to update group name!");
    }
}
    
private boolean isCurrentUserAdmin() {
    String sql = """
        SELECT 1 FROM GroupAdmins WHERE group_id = ? AND user_id = ?
        UNION
        SELECT 1 FROM Groups WHERE group_id = ? AND created_by = ?
    """;

    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, groupId);
        pstmt.setInt(2, loggedInUserId);
        pstmt.setInt(3, groupId);
        pstmt.setInt(4, loggedInUserId);
        ResultSet rs = pstmt.executeQuery();
        return rs.next(); // ✅ If found in either table, user is admin
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

private void deleteGroup() {
    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this group?", "Delete Group", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) return;

    String sql = "DELETE FROM Groups WHERE group_id = ?";

    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, groupId);
        pstmt.executeUpdate();

        JOptionPane.showMessageDialog(this, "Group deleted successfully!");
        dispose(); // ✅ Close the GroupDetailsWindow
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to delete group!", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void exitGroup() {
    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to leave this group?", "Exit Group", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) return;

    String sql = "DELETE FROM GroupMembers WHERE group_id = ? AND user_id = ?";

    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, groupId);
        pstmt.setInt(2, loggedInUserId);
        pstmt.executeUpdate();

        JOptionPane.showMessageDialog(this, "You have left the group.");
        dispose(); // ✅ Close the window after exiting

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to leave the group!", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void generateInviteLink() {
    String inviteCode = generateRandomInviteCode();
    String inviteLink = "https://yourapp.com/join/" + inviteCode;

    String sql = "INSERT INTO GroupInvites (group_id, invite_code, expires_at) VALUES (?, ?, DATETIME('now', '+1 hour'))";

    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, groupId);
        pstmt.setString(2, inviteCode);
        pstmt.executeUpdate();

        // ✅ Show Custom Popup with "Copy" and "Share" Buttons
        showInvitePopup(inviteLink);
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to generate invite link.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void showInvitePopup(String inviteLink) {
    JDialog dialog = new JDialog(this, "Invite Link", true);
    dialog.setSize(350, 180);
    dialog.setLayout(new BorderLayout());
    dialog.setLocationRelativeTo(this);

    JTextField linkField = new JTextField(inviteLink);
    linkField.setEditable(false);
    linkField.setFont(new Font("Arial", Font.PLAIN, 14));
    linkField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JButton copyButton = new JButton("Copy");
    copyButton.addActionListener(e -> {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(inviteLink), null);
        JOptionPane.showMessageDialog(dialog, "Invite link copied!");
    });

    // ✅ Fix "Share via Group" button
    JButton shareButton = new JButton("Share via Group");
    shareButton.addActionListener(e -> {
    if (parentChatWindow != null) {
        System.out.println("✅ parentChatWindow is NOT null! Sending message...");
        String message = "Join our group: " + inviteLink;
        parentChatWindow.sendGroupMessage(groupId, message);
        JOptionPane.showMessageDialog(dialog, "Invite link shared in the group!");
    } else {
        System.out.println("❌ ERROR: parentChatWindow is NULL!");
        JOptionPane.showMessageDialog(dialog, "Failed to share invite!", "Error", JOptionPane.ERROR_MESSAGE);
    }
});


    JPanel buttonPanel = new JPanel(new FlowLayout());
    buttonPanel.add(copyButton);
    buttonPanel.add(shareButton);

    dialog.add(linkField, BorderLayout.CENTER);
    dialog.add(buttonPanel, BorderLayout.SOUTH);
    dialog.setVisible(true);
}


// Generate random invite code
private String generateRandomInviteCode() {
    return UUID.randomUUID().toString().substring(0, 8); // Short random code
}

private void joinGroupWithInvite(String inviteCode) {
    String sql = "SELECT * FROM GroupInvites WHERE invite_code = ? AND expires_at > NOW() AND is_used = FALSE";
    
    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, inviteCode);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            // Invite is valid, add user to group
            String insertSql = "INSERT INTO GroupMembers (group_id, user_id) VALUES (?, ?)";
            try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                insertPstmt.setInt(1, groupId);
                insertPstmt.setInt(2, loggedInUserId); // Current user
                insertPstmt.executeUpdate();

                // Mark invite as used
                String updateSql = "UPDATE GroupInvites SET is_used = TRUE WHERE invite_code = ?";
                try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                    updatePstmt.setString(1, inviteCode);
                    updatePstmt.executeUpdate();
                }

                JOptionPane.showMessageDialog(this, "Successfully joined the group!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid or expired invite link.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to join group.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void sendInviteLinkAsMessage() {
    String inviteCode = generateRandomInviteCode();
    Timestamp expirationTime = new Timestamp(System.currentTimeMillis() + 3600000); // 1 hour expiration

    String sql = "INSERT INTO GroupInvites (group_id, invite_code, expires_at) VALUES (?, ?, ?)";

    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, groupId);
        pstmt.setString(2, inviteCode);
        pstmt.setTimestamp(3, expirationTime);
        pstmt.executeUpdate();

        // ✅ No need for casting, use parentChatWindow
        String inviteMessage = "Join the group using this invite link: https://yourapp.com/join/" + inviteCode;
        parentChatWindow.sendGroupMessage(groupId, inviteMessage); // ✅ Fixed!

        JOptionPane.showMessageDialog(this, "Invite link sent to the group.");
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to send invite link.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}




private void addCopyInviteButton() {

}

private void copyInviteLinkToClipboard() {
    String inviteCode = getLatestInviteCodeFromDB(); // ✅ Fetch the stored invite code
    if (inviteCode == null) {
        JOptionPane.showMessageDialog(this, "No invite link available!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String inviteLink = "https://yourapp.com/join/" + inviteCode;

    try {
        // ✅ Copy the invite link to clipboard
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(inviteLink), null);
        JOptionPane.showMessageDialog(this, "Invite link copied to clipboard!");
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to copy invite link!", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private String getLatestInviteCodeFromDB() {
    String sql = "SELECT invite_code FROM GroupInvites WHERE group_id = ? ORDER BY expires_at DESC LIMIT 1";

    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, groupId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            return rs.getString("invite_code"); // ✅ Return the most recent invite code
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null; // No invite found
}

private void loadGroupDescription() {
    String sql = "SELECT description FROM Groups WHERE group_id = ?";

    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, groupId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            groupDescriptionArea.setText(rs.getString("description"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

 private void updateGroupDescription() {
    String newDescription = groupDescriptionArea.getText().trim();
    String sql = "UPDATE Groups SET description = ? WHERE group_id = ?";

    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, newDescription);
        pstmt.setInt(2, groupId);
        pstmt.executeUpdate();

         showNotification("✅ Group description updated!");
    } catch (SQLException e) {
        e.printStackTrace();
        //JOptionPane.showMessageDialog(this, "Failed to update description!", "Error", JOptionPane.ERROR_MESSAGE);
        showNotification("❌ Failed to update group name!");
    }
}

 private void showNotification(String message) {
    JDialog popup = new JDialog(this, false);
    popup.setUndecorated(true);
    popup.setSize(300, 50);
    popup.setLayout(new BorderLayout());

    JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
    messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
    messageLabel.setForeground(Color.WHITE);
    popup.add(messageLabel, BorderLayout.CENTER);

    popup.getContentPane().setBackground(new Color(50, 50, 50));
    popup.setLocationRelativeTo(this);

    // Show notification at bottom-center of window
    int x = getLocation().x + getWidth() / 2 - popup.getWidth() / 2;
    int y = getLocation().y + getHeight() - 80;
    popup.setLocation(x, y);

    popup.setVisible(true);

    // Auto-close after 2 seconds
    new Timer(2000, e -> popup.dispose()).start();
}

private void loadPinnedMessage() {
    String sql = "SELECT pinned_message FROM Groups WHERE group_id = ?";

    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, groupId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String pinnedMessage = rs.getString("pinned_message");
            pinnedMessageLabel.setText((pinnedMessage != null && !pinnedMessage.isEmpty()) ? "📌 " + pinnedMessage : "No pinned message");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


 private void unpinMessage() {
    String sql = "UPDATE Groups SET pinned_message = NULL WHERE group_id = ?";

    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, groupId);
        pstmt.executeUpdate();

        pinnedMessageLabel.setText("No pinned message");
        JOptionPane.showMessageDialog(this, "✅ Pinned message removed!");
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "❌ Failed to unpin message!", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

public void pinMessageInGroup(int messageId, String messageText) {
    String sql = "UPDATE Groups SET pinned_message = ? WHERE group_id = ?";

    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, messageText);
        pstmt.setInt(2, groupId);
        pstmt.executeUpdate();

        JOptionPane.showMessageDialog(this, "✅ Message pinned in group!");
        loadPinnedMessage(); // Refresh UI
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "❌ Failed to pin message!", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

public void unpinMessageInGroup() {
    String sql = "UPDATE Groups SET pinned_message = NULL WHERE group_id = ?";

    try (Connection conn = DatabaseHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, groupId);
        pstmt.executeUpdate();

        JOptionPane.showMessageDialog(this, "✅ Pinned message removed!");
        loadPinnedMessage(); // Refresh UI
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "❌ Failed to unpin message!", "Error", JOptionPane.ERROR_MESSAGE);
    }
}


}

