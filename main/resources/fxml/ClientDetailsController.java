package main.resources.fxml;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import main.*;
import main.model.Client;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Calendar;


public class ClientDetailsController extends CommonController{
    @FXML
    private TextField number;
    @FXML
    private TextField surname;
    @FXML
    private TextField name;
    @FXML
    private TextField middleName;
    @FXML
    private TextField phone;
    @FXML
    private DatePicker birthDate;
    @FXML
    private ComboBox<String> sex;
    @FXML
    private DatePicker registrationDate;
    @FXML
    private TextField email;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    private Client currentClient;
    private ObservableList<String> sexTypes = FXCollections.observableArrayList();

    //Sex field
    private void setSexTypes() {
        this.sexTypes.add(Constants.sexFemale);
        this.sexTypes.add(Constants.sexMale);
        this.sex.setItems(this.sexTypes);
    }

    public void setSexValue(String sex) {
        if (sex == null)
        {
            this.sex.getSelectionModel().clearSelection();
            return;
        }
        this.sex.setValue(sex);
    }

    //Common field method
    private void updateTextFieldValue(TextField ctrl, String value){
        if ((value == null) || (value.compareTo(Constants.modeInactive) == 0)){
            ctrl.clear();
        }
        else{
            ctrl.setText(value);
        }
    }

    //Number field
    public void setNumberValue(String number) {
        this.number.setText(number == "-1" ? Constants.modeInactive : number);
    }

    //Surname field
    public void setSurnameValue(String s) {
        this.updateTextFieldValue(surname, s);
    }

    //Name field
    public void setNameValue(String s) {
        this.updateTextFieldValue(name, s);
    }

    //Middle name field
    public void setMiddleNameValue(String s) {
        this.updateTextFieldValue(middleName, s);
    }

    //Phone field
    public void setPhoneValue(String s) {
        this.updateTextFieldValue(phone, s);
    }


    //Birth date field
    public void setBirthDateValue(LocalDate d) {
        CalendarHelper.updateDateValue(birthDate, d);
    }

    //Registration date field
    public void setRegistrationDateValue(LocalDate d) {
        CalendarHelper.updateDateValue(registrationDate, d);
    }

    //Email field
    public void setEmailValue(String s) {
        this.updateTextFieldValue(email, s);
    }

    //Set current client value
    public void setClient(Client c){
        this.currentClient = c;
        this.setSurnameValue(currentClient.getSurname());
        this.setNameValue(currentClient.getName());
        this.setBirthDateValue(currentClient.getBirthDate());
        this.setRegistrationDateValue(currentClient.getRegistrationDate());
        this.setSexValue(currentClient.getSex());
        this.setEmailValue(currentClient.getEmail());
        this.setMiddleNameValue(currentClient.getMiddleName());
        this.setPhoneValue(currentClient.getPhone());
        this.setNumberValue(String.valueOf(currentClient.getId()));

        System.out.println("setClient: " + c.toString());
    }

    @FXML
    private void initialize()
    {
        this.saveButton.setDisable(false);
        this.cancelButton.setDisable(false);

        this.saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (Alerts.showConfirmationAlert("", Constants.msgSaveChanges) == ButtonType.OK){
                    System.out.println("Current client before save: " + currentClient.toString());

                    StringBuffer b = new StringBuffer();

                    if (!TextFieldHelper.isFieldValid(email, "  Email: ", b)||
                            !TextFieldHelper.isFieldValid(phone, "  Телефон: ", b) ||
                            !new ComboBoxFieldHelper<String>().isFieldValid(sex, "  Пол: ", b) ||
                            !CalendarHelper.isFieldValid(birthDate, "   Дата рождения: ", b)||
                            !CalendarHelper.isFieldValid(registrationDate, "    Дата регистрации: ", b)||
                            !TextFieldHelper.isFieldValid(surname, "    Фамилия: ", b) ||
                            !TextFieldHelper.isFieldValid(middleName, " Отчество: ", b)) {
                        Alerts.showInvalidDataAlert(b.toString());

                    }else {
                        currentClient.setEmail(email.getText());
                        currentClient.setPhone(phone.getText());
                        currentClient.setSex(sex.getValue());
                        currentClient.setBirthDate(CalendarHelper.getSelectedDate(birthDate));
                        currentClient.setRegistrationDate(CalendarHelper.getSelectedDate(registrationDate));
                        currentClient.setSurname(surname.getText());
                        currentClient.setName(name.getText());
                        currentClient.setMiddleName(middleName.getText());
                        System.out.println("Current client to save: " + currentClient.toString());
                        getMainApp().getDbManager().updateClient(currentClient);
                        getMainApp().showClientsView();
                    }
                }
            }
        });

        this.cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                getMainApp().showClientsView();
            }
        });

        this.setSexTypes();

        this.birthDate.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.matches(Constants.rexepDate)){
                    birthDate.setValue(CalendarHelper.parseDate(newValue));
                }
            }
        });

        this.registrationDate.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.matches(Constants.rexepDate)){
                    registrationDate.setValue(CalendarHelper.parseDate(newValue));
                }
            }
        });

    }
}
