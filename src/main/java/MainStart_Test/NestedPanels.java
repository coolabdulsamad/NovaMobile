/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainStart_Test;


import javax.swing.*;
import java.awt.*;

public class NestedPanels {
    public static void main(String[] args) {
        // Create the outer panel
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new BorderLayout());
        outerPanel.setBackground(Color.RED);

        // Create the inner panel
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new FlowLayout());
        innerPanel.setBackground(Color.BLUE);

        // Add the inner panel to the outer panel
        outerPanel.add(innerPanel, BorderLayout.CENTER);

        // Add some components to the inner panel
        innerPanel.add(new JButton("Button 1"));
        innerPanel.add(new JButton("Button 2"));

        // Create a JFrame and add the outer panel to it
        JFrame frame = new JFrame("Nested Panels");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(outerPanel);
        frame.setSize(400, 300);
        frame.setVisible(true);
    }
}
