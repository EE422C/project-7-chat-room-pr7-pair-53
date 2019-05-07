/*
 * CHATROOM ClientMain.java
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




package client;
/* From Daniel Liang's book */

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;


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


public class ClientMain extends Application {
	// IO streams
	static Stage classPrimaryStage;
	static DataOutputStream toServer = null;
	static DataInputStream fromServer = null;
	static public User user;
	static String chattingWith="Broadcast";
	static ArrayList<String> activeUsers=new ArrayList<>();
	static ArrayList<String> chatRooms=new ArrayList<String>(Arrays.asList("Broadcast","One Room","Two Room","Red Room","Blue Room"));

	static FXMLLoader InitLoader = new FXMLLoader();
	static FXMLLoader ClientLoader = new FXMLLoader();
	static ClientGUIController client;



	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) throws Exception {
		classPrimaryStage=primaryStage;
		InitLoader.setLocation(getClass().getResource("ClientInitGUI.fxml"));
		ClientLoader.setLocation(getClass().getResource("ClientGUI.fxml"));
		Parent root = InitLoader.load();
		primaryStage.setTitle("Connect to yMessage");
		primaryStage.setScene(new Scene(root));
		primaryStage.show(); // Display the stage
	}

	public static void startClient(String host) throws Exception{
		classPrimaryStage.close();
		// Create a scene and place it in the stage
		Parent root = ClientLoader.load();
		client = ClientLoader.getController();
		classPrimaryStage.setTitle("yMessage");
		classPrimaryStage.setScene(new Scene(root));
		classPrimaryStage.show(); // Display the stage
		client.setRooms(chatRooms);

		try {
			// Create a socket to connect to the server
			@SuppressWarnings("resource")
			Socket socket2 = new Socket(host, 8000);


			toServer = new DataOutputStream(socket2.getOutputStream());
			fromServer = new DataInputStream(socket2.getInputStream());

		} catch (IOException ex) {
			client.displayMessage(ex.toString());
		}

		Thread usrUpdate=new Thread(new Runnable() {
			@Override
			public void run() {
				if(fromServer!=null&&toServer!=null) {
					try {
						updateUsers();
						Thread.sleep(1000);
					} catch (Exception e) {
						System.out.println("");
					}
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
							if (msg.getFrom() == "Server")				//if from server
								client.displayMessage(msg.getFrom() + ": " + msg.getBody());
							else if (msg.getMode() == 0) {
								if (msg.getTo().equals("Broadcast")) {
									if (chattingWith.equals("Broadcast"))
										client.displayMessage(msg.getFrom() + ": " + msg.getBody());	//if on Broadcast
									else
										client.displayBackground("Broadcast", msg.getFrom() + ": " + msg.getBody());
								} else if (msg.getTo().equals(client.getUsername())) {		//if chat arriving to you
									if (chattingWith.equals(msg.getFrom()))				//if currently on that DM chat
										client.displayMessage(msg.getFrom() + ": " + msg.getBody());
									else
										client.displayBackground(msg.getFrom(), msg.getFrom() + ": " + msg.getBody());
								} else if(chatRooms.contains(msg.getTo())){
									if (chattingWith.equals(msg.getTo()))		//if currently in chat room where message was sent
										client.displayMessage(msg.getFrom() + ": " + msg.getBody());
									else
										client.displayBackground(msg.getTo(), msg.getFrom() + ": " + msg.getBody());
								} else if(msg.getFrom().equals(client.getUsername())){			//if message was sent from you
									client.displayMessage(msg.getFrom() + ": " + msg.getBody());
								}
							} else if (msg.getMode() == 2) {
								System.out.println(msg.getBody());
								String toMap = msg.getBody();
								toMap = toMap.substring(1, toMap.length() - 1);
								String[] toMapPT2 = toMap.split(",");
								ArrayList<String> userUP = new ArrayList<>();
								for (String s : toMapPT2) {
									userUP.add(s.split("=")[0].trim());
								}

								activeUsers = userUP;
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										updateLocalUsers();
									}
								});
							} else if(msg.getMode()==3){
								if(msg.getBody().contains(user.getUsername())){
									updateGMs(msg.getBody());
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();



		classPrimaryStage.setOnHiding(new EventHandler<WindowEvent>() {		//shut down code
            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        boolean flag = false;
                        try {
                            Message updateMsg = new Message(user.getUsername(), "leaving", "", 1);
                            toServer.writeUTF(updateMsg.toInfoString());
                            toServer.flush();

                            Message message1 = new Message();
                            message1 = message1.parseString(fromServer.readUTF());

                            if(message1.getFrom()=="Server")
                                client.displayMessage(message1.getFrom() + ": " + message1.getBody());

                            flag = true;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (flag) {
                            System.out.println("BYE FROM CLIENT");
                            System.exit(0);

                        }
                    }
                });
            }
        });
	}

	public static void userInit(ArrayList<Object> userData){

		user = new global.User((String)userData.get(0), (String)userData.get(1), (String)userData.get(2));

		try {
			Message welcome;
			welcome = user.welcomeMessage();
			welcome.setTo(client.chattingWith);
			welcome.setFrom(client.getUsername());
			toServer.writeUTF(welcome.toInfoString());
			toServer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendMessage(){
		try {

			String text = client.getMessage().trim();

			Message message = new Message(client.getUsername(),client.chattingWith,text,0);

			if (message.getMode()==0 && !text.equals("")) {
				toServer.writeUTF(message.toInfoString());
				toServer.flush();
			}


		}
		catch (IOException ex) {
			System.err.println(ex);
		}
	}

	public static void newGM(String gmUsers){
		String newGMusers=user.getUsername()+"+"+gmUsers;
		Message newGMmsg=new Message("","",newGMusers,3);
		try {
			toServer.writeUTF(newGMmsg.toInfoString());
			toServer.flush();
		}catch(Exception e){System.out.println("");}
	}

	public static void updateGMs(String gm){
		if(!chatRooms.contains(gm)) {
			chatRooms.add(gm);
			client.updateGMs(chatRooms);
		}
	}


	public static void updateUsers(){
		try {
			Message updateMsg = new Message(user.getUsername(), chattingWith, "", 1);
			toServer.writeUTF(updateMsg.toInfoString());
			toServer.flush();
		}catch(Exception e){e.printStackTrace();}
	}

	public static void updateLocalUsers(){
		client.updateLocalUsers(activeUsers);
	}

	public static void updateChattingWith(String s){chattingWith=s;}


	public static void main(String[] args) {
		launch(args);
	}
}