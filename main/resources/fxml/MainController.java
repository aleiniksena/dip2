package main.resources.fxml;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import main.Constants;

import static javafx.application.Platform.exit;

public class MainController extends CommonController{
    @FXML
    private MenuBar menuBar;
    @FXML
    private Button clientsButton;
    @FXML
    private Button gymsButton;
    @FXML
    private Button profilesButton;
    @FXML
    private Button scheduleButton;

    @FXML
    private void initialize() {
        Label menuAbout = new Label(Constants.menuAboutLabel);
        Label menuExit = new Label(Constants.menuExitLabel);
        this.menuBar.getMenus().add(new Menu(Constants.menuEmptyLabel, menuExit));
        menuExit.setOnMouseClicked(mouseEvent -> exit());
        this.menuBar.getMenus().add(new Menu(Constants.menuEmptyLabel, menuAbout));
        menuAbout.setOnMouseClicked(mouseEvent -> showAbout());

        this.clientsButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                getMainApp().showClientsView();
            }
        });

        this.profilesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                getMainApp().showClientProfileView();
            }
        });

        this.gymsButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                getMainApp().showGymsView();
            }
        });

        this.scheduleButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                getMainApp().showScheduleView();
            }
        });
    }

    @FXML
    public void showAbout(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Constants.menuAboutLabel);
        alert.setHeaderText(Constants.appTitleAbout);
        alert.setContentText(Constants.aboutAuthor);
        alert.showAndWait();
    }
}
