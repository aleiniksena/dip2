package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.model.Client;
import main.model.ClientProfile;
import main.model.DisplayedSchedule;
import main.model.Gym;
import main.resources.database.DbManager;
import main.resources.fxml.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class EnmeterApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private FXMLLoader rootLoader;
    private MainController mainAppController;

    private AnchorPane login;
    private LoginFormController loginFormController;
    private FXMLLoader loginLoader;

    private ClientsController clientsController;
    private FXMLLoader clientsControllerLoader;
    private AnchorPane clientsView;

    private ClientDetailsController clientDetailsController;
    private FXMLLoader clientDetailsLoader;
    private AnchorPane clientDetailsView;

    private GymsController gymsController;
    private FXMLLoader gymsLoader;
    private AnchorPane gymsView;

    private ScheduleController scheduleController;
    private FXMLLoader scheduleLoader;
    private AnchorPane scheduleView;

    private ScheduleDetailsController scheduleDetailsController;
    private FXMLLoader scheduleDetailsLoader;
    private AnchorPane scheduleDetailsView;

    private ClientProfileController clientProfilesController;
    private FXMLLoader clientProfilesLoader;
    private AnchorPane clientProfilesView;

    private ClientProfileDetailsController clientProfileDetailsController;
    private FXMLLoader clientProfileDetailsLoader;
    private AnchorPane clientProfileDetailsView;

    private List<Gym> gymsData;
    private List<Client> clientsData;
    private List<DisplayedSchedule> scheduleData;
    private List<ClientProfile> clientsProfilesData;

    private DbManager dbManager;

    public List<ClientProfile> getClientsProfilesData() {
        return clientsProfilesData;
    }

    public void setClientsProfilesData(List<ClientProfile> clientsProfilesData) {
        this.clientsProfilesData = clientsProfilesData;
        this.initClientProfiles();
    }

    public DbManager getDbManager(){
        return this.dbManager;
    }

    public ScheduleDetailsController getScheduleDetailsController() {
        return scheduleDetailsController;
    }

    public ClientDetailsController getClientDetailsController(){
        return this.clientDetailsController;
    }

    public ClientProfileDetailsController getCientProfileDetailsController(){
        return clientProfileDetailsController;
    }

    public List<Client> getClientsData() {
        this.clientsData = this.getDbManager().loadClients();
        return clientsData;
    }

    public ClientsController getClientsController(){

        return this.getClientsController();
    }

    public List<Gym> getGymsData() {

        return gymsData;
    }

    public void setGymsData(List<Gym> gymsData) {

        this.gymsData = gymsData;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Initialize database
        this.dbManager = new DbManager();

        //Initialize main app view
        this.rootLoader = new FXMLLoader(getClass().getResource(Constants.fxmlMainAppFormPath));
        this.rootLayout = this.rootLoader.load();
        this.mainAppController = this.rootLoader.getController();

        //Initialize login view
        this.loginLoader = new FXMLLoader(getClass().getResource(Constants.fxmlLoginFormPath));
        this.login = this.loginLoader.load();
        this.loginFormController = this.loginLoader.getController();

        //Initialize clients view
        this.clientsControllerLoader = new FXMLLoader(getClass().getResource(Constants.fxmlClientsViewPath));
        this.clientsView = this.clientsControllerLoader.load();
        this.clientsController = this.clientsControllerLoader.getController();

        //Initialize clientDetails view
        this.clientDetailsLoader = new FXMLLoader(getClass().getResource(Constants.fxmlClientDetailsViewPath));
        this.clientDetailsView = this.clientDetailsLoader.load();
        this.clientDetailsController = this.clientDetailsLoader.getController();

        //Initialize gyms view
        this.gymsLoader =  new FXMLLoader(getClass().getResource(Constants.fxmlGymsViewPath));
        this.gymsView = this.gymsLoader.load();
        this.gymsController = this.gymsLoader.getController();

        //Show initial login form
        primaryStage.setTitle(Constants.formTitleLogin);
        primaryStage.setScene(new Scene(this.login));
        primaryStage.resizableProperty().setValue(false);
        primaryStage.show();
        this.primaryStage = primaryStage;

        this.scheduleDetailsLoader = new FXMLLoader(getClass().getResource(Constants.fxmlScheduleDetailsViewPath));
        this.scheduleDetailsView = this.scheduleDetailsLoader.load();
        this.scheduleDetailsController = this.scheduleDetailsLoader.getController();

        this.scheduleLoader = new FXMLLoader(getClass().getResource(Constants.fxmlScheduleViewPath));
        this.scheduleView = this.scheduleLoader.load();
        this.scheduleController = this.scheduleLoader.getController();

        this.clientProfilesLoader =  new FXMLLoader(getClass().getResource(Constants.fxmlClientProfileViewPath));
        this.clientProfilesView = this.clientProfilesLoader.load();
        this.clientProfilesController = this.clientProfilesLoader.getController();

        this.clientProfileDetailsLoader =  new FXMLLoader(getClass().getResource(Constants.fxmlClientProfileDetailsViewPath));
        this.clientProfileDetailsView = this.clientProfileDetailsLoader.load();
        this.clientProfileDetailsController = this.clientProfileDetailsLoader.getController();


        //Set linke from children to main app
        this.loginFormController.setMainApp(this);
        this.clientsController.setMainApp(this);
        this.clientDetailsController.setMainApp(this);
        this.gymsController.setMainApp(this);
        this.scheduleController.setMainApp(this);
        this.scheduleDetailsController.setMainApp(this);
        this.clientProfilesController.setMainApp(this);
        this.clientProfileDetailsController.setMainApp(this);
        this.mainAppController.setMainApp(this);

        //Load gyms
        this.getDbManager().insertGyms(this.generateGyms());
        this.setGymsData(this.getDbManager().loadGyms());
        this.gymsController.setGymsData(this.getGymsData());

        //Load clients
        getDbManager().deleteAllClients();
        getDbManager().insertClients(generateClients());
        getDbManager().insertClientContacts(getDbManager().loadClients());
        this.clientsController.setClientsData(this.getClientsData());
        this.setClientsProfilesData(this.getDbManager().loadClientsProfiles(this));
        this.clientProfilesController.setClientProfilesData(this.getClientsProfilesData());
    }

    private void initClientProfiles(){
        for (ClientProfile p : this.getClientsProfilesData()){
            p.setMainApp(this);

            for (Client c : this.getClientsData()){
                if (c.getId() == p.getClientId()){
                    p.setClient(c);
                    break;
                }
            }
        }
    }

    //Common function to show the view
    public void loadViewIntoCentralStage(Node node){
        this.primaryStage.resizableProperty().setValue(true);
        this.rootLayout.setCenter(node);
        this.primaryStage.resizableProperty().setValue(false);
    }

    //Client Details view
    public void showClientDetailsView(){
        this.loadViewIntoCentralStage(this.clientDetailsView);
    }

    //Gyms view
    public void showGymsView(){
        this.loadViewIntoCentralStage(this.gymsView);
    }

    //Clients view
    public void showClientsView(){
        this.clientsController.setClientsData(this.getClientsData());
        this.loadViewIntoCentralStage(this.clientsView);
        this.clientsController.selectFirstRow();
    }

    //Schedule view
    public void setScheduleData(List<DisplayedSchedule> sData){
        this.scheduleData = sData;

    }
    public List<DisplayedSchedule> getScheduleData(){
        if (this.scheduleData == null)
            this.scheduleData = new ArrayList<>();
        return this.scheduleData;
    }

    public void showScheduleView(){
        this.setScheduleData(this.getDbManager().loadSchedules());
        this.scheduleController.setScheduleData(this.getScheduleData());
        this.loadViewIntoCentralStage(this.scheduleView);
    }

    //Schedule Details view
    public void showScheduleDetailsView(){
        this.scheduleDetailsController.resetFieldsToDefault();
        this.loadViewIntoCentralStage(this.scheduleDetailsView);
    }

    //Client Profiles view
    public void showClientProfileView(){
        this.setClientsProfilesData(this.getDbManager().loadClientsProfiles(this));
        this.clientProfilesController.setClientProfilesData(this.getClientsProfilesData());
        this.loadViewIntoCentralStage(this.clientProfilesView);
    }

    //Client Profile details view
    public void showClientProfileDetailsView(ClientProfile p){
        this.clientProfileDetailsController.setClientProfile(p);
        this.loadViewIntoCentralStage(this.clientProfileDetailsView);
    }

    //Show Main App view
    public void showMainApp(String userName){
        this.primaryStage.setScene(new Scene(this.rootLayout, Constants.appWidth, Constants.appHeight));
        this.primaryStage.setTitle(String.format(Constants.appTitleMain, userName));
        primaryStage.resizableProperty().setValue(false);
        this.primaryStage.show();
    }

    /*
    *  Generate data for test
    *
    * */

    //TEST: Generate gyms
    private List<Gym> generateGyms(){
        List<Gym> gyms = new ArrayList<Gym>();
        Random rand = new Random();

        for (int i = 0; i < 10 ; i++){
            Gym g = new Gym();
            g.setAddress(Constants.testGymAddress.get(rand.nextInt(Constants.testGymAddress.size())) + " д." +  String.valueOf(rand.nextInt(200)));
            g.setName(Constants.testGymName.get(rand.nextInt(Constants.testGymName.size()))+"-"+String.valueOf(i+1));;

            StringBuffer phone = new StringBuffer("+375");

            while (phone.length() < 13){
                phone.append(rand.nextInt(10));
            }

            g.setPhone(phone.toString());
            g.setSchedule(Constants.testGymSchedule.get(rand.nextInt(Constants.testGymSchedule.size())));;

            gyms.add(g);
        }
        return gyms;
    }


    //TEST: Generate clients for test
    private List<Client> generateClients(){
        List<Client> clients = new ArrayList<Client>();
        Random rand = new Random();
        LocalDate date = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        for (int i = 0; i < 5; i++){
            Client c = new Client();
            c.setActive(false);
            String sex = i % 3 == 0 ? Constants.sexFemale : Constants.sexMale;
            c.setSex(sex);

            if (sex.compareTo(Constants.sexFemale) == 0) {
                c.setMiddleName(Constants.testFMiddleNames.get(rand.nextInt(Constants.testFMiddleNames.size())));
                c.setName(Constants.testFNames.get(rand.nextInt(Constants.testFNames.size())));
                c.setSurname(Constants.testFSurnames.get(rand.nextInt(Constants.testFSurnames.size())));
            } else{
                c.setMiddleName(Constants.testMMiddleNames.get(rand.nextInt(Constants.testMMiddleNames.size())));
                c.setName(Constants.testMNames.get(rand.nextInt(Constants.testMNames.size())));
                c.setSurname(Constants.testMSurnames.get(rand.nextInt(Constants.testMSurnames.size())));
            }

            LocalDate dob = date.minusYears(20 * rand.nextInt(2))
                    .minusWeeks(rand.nextInt(3000))
                    .minusDays(rand.nextInt(31));

            c.setBirthDate(dob);

            c.setRegistrationDate(LocalDate.now());

            c.setEmail(c.getName().substring(0, 2) + c.getSurname().substring(0, 3) + "@mail.ru");

            StringBuffer phone = new StringBuffer("+375");

            while (phone.length() < 13){
                phone.append(rand.nextInt(10));
            }

            c.setPhone(phone.toString());
            clients.add(c);
        }
        return clients;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
