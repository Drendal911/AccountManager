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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML Label wTotalLabel, dTotalLabel;
    @FXML TableView<ListItem> wTableView, dTableView;
    @FXML TableColumn<ListItem, String> wNumTypeColumn, wPayeeReasonColumn, wDateColumn, dNumTypeColumn,
            dPayerMemoColumn, dDateColumn;
    @FXML TableColumn<ListItem, Integer > wAmtColumn, dAmtColumn;
    @FXML ChoiceBox<String> searchColumnChoiceBox, searchTableChoiceBox, addTableItemChoiceBox;
    @FXML Button searchButton, addTransactionButton;
    @FXML RadioButton checkingRadioButton, savingsRadioButton;
    @FXML TextField searchTextField;
    private ObservableList<ListItem> wObservableList = FXCollections.observableArrayList();
    private ObservableList<ListItem> dObservableList = FXCollections.observableArrayList();
    private ObservableList<String> columnChoiceBoxList = FXCollections.observableArrayList();
    private ToggleGroup toggleGroup;
    public static String account;

    public class ListItem {
        String numType, payeeReason, date;
        int amt;

        public ListItem(String numType, String payeeReason, String date, int amt) {
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
        public String getDate() {
            return date;
        }
        public void setDate(String date) {
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
        try{
            wObservableList.clear();
            dObservableList.clear();
        }catch (NullPointerException e){
            System.out.println(e.getMessage());
        }

        //searchColumnChoiceBox.getItems().addAll("Num/Type", "Payee/Reason", "Date", "Amount");
        searchTableChoiceBox.getItems().addAll("Check Withdrawal", "Non-Check Withdrawal", "Check Deposit",
                "Non-Check Deposit");
        addTableItemChoiceBox.getItems().addAll("Check Withdrawal", "Non-Check Withdrawal", "Check Deposit",
                "Non-Check Deposit");

        getTransactions("withdrawal");
        //getTransactions("deposit");

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

        try {
            String query = "select * from check_" + table;
            Statement stmt = db.makeConnection().createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                wObservableList.add(new ListItem(Integer.toString(rs.getInt(7)), rs.getString(8), rs.getDate(6).toString(), rs.getInt(3)));
                /*
                String checkNum = rs.getString(7);
                String payee = rs.getString(8);
                String date = rs.getDate(6).toString();
                int amt = rs.getInt(3);
                ListItem listItem = new ListItem(checkNum, payee, date, amt);

                if (table.equals("withdrawal")) {wObservableList.add(listItem);
                }else if (table.equals("deposit")) {dObservableList.add(listItem);}

                 */
            }
            rs.close();
            stmt.close();


            wNumTypeColumn.setCellValueFactory(new PropertyValueFactory<>("numType"));
            wPayeeReasonColumn.setCellValueFactory(new PropertyValueFactory<>("payeeReason"));
            wDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            wAmtColumn.setCellValueFactory(new PropertyValueFactory<>("amt"));

            //wNumTypeColumn, wPayeeReasonColumn, wDateColumn, wAmtColumn, dNumTypeColumn, dPayerMemoColumn, dDateColumn, dAmtColumn;
            //String numType, String payeeReason, java.sql.Date date, int amt
            wTableView.setItems(wObservableList);


            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
/*
        try {
            String query = "select * from non_check_" + table;
            Statement stmt = db.makeConnection().createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String wdtype = rs.getString(7);
                String reason = rs.getString(8);
                String date = rs.getDate(6).toString();
                int amt = rs.getInt(3);
                ListItem listItem = new ListItem(wdtype, reason, date, amt);
                if (table.equals("withdrawal")) {wObservableList.add(listItem);
                }else if (table.equals("deposit")) {dObservableList.add(listItem);}
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {System.out.println(e.getMessage());}

 */
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
        String table;
        String column;
        String strTable = searchTableChoiceBox.getSelectionModel().getSelectedItem();
        String strColumn = searchColumnChoiceBox.getSelectionModel().getSelectedItem();
        String strTextField = searchTextField.getText();
        boolean tChoiceBoxEmpty = searchTableChoiceBox.getSelectionModel().isEmpty();
        boolean cChoiceBoxEmpty = searchColumnChoiceBox.getSelectionModel().isEmpty();
        boolean sTextFieldEmpty = searchTextField.getText().isEmpty();

        if (tChoiceBoxEmpty) {
            DialogBox dialogBox = new DialogBox();
            dialogBox.infoAlertDialog("Missing Information", "Please select a table from the drop " +
                    "down menu and click the search button to display a table. Or select a table and column form the " +
                    "drop down menus and enter an enter the column entry into the search text field to search for a " +
                    "specific item.");
        } else if (!cChoiceBoxEmpty && sTextFieldEmpty) {
            DialogBox dialogBox = new DialogBox();
            dialogBox.infoAlertDialog("Missing Information", "Please select a table from the drop " +
                    "down menu and click the search button to display a table. Or select a table and column form the " +
                    "drop down menus and enter an enter the column entry into the search text field to search for a " +
                    "specific item.");
        } else if (cChoiceBoxEmpty && !sTextFieldEmpty) {
            DialogBox dialogBox = new DialogBox();
            dialogBox.infoAlertDialog("Missing Information", "Please select a table from the drop " +
                    "down menu and click the search button to display a table. Or select a table and column form the " +
                    "drop down menus and enter an enter the column entry into the search text field to search for a " +
                    "specific item.");
        }
/*
        if (!tChoiceBoxEmpty && cChoiceBoxEmpty && sTextFieldEmpty) {
            table = setTable(strTable);
            DBHelper db = new DBHelper();
            ObservableList<ListItem> searchObservableList = FXCollections.observableArrayList();
            try {
                Statement stmt = db.makeConnection().createStatement();
                ResultSet rs = stmt.executeQuery("select * from " + table);
                while (rs.next()) {
                    String checkNum = rs.getString(7);
                    String payee = rs.getString(8);
                    Date date = rs.getDate(6);
                    int amt = rs.getInt(3);
                    ListItem listItem = new ListItem(checkNum, payee, date, amt);
                    searchObservableList.add(listItem);
                }
                rs.close();
                stmt.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }



            //firstNameCol.setCellValueFactory(new PropertyValueFactory<Person,String>("firstName"));  Example of how to set tableview column information
        //*********************************************************************************************************************************************************************************************************

        }

        if (strTable.contains("Check Withdrawal") || strTable.contains("Non-Check Withdrawal")) {
            column = setColumnWithdrawal(strTable);
            System.out.println(column);
        }else if (strTable.contains("Check Deposit") || strTable.contains("Non-Check Deposit")) {
            column = setColumnDeposit(strTable);
            System.out.println(column);
        }

        if (strTextField.equals("")) {
            DBHelper db = new DBHelper();
            try {
                //Statement stmt = db.makeConnection().createStatement();
                //ResultSet rs = stmt.executeQuery("select * from " + table + " where " + column + " = "
                        //+ strTextField);
                //rs.
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            DBHelper db = new DBHelper();
            try {
                //Statement stmt = db.makeConnection().createStatement();
                //ResultSet rs = stmt.executeQuery("select * from " + table + " where " + column + " = "
                        //+ strTextField);
                //rs.
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        }

 */
    }











    private String setTable(String strTable) {
        String table = null;
        switch (strTable) {
            case "Check Withdrawal" -> table = "check_withdrawal";
            case "Non-Check Withdrawal" -> table = "non_check_withdrawal";
            case "Check Deposit" -> table = "check_deposit";
            case "Non-Check Deposit" -> table = "non_check_deposit";
        }
        return table;
    }

    private String setColumnWithdrawal(String strTable) {
        String column = null;
        switch (Objects.requireNonNull(strTable)) {
            case "Check Number" -> column = "checkNum";
            case "Payee" -> column = "payee";
            case "Date" -> column = "date";
            case "Amount" -> column = "amt";
            case "Type" -> column = "withdrawalType";
            case "Reason" -> column = "withdrawalReason";
        }
        return column;
    }

    private String setColumnDeposit(String strTable) {
        String column = null;
        switch (strTable) {
            case "Check Number" -> column = "checkNum";
            case "Date" -> column = "date";
            case "Amount" -> column = "amt";
            case "Payer" -> column = "payer";
            case "Memo" -> column = "depositMemo";
            case "Type" -> column = "depositType";
        }
        return column;
    }

}