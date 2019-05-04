package server;/* From Daniel Liang's book */


import java.io.*;
import java.net.*;
import java.util.Date;

import global.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


public class yMessageServer extends Application
{ // Text area for displaying contents

	FXMLLoader loader = new FXMLLoader();
	ServerGUIController server;

    // Number a client
    private int clientNo = 0;

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
                        server.postToServer("Starting thread for client " + clientNo +
                                " at " + new Date());

                        // Find the client's host name, and IP address
                        InetAddress inetAddress = socket.getInetAddress();
                        server.postToServer("Client " + clientNo + "'s host name is "
                                + inetAddress.getHostName());
                        server.postToServer("Client " + clientNo + "'s IP Address is "
                                + inetAddress.getHostAddress());
                    });


                    // Create and start a new thread for the connection
                    new Thread(new HandleAClient(socket)).start();
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
        /** Construct a thread */
        public HandleAClient(Socket socket) {
            this.socket = socket;
        }
        /** Run a thread */
        public void run() {
            try {
                // Create data input and output streams
                ObjectInputStream inFromClient=new ObjectInputStream(socket.getInputStream());

                //DataInputStream inputFromClient = new DataInputStream( socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream( socket.getOutputStream());
                // Continuously serve the client
                while (true) {
                    // Receive radius from the client
                    //String text = inputFromClient.readUTF();
                    Message message = new Message();


                    try {
                        message = (Message) inFromClient.readObject();
                    }catch (Exception e){
                        System.out.println("oof");
                    }

                    //NEW STUFF DYLAN ADDED*************************
                    //global.Message message = new global.Message();
                    //message.setBody(text);

                    String text=message.getBody();


                    // Compute area
                    String textback = "SENT to the server at " + message.printTime();
                    // Send area back to the client
                    outputToClient.writeUTF(textback);
                    Platform.runLater(() -> {
                        server.postToServer("Client " + clientNo + ": " +
                                text);
                        server.postToServer(textback);
                    });
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
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
