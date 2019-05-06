package client;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
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
    @FXML ListView active_users;

    String chattingWith="Broadcast";
    Map<String,String> chatHistory=new HashMap<>();
    Map<String,String> activeUsers=new HashMap<>();

    //boolean typingNoteSent=false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //window.setDisable(true);
        room_sel.setItems(FXCollections.observableArrayList("Broadcast","One Room","Two Room","Red Room","Blue Room"));
        ObservableList<String> users= FXCollections.observableArrayList("Guy","Dylan","Chad","Brad");
        DMs.setItems(users);
    }

    public void send(){
//displayMessage(send_text.getText());
        ClientMain.sendMessage();
send_text.clear();
    //typingNoteSent=false;
        updateUsers();
        sendMessage();
    }

//    public void typing(){
//        if(!typingNoteSent)
//            displayMessage("user is typing");
//        typingNoteSent=true;
//    }


    public String getMessage(){
        return send_text.getText();
    }

    public String getUsername(){
        return username.getText();
    }

    public void displayMessage(String msg){
        message_window.appendText(msg+"\n");
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
        }
        updateUsers();
    }

<<<<<<< HEAD
    public void updateLocalUsers(Map<String,String> m){
        activeUsers=m;
        Set<String> users=activeUsers.keySet();
        DMs.setItems(FXCollections.observableArrayList(users));
        ArrayList<String> userList=new ArrayList<>();
        for(String usr:activeUsers.keySet()){
            if(activeUsers.get(usr)==chattingWith)
                userList.add(usr);
        }
        System.out.println(userList);
        active_users.setItems(FXCollections.observableArrayList(users));

    }
=======
>>>>>>> parent of d7a2309... Group messages work

}
