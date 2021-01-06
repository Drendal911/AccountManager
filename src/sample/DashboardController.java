package sample;

import DBClasses.DBHelper;
import Utility.DialogBox;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML Label wTotalLabel, dTotalLabel;
    @FXML TableView<String> wTableView, dTableView;
    @FXML TableColumn wNumTypeColumn, wPayeeReasonColumn, wDateColumn, wAmtColumn, dNumTypeColumn, dPayerMemoColumn,
            dDateColumn, dAmtColumn;
    @FXML ChoiceBox<String> searchColumnChoiceBox, searchTableChoiceBox, addTableItemChoiceBox;
    @FXML Button searchButton, addTransactionButton;
    @FXML RadioButton checkingRadioButton, savingsRadioButton;
    @FXML TextField searchTextField;
    private ObservableList<ListItem> wObservableList = FXCollections.observableArrayList();
    private ObservableList<ListItem> dObservableList = FXCollections.observableArrayList();
    private ObservableList<String> columnChoiceBoxList = FXCollections.observableArrayList();
    private ToggleGroup toggleGroup;
    public static String account;

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

        toggleGroup = new ToggleGroup();
        checkingRadioButton.setToggleGroup(toggleGroup);
        savingsRadioButton.setToggleGroup(toggleGroup);
        checkingRadioButton.setSelected(true);
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
                if (table.equals("withdrawal")) {wObservableList.add(listItem);
                }else if (table.equals("deposit")) {dObservableList.add(listItem);
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
                if (table.equals("withdrawal")) {wObservableList.add(listItem);
                }else if (table.equals("deposit")) {dObservableList.add(listItem);}
            }
            rs.close();
            stmt.close();
        }catch (SQLException | ClassNotFoundException e) {e.printStackTrace();
        }catch (Exception e) {e.printStackTrace();}
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

    public void setAddTransactionButton(ActionEvent ev) {
        try {
            String strTable = addTableItemChoiceBox.getSelectionModel().getSelectedItem();
            switch (strTable) {
                case "Check Withdrawal" -> {
                    Parent sceneViewParent = FXMLLoader.load(getClass().getResource("addCheckWithdrawal.fxml"));
                    Scene sceneViewScene = new Scene(sceneViewParent);
                    Stage window = (Stage) ((Node) ev.getSource()).getScene().getWindow();
                    window.setScene(sceneViewScene);
                    window.show();
                    sceneViewParent.requestFocus();
                }
                case "Non-Check Withdrawal" -> {
                    Parent sceneViewParent = FXMLLoader.load(getClass().getResource("addNonCheckWithdrawal.fxml"));
                    Scene sceneViewScene = new Scene(sceneViewParent);
                    Stage window = (Stage) ((Node) ev.getSource()).getScene().getWindow();
                    window.setScene(sceneViewScene);
                    window.show();
                    sceneViewParent.requestFocus();
                }
                case "Check Deposit" -> {
                    Parent sceneViewParent = FXMLLoader.load(getClass().getResource("addCheckDeposit.fxml"));
                    Scene sceneViewScene = new Scene(sceneViewParent);
                    Stage window = (Stage) ((Node) ev.getSource()).getScene().getWindow();
                    window.setScene(sceneViewScene);
                    window.show();
                    sceneViewParent.requestFocus();
                }
                case "Non-Check Deposit" -> {
                    Parent sceneViewParent = FXMLLoader.load(getClass().getResource("addNonCheckDeposit.fxml"));
                    Scene sceneViewScene = new Scene(sceneViewParent);
                    Stage window = (Stage) ((Node) ev.getSource()).getScene().getWindow();
                    window.setScene(sceneViewScene);
                    window.show();
                    sceneViewParent.requestFocus();
                }
            }
        }catch (NullPointerException ex) {
            DialogBox dialogBox = new DialogBox();
            dialogBox.infoAlertDialog("Missing Information", "Please select the table where you " +
                    "want to add a transaction.");
        }catch (IOException ex) {
            System.out.println("Error! " + ex.getMessage());
            System.out.println(ex.getClass());
        }
    }

    public void setToggleGroup(ActionEvent e) {
        toggleGroup.setUserData(toggleGroup.getSelectedToggle().toString());
        String string = toggleGroup.getUserData().toString();
        if (string.contains("Savings")) {
            account = "Savings";
        }else if (string.contains("Checking")) {
            account = "Checking";
        }
    }

    public void setSearchButton() {
        //String table;
        String column;
        String strTable = searchTableChoiceBox.getSelectionModel().getSelectedItem();
        String strColumn = searchColumnChoiceBox.getSelectionModel().getSelectedItem();
        String strTextField = searchTextField.getText();

        if (strTable == null || strColumn == null) {
            DialogBox dialogBox = new DialogBox();
            dialogBox.infoAlertDialog("Missing Information", "Please select both a table and " +
                    "column to search for.");
        }

        assert strTable != null;
        String table = switch (strTable) {
            case "Check Withdrawal" -> table = "check_withdrawal";
            case "Non-Check Withdrawal" -> table = "non_check_withdrawal";
            case "Check Deposit" -> table = "check_deposit";
            case "Non-Check Deposit" -> table = "non_check_deposit";
            default -> throw new IllegalStateException("Unexpected value: " + strTable);
        };

        if (strTable.contains("Check Withdrawal") || strTable.contains("Non-Check Withdrawal")){
            switch (Objects.requireNonNull(strColumn)) {
                case "Check Number" -> column = "checkNum";
                case "Payee" -> column = "payee";
                case "Date" -> column = "date";
                case "Amount" -> column = "amt";
                case "Type" -> column = "withdrawalType";
                case "Reason" -> column = "withdrawalReason";
            }
        }
        if (strTable.contains("Check Deposit") || strTable.contains("Non-Check Deposit")) {
            switch (strTable) {
                case "Check Number" -> column = "checkNum";
                case "Date" -> column = "date";
                case "Amount" -> column = "amt";
                case "Payer" -> column = "payer";
                case "Memo" -> column = "depositMemo";
                case "Type" -> column = "depositType";
            }
        }

        System.out.println(strTable);
        System.out.println(table);
        System.out.println(column);


        if (strTextField.equals("")) {
            DBHelper db = new DBHelper();
            try {
                //Statement stmt = db.makeConnection().createStatement();
                //stmt.executeQuery("select * from " + table + " where ");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }



}