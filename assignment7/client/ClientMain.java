package client;
/* From Daniel Liang's book */

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import global.Message;
import global.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;


public class ClientMain extends Application {
	// IO streams
	static DataOutputStream toServer = null;
	static DataInputStream fromServer = null;
	static public User user;
	static String chattingWith="Broadcast";

	FXMLLoader loader = new FXMLLoader();
	static ClientGUIController client;


	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) throws Exception {

		// Create a scene and place it in the stage
		loader.setLocation(getClass().getResource("ClientGUI.fxml"));
		Parent root = loader.load();
		client =loader.getController();
		primaryStage.setTitle("yMessage");
		primaryStage.setScene(new Scene(root));
		primaryStage.show(); // Display the stage

		try {
			// Create a socket to connect to the server
			@SuppressWarnings("resource")
			Socket socket = new Socket("localhost", 8000);

			// Create an input stream to receive data from the server
			toServer=new DataOutputStream(socket.getOutputStream());
			fromServer = new DataInputStream(socket.getInputStream());



			// Create an output stream to send data to the server
			//toServer = new DataOutputStream(socket.getOutputStream());
		}
		catch (IOException ex) {
			client.displayMessage(ex.toString());
		}


	}

	public static void userInit(ArrayList<Object> userData){

				user = new global.User((String)userData.get(0), (String)userData.get(1), (String)userData.get(2));
		// user = new global.User();
		client.displayMessage(user.welcomeMessage().getBody());
	}

	public static void sendMessage(){
		try {
			// Get the radius from the text field
			//double radius = Double.parseDouble(tf.getText().trim());

			String text = client.getMessage().trim();
			//String text = client.send_text.getText();

            Message message = new Message(client.getUsername(),client.chattingWith,text,0);


			// Display to the text area
            if (message.getMode()==0 && !text.equals("")) {
                client.displayMessage(user.getUsername() + ": " + text);


                toServer.writeUTF(message.toInfoString());
                toServer.flush();
            }

		}
		catch (IOException ex) {
			System.err.println(ex);
		}
	}

	public static void sendMessage(String text){
		try {

			Message message = new Message();
			message = message.parseString(text);


			// Display to the text area
            if (message.getMode()==0 && !text.equals("")) {
                client.displayMessage(user.getUsername() + ": " + text);


                toServer.writeUTF(message.getBody());
                toServer.flush();
				System.out.println("sent from client");
            }

		}
		catch (IOException ex) {
			System.err.println(ex);
		}
	}

	public static void updateUsers(){
		Message updateMsg = new Message(user.getUsername(),chattingWith, "",1);
		sendMessage(updateMsg.toInfoString());
	}

	public static void updateChattingWith(String s){chattingWith=s;}

	public static void main(String[] args) {
		launch(args);
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				System.out.println("In shutdown hook");
			}
		}, "Shutdown-thread"));
	}
}
