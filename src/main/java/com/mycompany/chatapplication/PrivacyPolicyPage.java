/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class PrivacyPolicyPage extends JFrame {
    public PrivacyPolicyPage() {
        setTitle("Terms and Conditions");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // Set the icon for the frame (you can change the path to your icon)
        
        URL iconURL = MainTab.class.getClassLoader().getResource("icons/novamobile.jpg");
if (iconURL != null) {
    setIconImage(new ImageIcon(iconURL).getImage());
} else {
    System.err.println("Icon image not found.");
}
        setIconImage(new ImageIcon("C:\\Users\\coola\\Documents\\NetBeansProjects\\ChatApplication\\src\\main\\resources\\icons\\novamobile.jpg").getImage()); // Update the icon path

        // Add content to the page
        JTextArea textArea = new JTextArea("This is the terms and conditions page.");
        textArea.setEditable(false);
        add(new JScrollPane(textArea));
    }
}

