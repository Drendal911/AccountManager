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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import static sample.LoginController.userID;

public class DashboardController implements Initializable {
    @FXML Label wTotalLabel, dTotalLabel, abTotalLabel, nTotalLabel, acctBalanceLabel;
    @FXML TableView<ListItem> wTableView, dTableView;
    @FXML TableColumn<ListItem, String> wNumTypeColumn, wPayeeReasonColumn, wDateColumn, dNumTypeColumn,
            dPayerMemoColumn, dDateColumn;
    @FXML TableColumn<ListItem, Integer > wAmtColumn, dAmtColumn;
    @FXML ChoiceBox<String> searchColumnChoiceBox, searchTableChoiceBox, addTableItemChoiceBox;
    @FXML Button searchButton, addTransactionButton, defaultButton;
    @FXML RadioButton checkingRadioButton, savingsRadioButton;
    @FXML TextField searchTextField;
    private ObservableList<ListItem> wObservableList = FXCollections.observableArrayList();
    private ObservableList<ListItem> dObservableList = FXCollections.observableArrayList();
    private ObservableList<String> columnChoiceBoxList = FXCollections.observableArrayList();
    private ToggleGroup toggleGroup;
    public static String account;
    double wTotal = 0.00;
    double dTotal = 0.00;
    double nTotal;

    public class ListItem {
        String numType, payeeReason, date, amt;

