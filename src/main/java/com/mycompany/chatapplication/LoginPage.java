/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import Application_Connector.db.DatabaseHelper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.prefs.Preferences;



public class LoginPage {
    public static int loggedInUserId;
    private static final String URL = "jdbc:sqlite:chatting_app.db";
    
    public static JPanel createLoginPanel() { 
        // Left panel for login form
        JPanel loginPanel = new JPanel();
        loginPanel.setPreferredSize(new Dimension(450, 500));
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setLayout(null);

        // Logo
        JLabel logoLabel = new JLabel("NovaMobile", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setBounds(100, 30, 250, 40);
        JLabel subtitleLabel = new JLabel("Log in to your Account", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setBounds(100, 70, 250, 30);
        subtitleLabel.setForeground(Color.GRAY);
        loginPanel.add(subtitleLabel);
        
        Preferences prefs = Preferences.userNodeForPackage(LoginPage.class);
        String savedEmail = prefs.get("rememberedEmail", "");
        String savedPassword = prefs.get("rememberedPassword", "");


        // Email/Username field
        JLabel emailLabel = new JLabel("Email/Username");
        emailLabel.setBounds(50, 130, 350, 20);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setForeground(Color.GRAY);
        loginPanel.add(emailLabel);

        // Create a panel to hold the email field and the icon
JPanel emailPanel = new JPanel();
emailPanel.setLayout(null); // Absolute positioning
emailPanel.setBounds(50, 150, 350, 40);
emailPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
emailPanel.setBackground(Color.WHITE);
loginPanel.add(emailPanel);

// Load and resize email icon
ImageIcon emailIcon = new ImageIcon(
    new ImageIcon(LoginPage.class.getResource("/icons/mail.png")) // Path to your email icon
        .getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH)
);

// Create the email field
JTextField emailField = new JTextField();
emailField.setBounds(35, 5, 310, 30); // Leave space for the icon
emailField.setFont(new Font("Arial", Font.PLAIN, 14));
emailField.setBorder(null); // Remove default border
emailPanel.add(emailField);

// Add the email icon inside the text field
JLabel emailIconLabel = new JLabel(emailIcon);
emailIconLabel.setBounds(8, 10, 20, 20); // Adjust position inside panel
emailPanel.add(emailIconLabel);


        // Password field
JLabel passwordLabel = new JLabel("Password");
passwordLabel.setBounds(50, 210, 350, 20);
passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
passwordLabel.setForeground(Color.GRAY);
loginPanel.add(passwordLabel);


// Create a panel to hold the password field and the icon
JPanel passwordPanel = new JPanel();
passwordPanel.setLayout(null); // Absolute positioning
passwordPanel.setBounds(50, 230, 350, 40);
passwordPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
passwordPanel.setBackground(Color.WHITE);
loginPanel.add(passwordPanel);

// Initialize the password field inside the panel
JPasswordField passwordField = new JPasswordField();
passwordField.setBounds(35, 5, 270, 30); // Position the field inside the panel, leaving space for the icon
passwordField.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font for the password field
passwordField.setBorder(null); // Remove default border to make space for the icon
passwordPanel.add(passwordField); // Add the password field to the panel

// Load and resize the padlock icon for password field (16x16)
ImageIcon padlockIcon = new ImageIcon(
    new ImageIcon(LoginPage.class.getResource("/icons/padlock.png"))
        .getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH) // Resize the icon to 16x16
);

// Create the label for the padlock icon inside the password field
JLabel padlockIconLabel = new JLabel(padlockIcon);
padlockIconLabel.setBounds(8, 10, 20, 20); // Position the padlock icon inside the text field (left side)
passwordPanel.add(padlockIconLabel);

// Load and resize icons
ImageIcon showIcon = new ImageIcon(
    new ImageIcon(LoginPage.class.getResource("/icons/show.png"))
        .getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)
);

ImageIcon hideIcon = new ImageIcon(
    new ImageIcon(LoginPage.class.getResource("/icons/hide.png"))
        .getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)
);

// Create the show/hide password icon inside the text field
JLabel showPasswordLabel = new JLabel(showIcon);
showPasswordLabel.setBounds(320, 10, 20, 20); // Adjust inside position
passwordPanel.add(showPasswordLabel);

