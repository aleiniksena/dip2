package main.resources.fxml;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import main.*;
import main.model.Client;
import main.model.ClientProfile;

public class ClientProfileDetailsController extends CommonController{
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private ComboBox<Client> clientFIOValue; //read-only
    @FXML
    private TextField clientIdValue; //read-only
    @FXML
    private TextField goalValue; //editable
    @FXML
    private TextField startWeightValue; //editable
    @FXML
    private TextField heightValue; //editable
    @FXML
    private TextField emiValue; //read-only
    @FXML
    private TextField trainingsValue; //editable
    @FXML
    private TextField belValue; //read-only
    @FXML
    private TextField maxElValue; //read-only
    @FXML
    private TextField minTermValue; //read-only
    @FXML
    private TextField avgTermValue; //read-only
    @FXML
    private TextField maxTermValue; //read-only
    @FXML
    private DatePicker startProgramDateValue; //editable

    @Override
    public String toString() {
        return "ClientProfileDetailsController{" +
                "saveButton=" + saveButton +
                ", cancelButton=" + cancelButton +
                ", clientFIOValue=" + clientFIOValue +
                ", clientIdValue=" + clientIdValue +
                ", goalValue=" + goalValue +
                ", startWeightValue=" + startWeightValue +
                ", heightValue=" + heightValue +
                ", emiValue=" + emiValue +
                ", trainingsValue=" + trainingsValue +
                ", belValue=" + belValue +
                ", maxElValue=" + maxElValue +
                ", minTermValue=" + minTermValue +
                ", avgTermValue=" + avgTermValue +
                ", maxTermValue=" + maxTermValue +
                ", startProgramDateValue=" + startProgramDateValue +
                ", clientProfile=" + clientProfile +
                '}';
    }

    private ClientProfile clientProfile;

    public void setClientProfile(ClientProfile p) {
        this.clientProfile = p;
        this.clientIdValue.setText(TextFieldHelper.convertIntToText(p.getClientId()));
        this.goalValue.setText(TextFieldHelper.convertIntToText(p.getGoalWeight()));
        this.startWeightValue.setText(TextFieldHelper.convertIntToText(p.getStartWeight()));
        this.heightValue.setText(TextFieldHelper.convertIntToText(p.getHeight()));
        this.emiValue.setText(p.getEmi());
        this.belValue.setText(TextFieldHelper.convertIntToText(p.getBasicEnergyLevel()));
        this.maxElValue.setText(TextFieldHelper.convertIntToText(p.getMaxEnergyLevel()));
        this.minTermValue.setText(CalendarHelper.convertDateToString(p.getOptimisticTermDate(p.getWeightDiff())));
        this.avgTermValue.setText(CalendarHelper.convertDateToString(p.getAverageTermDate(p.getWeightDiff())));
        this.maxTermValue.setText(CalendarHelper.convertDateToString(p.getPessimisticTermDate(p.getWeightDiff())));

        this.clientIdValue.setText(String.valueOf(p.getClientId()));

        for (Client c : this.getClientsData()){
            if (c.getId() == p.getClientId()){
                this.clientFIOValue.setValue(c);
                int trainings = this.getMainApp().getDbManager().getClientsTrainingNumber(c.getId());
                this.trainingsValue.setText(String.valueOf(trainings));
                break;
            }
        }
    }

    //Check if the fields have been properly recalculated
    private boolean validateRequiredFields(StringBuffer b){
        boolean result = true;

        if (!TextFieldHelper.isNumericFieldValid(this.goalValue, " Цель: ", b) ||
        !TextFieldHelper.isNumericFieldValid(this.startWeightValue, " Исходный вес: ", b) ||
        !TextFieldHelper.isNumericFieldValid(this.heightValue, " Рост: ", b) ||
        !CalendarHelper.isFieldValid(startProgramDateValue, " Старт программы: ", b) ||
        !new ComboBoxFieldHelper<Client>().isFieldValid(clientFIOValue, " Клиент: ", b))
        {
            result = false;
        }

        return result;
    }

