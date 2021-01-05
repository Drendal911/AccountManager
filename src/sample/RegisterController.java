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
import java.sql.*;

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

    @FXML private void setRegisterButton(ActionEvent ev) {
        DBHelper db = new DBHelper();
        DialogBox dialogBox = new DialogBox();
        String username = registerUsernameTextField.getText();
        String password = registerPasswordTextField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            dialogBox.infoAlertDialog("Incomplete Fields", "Please complete both 'Username' and " +
                    "'Password' fields, then click the 'Register' button.");
        }else {
            try {
                //Check db to see if the username is already in use.
                Connection con = db.makeConnection();
                String query = "select * from users where UserName = ?";
                PreparedStatement pStmt = con.prepareStatement(query);
                pStmt.setString(1, username);
                ResultSet rs = pStmt.executeQuery();
                if (rs.next()) {
                    dialogBox.infoAlertDialog("Invalid Username",
                            "Username already in use. Please enter a unique username and try again.");
                    con.close();
                }else {
                    query = "insert into users values (?, ?)";
                    pStmt = con.prepareStatement(query);
                    pStmt.setString(1, username);
                    pStmt.setString(2, password);
                    dialogBox.infoAlertDialog("Account Registered", "Operation complete, " +
                            "account successfully registered");
                    con.close();
                    try {
                        Parent sceneViewParent = FXMLLoader.load(getClass().getResource("login.fxml"));
                        Scene sceneViewScene = new Scene(sceneViewParent);
                        Stage window = (Stage)((Node) ev.getSource()).getScene().getWindow();
                        window.setScene(sceneViewScene);
                        window.show();
                        sceneViewParent.requestFocus();
                    }catch (IOException ex) {
                        System.out.println("Error! " + ex.getMessage());
                        System.out.println(ex.getClass());
                    }
                }
            }catch (Exception ex) {
                System.out.println("Error! " + ex.getMessage());
                System.out.println(ex.getClass());
            }
        }
    }
}
