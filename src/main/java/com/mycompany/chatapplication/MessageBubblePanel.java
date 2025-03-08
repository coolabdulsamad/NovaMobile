/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import static java.awt.AWTEventMulticaster.add;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

class MessageBubblePanel extends JPanel {
    private String message;
    private Color backgroundColor;
    
    public MessageBubblePanel(String message, Color backgroundColor, boolean isSentMessage) {
        this.message = message;
        this.backgroundColor = backgroundColor;
        setOpaque(false);
        setLayout(new BorderLayout());
        
        JLabel messageLabel = new JLabel(message);
        messageLabel.setBackground(backgroundColor);
        messageLabel.setOpaque(true);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));  // Padding
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        // Align sent messages to the right, received messages to the left
        if (isSentMessage) {
            setLayout(new FlowLayout(FlowLayout.RIGHT));  // Sent messages aligned to the right
        } else {
            setLayout(new FlowLayout(FlowLayout.LEFT));  // Received messages aligned to the left
        }
        
        add(messageLabel, BorderLayout.CENTER);
    }
}



