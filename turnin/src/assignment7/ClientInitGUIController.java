/*
 * CHATROOM ClientGUIInitController.java
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

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.*;

import static client.ClientMain.*;

public class ClientInitGUIController implements Initializable {

    @FXML Button connect;
    @FXML TextField host;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setHost() throws Exception{
        setHost2(host.getText());
    }

    public static void setHost2(String host) throws Exception{
        ClientMain.startClient(host);
    }


}