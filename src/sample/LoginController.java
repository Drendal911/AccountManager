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
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    DBHelper db = new DBHelper();
    DialogBox dialogBox = new DialogBox();
    @FXML
    private TextField userNameTextField, passwordTextField;




    /* METHODS *********************************************************************************************************
    ********************************************************************************************************************
    *******************************************************************************************************************/


    @FXML private void setLoginButton(ActionEvent e) {
        String username = userNameTextField.getText();
        String password = passwordTextField.getText();

        try {
            db.makeQuery("select * from users where UserName = '" + username + "'");
            ResultSet resultSet = db.getResult();
            resultSet.first();
            String pw = resultSet.getString("Password");
            resultSet.close();

            if (password.equals(pw)) {
                System.out.println("Match!");
            }else {
                System.out.println("No Match!");
            }
        }catch (SQLException ex) {
            dialogBox.infoAlertDialog("USER NOT FOUND", "Please check the 'Username' field, " +
                    "username's are case sensitive. Click the 'Register' button to create a new account.");
        }
    }

    @FXML private void setRegisterButton(ActionEvent e) throws IOException {
            Parent sceneViewParent = FXMLLoader.load(getClass().getResource("CalendarView.fxml"));
            Scene sceneViewScene = new Scene(sceneViewParent);
            Stage window = (Stage)((Node) e.getSource()).getScene().getWindow();
            window.setScene(sceneViewScene);
            window.show();
    }
}
