package main.resources.fxml;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import main.Alerts;
import main.Constants;
import main.model.DisplayedSchedule;

import java.util.List;
import java.util.Optional;

public class ScheduleController extends CommonController{
        @FXML
        Button add;
        @FXML
        Button delete;
        @FXML
        private TableView<DisplayedSchedule> resultTable;
        @FXML
        private TableColumn<DisplayedSchedule, String> colName;
         @FXML
        private TableColumn<DisplayedSchedule, String> colDay;
        @FXML
        private TableColumn<DisplayedSchedule, String> colTime;
        @FXML
        private TableColumn<DisplayedSchedule, String> colGym;

        private ObservableList<DisplayedSchedule> scheduleData = FXCollections.observableArrayList();

        @FXML
        private void initialize(){
        this.colName.setCellValueFactory(cellData -> cellData.getValue().getNameProperty(getMainApp()));
        this.colDay.setCellValueFactory(cellData -> cellData.getValue().getDayProperty());
        this.colTime.setCellValueFactory(cellData -> cellData.getValue().getTimeProperty());
        this.colGym.setCellValueFactory(cellData -> cellData.getValue().getGymProperty(getMainApp()));
        this.resultTable.setItems(this.scheduleData);

        this.add.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                getMainApp().getScheduleDetailsController().setClientsData(getMainApp().getClientsData());
                getMainApp().getScheduleDetailsController().setGymsData(getMainApp().getGymsData());
                getMainApp().showScheduleDetailsView();
            }
        });

        this.delete.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                DisplayedSchedule current = resultTable.getSelectionModel().getSelectedItem();
                if (current != null) {
                    if (Alerts.showConfirmationAlert("", Constants.msgDeleteClient) == ButtonType.OK) {
                        getMainApp().getDbManager().deleteSchedule(current);
                        getMainApp().showScheduleView();
                    }
                }
            }
        });
    }

    public void setScheduleData(List<DisplayedSchedule> scheduleData) {
        this.scheduleData.clear();

        for (DisplayedSchedule s : scheduleData){
            this.scheduleData.add(s);
        }
    }

}
