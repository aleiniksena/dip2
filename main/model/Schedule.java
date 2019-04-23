package main.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Schedule {
    Client client;
    int clientId;
    Gym gym;
    int gymId;
    String time;
    int wday;

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

    public Client getClient() {
        return client;
    }

    public Gym getGym() {
        return gym;
    }

    public void setGym(Gym gym) {
        this.gym = gym;
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

    public StringProperty getNameProperty(){
        String val = client.getSurname() + " " + client.getName().substring(1, 1) + "." + client.getSurname().substring(1, 1) + ".";
        return getStringProperty(val);}

    public StringProperty getDayProperty(){
        return getStringProperty(String.valueOf(wday));
    }

    public StringProperty getTimeProperty(){
        return getStringProperty(time);
    }

    public StringProperty getGymProperty(){
        String val = gym.getName() + " (" + gym.getAddress() + ")";
        return new SimpleStringProperty(val);
    }

    private StringProperty getStringProperty(String value){
        return new SimpleStringProperty(value);
    }

    public void setClient(Client c){
        this.client = c;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "client=" + client +
                ", gym=" + gym +
                ", time='" + time + '\'' +
                ", wday='" + wday + '\'' +
                '}';
    }
}
