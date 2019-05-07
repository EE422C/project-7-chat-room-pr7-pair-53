/*
 * CHATROOM ClientGUIController.java
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

import com.sun.tools.javac.comp.Check;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.security.Key;
import java.util.*;

import static client.ClientMain.*;

public class ClientGUIController implements Initializable {

    URL activeURL= ClientGUIController.class.getResource("client/ClientGraphics/send_active.png");
    URL pressedURL= ClientGUIController.class.getResource("client/ClientGraphics/send_pressed.png");
    URL inactiveURL= ClientGUIController.class.getResource("client/ClientGraphics/send_inactive.png");

    @FXML HBox window;
    @FXML ImageView send_button;
    @FXML TextField send_text;
    @FXML TextArea message_window;
    @FXML TextField fname;
    @FXML TextField lname;
    @FXML TextField username;
    @FXML ListView DMs;
    @FXML ListView room_sel;
    @FXML Menu login_menu;
    @FXML Label chatting_with;
    @FXML FlowPane gm_sel;

    String chattingWith="Broadcast";
    Map<String,String> chatHistory=new HashMap<>();

    //boolean typingNoteSent=false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        window.setDisable(true);
    }

    public void send(){
//displayMessage(send_text.getText());
        ClientMain.sendMessage();
        send_text.clear();
        //typingNoteSent=false;
        updateUsers();
        sendMessage();
    }

    public void setRooms(ArrayList<String> rooms){
        room_sel.setItems(FXCollections.observableArrayList(rooms));
    }

    public String getMessage(){
        return send_text.getText();
    }

    public String getUsername(){
        return username.getText();
    }

    public void displayMessage(String msg){
        message_window.appendText(msg+"\n");
    }
    public void displayBackground(String to,String msg){
        String brd="";
        for(String k:chatHistory.keySet())
            if(k.equals(to))
                brd=chatHistory.get(k);
        brd=brd+(msg+"\n");
        chatHistory.put(to,brd);
    }

    public void login(){
        if(fname.getText().equals("")||lname.getText().equals("")||username.getText().equals(""))
            return;
        ArrayList<Object> data=new ArrayList<Object>();
        data.add(fname.getText());
        data.add(lname.getText());
        data.add(username.getText());
        window.setDisable(false);
        login_menu.hide();
        chatting_with.setText(chattingWith);
        userInit(data);
        updateUsers();
    }

    public void selectRoom(){
        String newRoom=room_sel.getSelectionModel().getSelectedItem().toString();
        chatHistory.put(chattingWith,message_window.getText());
        if(!chattingWith.equals(newRoom)) {
            System.out.println(newRoom);
            chattingWith = newRoom;
            message_window.setText(chatHistory.get(chattingWith));
            updateChattingWith(chattingWith);
            chatting_with.setText(chattingWith);
        }
        updateUsers();
    }

    public void selectDM(){
        String newRoom=DMs.getSelectionModel().getSelectedItem().toString();
        chatHistory.put(chattingWith,message_window.getText());
        if(!chattingWith.equals(newRoom)) {
            System.out.println(newRoom);
            chattingWith = newRoom;
            message_window.setText(chatHistory.get(chattingWith));
            updateChattingWith(chattingWith);
            chatting_with.setText(chattingWith);
        }
        updateUsers();
    }

    public void newGM(){
        ArrayList<String> gm_users=new ArrayList<>();
        for(CheckBox u:user_gms){
            if(u.isSelected())
                gm_users.add(u.getText());
            u.setSelected(false);
        }
        System.out.println(gm_users);
    }

    ArrayList<CheckBox> user_gms=new ArrayList<>();

    public void updateLocalUsers(List<String> users){
        user_gms.clear();
        for(String u:users){
            user_gms.add(new CheckBox(u));
        }
        gm_sel.getChildren().clear();
        gm_sel.getChildren().addAll(user_gms);
        DMs.getItems().clear();
        DMs.getItems().addAll(users);
    }

}