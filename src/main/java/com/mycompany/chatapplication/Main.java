///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.mycompany.chatapplication;
//    
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//
//public class Main {
//    private static JFrame frame;
//
//    public static void main(String[] args) {
//        frame = new JFrame("Chat Application");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setResizable(false);
//
//        JPanel loginPanel = LoginPage.createLoginPanel();
//        loginPanel.add(LoginPage.createSignPanel());
//
//        frame.add(loginPanel);
//        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//    }
//
//    public static void showSignupPanel() {
//        frame.getContentPane().removeAll();
//        JPanel signupPanel = SignupPage.createSignUpPanel();
//        signupPanel.add(SignupPage.createLoginPanel());
//        frame.add(signupPanel);
//        frame.revalidate();
//        frame.repaint();
//    }
//
//    public static void showLoginPanel() {
//        frame.getContentPane().removeAll();
//        JPanel loginPanel = LoginPage.createLoginPanel();
//        loginPanel.add(LoginPage.createSignUpPanel());
//        frame.add(loginPanel);
//        frame.revalidate();
//        frame.repaint();
//    }
//}
//
