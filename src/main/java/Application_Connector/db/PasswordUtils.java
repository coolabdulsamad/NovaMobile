/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application_Connector.db;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
    
    // Method to hash a password using bcrypt
    public static String hashPassword(String password) {
        // Generate a salt with a log_rounds of 12 (recommended value)
        String salt = BCrypt.gensalt(12);
        
        // Hash the password using the salt
        return BCrypt.hashpw(password, salt);
    }

    // Method to check if the password matches the hashed version
    public static boolean checkPassword(String password, String hashedPassword) {
        // Check if the given password matches the hashed password
        return BCrypt.checkpw(password, hashedPassword);
    }
}