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
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegisterController {
    @FXML
    TextField registerUsernameTextField, registerPasswordTextField, caBalanceTextField, saBalanceTextField;
    private BigDecimal cAcctBalance, sAcctBalance;
    private String username, password;
    private int uID;

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
        username = registerUsernameTextField.getText();
        password = registerPasswordTextField.getText();

        if (username.isEmpty() || password.isEmpty() || caBalanceTextField.getText().equals("") ||
                saBalanceTextField.getText().equals("")) {
            dialogBox.infoAlertDialog("Incomplete Fields", "Please complete 'Username' and " +
                    "'Password' fields. Enter a starting balance for checking and savings accounts. Then, " +
                    "click the 'Register' button.");
        }else {
            cAcctBalance = new BigDecimal(caBalanceTextField.getText());
            sAcctBalance = new BigDecimal(saBalanceTextField.getText());
            try {
                //Check db to see if the username is already in use.
                String query = "select * from users where UserName = ?";
                PreparedStatement pStmt = db.makeConnection().prepareStatement(query);
                pStmt.setString(1, username);
                ResultSet rs = pStmt.executeQuery();
                if (rs.next()) {
                    dialogBox.infoAlertDialog("Invalid Username",
                            "Username already in use. Please enter a unique username and try again.");
                    rs.close();
                    pStmt.close();
                }else {
                    insertUser();
                    getUserID();
                    insertStartingBalances();

                    dialogBox.infoAlertDialog("Account Registered", "Operation complete, " +
                            "account successfully registered");
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

    private void insertUser() {
        DBHelper db = new DBHelper();
        try {
            String query = "insert into users (UserName, Password) values (?, ?)";
            PreparedStatement pStmt = db.makeConnection().prepareStatement(query);
            pStmt.setString(1, username);
            pStmt.setString(2, password);
            pStmt.executeUpdate();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getUserID() {
        DBHelper db = new DBHelper();
        //Get the newly registered user's userID
        String query = "select * from users";
        try {
            Statement stmt = db.makeConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.last();
            uID = rs.getInt("UserID");
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertStartingBalances() {
        DBHelper db = new DBHelper();

        try {
            if (cAcctBalance != null && sAcctBalance != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                String dateNow = sdf.format(cal.getTime());

                //Insert starting balances into checking and savings tables
                String query = "insert into checking (userID, amt, transactionType, transactionID, date, total) " +
                        "values (?, ?, ?, ?, ?, ?)";
                PreparedStatement pStmt = db.makeConnection().prepareStatement(query);
                pStmt.setInt(1, uID);
                pStmt.setDouble(2, 0.00);
                pStmt.setString(3, "Starting Balance");
                pStmt.setInt(4, 0);
                pStmt.setString(5, dateNow);
                pStmt.setBigDecimal(6, cAcctBalance);
                pStmt.executeUpdate();
                pStmt.close();
                query = "insert into savings (userID, amt, transactionType, transactionID, date, total)" +
                        " values (?, ?, ?, ?, ?, ?)";
                pStmt = db.makeConnection().prepareStatement(query);
                pStmt.setInt(1, uID);
                pStmt.setDouble(2, 0.00);
                pStmt.setString(3, "Starting Balance");
                pStmt.setInt(4, 0);
                pStmt.setString(5, dateNow);
                pStmt.setBigDecimal(6, sAcctBalance);
                pStmt.executeUpdate();
                pStmt.close();
            }else {
                DialogBox dialogBox = new DialogBox();
                dialogBox.infoAlertDialog("Missing Information", "Please complete all fields.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
