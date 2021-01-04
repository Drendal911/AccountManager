package sample;

import DBClasses.DBHelper;
import Utility.DialogBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML TextField userNameTextField;
    @FXML PasswordField passwordField;

    @FXML private void setLoginButton(ActionEvent e) {
        DBHelper db = new DBHelper();
        DialogBox dialogBox = new DialogBox();
        String username = userNameTextField.getText();
        String password = passwordField.getText();

        try {
            Connection con = db.makeConnection();
            String query = "select * from users where UserName = ?";
            PreparedStatement pStmt = con.prepareStatement(query);
            pStmt.setString(1, username);

            ResultSet resultSet = pStmt.executeQuery();
            resultSet.first();
            String pw = resultSet.getString("Password");
            resultSet.close();

            if (password.equals(pw)) {
                Parent sceneViewParent = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
                Scene sceneViewScene = new Scene(sceneViewParent);
                Stage window = (Stage)((Node) e.getSource()).getScene().getWindow();
                window.setScene(sceneViewScene);
                window.show();
                sceneViewParent.requestFocus();
                con.close();
            }else {
                dialogBox.infoAlertDialog("Password Incorrect",
                        "The password you entered is incorrect. Please check the password and try again");
                con.close();
            }
        }catch (SQLException ex) {
            dialogBox.infoAlertDialog("USER NOT FOUND", "Please check the 'Username' field, " +
                    "username's are case sensitive. Click the 'Register' button to create a new account.");
        } catch (Exception classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }

    @FXML private void setRegisterButton(ActionEvent e) throws IOException {
        Parent sceneViewParent = FXMLLoader.load(getClass().getResource("register.fxml"));
        Scene sceneViewScene = new Scene(sceneViewParent);
        Stage window = (Stage)((Node) e.getSource()).getScene().getWindow();
        window.setScene(sceneViewScene);
        window.show();
        sceneViewParent.requestFocus();
    }
}
