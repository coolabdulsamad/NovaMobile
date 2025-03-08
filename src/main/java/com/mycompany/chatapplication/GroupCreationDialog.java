/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import Application_Connector.db.DatabaseHelper;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GroupCreationDialog extends JDialog {
    private JTextField groupNameField;
    private JPanel membersPanel;
    private JButton createGroupButton;
    private JButton uploadIconButton;
    private File groupIconFile;
    private List<Integer> selectedMemberIds;
    private int loggedInUserId;
    private JTextArea groupDescriptionArea; // ✅ New field for description
    private JLabel groupIconLabel; // ✅ Label to display the selected icon


    public GroupCreationDialog(JFrame parent, List<Friend> friends, int loggedInUserId) {
        super(parent, "Create Group", true);
        this.loggedInUserId = loggedInUserId;
        setSize(400, 500);
        setLayout(new BorderLayout());
        
        URL iconURL = MainTab.class.getClassLoader().getResource("icons/novamobile.jpg");
if (iconURL != null) {
    setIconImage(new ImageIcon(iconURL).getImage());
} else {
    System.err.println("Icon image not found.");
}

        selectedMemberIds = new ArrayList<>();

        groupNameField = new JTextField(20);
        uploadIconButton = new JButton("Upload Group Icon");
        createGroupButton = new JButton("Create Group");
        membersPanel = new JPanel();
        membersPanel.setLayout(new BoxLayout(membersPanel, BoxLayout.Y_AXIS));

        // Upload Icon Action
uploadIconButton.addActionListener(e -> {
    JFileChooser fileChooser = new JFileChooser();
    if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        groupIconFile = fileChooser.getSelectedFile();

        // ✅ Show preview in JLabel
        ImageIcon icon = new ImageIcon(groupIconFile.getAbsolutePath());
        Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        groupIconLabel.setIcon(new ImageIcon(image));
    }
});


        // Friends Checkboxes
        for (Friend friend : friends) {
            JCheckBox checkBox = new JCheckBox(friend.getUsername());
            checkBox.addActionListener(e -> {
                if (checkBox.isSelected()) {
                    selectedMemberIds.add(friend.getUserId());
                } else {
                    selectedMemberIds.remove(Integer.valueOf(friend.getUserId()));
                }
            });
            membersPanel.add(checkBox);
        }

        JPanel topPanel = new JPanel(new GridBagLayout());
GridBagConstraints gbc = new GridBagConstraints();
gbc.insets = new Insets(5, 5, 5, 5); // Add padding

gbc.gridx = 0;
gbc.gridy = 0;
gbc.anchor = GridBagConstraints.WEST;
topPanel.add(new JLabel("Group Name: "), gbc);

gbc.gridx = 1;
gbc.fill = GridBagConstraints.HORIZONTAL;
topPanel.add(groupNameField, gbc);

gbc.gridx = 0;
gbc.gridy = 2;
gbc.gridwidth = 2;
gbc.fill = GridBagConstraints.NONE;
gbc.anchor = GridBagConstraints.CENTER;
topPanel.add(uploadIconButton, gbc);

uploadIconButton.setBackground(new Color(100, 149, 237)); // Cornflower Blue
uploadIconButton.setForeground(Color.WHITE);
uploadIconButton.setFocusPainted(false);
uploadIconButton.setFont(new Font("Segoe UI", Font.BOLD, 12));

// ✅ Initialize icon preview
groupIconLabel = new JLabel();
groupIconLabel.setPreferredSize(new Dimension(100, 100)); // ✅ Set size
groupIconLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // ✅ Add a border
groupIconLabel.setHorizontalAlignment(JLabel.CENTER);

gbc.gridx = 0;
gbc.gridy = 1; // ✅ Place it above the upload button
gbc.gridwidth = 2;
gbc.insets = new Insets(5, 5, 5, 5);
topPanel.add(groupIconLabel, gbc);


JLabel descriptionLabel = new JLabel("Group Description:");
groupDescriptionArea = new JTextArea(3, 20); // 3 rows, 20 columns
groupDescriptionArea.setLineWrap(true);
groupDescriptionArea.setWrapStyleWord(true);
JScrollPane descriptionScroll = new JScrollPane(groupDescriptionArea);

// ✅ Add Group Description Field with Proper Spacing
gbc.gridx = 0;
gbc.gridy = 3;
gbc.anchor = GridBagConstraints.WEST;
gbc.insets = new Insets(10, 5, 5, 5); // ✅ Add spacing to prevent overlapping
topPanel.add(new JLabel("Description:"), gbc);

gbc.gridx = 1;
gbc.fill = GridBagConstraints.HORIZONTAL;
gbc.insets = new Insets(10, 5, 5, 10); // ✅ Add right padding
topPanel.add(descriptionScroll, gbc);




        JPanel bottomPanel = new JPanel();
        bottomPanel.add(createGroupButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(membersPanel), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Create Group Action
        createGroupButton.addActionListener(e -> createGroup());
    }

private void createGroup() {
    String groupName = groupNameField.getText().trim();
    String groupDescription = groupDescriptionArea.getText().trim(); // ✅ Get description

    if (groupName.isEmpty() || selectedMemberIds.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Group name and at least one member are required.");
        return;
    }

    try (Connection conn = DatabaseHelper.connect()) {
        conn.setAutoCommit(false);

        // ✅ Update SQL to insert the group description
        String insertGroupSQL = "INSERT INTO Groups (group_name, description, created_by, icon) VALUES (?, ?, ?, ?)";
        PreparedStatement groupStmt = conn.prepareStatement(insertGroupSQL, Statement.RETURN_GENERATED_KEYS);
        groupStmt.setString(1, groupName);
        groupStmt.setString(2, groupDescription); // ✅ Insert description
        groupStmt.setInt(3, loggedInUserId);

        // Convert file to byte array (if an icon is selected)
        if (groupIconFile != null) {
            try (FileInputStream fis = new FileInputStream(groupIconFile)) {
                groupStmt.setBinaryStream(4, fis, (int) groupIconFile.length());
            }
        } else {
            groupStmt.setNull(4, java.sql.Types.BLOB);
        }

        groupStmt.executeUpdate();

        // Retrieve the generated group ID
        ResultSet rs = groupStmt.getGeneratedKeys();
        int groupId = -1;
        if (rs.next()) {
            groupId = rs.getInt(1);
        }

        // ✅ Ensure the creator is added as a group member
        String insertCreatorSQL = "INSERT INTO GroupMembers (group_id, user_id) VALUES (?, ?)";
        PreparedStatement creatorStmt = conn.prepareStatement(insertCreatorSQL);
        creatorStmt.setInt(1, groupId);
        creatorStmt.setInt(2, loggedInUserId);
        creatorStmt.executeUpdate();

        // ✅ Prevent adding duplicate users to the same group
        String insertMemberSQL = "INSERT INTO GroupMembers (group_id, user_id) SELECT ?, ? WHERE NOT EXISTS "
                + "(SELECT 1 FROM GroupMembers WHERE group_id = ? AND user_id = ?)";
        PreparedStatement memberStmt = conn.prepareStatement(insertMemberSQL);

        for (int memberId : selectedMemberIds) {
            memberStmt.setInt(1, groupId);
            memberStmt.setInt(2, memberId);
            memberStmt.setInt(3, groupId);
            memberStmt.setInt(4, memberId);
            memberStmt.executeUpdate();
        }

        conn.commit();
        JOptionPane.showMessageDialog(this, "Group created successfully!");
        dispose();

    } catch (SQLException | IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Group creation failed!\nError: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}





    // Example Friend class representing a friend (should match your user data)
    public static class Friend {
        private int userId;
        private String username;

        public Friend(int userId, String username) {
            this.userId = userId;
            this.username = username;
        }

        public int getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }
    }
}

