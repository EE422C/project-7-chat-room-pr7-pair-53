package client;
/* From Daniel Liang's book */

import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class ChatClient extends Application {
	// IO streams 
	DataOutputStream toServer = null;
	DataInputStream fromServer = null;
	public global.User client;


	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {
		// Panel p to hold the label and text field
		BorderPane paneForTextField = new BorderPane();
		paneForTextField.setPadding(new Insets(5, 5, 5, 5));
		paneForTextField.setStyle("-fx-border-color: green");
		paneForTextField.setLeft(new Label("Enter a message: "));

		TextField tf = new TextField();
		tf.setAlignment(Pos.BOTTOM_RIGHT);
		paneForTextField.setCenter(tf);

		BorderPane mainPane = new BorderPane();
		// Text area to display contents
		TextArea ta = new TextArea();
		mainPane.setCenter(new ScrollPane(ta));
		mainPane.setTop(paneForTextField);


		// Create a scene and place it in the stage
		Scene scene = new Scene(mainPane, 450, 200);
		primaryStage.setTitle("Client"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage

		client = new global.User("Dylan", "Wolford", 1999, 5,1);
		ta.appendText(client.adminMessage());


		tf.setOnAction(e -> {
			try {
				// Get the radius from the text field
				//double radius = Double.parseDouble(tf.getText().trim());




				String text = tf.getText().trim();

				// Send the radius to the server
				toServer.writeUTF(text);
				toServer.flush();

				// Get area from the server
				String textback = fromServer.readUTF();

				// Display to the text area
				ta.appendText(client.fullName()+": " + text + "\n");
				ta.appendText(textback + '\n');

			}
			catch (IOException ex) {
				System.err.println(ex);
			}
		});

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
			ta.appendText(ex.toString() + '\n');
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
