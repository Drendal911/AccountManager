package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AddCheckWithdrawalController {
    @FXML Button cancelAddCheckWithdrawalButton;

    public void setCancelAddCheckWithdrawalButton(ActionEvent ev) {
        Parent sceneViewParent = null;
        try {
            sceneViewParent = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sceneViewParent != null;
        Scene sceneViewScene = new Scene(sceneViewParent);
        Stage window = (Stage) ((Node) ev.getSource()).getScene().getWindow();
        window.setScene(sceneViewScene);
        window.show();
        sceneViewParent.requestFocus();
    }
}
