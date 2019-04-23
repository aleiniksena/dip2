package main;

import java.util.List;

import static java.util.Arrays.asList;

public class Constants {
    //DB Manager constants
    public static final String magicString = "org.sqlite.JDBC";

    //View paths
    public static final String fxmlLoginFormPath = "/main/resources/fxml/loginForm.fxml";
    public static final String fxmlClientsViewPath = "/main/resources/fxml/clients.fxml";
    public static final String fxmlClientDetailsViewPath = "/main/resources/fxml/clientDetails.fxml";
    public static final String fxmlGymsViewPath = "/main/resources/fxml/Gyms.fxml";
    public static final String fxmlMainAppFormPath = "/main/resources/fxml/mainApp.fxml";
    public static final String fxmlScheduleViewPath = "/main/resources/fxml/schedule.fxml";
    public static final String fxmlScheduleDetailsViewPath = "/main/resources/fxml/scheduleDetails.fxml";
    public static final String fxmlClientProfileViewPath = "/main/resources/fxml/clientProfile.fxml";
    public static final String fxmlClientProfileDetailsViewPath = "/main/resources/fxml/clientProfileDetails.fxml";

    //MainApp constants
    public static final String appTitleMain = "Ассистент фитнес-тренера: {%s}";
    public static final int appWidth = 900;
    public static final int appHeight = 700;
    public static final String menuAboutLabel = "О программе";
    public static final String menuExitLabel = "Выход";
    public static final String menuEmptyLabel = "";
    public static final String appTitleAbout = "Ассистент фитнес-тренера";
    public static final String aboutAuthor = "Дипломная работа ст. гр. Пв1-17ПО \n ГУО 'Бизнеса и Менеджмента Технологий' \nБелорусского Государственного Университета \nАлейниковой Ксении Анатольевны";

    //Login form constants
    //Form title
    public static final String formTitleLogin = "АФТ Вход";
    //CSS constants
    public static final String styleLabelValidText = "-fx-background-color: #ffffff;";
    public static final String styleLabelErrorText = "-fx-background-color: #bf93a7;";

    //Database manager constants
    public static final String dbPath = "/main/resources/database/enmeter_db.db";
    public static final String dbDriverType = "jdbc:sqlite";
    public static final String sqlCheckUser = "SELECT COUNT(*) FROM users WHERE login = ? AND password = ?;";
    public static final String sqlGetAllClients = "SELECT * FROM client c;";
    public static final String sqlGetAllGyms = "SELECT * FROM gym g;";
    public static final String sqlGetAllSchedules = "SELECT * FROM client_gym_schedule c;";
    public static final String sqlGetAllClientsProfiles = "SELECT * FROM client_profile c;";
    public static final String sqlInsertClients1 = "INSERT INTO client (name, surname, middlename, birthdate, regdate, sex, active) VALUES";
    public static final String sqlInsertClientsProfile = "INSERT INTO client_profile (client_id, profile_id, height, start_date, start_weight, goal) VALUES";
    public static final String sqlDeleteClient = "DELETE FROM client WHERE id = %d;";
    public static final String sqlDeleteSchedule = "DELETE FROM client_gym_schedule WHERE client_id = %d AND gym_id = %d AND wday = %d AND time ='%s';";
    public static final String sqlDeleteClientProfile = "DELETE FROM client_profile WHERE id =%d;";
    public static final String sqlDeleteAllClients = "DELETE FROM client;";
    public static final String sqlDeleteAllGyms = "DELETE FROM gym;";
    public static final String sqlStoreClientContacts = "INSERT INTO client_contact (client_id, contypeid, contact) VALUES";
    public static final String sqlUpdateClientContacts = "UPDATE client_contact SET contact = '%s' WHERE client_id = %d AND contypeid = %d";
    public static final String sqlGetContact = "SELECT * FROM client_contact WHERE client_id = %d;";
    public static final String sqlInsertGym = "INSERT INTO gym (name, address, schedule, phone) VALUES";
    public static final String sqlInsertSchedule = "INSERT INTO client_gym_schedule (client_id, gym_id, time, wday) VALUES";
    public static final String sqlUpdateClient = "UPDATE client SET name='%s', surname = '%s', middlename='%s', birthdate='%s', sex='%s', active='%s' WHERE id=%d;";
    public static final String sqlUpdateClientProfile = "UPDATE client_profile SET height='%d', start_date = '%s', start_weight='%d', goal='%d' WHERE profile_id=%d;";
    public static final String sqlGetClientTrainingsNumber = "SELECT COUNT(*) FROM client_gym_schedule WHERE client_id = %d;";
    public static final String sqlUpdateClientActivityStatus = "UPDATE client SET active = ((SELECT COUNT(*) FROM client_gym_schedule WHERE client_id = %d) > 0) WHERE id = %d;";

