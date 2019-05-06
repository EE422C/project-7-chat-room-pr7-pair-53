package client;
/* From Daniel Liang's book */

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import global.Message;
import global.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class ClientMain extends Application {
	// IO streams
	static DataOutputStream toServer = null;
	static DataInputStream fromServer = null;
	static public User user;
	static String chattingWith="Broadcast";
	ArrayList<String> activeUsers=new ArrayList<>();
	ArrayList<String> chatRooms=new ArrayList<String>(Arrays.asList("Broadcast","One Room","Two Room","Red Room","Blue Room"));

	FXMLLoader loader = new FXMLLoader();
	static ClientGUIController client;


	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) throws Exception {

		// Create a scene and place it in the stage
		loader.setLocation(getClass().getResource("ClientGUI.fxml"));
		Parent root = loader.load();
		client = loader.getController();
		primaryStage.setTitle("yMessage");
		primaryStage.setScene(new Scene(root));
		primaryStage.show(); // Display the stage
		client.setRooms(chatRooms);

		try {
			// Create a socket to connect to the server
			@SuppressWarnings("resource")
			Socket socket = new Socket("localhost", 8000);

			// Create an input stream to receive data from the server
			toServer = new DataOutputStream(socket.getOutputStream());
			fromServer = new DataInputStream(socket.getInputStream());



			// Create an output stream to send data to the server
			//toServer = new DataOutputStream(socket.getOutputStream());
		} catch (IOException ex) {
			client.displayMessage(ex.toString());
		}
		Thread usrUpdate=new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					updateUsers();
					Thread.sleep(1000);
				}catch(Exception e){
					System.out.println("");
				}
			}
		});
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Message msg = new Message();
						msg = msg.parseString(fromServer.readUTF());
System.out.println(msg.toInfoString());
						if (msg!=null) {
							if (msg.getMode() == 0) {
								if (msg.getTo().equals("Broadcast")) {
									if (chattingWith.equals("Broadcast"))
										client.displayMessage(msg.getFrom() + ": " + msg.getBody());
									else
										client.displayBackground("Broadcast", msg.getFrom() + ": " + msg.getBody());
								} else if (msg.getTo().equals(client.getUsername())) {
									if (chattingWith.equals(msg.getFrom()))
										client.displayMessage(msg.getFrom() + ": " + msg.getBody());
									else
										client.displayBackground(msg.getFrom(), msg.getFrom() + ": " + msg.getBody());
								} else if(chatRooms.contains(msg.getTo())){
									if (chattingWith.equals(msg.getTo()))
										client.displayMessage(msg.getFrom() + ": " + msg.getBody());
									else
										client.displayBackground(msg.getTo(), msg.getFrom() + ": " + msg.getBody());
								} else if(msg.getFrom().equals(client.getUsername())){
									client.displayMessage(msg.getFrom() + ": " + msg.getBody());
								}
							} else if (msg.getFrom() == "Server")
								client.displayMessage(msg.getFrom() + ": " + msg.getBody());
							else if (msg.getMode() == 2) {
								System.out.println(msg.getBody());
								String toMap = msg.getBody();
								toMap = toMap.substring(1, toMap.length() - 1);
								String[] toMapPT2 = toMap.split(",");
								ArrayList<String> userUP = new ArrayList<>();
								for (String s : toMapPT2) {
									userUP.add(s.split("=")[0].trim());
								}
								//activeUsers.clear();
								activeUsers = userUP;
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										updateLocalUsers();
									}
								});
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}

	public static void userInit(ArrayList<Object> userData){

		user = new global.User((String)userData.get(0), (String)userData.get(1), (String)userData.get(2));
		// user = new global.User();
		try {
			Message welcome;
			welcome = user.welcomeMessage();
			welcome.setTo(client.chattingWith);
			welcome.setFrom(client.getUsername());
			toServer.writeUTF(welcome.toInfoString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendMessage(){
		try {
			// Get the radius from the text field
			//double radius = Double.parseDouble(tf.getText().trim());

			String text = client.getMessage().trim();
			//String text = client.send_text.getText();

			Message message = new Message(client.getUsername(),client.chattingWith,text,0);

			/*
			// Display to the text area
            if (message.getMode()==0 && !text.equals("")) {
                client.displayMessage(user.getUsername() + ": " + text);
                toServer.writeUTF(message.toInfoString());
                toServer.flush();
            }
*/
			if (message.getMode()==0 && !text.equals("")) {
				toServer.writeUTF(message.toInfoString());
				toServer.flush();
			}


		}
		catch (IOException ex) {
			System.err.println(ex);
		}
	}


	public static void updateUsers(){
		try {
			Message updateMsg = new Message(user.getUsername(), chattingWith, "", 1);
			toServer.writeUTF(updateMsg.toInfoString());
			toServer.flush();
		}catch(Exception e){e.printStackTrace();}
	}

	public void updateLocalUsers(){
		client.updateLocalUsers(activeUsers);
	}

	public static void updateChattingWith(String s){chattingWith=s;}

	public static void main(String[] args) {
		launch(args);
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				System.out.println("In shutdown hook");
				try {
					Message updateMsg = new Message(user.getUsername(), "leaving", "", 1);
					toServer.writeUTF(updateMsg.toInfoString());
					toServer.flush();
				}catch(Exception e){e.printStackTrace();}
			}
		}, "Shutdown-thread"));
	}
}