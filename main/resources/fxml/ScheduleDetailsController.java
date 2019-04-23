package main.resources.fxml;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import main.Alerts;
import main.Constants;
import main.model.Client;
import main.model.DisplayedSchedule;
import main.model.Gym;
import java.util.List;

public class ScheduleDetailsController extends CommonController{
    //UI controls
    @FXML
    Button saveButton;
    @FXML
    Button cancelButton;
    @FXML
    private TextField number;
    @FXML
    private TextField surname;
    @FXML
    private TextField name;
    @FXML
    private TextField middleName;
    @FXML
    private TextField time;
    @FXML
    private ComboBox<String> wday;
    @FXML
    private ComboBox<Gym> gym;
    @FXML
    private ComboBox<Client> client;

    //Data to display in comboboxes
    private ObservableList<String> weekDays = FXCollections.observableArrayList();
    private ObservableList<Gym> gymsData = FXCollections.observableArrayList();

    public void setGymsData(List<Gym> gymsData) {
        this.gymsData.clear();
        this.gymsData.addAll(gymsData);
    }

    //Items for weekDays combobox
    private void setWeekDays() {
        this.weekDays.add("Понедельник");
        this.weekDays.add("Вторник");
        this.weekDays.add("Среда");
        this.weekDays.add("Четверг");
        this.weekDays.add("Пятница");
        this.weekDays.add("Суббота");
        this.weekDays.add("Воскресенье");
        this.wday.setItems(this.weekDays);
    }

        public void setNumberValue(String number) {
            this.number.setText(number == null ? Constants.modeInactive : number);
        }

        public void setSurnameValue(String surname) {
            this.surname.setText(surname == null ? Constants.modeInactive : surname);
        }

        public void setNameValue(String name) {
            this.name.setText(name == null ? Constants.modeInactive : name);
        }

        public void setMiddleNameValue(String middleName) {
            this.middleName.setText(middleName == null ? Constants.modeInactive : middleName);
        }

    @FXML
    private void initialize()
    {
        this.saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                boolean incorrectData = false;

                if ((client.getSelectionModel().getSelectedItem() == null) ||
                        (gym.getSelectionModel().getSelectedItem() == null) ||
                        (wday.getSelectionModel().getSelectedIndex() == -1)){
                    incorrectData = true;
                }

                if (incorrectData){
                    Alerts.showInvalidDataAlert();
                } else{
                        DisplayedSchedule s = new DisplayedSchedule();
                        s.setTime(time.getText());
                        s.setClientId(client.getSelectionModel().getSelectedItem().getId());
                        s.setGymId(gym.getSelectionModel().getSelectedItem().getId());
                        s.setWday(wday.getSelectionModel().getSelectedIndex());

                    if (Alerts.showConfirmationAlert("", Constants.msgSaveChanges) == ButtonType.OK){
                        getMainApp().getDbManager().saveSchedule(s);
                        getMainApp().showScheduleView();
                    }
                }
            }
        });

        this.cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                getMainApp().showScheduleView();
            }
        });

        client.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
                System.out.println(newValue);
                setNumberValue(String.valueOf(newValue == null ? "" : newValue.getId()));
                setNameValue(newValue == null ? "" : newValue.getName());
                setSurnameValue(newValue == null ? "" : newValue.getSurname());
                setMiddleNameValue(newValue == null ? "" : newValue.getMiddleName());
            }
        );

        this.setWeekDays();

        this.client.setItems(getClientsData());
        this.gym.setItems(this.gymsData);

        this.client.setConverter(new StringConverter<Client>() {
            @Override
            public String toString(Client object) {
                String result;
                if (object != null)
                    result =  object.getSurname() + " " + object.getName() + " " + object.getMiddleName();
                else
                    result = "";
                return result;
            }

            @Override
            public Client fromString(String string) {
                Client result = null;

                for (Client c : getClientsData()){
                    if (c.getId() == Integer.getInteger(number.getText())){
                        result = c;
                        break;
                    }
                }
                return result;
            }
        });

        this.gym.setConverter(new StringConverter<Gym>() {

            @Override
            public String toString(Gym object) {
                String result;
                if (object != null)
                    result =  object.getName() + " (" + object.getAddress()+ ")";
                else
                    result = "";
                return result;
            }

            @Override
            public Gym fromString(String string) {
                Gym result = null;
                String gymName = string.substring(0, string.indexOf("("));

                for (Gym g : gymsData){
                    if (g.getName().compareTo(gymName) == 0){
                        result = g;
                        break;
                    }
                }
                return result;
            }
        });
    }

    public void resetFieldsToDefault(){
        this.number.clear();
        this.surname.clear();
        this.name.clear();
        this.middleName.clear();
        this.time.clear();
        this.wday.getSelectionModel().clearSelection();
        this.gym.getSelectionModel().clearSelection();
    }
}


