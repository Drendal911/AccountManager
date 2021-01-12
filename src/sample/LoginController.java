package sample;

import DBClasses.DBHelper;
import Utility.DialogBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML TextField userNameTextField;
    @FXML PasswordField passwordField;
    public static int userID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Attempts to log the user in when the enter key is pressed while passwordField is focused
        passwordField.setOnKeyPressed(new EventHandler<>() {
            @Override
            public void handle(KeyEvent e) {
                if (e.getCode().equals(KeyCode.ENTER)) {
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
                        userID = resultSet.getInt("userID");
                        pStmt.close();
                        resultSet.close();

                        if (password.equals(pw)) {
                            Parent sceneViewParent = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
                            Scene sceneViewScene = new Scene(sceneViewParent);
                            Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
                            window.setScene(sceneViewScene);
                            window.show();
                            sceneViewParent.requestFocus();
                        } else {
                            dialogBox.infoAlertDialog("Password Incorrect",
                                    "The password you entered is incorrect. Please check the password and try again");
                        }
                    } catch (SQLException ex) {
                        dialogBox.infoAlertDialog("USER NOT FOUND", "Please check the 'Username' field, " +
                                "username's are case sensitive. Click the 'Register' button to create a new account.");
                    } catch (Exception classNotFoundException) {
                        classNotFoundException.printStackTrace();
                    }
                }
            }
        });
    }


    /* METHODS *********************************************************************************************************
     ********************************************************************************************************************
     *******************************************************************************************************************/


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
            userID = resultSet.getInt("userID");
            pStmt.close();
            resultSet.close();

            if (password.equals(pw)) {
                Parent sceneViewParent = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
                Scene sceneViewScene = new Scene(sceneViewParent);
                Stage window = (Stage)((Node) e.getSource()).getScene().getWindow();
                window.setScene(sceneViewScene);
                window.show();
                sceneViewParent.requestFocus();
            }else {
                dialogBox.infoAlertDialog("Password Incorrect",
                        "The password you entered is incorrect. Please check the password and try again");
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
