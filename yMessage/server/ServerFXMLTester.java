package server;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class ServerFXMLTester extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("ServerGUI.fxml"));
        primaryStage.setTitle("yMessage Server");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}
