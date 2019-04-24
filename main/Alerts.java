package main;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class Alerts {

    public static ButtonType showConfirmationAlert(String content, String header){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(content);
        alert.setTitle(Constants.msgConfirmTitle);
        alert.setHeaderText(header);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get();
    }

    public static void showInvalidDataAlert(String content){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(Constants.msgDataErrorTitle + ": " + content);
        alert.setTitle(Constants.msgDataErrorTitle);
        alert.setHeaderText(Constants.msgFillDataEror);
        alert.showAndWait();
    }
}
