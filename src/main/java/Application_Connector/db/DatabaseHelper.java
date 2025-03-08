/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application_Connector.db;

import com.mycompany.chatapplication.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
 import java.sql.*;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:chatting_app.db";

    // *Connect to SQLite*
    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to check if username or email already exists
    public boolean doesUserExist(String username, String email) {
        String checkUserSQL = "SELECT * FROM Users WHERE username = ? OR email = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(checkUserSQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);

            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if a user is found
        } catch (Exception e) {
            e.printStackTrace();
            return true; // Assume user exists if there's an error
        }
    }

    // Method for user signup
    public boolean signup(String username, String email, String password, String firstName, String lastName, String dob, String country, String phoneNumber) {
        String insertUserSQL = "INSERT INTO Users (username, email, password, first_name, last_name, dob, country, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(insertUserSQL)) {
            String hashedPassword = PasswordUtils.hashPassword(password);
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, hashedPassword);
            pstmt.setString(4, firstName);
            pstmt.setString(5, lastName);
            pstmt.setString(6, dob); // Store the date of birth as a String
            pstmt.setString(7, country);
            pstmt.setString(8, phoneNumber);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Updated validateLogin method to handle both username and email
public String validateLogin(String usernameOrEmail, String password) {
    String validateSQL = "SELECT user_id, password FROM Users WHERE username = ? OR email = ?";
    try (Connection conn = DriverManager.getConnection(URL);
         PreparedStatement pstmt = conn.prepareStatement(validateSQL)) {
        
        pstmt.setString(1, usernameOrEmail);  
        pstmt.setString(2, usernameOrEmail);  

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            String storedHashedPassword = rs.getString("password");
            if (PasswordUtils.checkPassword(password, storedHashedPassword)) {
                String userId = String.valueOf(rs.getInt("user_id"));
                System.out.println("User ID retrieved: " + userId); // Debugging line
                return userId;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    System.out.println("Login failed: No matching user or incorrect password."); // Debugging line
    return null;  
}


    // New method to check if a Google user exists by email
    public boolean doesGoogleUserExist(String email) {
        String checkGoogleUserSQL = "SELECT * FROM Users WHERE email = ? AND password IS NULL"; // Google users have no password
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(checkGoogleUserSQL)) {
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if a user is found
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // New method for Google user signup
    public boolean signupGoogleUser(String email, String firstName, String lastName) {
    String insertGoogleUserSQL = """
        INSERT INTO Users (username, email, password, first_name, last_name, dob, country, phone_number)
        VALUES (?, ?, NULL, ?, ?, '2000-01-01', 'Unknown', '0000000000')
    """;
    try (Connection conn = DriverManager.getConnection(URL);
         PreparedStatement pstmt = conn.prepareStatement(insertGoogleUserSQL)) {
        pstmt.setString(1, email.split("@")[0]); // Use the email prefix as the username
        pstmt.setString(2, email);
        pstmt.setString(3, firstName);
        pstmt.setString(4, lastName);

        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
    }
    
   public void updateOnlineStatus(int userId, String status) {
    String sql = "UPDATE Users SET online_status = ? WHERE user_id = ?";
    
    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:chatting_app.db");
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, status);
        pstmt.setInt(2, userId);
        pstmt.executeUpdate();
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
   
   public String getUsernameById(int userId) {
    String query = "SELECT username FROM Users WHERE user_id = ?";
    try (Connection conn = DriverManager.getConnection(URL);
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getString("username");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
   
   public boolean updateProfileImage(int userId, byte[] imageData) {
    String updateSQL = "UPDATE Users SET profile_image = ? WHERE user_id = ?";
    try (Connection conn = DriverManager.getConnection(URL);
         PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
        pstmt.setBytes(1, imageData);
        pstmt.setInt(2, userId);
        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

  public boolean updateUserProfile(int userId, String firstName, String lastName, String email, String bio, String dob, String country, String phoneNumber) {
    String updateSQL = "UPDATE Users SET first_name = ?, last_name = ?, email = ?, bio = ?, dob = ?, country = ?, phone_number = ? WHERE user_id = ?";
    try (Connection conn = DriverManager.getConnection(URL);
         PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
        
        pstmt.setString(1, firstName);
        pstmt.setString(2, lastName);
        pstmt.setString(3, email);
        pstmt.setString(4, bio);  // Add bio
        pstmt.setString(5, dob);  // Add date of birth
        pstmt.setString(6, country);
        pstmt.setString(7, phoneNumber);
        pstmt.setInt(8, userId);
        
        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0; // Returns true if update was successful
    } catch (SQLException e) {
        e.printStackTrace();
        return false; // Returns false if there's an error
    }
}

  public User getUserProfile(int userId) {
    User user = null;
    String sql = """
        SELECT u.username, u.bio, u.dob, u.first_name, u.last_name, u.email, 
               u.country, u.phone_number, u.profile_image,
               (SELECT COUNT(*) FROM Friends WHERE Friends.user_id = u.user_id OR Friends.friend_id = u.user_id) AS friendsCount
        FROM Users u WHERE u.user_id = ?
    """;

    try (Connection conn = DriverManager.getConnection(URL); 
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            String username = rs.getString("username");
            String bio = rs.getString("bio");
            String dob = rs.getString("dob");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String email = rs.getString("email");
            String country = rs.getString("country");
            String phoneNumber = rs.getString("phone_number");
            byte[] profileImage = rs.getBytes("profile_image"); // Fetch profile image
            int friendsCount = rs.getInt("friendsCount"); // Fetch friends count

            user = new User(userId, username, bio, dob, firstName, lastName, email, country, phoneNumber, profileImage);
            user.setFriendsCount(friendsCount); // Store friends count
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return user;
}


public byte[] getProfileImage(int userId) {
    String sql = "SELECT profile_image FROM Users WHERE user_id = ?";
    try (Connection conn = DriverManager.getConnection(URL);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getBytes("profile_image"); // Return the image data as byte array
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null; // Return null if no image is found
}


}