package client;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.*;

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
    @FXML TextField birth;
    @FXML ListView DMs;
    @FXML ListView room_sel;

    String chattingWith="Server-wide";

    //boolean typingNoteSent=false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //window.setDisable(true);
        room_sel.setItems(FXCollections.observableArrayList("Server-wide","One Room","Two Room","Red Room","Blue Room"));
        ObservableList<String> users= FXCollections.observableArrayList("Guy","Dylan","Chad","Brad");
        DMs.setItems(users);
    }

    public void send(){
System.out.println("sent");
//displayMessage(send_text.getText());
        ClientMain.sendMessage();
send_text.clear();
    //typingNoteSent=false;
        //ClientMain.sendMessage();
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
        if(fname.getText().equals("")||lname.getText().equals("")||birth.getText().equals(""))
            return;
        ArrayList<Object> data=new ArrayList<Object>();
        data.add(fname.getText());
        data.add(lname.getText());
        data.add(username.getText());
        String[] DOB=birth.getText().split("/");
        data.add(Integer.parseInt(DOB[2]));
        data.add(Integer.parseInt(DOB[0]));
        data.add(Integer.parseInt(DOB[1]));
        window.setDisable(false);
        ClientMain.userInit(data);
    }

    public void selectRoom(){
        String newRoom=room_sel.getSelectionModel().getSelectedItem().toString();
        if(!chattingWith.equals(newRoom)) {
            System.out.println(newRoom);
            chattingWith = newRoom;
        }
    }

    public void selectDM(){
        String newRoom=DMs.getSelectionModel().getSelectedItem().toString();
        if(!chattingWith.equals(newRoom)) {
            System.out.println(newRoom);
            chattingWith = newRoom;
        }
    }


}
