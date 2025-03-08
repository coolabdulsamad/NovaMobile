/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

class Friend {
    private String username;
    private String onlineStatus; // Online or Offline

    public Friend(String username, String onlineStatus) {
        this.username = username;
        this.onlineStatus = onlineStatus;
    }

    public String getUsername() {
        return username;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }
}

