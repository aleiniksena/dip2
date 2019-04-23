package main.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.EnmeterApp;

import java.util.List;

public class DisplayedSchedule {
    private int clientId;
    private int gymId;
    private String time;
    private int wday;


    public DisplayedSchedule(){
        this.clientId = -1;
        this.time = "";
        this.wday = -1;
        this.gymId = -1;
    }

    public StringProperty getNameProperty(EnmeterApp mainApp){

        List<Client> clients = mainApp.getClientsData();
        StringBuffer buf = new StringBuffer();

        for (Client c : clients){
            if (c.getId() == this.getClientId()){
                buf.append(c.getSurname() + " " + c.getName().substring(0, 1) + ". " + c.getMiddleName().substring(0, 1)+ ".");
                break;
            }
        }

        return this.getStringProperty(buf.toString());
    }


    public StringProperty getGymProperty(EnmeterApp mainApp){
        List<Gym> gyms = mainApp.getGymsData();
        StringBuffer buf = new StringBuffer();

        for (Gym g : gyms){
            if (g.getId() == this.getGymId()){
                buf.append(g.getName() + "(" + g.getAddress() + ")");
                break;
            }
        }
        return this.getStringProperty(buf.toString());
    }

    public StringProperty getDayProperty(){
        String res;
        switch(wday){
            case 1: res = "Понедельник"; break;
            case 2: res = "Вторник"; break;
            case 3: res = "Среда"; break;
            case 4: res = "Четверг"; break;
            case 5: res = "Пятница"; break;
            case 6: res = "Суббота"; break;
            default: res = "Воскресенье"; break;

        };
        return getStringProperty(res);
    }

    public StringProperty getTimeProperty(){
        return getStringProperty(time);
    }

    private StringProperty getStringProperty(String value){
        return new SimpleStringProperty(value);
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getGymId() {
        return gymId;
    }

    public void setGymId(int gymId) {
        this.gymId = gymId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getWday() {
        return wday;
    }

    public void setWday(int wday) {
        this.wday = wday;
    }

    @Override
    public String toString() {
        return "DisplayedSchedule{" +
                "clientId=" + clientId +
                ", gymId=" + gymId +
                ", time='" + time + '\'' +
                ", wday=" + wday +
                '}';
    }
}
