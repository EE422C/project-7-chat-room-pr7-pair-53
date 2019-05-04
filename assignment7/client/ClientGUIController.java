package client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
    @FXML TextField birth;

    //boolean typingNoteSent=false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        window.setDisable(true);
    }

    public void send(){
System.out.println("sent");
displayMessage(send_text.getText());
send_text.clear();
    //typingNoteSent=false;
        yMessageClient.sendMessage();
    }

//    public void typing(){
//        if(!typingNoteSent)
//            displayMessage("user is typing");
//        typingNoteSent=true;
//    }


    public String getMessage(){
        return send_text.getText();
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
        String[] DOB=birth.getText().split("/");
        data.add(Integer.parseInt(DOB[2]));
        data.add(Integer.parseInt(DOB[0]));
        data.add(Integer.parseInt(DOB[1]));
        window.setDisable(false);
        yMessageClient.userInit(data);
    }



}
