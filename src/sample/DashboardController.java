package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DashboardController {
    @FXML Label wTotalLabel, dTotalLabel;
    @FXML TableView wTableView, dTableView;
    @FXML TableColumn wNumTypeColumn, wPayeeReasonColumn, wDateColumn, wAmtColumn, dNumTypeColumn, dPayerMemoColumn,
            dDateColumn, dAmtColumn;
    @FXML ChoiceBox columnChoiceBox, tableChoiceBox;
    @FXML Button searchButton;
    @FXML RadioButton checkingRadioButton, savingsRadioButton;
}