// Add a mouse listener to toggle password visibility
showPasswordLabel.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        if (passwordField.getEchoChar() == (char) 0) {
            passwordField.setEchoChar('*');
            showPasswordLabel.setIcon(showIcon);
        } else {
            passwordField.setEchoChar((char) 0);
            showPasswordLabel.setIcon(hideIcon);
        }
    }
});


        // Remember me and forgot password
        JCheckBox rememberMe = new JCheckBox("Remember me");
        rememberMe.setBounds(50, 290, 150, 20);
        rememberMe.setBackground(Color.WHITE);
        rememberMe.setFont(new Font("Arial", Font.PLAIN, 12));
        rememberMe.setForeground(Color.GRAY);
        loginPanel.add(rememberMe);
        

        JLabel forgotPassword = new JLabel("Forgot Password?", SwingConstants.RIGHT);
        forgotPassword.setBounds(250, 290, 150, 20);
        forgotPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        forgotPassword.setForeground(new Color(0, 123, 255));
        loginPanel.add(forgotPassword);
        
        // Forgot password click listener
        // Forgot password click listener
        forgotPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Open forgot password dialog
                //ForgotPasswordHandler.openForgotPasswordDialog();
        ForgotPasswordRequestPage forgotPasswordDialog = new ForgotPasswordRequestPage();
        forgotPasswordDialog.setVisible(true);

                
            }
        });

        emailField.setText(savedEmail);
passwordField.setText(savedPassword);

if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
    rememberMe.setSelected(true);
}


        // Login button
        JButton loginButton = new JButton("Log In");
        loginButton.setBounds(50, 330, 350, 40);
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setFocusPainted(false);
        loginPanel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String usernameOrEmail = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginPanel, "Both username/email and password are required.");
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper();
        String userId = dbHelper.validateLogin(usernameOrEmail, password);

        if (userId != null) {
            int loggedInUserId = Integer.parseInt(userId);

            // Update user status to 'online'
            dbHelper.updateOnlineStatus(loggedInUserId, "Online");
            
            // Retrieve the actual username from the database
            String username = dbHelper.getUsernameById(loggedInUserId);

            JOptionPane.showMessageDialog(loginPanel, "Login successful!");
            
            
                        // Store login credentials if "Remember Me" is checked
            if (rememberMe.isSelected()) {
                Preferences prefs = Preferences.userNodeForPackage(LoginPage.class);
                prefs.put("rememberedEmail", usernameOrEmail);
                prefs.put("rememberedPassword", password);
            } else {
                // Clear saved credentials if not checked
                Preferences prefs = Preferences.userNodeForPackage(LoginPage.class);
                prefs.remove("rememberedEmail");
                prefs.remove("rememberedPassword");
            }
            
            Dashboard dashboard = new Dashboard(username);
            dashboard.setVisible(true);
            UserSession.setCurrentUsername(loggedInUserId, username);
           
//            // Open chat window with userId and username
//            new ChatWindow(Integer.parseInt(userId), username);

            // Dispose the login frame correctly
            JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(loginPanel);
            if (loginFrame != null) {
                loginFrame.dispose();
            }
        } else {
            JOptionPane.showMessageDialog(loginPanel, "Invalid username/email or password. Please try again.");
        }
    }
});
      
        
                
        // Signup link
        JLabel signupLabel = new JLabel("Don't have an account? ", SwingConstants.CENTER);
        signupLabel.setBounds(1, 380, 350, 20);
        signupLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        signupLabel.setForeground(Color.GRAY);
        loginPanel.add(signupLabel);
        
        JLabel signupLabel2 = new JLabel("Create an account", SwingConstants.CENTER);
        signupLabel2.setBounds(117, 380, 350, 20);
        signupLabel2.setFont(new Font("Arial", Font.PLAIN, 12));
        signupLabel2.setForeground(Color.BLACK);
        signupLabel2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        signupLabel2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                signupLabel2.setForeground(Color.BLUE); // Change color on mouse hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                signupLabel2.setForeground(Color.BLACK); // Revert color on mouse exit
            }

            @Override
            public void mouseClicked(MouseEvent e) {
        
                MainTab2.main(null);
            }
        });

        
        loginPanel.add(signupLabel2);

        // Right panel for illustration
