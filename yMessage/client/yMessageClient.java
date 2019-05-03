package client;
/* From Daniel Liang's book */

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;


public class yMessageClient extends Application {
	// IO streams 
	static DataOutputStream toServer = null;
	static DataInputStream fromServer = null;
	static public global.User user;

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
			fromServer = new DataInputStream(socket.getInputStream());

			// Create an output stream to send data to the server 
			toServer = new DataOutputStream(socket.getOutputStream());
		}
		catch (IOException ex) {
			client.displayMessage(ex.toString());
		}
	}

	public static void userInit(ArrayList<Object> userData){
		user = new global.User((String)userData.get(0), (String)userData.get(1), (int)userData.get(2), (int)userData.get(3),(int)userData.get(4));
		client.displayMessage(user.adminMessage());
	}

	public static void sendMessage(){
		try {
			// Get the radius from the text field
			//double radius = Double.parseDouble(tf.getText().trim());

			String text = client.getMessage().trim();

			// Send the radius to the server
			toServer.writeUTF(text);
			toServer.flush();

			// Get area from the server
			String textback = fromServer.readUTF().trim();

			// Display to the text area
			client.displayMessage(user.fullName()+": " + text);
			client.displayMessage(textback);

		}
		catch (IOException ex) {
			System.err.println(ex);
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
