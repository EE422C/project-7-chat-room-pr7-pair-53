/*
 * CHATROOM ServerGUIController.java
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

package server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class ServerGUIController implements Initializable{

    @FXML ImageView background;
    @FXML Button stats_button;
    @FXML ScrollPane server_window;
    @FXML TextArea server_stats;
    private boolean server_window_status =false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        server_stats.appendText("Hello, Welcome to the Multithread Server\n");

    }

    public void postToServer(String s){
        server_stats.appendText(s+"\n");
    }

        public void showStats(){
        server_window_status =!server_window_status;
        if (server_window_status) {
            background.setOpacity(1);
            server_window.setOpacity(.60);
            stats_button.setText("Hide Stats for Nerds");
        }
        else {
            background.setOpacity(0);
            server_window.setOpacity(0);
            stats_button.setText("Show Stats for Nerds");
        }
    }


}
