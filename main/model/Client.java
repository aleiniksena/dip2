package main.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.CalendarHelper;
import main.Constants;

import java.time.LocalDate;
import java.util.Objects;

public class Client implements Comparable<Client>{
    private String name;
    private String surname;
    private String middleName;
    private int id;
    private LocalDate birthDate;
    private LocalDate registrationDate;
    private String sex;
    private String email;
    private String phone;
    private boolean isActive;

    //IComparable implementation
    public int compareTo(Client c){
        int result = 1;
        int c2 = this.getName().compareTo(c.getName());
        int c1 = this.getSurname().compareTo(c.getSurname());
        int c3 = this.getMiddleName().compareTo(c.getMiddleName());

        if (c1 < 0){
            result = -1;
        } else if (c1 == 0){
            if (c2 < 0){
                result = -1;
            } else if (c2 == 0){
                result = c3;
            }
        }
        return result;
    }

    public Client(){
        setId(-1);
    }

    //Active field
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
    }

    //Name field
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    //Surname field
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    //Middle name field
    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    //Id field
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    //Birthdate field
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        System.out.println("Set birthdate to : " + String.valueOf(birthDate));
        this.birthDate = birthDate;
    }

    public int getFullAgeYears(){
        int result = CalendarHelper.getDateDiffYears(getBirthDate(), CalendarHelper.now());
        return result;
    }

    //Registration date
    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    //Sex field
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    //Email field
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    //Phone field
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    //Properties: used to display the client in the clients table
    public IntegerProperty getIdProperty(){
        return new SimpleIntegerProperty(this.getId());
    }
    public StringProperty getNameProperty(){
        return getStringProperty(this.getName());
    }
    public StringProperty getSurnameProperty(){
        return getStringProperty(this.getSurname());
    }
    public StringProperty getPhoneProperty(){
        return getStringProperty(this.getPhone());
    }
    public StringProperty getActiveProperty(){
        return getStringProperty(this.isActive() ? Constants.modeActive : Constants.modeInactive); }

    private StringProperty getStringProperty(String value){ return new SimpleStringProperty(value); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id &&
                isActive == client.isActive &&
                name.equals(client.name) &&
                surname.equals(client.surname) &&
                Objects.equals(middleName, client.middleName) &&
                birthDate.equals(client.birthDate) &&
                registrationDate.equals(client.registrationDate) &&
                sex.equals(client.sex) &&
                Objects.equals(email, client.email) &&
                Objects.equals(phone, client.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, middleName, id, birthDate, registrationDate, sex, email, phone, isActive);
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", middleName='" + middleName + '\'' +
                ", id=" + id +
                ", birthDate=" + birthDate +
                ", registrationDate=" + registrationDate +
                ", sex='" + sex + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