    @FXML
    private void initialize()
    {
        this.saveButton.setDisable(false);
        this.cancelButton.setDisable(false);

        this.saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                StringBuffer b = new StringBuffer();

                if (! validateRequiredFields(b)){
                    Alerts.showInvalidDataAlert(b.toString());
                } else{
                    if (Alerts.showConfirmationAlert("", Constants.msgSaveChanges) == ButtonType.OK) {
                        System.out.println("Current profile to save: " + clientProfile.toString());
                        if (clientProfile.getProfileId() > -1) {
                            getMainApp().getDbManager().updateClientProfile(clientProfile);
                        } else{
                            int id = getMainApp().getDbManager().insertClientProfile(clientProfile);
                            clientProfile.setProfileId(id);
                        }
                        getMainApp().showClientProfileView();
                    }
                }
            }
        });

        this.cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                getMainApp().showClientProfileView();
            }
        });

        this.clientFIOValue.setItems(this.getClientsData());
        this.clientFIOValue.setConverter(new StringConverter<Client>() {

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
                    if (c.getId() == Integer.getInteger(clientIdValue.getText())){
                        result = c;
                        break;
                    }
                }
                return result;
            }
        });

        this.clientFIOValue.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
                    System.out.println(newValue);
                    this.clientProfile.setClient(newValue);
                    this.clientIdValue.setText(String.valueOf(this.clientProfile.getClientId()));
                    this.trainingsValue.setText(String.valueOf(this.clientProfile.getNumberOfTrainingsPerWeek(getMainApp())));
                    recalcFields();
                }
        );

        this.startProgramDateValue.valueProperty().addListener((ov, oldValue, newValue) ->{
                recalcFields();
        });

        this.startProgramDateValue.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.matches(Constants.rexepDate)){
                    startProgramDateValue.setValue(CalendarHelper.parseDate(newValue));
                }
            }
        });

        this.heightValue.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue,
                            String newValue) {
            if (!newValue.matches("\\d*")) {
                heightValue.setText(newValue.replaceAll("[^\\d]", ""));
                }

            if (heightValue.getText().compareTo("") != 0){
                recalcFields();
            }
            }
        });

        this.startWeightValue.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    startWeightValue.setText(newValue.replaceAll("[^\\d]", ""));
                }

                if (startWeightValue.getText().compareTo("") != 0){
                    recalcFields();
                }
            }
        });

        this.goalValue.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    goalValue.setText(newValue.replaceAll("[^\\d]", ""));
                }

                if (!goalValue.getText().isBlank() && !goalValue.getText().isEmpty()){
                    recalcFields();
                }
            }
        });
    }

    private void recalcFields(){
        System.out.println("recalcFields(): " + this.clientProfile.toString());

        if ((this.clientProfile != null) && (this.clientProfile.getClient() != null)) {

            this.clientProfile.setGoalWeight(TextFieldHelper.convertTextToInt(this.goalValue.getText()));
            this.clientProfile.setStartWeight(TextFieldHelper.convertTextToInt(this.startWeightValue.getText()));
            this.clientProfile.setHeight(TextFieldHelper.convertTextToInt(this.heightValue.getText()));
            this.clientProfile.setProgramStartDate(CalendarHelper.getSelectedDate(startProgramDateValue));

            this.emiValue.setText(this.clientProfile.getEmi());
            this.belValue.setText(String.valueOf(this.clientProfile.getBasicEnergyLevel()));
            this.maxElValue.setText(String.valueOf(this.clientProfile.getMaxEnergyLevel()));
            System.out.println(this.clientProfile.getProgramStartDate());
            this.minTermValue.setText(CalendarHelper.convertDateToString(this.clientProfile.getOptimisticTermDate(this.clientProfile.getWeightDiff())));
            this.maxTermValue.setText(CalendarHelper.convertDateToString(this.clientProfile.getPessimisticTermDate(this.clientProfile.getWeightDiff())));
            this.avgTermValue.setText(CalendarHelper.convertDateToString(this.clientProfile.getAverageTermDate(this.clientProfile.getWeightDiff())));
        }
    }

}

