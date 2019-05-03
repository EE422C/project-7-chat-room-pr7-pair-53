package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientFXMLTester extends Application {


    FXMLLoader loader = new FXMLLoader();
    ClientGUIController client;

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Create a scene and place it in the stage
        loader.setLocation(getClass().getResource("ClientGUI.fxml"));
        Parent root = loader.load();
        client=loader.getController();
        primaryStage.setTitle("yMessage");
        primaryStage.setScene(new Scene(root));
        primaryStage.show(); // Display the stage
    }

}
