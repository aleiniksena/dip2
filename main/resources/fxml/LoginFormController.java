package main.resources.fxml;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import main.Constants;

public class LoginFormController extends CommonController{
    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    public void initialize() {
        this.loginButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try{
                    String login = userNameField.getText();
                    String password = passwordField.getText();
                    boolean userExists = getMainApp().getDbManager().validateLogin(login, password);
                    userNameField.setStyle(userExists ? Constants.styleLabelValidText : Constants.styleLabelErrorText);
                    passwordField.setStyle(userExists ? Constants.styleLabelValidText : Constants.styleLabelErrorText);

                    if (userExists){
                        getMainApp().showMainApp(login);
                        getMainApp().showClientsView();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
