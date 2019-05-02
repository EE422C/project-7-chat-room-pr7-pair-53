package server;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerGUIController implements Initializable {

    @FXML
    ScrollPane server_stats;
    private boolean server_stats_status=false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {


    }

    public void showStats(){
        server_stats_status=!server_stats_status;
        if (server_stats_status) {
            server_stats.setOpacity(1);
        }
        else
            server_stats.setOpacity(0);
    }

    public ScrollPane getServerStats(){
        return server_stats;
    }

}
