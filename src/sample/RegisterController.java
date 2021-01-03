package sample;

import DBClasses.DBHelper;
import Utility.DialogBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {
    @FXML
    TextField registerUsernameTextField, registerPasswordTextField;


    @FXML private void setCancelButton(ActionEvent ev) throws IOException {
        Parent sceneViewParent = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene sceneViewScene = new Scene(sceneViewParent);
        Stage window = (Stage)((Node) ev.getSource()).getScene().getWindow();
        window.setScene(sceneViewScene);
        window.show();
        sceneViewParent.requestFocus();
    }

    @FXML private void setRegisterButton(ActionEvent ev) throws IOException, SQLException {
        DialogBox dialogBox = new DialogBox();
        String username = registerUsernameTextField.getText();
        String password = registerPasswordTextField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            dialogBox.infoAlertDialog("Incomplete Fields", "Please complete both 'Username' and " +
                    "'Password' fields, then click the 'Register' button.");
        }else {
            DBHelper db = new DBHelper();
            db.makeQuery("insert into users values (null, '" + username + "', '" + password + "');");
            dialogBox.infoAlertDialog("Account Registered", "Operation complete, " +
                    "account successfully registered");

            Parent sceneViewParent = FXMLLoader.load(getClass().getResource("login.fxml"));   //change the resource here so it takes you into the app instead of having to login again
            Scene sceneViewScene = new Scene(sceneViewParent);
            Stage window = (Stage)((Node) ev.getSource()).getScene().getWindow();
            window.setScene(sceneViewScene);
            window.show();
            sceneViewParent.requestFocus();
        }


    }
}
