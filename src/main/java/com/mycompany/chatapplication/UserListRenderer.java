/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

// Custom ListCellRenderer for displaying both username and profile image

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

class UserListRenderer extends DefaultListCellRenderer {
    @Override
public Component getListCellRendererComponent(
    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    
    if (value instanceof UserListItem) {
        UserListItem item = (UserListItem) value;
        setText(" " + item.getUsername()); // Add padding
        setIcon(item.getProfileImage()); // Show profile picture
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add spacing
    }
    
    return this;
}

}

