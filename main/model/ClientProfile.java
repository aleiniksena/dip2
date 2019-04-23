package main.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.CalendarHelper;
import main.Constants;
import main.EnmeterApp;

import java.time.LocalDate;
import java.util.List;

public class ClientProfile implements Comparable<ClientProfile>{
    private EnmeterApp mainApp;
    private Client client;
    private int clientId;
    private double mHeight;
    private int startWeight;
    private int goalWeight;
    private LocalDate programStartDate;
    private LocalDate programOptimisticEndDate;
    private LocalDate programAverageEndDate;
    private LocalDate programPessimisticEndDate;
    private int numberOfTrainingsPerWeek;

    private int profileId;

    //clientId property
    public int getClientId() {
        return clientId;
    }
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    //client property
    public Client getClient() { return client; }

    public void setClient(Client client) {
        if (client == null) {
            client = new Client();
            client.setId(-1);
        }
        this.client = client;
        this.clientId = client.getId();
    }

    //mainApp property
    public void setMainApp(EnmeterApp mainApp) {
        this.mainApp = mainApp;
    }

    //implementation of IComparable
    public int compareTo(ClientProfile c){
        System.out.println(String.format("Compare: '%s' and '%s'", this.toString(), c.toString()));
        int result = this.client.compareTo(c.client);
        return result;
    }

    //startWeight property
    public int getStartWeight() {
        return startWeight;
    }
    public void setStartWeight(int startWeight) {
        this.startWeight = startWeight;
    }

    //GoalWeight property
    public void setGoalWeight(int goalWeight) {
        this.goalWeight = goalWeight;
    }
    public int getGoalWeight(){
        return this.goalWeight;
    }

    //ProgramStartDate property
    public LocalDate getProgramStartDate() {
        return programStartDate;
    }
    public void setProgramStartDate(LocalDate programStartDate) {
        this.programStartDate = programStartDate;
    }

    //ProfileId property
    public int getProfileId(){
            return this.profileId;
    }
    public void setProfileId(int id){
        this.profileId = id;
    }

    //Common string operations
    public StringProperty getStringProperty(String colName){
        String value = "";

        switch (colName) {
            case "colName": value =  client.getSurname() + " " + client.getName() + " " + client.getMiddleName(); break;
            case "colAverageDate": value = CalendarHelper.convertDateToString(this.getAverageTermDate()); break;
            case "colEndWeight": value = String.valueOf(this.getGoalWeight()); break;
            case "colOptimisticDate": value = CalendarHelper.convertDateToString(this.getOptimisticTermDate()); break;
            case "colStartDate": value = CalendarHelper.convertDateToString(this.programStartDate); break;
            case "colStartWeight": value = String.valueOf(this.startWeight); break;
            default: break;
        }

        return new SimpleStringProperty(value);
    }

    //Height property
    public int getHeight(){
        return (int)Math.round(this.mHeight * 100);
    }
    public void setHeight(int height){
            this.mHeight = height / 100;
    }


    public LocalDate getOptimisticTermDate(){
        this.programOptimisticEndDate = CalendarHelper.addMonthsToDate(programStartDate, Constants.optimisticSpeed);
        System.out.println(String.format("Optimistic finish date: %s", CalendarHelper.convertDateToString(this.programOptimisticEndDate)));
        return this.programOptimisticEndDate;
    }

    public LocalDate getPessimisticTermDate(){
        this.programPessimisticEndDate = CalendarHelper.addMonthsToDate(programStartDate, Constants.pessimisticSpeed);
        System.out.println(String.format("Pessimistic finish date: %s", CalendarHelper.convertDateToString(programPessimisticEndDate)));
        return this.programPessimisticEndDate;
    }

    public LocalDate getAverageTermDate(){
        this.programAverageEndDate = CalendarHelper.addMonthsToDate(programStartDate, Constants.averageSpeed);
        System.out.println(String.format("Average finish date: %s", CalendarHelper.convertDateToString(this.programAverageEndDate)));
        return this.programAverageEndDate;
    }

    public void setStartTerm(LocalDate date){
        this.programStartDate = date;
    }

    public int getMaxEnergyLevel(){
        int bel = this.getBasicEnergyLevel();
        return (int)Math.round(bel * this.getMultiplicator());
    }

    public double getMultiplicator(){
        int not = this.mainApp == null ? 0 : this.getNumberOfTrainingsPerWeek(this.mainApp);
        double result = 0;

        switch (not){
            case 0: result = 1.2; break;
            case 1:
            case 2:
            case 3: result = 1.375; break;
            case 4:
            case 5: result = 1.55; break;
            case 6:
            case 7: result = 1.725; break;
            default: result = 1; break;
        }

        return result;
    }

    public int getNumberOfTrainingsPerWeek(EnmeterApp mainApp){
        List<DisplayedSchedule> schedules = mainApp.getScheduleData();

        this.numberOfTrainingsPerWeek  = 0;

        for (DisplayedSchedule sc : schedules){
            if (sc.getClientId() == this.client.getId())
                this.numberOfTrainingsPerWeek++;
        }
        return  this.numberOfTrainingsPerWeek;
    }


    @Override
    public String toString() {
        return "ClientProfile{" +
                "client=" + client +
                ", height=" + mHeight +
                ", startWeight=" + startWeight +
                ", goalWeight=" + goalWeight +
                ", programStartDate=" + programStartDate +
                ", programOptimisticEndDate=" + programOptimisticEndDate +
                ", programAverageEndDate=" + programAverageEndDate +
                ", programPessimisticEndDate=" + programPessimisticEndDate +
                ", numberOfTrainingsPerWeek=" + numberOfTrainingsPerWeek +
                ", pessimisticSpeed=" + Constants.pessimisticSpeed +
                ", optimisticSpeed=" + Constants.optimisticSpeed +
                ", averageSpeed=" + Constants.averageSpeed +
                '}';
    }

    //Calculated basic energy level
    public int getBasicEnergyLevel(){
        int bel = 0;

        if ((client != null) && (client.getSex().equals(Constants.sexFemale))){
            bel = 0;
        } else{
            bel = 1;
        }
        return bel;
    }

    //Calculate extra mass index
    public String getEmi(){
        System.out.println(String.format("Height %f, weight %d", mHeight, startWeight));
        StringBuffer res = new StringBuffer();

        if (mHeight > 0) {
            double result = startWeight / Math.pow(mHeight, 2);
            res.append(result).append("(");

            if (result < 16) {
                res.append(Constants.level0);
            } else if (result < 18.5) {
                res.append(Constants.level1);
            } else if (result < 25) {
                res.append(Constants.level2);
            } else if (result < 30) {
                res.append(Constants.level3);
            } else if (result < 35) {
                res.append(Constants.level4);
            } else if (result < 40) {
                res.append(Constants.level5);
            } else {
                res.append(Constants.level6);
            }
            res.append(")");
        } else{
            res.append("");
        }
        System.out.println(res.toString());
        return res.toString();
    }

}