JPanel rightPanel = new JPanel();
rightPanel.setPreferredSize(new Dimension(450, 500));
rightPanel.setBackground(new Color(0, 123, 255));
rightPanel.setLayout(new BorderLayout());

// Array of image paths
String[] imagePaths = {
    "C:\\Users\\coola\\Downloads\\undraw_chatting_2b1g.png",
    "C:\\Users\\coola\\Downloads\\undraw_chatting_1.png",
    "C:\\Users\\coola\\Downloads\\undraw_chatting_2.png",
    "C:\\Users\\coola\\Downloads\\undraw_chatting_3.png",
    "C:\\Users\\coola\\Downloads\\undraw_chatting_4.png",
    "C:\\Users\\coola\\Downloads\\undraw_chatting_5.png",
    "C:\\Users\\coola\\Downloads\\undraw_chatting_6.png",
    "C:\\Users\\coola\\Downloads\\undraw_chatting_7.png",
    "C:\\Users\\coola\\Downloads\\undraw_chatting_8.png",
    "C:\\Users\\coola\\Downloads\\undraw_chatting_9.png",
    "C:\\Users\\coola\\Downloads\\undraw_chatting_10.png",
    "C:\\Users\\coola\\Downloads\\undraw_chatting_11.png",
    "C:\\Users\\coola\\Downloads\\undraw_chatting_12.png",
    "C:\\Users\\coola\\Downloads\\undraw_chatting_13.png",
    "C:\\Users\\coola\\Downloads\\undraw_chatting_14.png",
};

// Create a JLabel for the illustration image
final JLabel illustrationLabel = new JLabel();
illustrationLabel.setHorizontalAlignment(SwingConstants.CENTER);

// Create a Timer to change images every 3 seconds (3000 ms)
int interval = 3000; // Change image every 3 seconds
Timer timer = new Timer(interval, new ActionListener() {
    int currentIndex = 0;

    @Override
    public void actionPerformed(ActionEvent e) {
        // Load the next image in the array
        ImageIcon icon = new ImageIcon(imagePaths[currentIndex]);
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(500, 500, Image.SCALE_SMOOTH); // Resize image
        icon.setImage(newImage);
        illustrationLabel.setIcon(icon);

        // Move to the next image, loop back to the first one when at the end
        currentIndex = (currentIndex + 1) % imagePaths.length;
    }
});

// Start the timer to begin changing images
timer.start();

// Add the illustration label to the right panel
rightPanel.add(illustrationLabel, BorderLayout.CENTER);

// Return the login panel containing both parts
JPanel finalPanel = new JPanel();
finalPanel.setLayout(new BorderLayout());
finalPanel.add(loginPanel, BorderLayout.WEST);
finalPanel.add(rightPanel, BorderLayout.EAST);

return finalPanel;

    }
    
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
    public static void showLoginScreen() {
    JFrame loginFrame = new JFrame("NovaMobile - Login");
    loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    loginFrame.setSize(900, 500);  // Adjust size as needed
    loginFrame.setLocationRelativeTo(null);
    
    // Add the login panel to the frame
    loginFrame.setContentPane(createLoginPanel());
    
    loginFrame.setVisible(true);
}

public static void attemptAutoLogin() {
    Preferences prefs = Preferences.userNodeForPackage(LoginPage.class);
    String savedEmail = prefs.get("rememberedEmail", "");
    String savedPassword = prefs.get("rememberedPassword", "");

    if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
        DatabaseHelper dbHelper = new DatabaseHelper();
        String userId = dbHelper.validateLogin(savedEmail, savedPassword);

        if (userId != null) {
            int loggedInUserId = Integer.parseInt(userId);
            dbHelper.updateOnlineStatus(loggedInUserId, "Online");

            String username = dbHelper.getUsernameById(loggedInUserId);
            JOptionPane.showMessageDialog(null, "Auto-login successful!");

            new Dashboard(username).setVisible(true);
            UserSession.setCurrentUsername(loggedInUserId, username);

            return;  // STOP execution here so login screen doesn't open!
        }
    }

    // If auto-login fails, show the login screen
    showLoginScreen();
}



}
