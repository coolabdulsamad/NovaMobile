///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.mycompany.chatapplication;
//
//
//import java.net.URI;
//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.handshake.ServerHandshake;
//
//public class ChatClient extends WebSocketClient {
//    public ChatClient(URI serverUri) {
//        super(serverUri);
//    }
//
//    @Override
//    public void onOpen(ServerHandshake handshakedata) {
//        System.out.println("Connected to server");
//    }
//
//    @Override
//    public void onMessage(String message) {
//        // Add the received message to the chat area
//        addMessageToChat(message, false, "Just now"); // Change the timestamp logic as needed
//    }
//
//    @Override
//    public void onClose(int code, String reason, boolean remote) {
//        System.out.println("Disconnected from server");
//    }
//
//    @Override
//    public void onError(Exception ex) {
//        ex.printStackTrace();
//    }
//    
//    @Override
//    public void sendMessage(String message) {
//    if (client != null && client.isOpen()) {
//        client.send(message);
//        addMessageToChat(message, true, "Just now"); // For sent messages
//    }
//}
//
//    public static void main(String[] args) {
//        try {
//            URI uri = new URI("ws://localhost:8887"); // Server URI
//            ChatClient client = new ChatClient(uri);
//            client.connect();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}