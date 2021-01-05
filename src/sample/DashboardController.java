package sample;

import DBClasses.DBHelper;
import Utility.DialogBox;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML Label wTotalLabel, dTotalLabel;
    @FXML TableView<String> wTableView, dTableView;
    @FXML TableColumn wNumTypeColumn, wPayeeReasonColumn, wDateColumn, wAmtColumn, dNumTypeColumn, dPayerMemoColumn,
            dDateColumn, dAmtColumn;
    @FXML ChoiceBox<String> searchColumnChoiceBox, searchTableChoiceBox, addTableItemChoiceBox;
    @FXML Button searchButton, addTransactionButton;
    @FXML RadioButton checkingRadioButton, savingsRadioButton;
    private ObservableList<ListItem> wObservableList = FXCollections.observableArrayList();
    private ObservableList<ListItem> dObservableList = FXCollections.observableArrayList();
    private ObservableList<String> columnChoiceBoxList = FXCollections.observableArrayList();

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
        //searchColumnChoiceBox.getItems().addAll("Num/Type", "Payee/Reason", "Date", "Amount");
        searchTableChoiceBox.getItems().addAll("Check Withdrawal", "Non-Check Withdrawal", "Check Deposit",
                "Non-Check Deposit");
        addTableItemChoiceBox.getItems().addAll("Check Withdrawal", "Non-Check Withdrawal", "Check Deposit",
                "Non-Check Deposit");
        getTransactions("withdrawal");
        getTransactions("deposit");

        //Set listener for addTableItemChoiceBox and adjusts the available selections for addColumnItemChoiceBox
        searchTableChoiceBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<?
                extends String> observable, String oldValue, String newValue) -> setSearchColumnItemChoiceBox() );
    }


    /* METHODS *********************************************************************************************************
    ********************************************************************************************************************
    *******************************************************************************************************************/


    private void getTransactions(String table) {
        DBHelper db = new DBHelper();
        ResultSet rs;
        if (wObservableList != null) {wObservableList.clear();}
        if (dObservableList != null) {dObservableList.clear();}

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
            db.closeConnection();
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
            db.closeConnection();
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSearchColumnItemChoiceBox() {
        if (columnChoiceBoxList != null) {
            columnChoiceBoxList.clear();
        }
        String string = searchTableChoiceBox.getSelectionModel().getSelectedItem();
        switch (string) {
            case "Check Withdrawal" -> columnChoiceBoxList.addAll("Check Number", "Payee", "Date", "Amount");
            case "Non-Check Withdrawal" -> columnChoiceBoxList.addAll("Type", "Reason", "Date", "Amount");
            case "Check Deposit" -> columnChoiceBoxList.addAll("Check Number", "Payer", "Date", "Amount");
            case "Non-Check Deposit" -> columnChoiceBoxList.addAll("Type", "Memo", "Date", "Amount");
        }
        searchColumnChoiceBox.setItems(columnChoiceBoxList);
    }

    

}