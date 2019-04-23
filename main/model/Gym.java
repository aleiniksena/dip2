package main.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Gym {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String address;
    private int id;

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    private String comment;

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    private String schedule;

    public StringProperty getScheduleProperty(){ return getStringProperty(this.schedule);}

    public StringProperty getNameProperty(){
        return getStringProperty(this.name);
    }

    public StringProperty getAddressProperty(){
        return getStringProperty(this.address);
    }

    private StringProperty getStringProperty(String value){
        return new SimpleStringProperty(value);
    }

    public StringProperty getPhoneProperty(){return getStringProperty(this.phone);}

}
