package main.resources.fxml;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.model.Gym;


import java.util.List;

public class GymsController extends CommonController{

    @FXML
    private TableView<Gym> resultTable;
    @FXML
    private TableColumn<Gym, String> colName;
    @FXML
    private TableColumn<Gym, String> colAddress;
    @FXML
    private TableColumn<Gym, String> colPhone;
    @FXML
    private TableColumn<Gym, String> colSchedule;


    public void setGymsData(List<Gym> gymsData) {
        this.gymsData.clear();

        for (Gym g : gymsData){
            this.gymsData.add(g);
        }
    }

    private ObservableList<Gym> gymsData = FXCollections.observableArrayList();

    @FXML
    private void initialize(){
        this.colName.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        this.colAddress.setCellValueFactory(cellData -> cellData.getValue().getAddressProperty());
        this.colSchedule.setCellValueFactory(cellData -> cellData.getValue().getScheduleProperty());
        this.colPhone.setCellValueFactory(cellData -> cellData.getValue().getPhoneProperty());
        this.resultTable.setItems(this.gymsData);
    }


}
