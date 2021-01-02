package sample;

import DBClasses.DBHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    DBHelper db = new DBHelper();
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
            String pw = db.getResult().getString("Password");
            if (password.equals(pw)) {
                System.out.println("Match!");
            }else {
                System.out.println("No Match!");
            }
        }catch (SQLException e) {
            throw e;
        }

    }
}
