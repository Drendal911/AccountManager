package Utility;

import javafx.scene.control.Alert;

public class DialogBox {

    public DialogBox() {
    }

    public void infoAlertDialog(String headerText, String alertText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        //alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(alertText);
        alert.showAndWait();
    }
}
