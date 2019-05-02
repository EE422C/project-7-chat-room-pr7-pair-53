package server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
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

    @FXML
    ScrollPane server_window;
    @FXML
    TextArea server_stats;
    private boolean server_window_status =false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        server_stats.appendText("Hello, Welcome to the Multithread Server\n");

    }

    public void postToServer(String s){
        server_stats.appendText(s);
    }

        public void showStats(){
        server_window_status =!server_window_status;
        if (server_window_status) {
            server_window.setOpacity(1);
        }
        else
            server_window.setOpacity(0);
    }


}