    //Clients view constants
    public static final String sexMale = "Мужской";
    public static final String sexFemale = "Женский";
    public static final String modeInactive = "Н/А";
    public static final String modeActive = "А";
    public static final String dateFormat = "dd.MM.yyyy";

    //Client details view constants
    public static final String msgDeleteClient = "Удалить клиента?";

    //Common alert view constants
    public static final String msgConfirmTitle = "Подтвердите действие";
    public static final String msgSaveChanges = "Сохранить изменения?";
    public static final String msgDataErrorTitle = "Ошибка данных";
    public static final String msgFillDataEror = "Пожалуйста, заполните все необходимые поля!";

    //Client profiles view constants
    public static final String colName = "colName";
    public static final String colAverageDate = "colAverageDate";
    public static final String colEndWeight = "colEndWeight";
    public static final String colOptimisticDate = "colOptimisticDate";
    public static final String colPessimisticDate = "colPessimisticDate";
    public static final String colStartDate = "colStartDate";
    public static final String colStartWeight = "colStartWeight";
    public static final String msgFormatCurrClientProfile = "Профиль клиента: %s";
    public static final String msgRemoveProfileHeaderText = "Удалить профиль?";

    //Client profile details view constants
    public static final String msgSaveProfileDataError = "Невозможно сохранить профиль клиента.";
    public static final String msgPressRecalc = "Пожалуйста, нажмите кнопку 'Рассчитать'!";

    //Schedule
    public static final String msgSaveSchedule = "Невозможно сохранить расписание.";
    public static final String msqDeleteSchedule = "Удалить расписание?";

    //For test
    public static final List<String> testFNames = asList("Ксения", "Галина", "Елена", "Екатерина", "Мария", "Наталия",
            "Ольга", "Валентина", "Вера", "Дарья", "Анна", "Лидия", "Людмила");
    public static final List<String> testFSurnames = asList("Алейникова", "Иванова", "Петрова", "Сидорова", "Смирнова",
            "Киселева", "Бондарь", "Рыбакова", "Лихачева", "Романенко");
    public static final List<String> testFMiddleNames = asList("Ивановна", "Петровна", "Романовна", "Сергеевна",
            "Владимировна", "Александровна", "Евгеньевна", "Андреевна", "Юрьевна", "Львовна");
    public static final List<String> testMNames = asList("Владимир", "Евгений", "Юрий", "Владислав", "Михаил",
            "Геннадий", "Павел", "Александр", "Сергей", "Петр", "Станислав", "Глеб", "Анатолий");
    public static final List<String> testMSurnames = asList("Лазарев", "Соколов", "Козлов", "Кузнецов", "Тихонов",
            "Папанов", "Орехов", "Кулагин", "Брекотин", "Рожков");
    public static final List<String> testMMiddleNames = asList("Иванович", "Алексеевич", "Романович", "Сергеевич",
            "Владимирович", "Александрович", "Евгеньевич", "Андреевич", "Юрьевич", "Пантелеевич");
    public static final List<String> testComment = asList("Подготовка к соревнованиям", "Аллергия на сою",
            "Травма правого колена", "Варикоз", "Акцент на бицепс бедра", "Вегетарианка", "Беременность",
            "Травма позвоночника", "Строгий контроль питания");

    public static final List<String> testGymAddress = asList("пр-т Победителей", "ул. П. Мстиславца", "ул. Филимонова",
            "ул. Кальварийская", "ул. Максима Богдановича", "ул. Скрыганова", "ул. Кнорина", "ул. Я. Коласа");

    public static final List<String> testGymName = asList("Адреналин", "EVO", "MegaFit", "Европа", "Империя силы",
            "Powerclub", "CULT");

    public static final List<String> testGymSchedule = asList("пн-пт", "пн-вс", "пн-пт, сб");

    //Weight Loose speed
    public static final int pessimisticSpeed = 3; //constant, kg per month
    public static  final int optimisticSpeed = 5; //constant, kg per month
    public static  final int averageSpeed = 4; //constant, kg per month

    //EMI - IMT Scale
    public static final String level0 = "Выраженный дефицит массы тела";
    public static final String level1 = "Недостаточная масса тела";
    public static final String level2 = "Нормальная масса тела";
    public static final String level3 = "Предожирение";
    public static final String level4 = "Ожирение 1й степени";
    public static final String level5 = "Ожирение 2й степени";
    public static final String level6 = "Ожирение 3й степени";
}
