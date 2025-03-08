///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.mycompany.chatapplication;
//
//import java.net.InetSocketAddress;
//import java.net.http.WebSocket;
//import org.java_websocket.handshake.ClientHandshake;
//import org.java_websocket.server.WebSocketServer;
//
//public class ChatServer extends WebSocketServer {
//    public ChatServer(int port) {
//        super(new InetSocketAddress(port));
//    }
//
//    @Override
//    public void onOpen(WebSocket conn, ClientHandshake handshake) {
//        System.out.println("New connection: " + conn.getRemoteSocketAddress());
//    }
//
//    @Override
//    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
//        System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
//    }
//
//    @Override
//    public void onMessage(WebSocket conn, String message) {
//        System.out.println("Message from client: " + message);
//        // Broadcast the message to all clients
//        this.broadcast(message);
//    }
//
//    @Override
//    public void onError(WebSocket conn, Exception ex) {
//        ex.printStackTrace();
//    }
//
//    @Override
//    public void onStart() {
//        System.out.println("Server started successfully");
//    }
//
//    public static void main(String[] args) {
//        int port = 8887; // You can change this port if needed
//        ChatServer server = new ChatServer(port);
//        server.start();
//        System.out.println("Server started on port: " + port);
//    }
//}
