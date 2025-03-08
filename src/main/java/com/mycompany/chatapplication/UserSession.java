/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

public class UserSession {
    private static String currentUsername;
    private static int currentUserId;

    public static void setCurrentUsername(int userId, String username) {
        currentUsername = username;
        currentUserId = userId;
    }
    
    public static int getCurrentUserId(){
        return currentUserId;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }
}
