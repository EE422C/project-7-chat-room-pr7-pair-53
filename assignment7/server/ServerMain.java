package server;/* From Daniel Liang's book */


import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import global.Message;
import global.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


public class ServerMain extends Application
{ // Text area for displaying contents

    FXMLLoader loader = new FXMLLoader();
    ServerGUIController server;

    // Number a client
    private int clientNo = 0;
    private ArrayList<HandleAClient> clients=new ArrayList<>();
    Map<String,String> activeUsers=new HashMap<>();

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) throws Exception {
        // Create a scene and place it in the stage
        loader.setLocation(getClass().getResource("ServerGUI.fxml"));
        Parent root = loader.load();
        server=loader.getController();
        primaryStage.setTitle("yMessage Server");
        primaryStage.setScene(new Scene(root));
        primaryStage.show(); // Display the stage

        new Thread( () -> {
            try {  // Create a server socket
                @SuppressWarnings("resource")
                ServerSocket serverSocket = new ServerSocket(8000);
                server.postToServer("MultiThreadServer started at "
                        + new Date());


                while (true) {
                    // Listen for a new connection request
                    Socket socket = serverSocket.accept();

                    // Increment clientNo
                    clientNo++;


                    Platform.runLater( () -> {
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
            }
            catch(IOException ex) {
                System.err.println(ex);
            }
        }).start();
    }


    // Define the thread class for handling
    class HandleAClient implements Runnable {
        private Socket socket; // A connected socket
        DataOutputStream outputToClient;
        DataInputStream inputFromClient;
        /** Construct a thread */
        public HandleAClient(Socket socket) {
            this.socket = socket;
        }
        /** Run a thread */
        public void run() {
            try {
                // Create data input and output streams
                outputToClient = new DataOutputStream( socket.getOutputStream());
                inputFromClient = new DataInputStream( socket.getInputStream());
                // Continuously serve the client
                while (true) {
                    // Receive radius from the client
                    //String text = inputFromClient.readUTF();
                    Message message = new Message();
                    message = message.parseString(inputFromClient.readUTF());


                    if(message.getMode()==0) {
                        String from = message.getFrom();
                        String body = message.getBody();

                        outputToClient.writeUTF(from+":"+message.getBody());

                        // Compute area
                        String textback = "SENT to " + message.getTo() + " at " + message.printTime();

                        outputToClient.writeUTF(textback);



                        Platform.runLater(() -> {
                            server.postToServer(from + ": " +
                                    body);
                            server.postToServer(textback);
                        });
                    }
                    else if(message.getMode()==1){
                        updateActiveUsers(message);
                    }




                }

            } catch(IOException e) {
                e.printStackTrace();
            }

        }

        public void updateActiveUsers(Message msg){
            if(msg.getMode()!=1)
                return;
            activeUsers.put(msg.getFrom(),msg.getTo());
            System.out.println(msg.toInfoString());
            System.out.println(activeUsers);
        }

    }




    public static void main(String[] args) {
        launch(args);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("In shutdown hook");
            }
        }, "Shutdown-thread"));
    }
}
