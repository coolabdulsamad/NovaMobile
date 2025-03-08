/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

public class User {
    private int userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String dob;
    private String country;
    private String phoneNumber;
    private String bio;
    private byte[] profileImage; // Profile image stored as byte array
    private int friendsCount;
    
    
     // Constructor
    public User(int userId, String username, String bio, String dob, String firstName, String lastName, String email, 
                String country, String phoneNumber, byte[] profileImage) {
        this.userId = userId;
        this.username = username;
        this.bio = bio;
        this.dob = dob;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
    }

    // Getters and setters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getDob() { return dob; }
    public String getCountry() { return country; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getBio() { return bio; }
    public byte[] getProfileImage() { return profileImage; } // Ensure this method exists
    public int getFriendsCount() {
    return friendsCount;
}

    public void setUsername(String username) { this.username = username; }
    public void setBio(String bio) { this.bio = bio; }
    public void setDob(String dob) { this.dob = dob; }
    public void setCountry(String country) { this.country = country; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    // Setter for profile image if needed
    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }
    public void setFriendsCount(int friendsCount) {
    this.friendsCount = friendsCount;
}
    
 public User(int userId, String username) {
    this.userId = userId;
    this.username = username;
}
   
}
