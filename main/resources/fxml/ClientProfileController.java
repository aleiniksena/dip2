package main.resources.fxml;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import main.Alerts;
import main.Constants;
import main.model.Client;
import main.model.ClientProfile;
import java.util.List;

public class ClientProfileController extends CommonController {
    //UI controls
    @FXML
    Button add;
    @FXML
    Button delete;
    @FXML
    Button edit;
    @FXML
    private TableView<ClientProfile> resultTable;
    @FXML
    private TableColumn<ClientProfile, String> colName;
    @FXML
    private TableColumn<ClientProfile, String> colStartWeight;
    @FXML
    private TableColumn<ClientProfile, String> colEndWeight;
    @FXML
    private TableColumn<ClientProfile, String> colPessimisticDate;
    @FXML
    private TableColumn<ClientProfile, String> colAverageDate;
    @FXML
    private TableColumn<ClientProfile, String> colOptimisticDate;

    @FXML
    //Data to display in UI
    private TableColumn<ClientProfile, String> colStartDate;
    private ObservableList<ClientProfile> profileData = FXCollections.observableArrayList();
    private ClientProfile currentProfile;

    //Fill in data to display in the result table
    public void setClientProfilesData(List<ClientProfile> profileData) {
        this.profileData.clear();
        this.profileData.addAll(profileData);
    }

    @FXML
    private void initialize(){
        this.colName.setCellValueFactory(cellData -> cellData.getValue().getStringProperty(Constants.colName));
        this.colAverageDate.setCellValueFactory(cellData -> cellData.getValue().getStringProperty(Constants.colAverageDate));
        this.colEndWeight.setCellValueFactory(cellData -> cellData.getValue().getStringProperty(Constants.colEndWeight));
        this.colOptimisticDate.setCellValueFactory(cellData -> cellData.getValue().getStringProperty(Constants.colOptimisticDate));
        this.colPessimisticDate.setCellValueFactory(cellData -> cellData.getValue().getStringProperty(Constants.colPessimisticDate));
        this.colStartDate.setCellValueFactory(cellData -> cellData.getValue().getStringProperty(Constants.colStartDate));
        this.colStartWeight.setCellValueFactory(cellData -> cellData.getValue().getStringProperty(Constants.colStartWeight));
        this.resultTable.setItems(this.profileData);

        this.add.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                List<Client> clients = getMainApp().getClientsData(); //get current clients data
                getMainApp().getCientProfileDetailsController().setClientsData(clients);
                getMainApp().showClientProfileDetailsView(new ClientProfile());
            }
        });

        this.edit.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                List<Client> clients = getMainApp().getClientsData(); //get current clients data
                getMainApp().getCientProfileDetailsController().setClientsData(clients);
                getMainApp().showClientProfileDetailsView(currentProfile);
            }
        });

        this.delete.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                ClientProfile current = resultTable.getSelectionModel().getSelectedItem();
                if (current != null) {
                    if (Alerts.showConfirmationAlert("", Constants.msgDeleteClient) == ButtonType.OK) {
                       getMainApp().getDbManager().deleteClientProfile(current.getProfileId());
                       getMainApp().showClientProfileView();
                    }
                }
            }
        });

        this.resultTable.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                currentProfile = resultTable.getSelectionModel().getSelectedItem();
            }
        });
    }
}
