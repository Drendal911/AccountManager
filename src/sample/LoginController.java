package sample;

import DBClasses.DBHelper;
import Utility.DialogBox;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    DBHelper db = new DBHelper();
    DialogBox dialogBox = new DialogBox();
    @FXML
    private TextField userNameTextField, passwordTextField;
    private Button loginButton, registerButton;




    /* METHODS *********************************************************************************************************
    ********************************************************************************************************************
    *******************************************************************************************************************/


    @FXML private void setLoginButton() throws SQLException {
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
        }catch (SQLException e) {
            dialogBox.infoAlertDialog("USER NOT FOUND", "Please check the 'Username' field or click " +
                    "the 'Register' button to create a new account.");
        }


    }
}
