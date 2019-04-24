package main.resources.database;

import main.CalendarHelper;
import main.Constants;
import main.EnmeterApp;
import main.model.Client;
import main.model.ClientProfile;
import main.model.DisplayedSchedule;
import main.model.Gym;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DbManager {

    private Connection conn;
    private String dbPath;

    public DbManager(){
        this.dbPath = (Constants.dbDriverType + getClass().getResource(Constants.dbPath)).replace("file:/", ":");
    }

    public Connection getConnection() {
        try {
            if ((this.conn == null) || (this.conn.isClosed())) {
                Class.forName(Constants.magicString); //Magic! Don't delete it
                System.out.println(this.dbPath);
                this.conn = DriverManager.getConnection(this.dbPath);
                System.out.println("Debug: Connection to SQLite has been established.");
            }

        }  catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return this.conn;
    }

    private void closeConnection() {
        try {
            this.conn.close();
            System.out.println("Debug: Connection closed");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Load all clients from database
    public List<Client> loadClients(){

        List<Client> result = new ArrayList<Client>();
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement(Constants.sqlGetAllClients);
            ResultSet res = stmt.executeQuery();

            while (res.next()){
                Client c = new Client();
                c.setId(res.getInt("id"));
                c.setSex(res.getString("sex"));
                c.setName(res.getString("name"));
                c.setSurname(res.getString("surname"));
                c.setMiddleName(res.getString("middlename"));
                c.setBirthDate(CalendarHelper.parseDate(res.getString("birthdate")));
                c.setRegistrationDate(CalendarHelper.parseDate(res.getString("regdate")));
                c.setActive(res.getInt("active") == 1);


                Statement stmtContact = this.getConnection().createStatement();
                ResultSet resContact = stmtContact.executeQuery(String.format(Constants.sqlGetContact, c.getId()));

                while (resContact.next()){

                    int type = resContact.getInt("contypeid");
                    if (type == 1)

                        c.setEmail(resContact.getString("contact"));
                    else
                        c.setPhone(resContact.getString("contact"));
                }
                result.add(c);
                System.out.println("loadClients(): " + c.toString());
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
        return result;
    }

    private void updateClientActivityStatus(int clientId){
        try{
            Statement stmtSchedule = this.getConnection().createStatement();
            String sqlQuery = String.format(Constants.sqlUpdateClientActivityStatus, clientId, clientId);
            System.out.println(sqlQuery);
            stmtSchedule.executeUpdate(sqlQuery);
            stmtSchedule.close();

        } catch(SQLException e){
            e.printStackTrace();
        } finally{
            closeConnection();
        }
    }

    public void saveSchedule(DisplayedSchedule s){
        try{

            Statement stmtSchedule = this.getConnection().createStatement();
            //INSERT INTO client_gym_schedule (client_id, gym_id, time, wday) VALUES ('%s', '%s', '%s', '%s');
            String sqlQuery = String.format(Constants.sqlInsertSchedule, s.getClientId(), s.getGymId(),
                        s.getTime(), s.getWday());
            stmtSchedule.executeUpdate(sqlQuery);
            stmtSchedule.close();

            //Update client activity status
            updateClientActivityStatus(s.getClientId());

        }catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
    }

    //Load all schedules from database
    public List<DisplayedSchedule> loadSchedules(){
        List<DisplayedSchedule> result = new ArrayList<DisplayedSchedule>();
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement(Constants.sqlGetAllSchedules);
            ResultSet res = stmt.executeQuery();

            while (res.next()){
                DisplayedSchedule c = new DisplayedSchedule();
                c.setClientId(res.getInt("client_id"));
                c.setGymId(res.getInt("gym_id"));
                c.setWday(res.getInt("wday"));
                c.setTime(res.getString("time"));
                result.add(c);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
        return result;
    }

    //Load all client profiles from database
    public List<ClientProfile> loadClientsProfiles(EnmeterApp main){
        List<ClientProfile> result = new ArrayList<ClientProfile>();
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement(Constants.sqlGetAllClientsProfiles);
            ResultSet res = stmt.executeQuery();

            while (res.next()){
                ClientProfile c = new ClientProfile();
                c.setClientId(res.getInt("client_id"));
                c.setProfileId(res.getInt("profile_id"));
                c.setHeight(res.getInt("height"));
                c.setStartTerm(CalendarHelper.parseDate(res.getString("start_date")));
                c.setStartWeight(res.getInt("start_weight"));
                c.setGoalWeight(res.getInt("goal"));
                c.setMainApp(main);
                result.add(c);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
        return result;
    }

    //The the number of trainings for the specified client.
    public int getClientsTrainingNumber(int clientId){
        int result = 0;
        try {
            PreparedStatement stmt = this.getConnection().prepareStatement(String.format(Constants.sqlGetClientTrainingsNumber, clientId));
            ResultSet res = stmt.executeQuery();

            while (res.next()){
                result = res.getInt(1);
                break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = 0;
        }finally {
            closeConnection();
        }
        return result;
    }

    //delete all data from client
    public void deleteAllClients(){
        try {
            PreparedStatement stmt = this.getConnection().prepareStatement(Constants.sqlDeleteAllClients);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
    }

    //Delete the specified client from the database
    public void deleteClient(Client client){
        try {
            Statement stmtClient = this.getConnection().createStatement();
            String sqlQuery = String.format(Constants.sqlDeleteClient, client.getId());
            System.out.println(sqlQuery);
            stmtClient.executeUpdate(sqlQuery);

            ResultSet r = stmtClient.getGeneratedKeys();

            if (r.next()){
                int id = r.getInt(1);
                System.out.println("Deleted id -" + id); // display inserted record
            }
            stmtClient.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
    }

    public void deleteClientProfile(int profId){
        try {
            Statement stmt = this.getConnection().createStatement();
            //sqlDeleteSchedule = "DELETE FROM schedule WHERE client_id = %d AND gym_id = %d AND wday = %d AND time ='%s';";
            String sqlQuery = String.format(Constants.sqlDeleteClientProfile, profId);
            System.out.println(sqlQuery);
            stmt.executeUpdate(sqlQuery);

            ResultSet r = stmt.getGeneratedKeys();

            if (r.next()){
                int id = r.getInt(1);
                System.out.println("Deleted id -" + id); // display inserted record
            }
            stmt.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
    }

    //Delete the specified scheduled from database
    public void deleteSchedule(DisplayedSchedule sc){
        try {
            Statement stmtClient = this.getConnection().createStatement();

            //sqlDeleteSchedule = "DELETE FROM schedule WHERE client_id = %d AND gym_id = %d AND wday = %d AND time ='%s';";
            String sqlQuery = String.format(Constants.sqlDeleteSchedule, sc.getClientId(), sc.getGymId(), sc.getWday(), sc.getTime());
            System.out.println(sqlQuery);

            stmtClient.executeUpdate(sqlQuery);

            ResultSet r = stmtClient.getGeneratedKeys();

            if (r.next()){
                int id = r.getInt(1);
                System.out.println("Deleted id -" + id); // display inserted record
            }

            stmtClient.close();

            updateClientActivityStatus(sc.getClientId());
        }catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
    }

    //delete all data from client
    public void deleteAllGyms(){

        try {
            PreparedStatement stmt = this.getConnection().prepareStatement(Constants.sqlDeleteAllClients);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }

    }
    //Store gyms
    public void insertGyms(List<Gym> gyms){
        try{
            String sqlQuery;
            Statement stmtGym = this.getConnection().createStatement();

            for (Gym g : gyms){
                sqlQuery = String.format(Constants.sqlInsertGym, g.getName(), g.getAddress(),
                        g.getSchedule(), g.getPhone());
                System.out.println(sqlQuery);
                stmtGym.executeUpdate(sqlQuery);
            }
            stmtGym.close();

        }catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
    }

    public List<Gym> loadGyms(){
        List<Gym> result = new ArrayList<Gym>();
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement(Constants.sqlGetAllGyms);
            ResultSet res = stmt.executeQuery();

            while (res.next()){
                Gym g = new Gym();
                g.setId(res.getInt("id"));
                g.setName(res.getString("name"));
                g.setAddress(res.getString("address"));
                g.setPhone(res.getString("phone"));
                g.setSchedule(res.getString("schedule"));
                g.setComment(res.getString("comment"));
                result.add(g);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
        return result;
    }

    //Save clients
    public void insertClients(List<Client> clients){
        for (Client client : clients) {
            insertClient(client);
        }
    }


    public int insertClient(Client client){
        int resultId = -1;
        try {
            Statement stmtClient = this.getConnection().createStatement();
            String sqlQuery = String.format(Constants.sqlInsertClients1,
                        client.getName(),
                        client.getSurname(),
                        client.getMiddleName(),
                        CalendarHelper.convertDateToString(client.getBirthDate()),
                        CalendarHelper.convertDateToString(client.getRegistrationDate()),
                        client.getSex(),
                        client.isActive());

                System.out.println(sqlQuery);
                stmtClient.executeUpdate(sqlQuery);

                ResultSet r = stmtClient.getGeneratedKeys();

                if (r.next()){
                    resultId = r.getInt(1);
                    System.out.println("Inserted ID -" + resultId); // display inserted record
                }

            stmtClient.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
        return resultId;
    }


    //Update client
    public void updateClient(Client client){
        try {
            Statement stmtClient = this.getConnection().createStatement();
            if (client.getId() > -1) { //Обновляем существующего клиента
                //"UPDATE client SET name='%s', surname = '%s', middlename='%s', birthdate='%s', sex='%s', active='%s' WHERE id=%d;";
                String sqlQuery = String.format(Constants.sqlUpdateClient,
                        client.getName(),
                        client.getSurname(),
                        client.getMiddleName(),
                        CalendarHelper.convertDateToString(client.getBirthDate()),
                        client.getSex(),
                        client.isActive() ? 1 : 0,
                        client.getId());

                System.out.println(sqlQuery);
                stmtClient.executeUpdate(sqlQuery);

                ResultSet r = stmtClient.getGeneratedKeys();

                /*if (r.next()){
                    int id = r.getInt(1);
                    System.out.println("Updated ID -" + id); // display inserted record
                }*/
                stmtClient.close();
                updateClientContacts(client);

            } else{ //Добавляем нового клиента
                int nc = insertClient(client);
                client.setId(nc);
                insertClientContacts(client);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
    }

    //Update client profile
    public void updateClientProfile(ClientProfile profile){
        try {
            Statement stmtClient = this.getConnection().createStatement();

            //"UPDATE client_profile SET height='%d', start_date = '%s', start_weight='%d', goal='%d' WHERE profile_id=%d;"
            if (profile.getProfileId() > -1) { //Обновляем существующий профиль клиента
                String sqlQuery = String.format(Constants.sqlUpdateClientProfile,
                        profile.getHeight(),
                        CalendarHelper.convertDateToString(profile.getProgramStartDate()),
                        profile.getStartWeight(),
                        profile.getGoalWeight(),
                        profile.getProfileId());

                System.out.println(sqlQuery.toString());
                stmtClient.executeUpdate(sqlQuery.toString());

                ResultSet r = stmtClient.getGeneratedKeys();

                if (r.next()){
                    int id = r.getInt(1);
                    System.out.println("Updated ID -" + id); // display inserted record
                }
                stmtClient.close();

            } else{ //Добавляем нового клиента
                int n = insertClientProfile(profile);
                profile.setProfileId(n);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
    }

    //Insert new client profile
    public int insertClientProfile(ClientProfile profile){
        int resultId = -1;
        try {
            Statement stmtClient = this.getConnection().createStatement();
            ////"INSERT INTO client_profile (client_id, height, start_date, start_weight, goal) VALUES"
            String sqlQuery = String.format(Constants.sqlInsertClientsProfile,
                   profile.getClientId(),
                    profile.getHeight(),
                    CalendarHelper.convertDateToString(profile.getProgramStartDate()),
                    profile.getStartWeight(),
                    profile.getGoalWeight());

            System.out.println(sqlQuery);
            stmtClient.executeUpdate(sqlQuery);

            ResultSet r = stmtClient.getGeneratedKeys();

            if (r.next()){
                resultId = r.getInt(1);
                System.out.println("Inserted ID -" + resultId); // display inserted record
            }

            stmtClient.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
        return resultId;
    }

    private void modifyContact(int clientId, int contactType, String val, boolean isNew){
        try {
            Statement stmtClientContact = this.getConnection().createStatement();
            String template = isNew ? Constants.sqlInsertClientContacts : Constants.sqlUpdateClientContacts;

            //"UPDATE client_contact SET contact = '%s' WHERE client_id = %d AND contypeid = %d";
            //"INSERT INTO client_contact (contact, client_id, contypeid) VALUES ('%s', %d, %d);";
            String sqlQuery = String.format(template,
                    val,
                    clientId,
                    contactType);

            System.out.println(sqlQuery);
            stmtClientContact.execute(sqlQuery);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
    }

    //Save clients contat for 1st time
    public void insertClientContacts(Client client){
        modifyContact(client.getId(), 1, client.getEmail(), true);
        modifyContact(client.getId(), 2, client.getPhone(), true);
    }

    //Update existing clients contact data
    public void updateClientContacts(Client client){
        modifyContact(client.getId(), 1, client.getEmail(), false);
        modifyContact(client.getId(), 2, client.getPhone(), false);
    }

    //Save clients contats for 1st time
    public void insertGeneratedClientContacts(List<Client> clients){
        Random rand = new Random();

        for (Client client : clients){
            client.setEmail(client.getName().substring(0, 3) + client.getSurname().substring(0, 4) + "@mail.ru");

            StringBuffer phone = new StringBuffer("+375");

            while (phone.length() < 13){
                phone.append(rand.nextInt(10));
            }

            client.setPhone(phone.toString());
            insertClientContacts(client);
        }
    }

    //Check if this login and password exists in the database in users table
    public boolean validateLogin(String login, String password) {
        boolean result = false;

        try {
            Statement stmtUser = this.getConnection().createStatement();
            String sqlQuery = String.format(Constants.sqlCheckUser,
                    login,
                    password);

            System.out.println(sqlQuery);
            ResultSet res = stmtUser.executeQuery(sqlQuery);

            while (res.next()){
                if (res.getInt(1) == 1)
                    result = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
        return result;
    }
}

