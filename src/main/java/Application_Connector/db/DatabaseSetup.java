/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Application_Connector.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseSetup {
    public static void main(String[] args) {
        // SQLite database URL
        String url = "jdbc:sqlite:chatting_app.db";

        // SQL statements for creating tables
        String createUsersTable = """
                CREATE TABLE IF NOT EXISTS Users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    email TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    first_name VARCHAR(50) NOT NULL,
                    last_name VARCHAR(50) NOT NULL,
                    dob DATE NOT NULL,
                    country VARCHAR(100) NOT NULL,
                    phone_number VARCHAR(20) NOT NULL
                );
                """;

        String createFriendsTable = """
                CREATE TABLE IF NOT EXISTS Friends (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    friend_id INTEGER NOT NULL,
                    status TEXT NOT NULL CHECK(status IN ('Pending', 'Accepted', 'Rejected')),
                    FOREIGN KEY (user_id) REFERENCES Users(user_id),
                    FOREIGN KEY (friend_id) REFERENCES Users(user_id)
                );
                """;

        String createMessagesTable = """
                CREATE TABLE IF NOT EXISTS Messages (
                    message_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    sender_id INTEGER NOT NULL,
                    receiver_id INTEGER NOT NULL,
                    message_text TEXT NOT NULL,
                    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (sender_id) REFERENCES Users(user_id),
                    FOREIGN KEY (receiver_id) REFERENCES Users(user_id)
                );
                """;

        // Establish database connection and execute the SQL statements
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // Execute table creation
            stmt.execute(createUsersTable);
            stmt.execute(createFriendsTable);
            stmt.execute(createMessagesTable);

            System.out.println("Database setup complete!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
