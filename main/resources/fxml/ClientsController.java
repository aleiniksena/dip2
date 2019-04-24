package main.resources.fxml;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import main.Alerts;
import main.CalendarHelper;
import main.Constants;
import main.model.Client;
import java.time.LocalDate;

public class ClientsController extends CommonController{
    @FXML
    private Label number;
    @FXML
    private Label surname;
    @FXML
    private Label name;
    @FXML
    private Label middleName;
    @FXML
    private Label phone;
    @FXML
    private Label birthDate;
    @FXML
    private Label sex;
    @FXML
    private Label registrationDate;
    @FXML
    private Label email;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private TableView<Client> resultTable;
    @FXML
    private TableColumn<Client, Integer> colId;
    @FXML
    private TableColumn<Client, String> colName;
    @FXML
    private TableColumn<Client, String> colSurname;
    @FXML
    private TableColumn<Client, String> colPhone;
    @FXML
    private TableColumn<Client, String> colActive;

    private Client currentClient;

    public void setNumberValue(String number) {
        this.number.setText(((number == "-1") || (number == null)) ? Constants.modeInactive : number);
    }

    public void setSurnameValue(String surname) {
        this.surname.setText(surname == null ? Constants.modeInactive : surname);
    }

    public void setNameValue(String name) {
        this.name.setText(name == null ? Constants.modeInactive  : name);
    }

    public void setMiddleNameValue(String middleName) {
        this.middleName.setText(middleName == null ? Constants.modeInactive  : middleName);
    }

    public void setPhoneValue(String phone) {
        this.phone.setText(phone == null ? Constants.modeInactive  : phone);
    }

    public void setBirthDateValue(LocalDate birthDate) {
        this.birthDate.setText(birthDate == null ? Constants.modeInactive  : CalendarHelper.convertDateToString(birthDate));
    }

    public void setSexValue(String sex) {
        this.sex.setText(sex == null ? Constants.modeInactive  : sex);
    }

    public void setRegistrationDateValue(LocalDate registrationDate) {
        this.registrationDate.setText(registrationDate == null ? Constants.modeInactive  : CalendarHelper.convertDateToString(registrationDate));
    }


    public void setEmailValue(String email) {
        this.email.setText(email == null ? Constants.modeInactive  : email);
    }

    @FXML
    private void initialize() {
        this.colId.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        this.colName.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        this.colSurname.setCellValueFactory(cellData -> cellData.getValue().getSurnameProperty());
        this.colPhone.setCellValueFactory(cellData -> cellData.getValue().getPhoneProperty());
        this.colActive.setCellValueFactory(cellData -> cellData.getValue().getActiveProperty());

        this.resultTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                currentClient = resultTable.getSelectionModel().getSelectedItem();
                syncFields();
                disableEditDelete(false);
            }
        });

        this.addButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                getMainApp().getClientDetailsController().setClient(new Client());
                getMainApp().showClientDetailsView();
            }
        });

        this.deleteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (Alerts.showConfirmationAlert("", Constants.msgDeleteClient) == ButtonType.OK){
                    getMainApp().getDbManager().deleteClient(currentClient);
                    setClientsData(getMainApp().getDbManager().loadClients());
                    reloadTable();
                }
            }
        });

        this.editButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                getMainApp().getClientDetailsController().setClient(currentClient);
                getMainApp().showClientDetailsView();
            }
        });

        reloadTable();
        syncFields();
        disableEditDelete(true);
    }


    public void disableEditDelete(boolean disable){
        deleteButton.setDisable(disable);
        editButton.setDisable(disable);
    }

    private void reloadTable(){
        resultTable.setItems(getClientsData());
        selectFirstRow();
    }

    public void selectFirstRow(){
        if (getClientsData().size() > 0) {
            resultTable.getSelectionModel().select(0);
        }
    }

    private void syncFields(){
        boolean isSelected = (currentClient != null);
        this.deleteButton.setDisable(!isSelected);
        this.editButton.setDisable(!isSelected);
        this.addButton.setDisable(false);

        if (currentClient == null) {
            currentClient = new Client();
        }

        System.out.println("syncFields(): " + currentClient.toString());
        this.setSurnameValue(currentClient.getSurname());
        this.setNameValue(currentClient.getName());
        this.setBirthDateValue(currentClient.getBirthDate());
        this.setRegistrationDateValue(currentClient.getRegistrationDate());

        this.setSexValue(currentClient.getSex());
        this.setEmailValue(currentClient.getEmail());
        this.setMiddleNameValue(currentClient.getMiddleName());
        this.setPhoneValue(currentClient.getPhone());
        this.setNumberValue(String.valueOf(currentClient.getId()));
    }

}
