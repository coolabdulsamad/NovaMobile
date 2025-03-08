/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

// Class to hold user data (username + profile image)

import javax.swing.ImageIcon;

class UserListItem {
    private String username;
    private ImageIcon profileImage;

    public UserListItem(String username, ImageIcon profileImage) {
        this.username = username;
        this.profileImage = profileImage;
    }

    public String getUsername() {
        return username;
    }

    public ImageIcon getProfileImage() {
        return profileImage;
    }

    @Override
    public String toString() {
        return username; // Ensure the list displays the username
    }
}

