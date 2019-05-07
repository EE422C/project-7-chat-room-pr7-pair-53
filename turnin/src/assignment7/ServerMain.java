/*
 * CHATROOM ServerMain.java
 * EE422C Project 7 submission by
 * Guy Sexton
 * gwm639
 * 16190
 * Dylan Wolford
 * ddw2379
 * 16190
 * Slip days used: 0
 * Spring 2019
 */




package server;


import java.io.*;
import java.net.*;
import java.util.*;

import global.Message;
import global.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class ServerMain extends Application { // Text area for displaying contents

    FXMLLoader loader = new FXMLLoader();
    ServerGUIController server;

    // Number a client
    private int clientNo = 0;
    private static ArrayList<HandleAClient> clients = new ArrayList<>();
    Map<String, String> activeUsers = new HashMap<>();


    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) throws Exception {
        // Create a scene and place it in the stage
        loader.setLocation(getClass().getResource("ServerGUI.fxml"));
        Parent root = loader.load();
        server = loader.getController();
        primaryStage.setTitle("yMessage Server");
        primaryStage.setScene(new Scene(root));
        primaryStage.show(); // Display the stage

        new Thread(() -> {
            try {  // Create a server socket
                @SuppressWarnings("resource")
                ServerSocket serverSocket = new ServerSocket(8000);
                String ip = InetAddress.getLocalHost().getHostAddress();
                System.out.println(ip);
                server.postToServer("MultiThreadServer started at "
                        + new Date());
                server.postToServer("This server's IP address is " + ip);



                while (true) {
                    // Listen for a new connection request
                    Socket socket = serverSocket.accept();

                    // Increment clientNo
                    clientNo++;


                    Platform.runLater(() -> {
                        // Display the client number
                        server.postToServer("Starting thread for Client " + clientNo +
                                " at " + new Date());

                        // Find the client's host name, and IP address
                        InetAddress inetAddress = socket.getInetAddress();
                        server.postToServer("Client " + clientNo + "'s host name is "
                                + inetAddress.getHostName());
                        server.postToServer("Client " + clientNo + "'s IP Address is "
                                + inetAddress.getHostAddress());
                    });


                    // Create and start a new thread for the connection
                    HandleAClient cl = new HandleAClient(socket);
                    clients.add(cl);
                    new Thread(cl).start();
                }
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }).start();
    }


    // Define the thread class for handling
    class HandleAClient implements Runnable {
        private Socket socket; // A connected socket
        DataOutputStream outputToClient;
        DataInputStream inputFromClient;

        /**
         * Construct a thread
         */
        public HandleAClient(Socket socket) {
            this.socket = socket;
        }

        /**
         * Run a thread
         */
        public void run() {
            try {
                // Create data input and output streams
                outputToClient = new DataOutputStream(socket.getOutputStream());
                inputFromClient = new DataInputStream(socket.getInputStream());
                // Continuously serve the client
                while (true) {

                    String text = inputFromClient.readUTF();
                    Message message = new Message();
                    message = message.parseString(text);

                    if (message.getMode() == 0) {
                        String from = message.getFrom();
                        String body = message.getBody();


                        for (HandleAClient cl : clients) {
                            cl.broadcastMessage(message.toInfoString());
                        }


                        String textback = "SENT to " + message.getTo() + " at " + message.printTime();


                        Platform.runLater(() -> {
                            server.postToServer(from + ": " +
                                    body);
                            server.postToServer(textback);
                        });
                    } else if (message.getMode() == 1) {
                        updateActiveUsers(message);

                        if (message.getTo().equals("leaving")) {
                            String msg = message.getFrom() + ": User " + message.getFrom() + " has left the server." ;
                            Platform.runLater(() -> {
                                server.postToServer(msg);
                            });
                            broadcastMessage(msg);
                        }
                    } else if(message.getMode()==3){
                        for (HandleAClient cl : clients) {
                            cl.broadcastMessage(message.toInfoString());
                        }
                    }


                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void broadcastMessage(String msg) {
            try {
                outputToClient.writeUTF(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void updateActiveUsers(Message msg) {

            if (msg.getTo().equals("leaving")) {
                activeUsers.remove(msg.getFrom());
            } else
                activeUsers.put(msg.getFrom(), msg.getTo());
            Message userUpdate = new Message("", "", activeUsers.toString(), 2);
            for (HandleAClient cl : clients) {
                try {
                    cl.broadcastMessage(userUpdate.toInfoString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}