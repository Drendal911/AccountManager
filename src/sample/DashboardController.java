package sample;

import DBClasses.DBHelper;
import DBClasses.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML Label wTotalLabel, dTotalLabel;
    @FXML TableView<String> wTableView, dTableView;
    @FXML TableColumn wNumTypeColumn, wPayeeReasonColumn, wDateColumn, wAmtColumn, dNumTypeColumn, dPayerMemoColumn,
            dDateColumn, dAmtColumn;
    @FXML ChoiceBox<String> columnChoiceBox, tableChoiceBox;
    @FXML Button searchButton;
    @FXML RadioButton checkingRadioButton, savingsRadioButton;
    private ObservableList<ListItem> wObservableList = FXCollections.observableArrayList();
    private ObservableList<ListItem> dObservableList = FXCollections.observableArrayList();


    private class ListItem {
        String numType, payeeReason;
        java.sql.Date date;
        int amt;

        public ListItem(String numType, String payeeReason, java.sql.Date date, int amt) {
            this.numType = numType;
            this.payeeReason = payeeReason;
            this.date = date;
            this.amt = amt;
        }
        public String getNumType() {
            return numType;
        }
        public void setNumType(String numType) {
            this.numType = numType;
        }
        public String getPayeeReason() {
            return payeeReason;
        }
        public void setPayeeReason(String payeeReason) {
            this.payeeReason = payeeReason;
        }
        public java.sql.Date getDate() {
            return date;
        }
        public void setDate(java.sql.Date date) {
            this.date = date;
        }
        public int getAmt() {
            return amt;
        }
        public void setAmt(int amt) {
            this.amt = amt;
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        wObservableList.clear();
        dObservableList.clear();
        columnChoiceBox.getItems().add("Num/Type");
        columnChoiceBox.getItems().add("Payee/Reason");
        columnChoiceBox.getItems().add("Date");
        columnChoiceBox.getItems().add("Amount");

        getTransactions("withdrawal");
        getTransactions("deposit");

    }


    /* METHODS *********************************************************************************************************
    ********************************************************************************************************************
    *******************************************************************************************************************/


    private void getTransactions(String table) {
        DBHelper db = new DBHelper();
        ResultSet rs;

        try {
            String query = "select * from check_" + table;
            Statement stmt = db.makeConnection().createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String checkNum = rs.getString(7);
                String payee = rs.getString(8);
                Date date = rs.getDate(6);
                int amt = rs.getInt(3);
                ListItem listItem = new ListItem(checkNum, payee, date, amt);
                if (table.equals("withdrawal")) {
                    wObservableList.add(listItem);
                }else if (table.equals("deposit")) {
                    dObservableList.add(listItem);
                }
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String query = "select * from non_check_" + table;
            Statement stmt = db.makeConnection().createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String wdtype = rs.getString(7);
                String reason = rs.getString(8);
                Date date = rs.getDate(6);
                int amt = rs.getInt(3);
                ListItem listItem = new ListItem(wdtype, reason, date, amt);
                if (table.equals("withdrawal")) {
                    wObservableList.add(listItem);
                }else if (table.equals("deposit")) {
                    dObservableList.add(listItem);
                }
            }
            rs.close();
            stmt.close();
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