        public ListItem(String numType, String payeeReason, String date, String amt) {
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
        public String getAmt() {
            return amt;
        }
        public void setAmt(String amt) {
            this.amt = amt;
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialSetup();
        clearObservableLists();
        getAccount();
        setAccountLabel();
        getTransactions("withdrawal");
        getTransactions("deposit");

        nTotal = dTotal - wTotal;
        nTotalLabel.setText("$" + nTotal);

        //Set listener for addTableItemChoiceBox and adjusts the available selections for addColumnItemChoiceBox
        searchTableChoiceBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<?
                extends String> observable, String oldValue, String newValue) -> setSearchColumnItemChoiceBox() );
        //Set listener for togglegroup, adjusts observable lists according to the item selected
        toggleGroup.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            setToggleGroup();
        });
    }


    /* METHODS *********************************************************************************************************
    ********************************************************************************************************************
    *******************************************************************************************************************/


    private void getTransactions(String table) {
        getAccount();
        setObservableLists(table);
        setFXMLItems();
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

    public void setToggleGroup() {
        toggleGroup.setUserData(toggleGroup.getSelectedToggle().toString());
        String string = toggleGroup.getUserData().toString();
        if (string.contains("Savings")) {
            account = "Savings";
        }else if (string.contains("Checking")) {
            account = "Checking";
        }
        wTotal = 0.00;
        dTotal = 0.00;
        wObservableList.clear();
        dObservableList.clear();
        getTransactions("withdrawal");
        getTransactions("deposit");
        nTotal = dTotal - wTotal;
        nTotalLabel.setText("$" + nTotal);
        setAccountLabel();
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

        if (!tChoiceBoxEmpty && cChoiceBoxEmpty && sTextFieldEmpty) {
            wObservableList.clear();
            dObservableList.clear();
            table = setTable(strTable);
            DBHelper db = new DBHelper();
            try {
                Statement stmt = db.makeConnection().createStatement();
                ResultSet rs = stmt.executeQuery("select * from " + table + " where userID = " + userID);
                while (rs.next()) {
                    String checkNum = rs.getString(6);
                    String payee = rs.getString(7);
                    String date = rs.getDate(5).toString();
                    String amt = Integer.toString(rs.getInt(3));
                    switch (table) {
                        case "check_withdrawal", "non_check_withdrawal" -> wObservableList.add(new ListItem
                                (checkNum, payee, date, amt));
                        case "check_deposit", "non_check_deposit" -> dObservableList.add(new ListItem
                                (checkNum, payee, date, amt));
                    }
                }
                rs.close();
                stmt.close();
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        if (!tChoiceBoxEmpty && !cChoiceBoxEmpty && !sTextFieldEmpty) {
            wObservableList.clear();
            dObservableList.clear();
            table = setTable(strTable);
            DBHelper db = new DBHelper();
            String query;
            if (strTable.contains("Withdrawal")) {
                column = setColumnWithdrawal(strColumn);
                query = "select * from " + table + " where " + column + " = ? and userID = " + userID;
                try {
                    PreparedStatement pstmt = db.makeConnection().prepareStatement(query);
                    pstmt.setString(1, strTextField);
                    ResultSet rs = pstmt.executeQuery();
                    if (!rs.next()) {
                        DialogBox dialogBox = new DialogBox();
                        dialogBox.infoAlertDialog("None Found", "No matching entries were found in " +
                                "the database.");
                    }else {
                        rs.beforeFirst();
                        while (rs.next()) {
                            String checkNum = rs.getString(6);
                            String payee = rs.getString(7);
                            String date = rs.getDate(5).toString();
                            String amt = Integer.toString(rs.getInt(3));
                            wObservableList.add(new ListItem(checkNum, payee, date, amt));
                        }
                    }
                    rs.close();
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (strTable.contains("Deposit")) {
                column = setColumnDeposit(strColumn);
                query = "select * from " + table + " where " + column + " = ? and userID = " + userID;
                try {
                    PreparedStatement pstmt = db.makeConnection().prepareStatement(query);
                    pstmt.setString(1, strTextField);
                    ResultSet rs = pstmt.executeQuery();
                    if (!rs.next()) {
                        DialogBox dialogBox = new DialogBox();
                        dialogBox.infoAlertDialog("None Found", "No matching entries were found in " +
                                "the database.");
                    }else {
                        rs.beforeFirst();
                        while (rs.next()) {
                            String checkNum = rs.getString(6);
                            String payee = rs.getString(7);
                            String date = rs.getDate(5).toString();
                            String amt = Integer.toString(rs.getInt(3));
                            dObservableList.add(new ListItem(checkNum, payee, date, amt));
                        }
                    }
                    rs.close();
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void setDefaultButton() {
        checkingRadioButton.setSelected(true);
        searchColumnChoiceBox.setValue("");
        searchTableChoiceBox.setValue("");
        addTableItemChoiceBox.setValue("");
        searchTextField.setText("");
        wObservableList.clear();
        dObservableList.clear();
        wTotal = 0.00;
        dTotal = 0.00;
        getTransactions("withdrawal");
        getTransactions("deposit");
        nTotal = dTotal - wTotal;
        nTotalLabel.setText("$" + nTotal);
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

    private String setColumnWithdrawal(String strColumn) {
        String column = null;
        switch (strColumn) {
            case "Check Number" -> column = "checkNum";
            case "Payee" -> column = "payee";
            case "Date" -> column = "date";
            case "Amount" -> column = "amt";
            case "Type" -> column = "withdrawalType";
            case "Reason" -> column = "withdrawalReason";
        }
        return column;
    }

    private String setColumnDeposit(String strColumn) {
        String column = null;
        switch (strColumn) {
            case "Check Number" -> column = "checkNum";
            case "Date" -> column = "date";
            case "Amount" -> column = "amt";
            case "Payer" -> column = "payer";
            case "Memo" -> column = "depositMemo";
            case "Type" -> column = "depositType";
        }
        return column;
    }

    private void getAccount() {
        String rButtons = toggleGroup.getSelectedToggle().toString();
        if (rButtons.contains("Checking")) {account = "checking";}
        else if (rButtons.contains("Savings")) {account = "savings";}
    }

    private void setAccountLabel() {
        String acct = account.substring(0,1).toUpperCase() + account.substring(1);
        acctBalanceLabel.setText(acct + " Balace");
    }

    private void setObservableLists(String table) {
        DBHelper db = new DBHelper();
        ResultSet rs;

        try {
            String query = "select * from check_" + table + " where userID = " + userID + " and acct = '"
                    + account + "'";
            Statement stmt = db.makeConnection().createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                if (table.equals("withdrawal")) {
                    wObservableList.add(new ListItem(Integer.toString(rs.getInt(6)), rs.getString
                            (7), rs.getDate(5).toString(), Integer.toString
                            (rs.getInt(3))));
                    try {
                        wTotal = wTotal + Double.parseDouble(rs.getString(3));
                    }catch (NullPointerException e) {
                        System.out.println(e.getMessage());
                    }
                }else if (table.equals("deposit")) {
                    dObservableList.add(new ListItem(Integer.toString(rs.getInt(6)), rs.getString
                            (7), rs.getDate(5).toString(), Integer.toString
                            (rs.getInt(3))));
                    try {
                        dTotal = dTotal + Double.parseDouble(rs.getString(3));
                    }catch (NullPointerException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {System.out.println(e.getMessage());}

        try {
            String query = "select * from non_check_" + table + " where userID = " + userID + " and acct = '"
                    + account + "'";
            Statement stmt = db.makeConnection().createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                if (table.equals("withdrawal")) {
                    wObservableList.add(new ListItem(rs.getString(6), rs.getString
                            (7), rs.getDate(5).toString(), Integer.toString
                            (rs.getInt(3))));
                    try {
                        wTotal = wTotal + Double.parseDouble(rs.getString(3));
                    }catch (NullPointerException e) {
                        System.out.println(e.getMessage());
                    }
                }else if (table.equals("deposit")) {
                    dObservableList.add(new ListItem(rs.getString(6), rs.getString
                            (7), rs.getDate(5).toString(), Integer.toString
                            (rs.getInt(3))));
                    try {
                        dTotal = dTotal + Double.parseDouble(rs.getString(3));
                    }catch (NullPointerException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            rs.close();
            stmt.close();
        }catch (Exception e) {System.out.println(e.getMessage());}
    }

    private void setFXMLItems() {
        DBHelper db = new DBHelper();
        wNumTypeColumn.setCellValueFactory(new PropertyValueFactory<>("numType"));
        wPayeeReasonColumn.setCellValueFactory(new PropertyValueFactory<>("payeeReason"));
        wDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        wAmtColumn.setCellValueFactory(new PropertyValueFactory<>("amt"));
        dNumTypeColumn.setCellValueFactory(new PropertyValueFactory<>("numType"));
        dPayerMemoColumn.setCellValueFactory(new PropertyValueFactory<>("payeeReason"));
        dDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dAmtColumn.setCellValueFactory(new PropertyValueFactory<>("amt"));

        wNumTypeColumn.setStyle( "-fx-alignment: CENTER;");
        wPayeeReasonColumn.setStyle( "-fx-alignment: CENTER;");
        wDateColumn.setStyle( "-fx-alignment: CENTER;");
        wAmtColumn.setStyle( "-fx-alignment: CENTER;");
        dNumTypeColumn.setStyle( "-fx-alignment: CENTER;");
        dPayerMemoColumn.setStyle( "-fx-alignment: CENTER;");
        dDateColumn.setStyle( "-fx-alignment: CENTER;");
        dAmtColumn.setStyle( "-fx-alignment: CENTER;");

        wTableView.setItems(wObservableList);
        dTableView.setItems(dObservableList);
        wTableView.getSortOrder().add(wDateColumn);
        dTableView.getSortOrder().add(dDateColumn);

        wTotalLabel.setText("$" + wTotal);
        dTotalLabel.setText("$" + dTotal);
        try {
            Statement statement = db.makeConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from " + account);
            resultSet.last();
            String abTotal = resultSet.getString(7);
            abTotalLabel.setText(abTotal);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearObservableLists() {
        try {
            wObservableList.clear();
            dObservableList.clear();
        }catch (NullPointerException e) {System.out.println(e.getMessage());}
    }

    private void initialSetup() {
        toggleGroup = new ToggleGroup();
        checkingRadioButton.setToggleGroup(toggleGroup);
        savingsRadioButton.setToggleGroup(toggleGroup);
        checkingRadioButton.setSelected(true);
        searchTableChoiceBox.getItems().addAll("Check Withdrawal", "Non-Check Withdrawal", "Check Deposit",
                "Non-Check Deposit");
        addTableItemChoiceBox.getItems().addAll("Check Withdrawal", "Non-Check Withdrawal", "Check Deposit",
                "Non-Check Deposit");
    }

}