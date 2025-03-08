/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainStart_Test;

import com.mycompany.chatapplication.LoginPage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TypingEffectDemo {
    public static JPanel createTypingEffectPanel(JFrame mainFrame) {
        // Create panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(225, 233, 241));

        // Load and resize the padlock icon for password field (resize to 200x200)
        ImageIcon padlockIcon = new ImageIcon(
            new ImageIcon(TypingEffectDemo.class.getResource("/icons/novamobile.jpg")) // Ensure this path is correct
                .getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)
        );

        // Create JLabel for padlock icon
        JLabel iconLabel = new JLabel(padlockIcon);
        iconLabel.setBounds(250, 50, 200, 200); // Adjust the positioning of the icon

        // Create JLabel for typing effect
        JLabel wel = new JLabel("<html><div style='text-align: center;'></div></html>");
        wel.setFont(new Font("Arial", Font.BOLD, 16));
        wel.setForeground(new Color(0, 123, 255));

        // Create "Get Started" Button
        JButton getStartedButton = new JButton("Get Started");
        getStartedButton.setFont(new Font("Arial", Font.BOLD, 16));
        getStartedButton.setBackground(new Color(0, 123, 255)); // Blue background
        getStartedButton.setForeground(Color.WHITE); // White text
        getStartedButton.setFocusPainted(false);
        getStartedButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

         // Add button action to replace the panel with LoginPage
        getStartedButton.addActionListener(e -> {
        // Replace TypingEffectDemo with LoginPage
        JPanel loginPanel = LoginPage.createLoginPanel();
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(loginPanel);
        mainFrame.revalidate(); // Revalidate the layout to update the frame
        mainFrame.repaint();    // Repaint the frame
        });


        // Set layout constraints to center components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 20, 0); // Add top margin
        panel.add(iconLabel, gbc); // Add icon at the top

        // Add welcome text
        gbc.gridy = 1;
        panel.add(wel, gbc);

        // Add spacing between text and button
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 0, 0); // Adds space above button
        panel.add(getStartedButton, gbc);

        // Start typing effect
        String message = """
            Greetings and welcome to <b>NovaMobile</b>!<br>
            We are delighted to see you as a part of our community.<br>
            Feel free to discover, interact, and collaborate<br>
            with us to create something wonderful.
        """;

        startTypingEffect(wel, message, getStartedButton);

        return panel; // Return the panel for addition to the main JFrame
    }

    private static void startTypingEffect(JLabel label, String text, JButton button) {
        Timer timer = new Timer(50, new ActionListener() { // Faster speed for longer text
            private int index = 0;
            private StringBuilder displayedText = new StringBuilder("<html><div style='text-align: center;'>");

            @Override
            public void actionPerformed(ActionEvent e) {
                if (index < text.length()) {
                    displayedText.append(text.charAt(index));
                    label.setText(displayedText.toString() + "</div></html>");
                    index++;
                } else {
                    ((Timer) e.getSource()).stop(); // Stop timer when complete
                    button.setVisible(true); // Show the button after text finishes
                }
            }
        });
        timer.start();
        button.setVisible(false); // Hide button until text is fully displayed
    }
}
