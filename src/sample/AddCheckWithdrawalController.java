package sample;

import DBClasses.DBHelper;
import Utility.DialogBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import static sample.LoginController.userID;

public class AddCheckWithdrawalController implements Initializable {
    @FXML Button cancelAddCheckWithdrawalButton, cwAddButton;
    @FXML TextField cwAmt, cwPayee, cwCheckNum;
    @FXML ComboBox<String> cwAcct;
    @FXML DatePicker cwDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cwAcct.getItems().addAll("Checking", "Savings");
    }


    /* METHODS *********************************************************************************************************
    ********************************************************************************************************************
    *******************************************************************************************************************/


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

    public void setAddButton(ActionEvent ev) {
        DBHelper db = new DBHelper();
        String totalQuery = null;
        String total = null;
        String date = null;
        String acct = null;
        String checkNum = cwCheckNum.getText();
        String amount = cwAmt.getText();
        String payee = cwPayee.getText();
        String query = "insert into check_withdrawal (userID, amt, oldtotal, date, checkNum, payee, acct) values (?,?,?,?,?,?,?)";

        try {
            date = getDatePickerValue();
            acct = cwAcct.getSelectionModel().getSelectedItem().toLowerCase();
            if (acct.equals("") || checkNum.equals("") || amount.equals("") || payee.equals("") || date == null) {
                DialogBox dialogBox = new DialogBox();
                dialogBox.infoAlertDialog("Missing Information", "Please complete all fields.");
            }else {
                totalQuery = "select * from " + acct + " where userID = " + userID;
                try {
                    Statement stmt = db.makeConnection().createStatement();
                    ResultSet rs = stmt.executeQuery(totalQuery);
                    rs.last();
                    total = rs.getBigDecimal("total").toString();
                    rs.close();
                    stmt.close();
                }catch (Exception e) {System.out.println(e.getMessage());}
                try {
                    PreparedStatement pStmt = db.makeConnection().prepareStatement(query);
                    pStmt.setInt(1, userID);
                    pStmt.setString(2, cwAmt.getText());
                    pStmt.setString(3, total);
                    pStmt.setString(4, date);
                    pStmt.setString(5, checkNum);
                    pStmt.setString(6, payee);
                    pStmt.setString(7, acct);
                    pStmt.executeUpdate();
                    DialogBox dialogBox = new DialogBox();
                    dialogBox.infoAlertDialog("Transaction Successful", "Your transaction has been saved " +
                            "to the database.");

                    Parent sceneViewParent = FXMLLoader.load(getClass().getResource("DashboardController.fxml"));
                    Scene sceneViewScene = new Scene(sceneViewParent);
                    Stage window = (Stage) ((Node) ev.getSource()).getScene().getWindow();
                    window.setScene(sceneViewScene);
                    window.show();
                    sceneViewParent.requestFocus();
                }catch (Exception e) {System.out.println(e.getMessage());}
            }
        }catch (NullPointerException e) {
            DialogBox dialogBox = new DialogBox();
            dialogBox.infoAlertDialog("Missing Information", "Please complete all fields.");
        }

    }



    public String getDatePickerValue() {
        return cwDate.getValue().toString();
    }

    public String getAcctValue() {
        return cwAcct.getSelectionModel().getSelectedItem().toLowerCase();
    }


}
