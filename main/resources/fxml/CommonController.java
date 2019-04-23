package main.resources.fxml;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.EnmeterApp;
import main.model.Client;
import java.util.Collections;
import java.util.List;

public class CommonController {
    private EnmeterApp mainApp;
    private ObservableList<Client> clientsData = FXCollections.observableArrayList();

    public EnmeterApp getMainApp() {
        return mainApp;
    }
    public void setMainApp(EnmeterApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setClientsData(List<Client> cdata) {
        this.clientsData.clear();

        Collections.sort(cdata);

        for (Client client : cdata){
            this.clientsData.add(client);
        }
    }

    public ObservableList<Client> getClientsData(){
        return this.clientsData;
    }
}
