package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.IOException;
import java.net.URL;
//import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;

public class UpdateAppointmentController implements Initializable {
    @FXML
    private TextField appointmentIDText;

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<String> contactComboBox;

    @FXML
    private Label contactError;

    @FXML
    private Label customerIDError;

    @FXML
    private TextField customerIDText;

    @FXML
    private Label descriptionError;

    @FXML
    private TextArea descriptionFieldText;

    @FXML
    private Label endDateError;

    @FXML
    private DatePicker endDateText;

    @FXML
    private ComboBox<String> endTimeComboBox;

    @FXML
    private Label endTimeError;

    @FXML
    private Label errorTextLabel;

    @FXML
    private Label locationError;

    @FXML
    private TextField locationText;

    @FXML
    private Label startDateError;

    @FXML
    private DatePicker startDateText;

    @FXML
    private ComboBox<String> startTimeComboBox;

    @FXML
    private Label startTimeError;

    @FXML
    private Button submitButton;

    @FXML
    private Label titleError;

    @FXML
    private TextField titleText;

    @FXML
    private Label userIDError;

    @FXML
    private TextField userIDText;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private Label typeError;

    Stage stage;

    static ObservableList<Appointments> allAppointments;

    static ObservableList<Contacts> allContacts = FXCollections.observableArrayList();

    static ObservableList<String> allContactNames = FXCollections.observableArrayList();

    static ObservableList<String> allTimesForComboBox = FXCollections.observableArrayList();

    static ObservableList<String> allTypesForComboBox = FXCollections.observableArrayList();

    public static Appointments selectedAppointment;

    public static int userID;

    public static LocalDate selectedStartDate;

    public static LocalDate selectedEndDate;

    public static LocalTime selectedStartTime;

    public static String selectedStartLocalTime;

    public static String selectedEndLocalTime;

    public static LocalTime selectedEndTime;

    public static String selectedContact;

    public static String selectedType;

    public static int selectedApptID;

    public static Timestamp selectedCreatedTime;

    static ObservableList<Appointments> allAppointmentsForThisUserID = FXCollections.observableArrayList();

    static long timeDifference;

    /**
     * initSelectedAppointment accepts the selected appointment from the previous scene and assigns it's appointmentID to selectedApptID.
     * @param appointment
     */
    @FXML
    public static void initSelectedAppointment(Appointments appointment) {
        selectedAppointment = appointment;
        selectedApptID = appointment.getApptID();
    }

    /**
     * initUserID accepts an integer from another controller and assigns the ObservableList allAppointmentsForThisUserID.
     * @param userIDNumber
     * @throws SQLException
     */
    @FXML
    public static void initUserID(int userIDNumber) {
        userID = userIDNumber;
    }

    /**
     * initAllAppts accepts an observableList of all appointments and assigns it to an observableList.
     * @param allApts
     */
    @FXML
    public static void initAllAppts(ObservableList allApts) {
        allAppointments = allApts;
    }

    /**
     * initAllTypes queries the database for all types and sets it to an observableList to be used in the Types combo box.
     * @throws SQLException
     */
    @FXML
    public static void initAllTypes() throws SQLException {
        allTypesForComboBox = AppointmentsQuery.allTypes();
    }

    /**
     * initSelectedType retrieves the type associated with the selected appointment.
     * @param selectedAppointment
     */
    @FXML
    public static void initSelectedType(Appointments selectedAppointment) {
        selectedType = selectedAppointment.getType();
    }

    /**
     * initFormattedTimes formats the selected appointment's start and end date to local time.
     * @param selectedAppointment
     */
    @FXML
    public static void initFormattedTimes(Appointments selectedAppointment) {
        selectedCreatedTime = selectedAppointment.getCreateDate();
        Timestamp selectedEndTimestamp = selectedAppointment.getEnd();
        Timestamp selectedStartTimestamp = selectedAppointment.getStart();
        LocalDateTime selectedStartLocalDateTime = selectedStartTimestamp.toLocalDateTime();
        LocalDateTime selectedEndLocalDateTime = selectedEndTimestamp.toLocalDateTime();
        String tempSelectedStartLocalTime = selectedStartTimestamp.toLocalDateTime().toLocalTime().toString();
        String tempSelectedEndLocalTime = selectedEndTimestamp.toLocalDateTime().toLocalTime().toString();

        selectedStartDate = selectedStartLocalDateTime.toLocalDate();
        selectedEndDate = selectedEndLocalDateTime.toLocalDate();

        System.out.println(tempSelectedEndLocalTime);

        selectedStartLocalTime = localTimeConverter(tempSelectedStartLocalTime);
        selectedEndLocalTime = localTimeConverter(tempSelectedEndLocalTime);
    }

    /**
     * initApptTimes adds strings representing possible times for appointments to the observable list.
     */
    @FXML
    public static void initApptTimes() {
        allTimesForComboBox.add("12:00 AM");
        allTimesForComboBox.add("1:00 AM");
        allTimesForComboBox.add("2:00 AM");
        allTimesForComboBox.add("3:00 AM");
        allTimesForComboBox.add("4:00 AM");
        allTimesForComboBox.add("5:00 AM");
        allTimesForComboBox.add("6:00 AM");
        allTimesForComboBox.add("7:00 AM");
        allTimesForComboBox.add("8:00 AM");
        allTimesForComboBox.add("9:00 AM");
        allTimesForComboBox.add("10:00 AM");
        allTimesForComboBox.add("11:00 AM");
        allTimesForComboBox.add("12:00 PM");
        allTimesForComboBox.add("1:00 PM");
        allTimesForComboBox.add("2:00 PM");
        allTimesForComboBox.add("3:00 PM");
        allTimesForComboBox.add("4:00 PM");
        allTimesForComboBox.add("5:00 PM");
        allTimesForComboBox.add("6:00 PM");
        allTimesForComboBox.add("7:00 PM");
        allTimesForComboBox.add("8:00 PM");
        allTimesForComboBox.add("9:00 PM");
        allTimesForComboBox.add("10:00 PM");
        allTimesForComboBox.add("11:00 PM");
    }

    /**
     * localTimeConverter looks at the value of the string selected in the combo box and assigns a formated String result, which it returns.
     * @param comboBoxTime String value of the selection in the combo box.
     * @return
     */
    @FXML
    public static String localTimeConverter(String comboBoxTime) {
        String result = null;
        if (comboBoxTime.equals("00:00")){
            result = "12:00 AM";
        }
        if (comboBoxTime.equals("01:00")){
            result = "1:00 AM";
        }
        if (comboBoxTime.equals("02:00")){
            result = "2:00 AM";
        }
        if (comboBoxTime.equals("03:00")){
            result = "3:00 AM";
        }
        if (comboBoxTime.equals("04:00")){
            result = "4:00 AM";
        }
        if (comboBoxTime.equals("05:00")){
            result = "5:00 AM";
        }
        if (comboBoxTime.equals("06:00")){
            result = "6:00 AM";
        }
        if (comboBoxTime.equals("07:00")){
            result = "7:00 AM";
        }
        if (comboBoxTime.equals("08:00")){
            result = "8:00 AM";
        }
        if (comboBoxTime.equals("09:00")){
            result = "9:00 AM";
        }
        if (comboBoxTime.equals("10:00")){
            result = "10:00 AM";
        }
        if (comboBoxTime.equals("11:00")){
            result = "11:00 AM";
        }
        if (comboBoxTime.equals("12:00")){
            result = "12:00 PM";
        }
        if (comboBoxTime.equals("13:00")){
            result = "1:00 PM";
        }
        if (comboBoxTime.equals("14:00")){
            result = "2:00 PM";
        }
        if (comboBoxTime.equals("15:00")){
            result = "3:00 PM";
        }
        if (comboBoxTime.equals("16:00")){
            result = "4:00 PM";
        }
        if (comboBoxTime.equals("17:00")){
            result = "5:00 PM";
        }
        if (comboBoxTime.equals("18:00")){
            result = "6:00 PM";
        }
        if (comboBoxTime.equals("19:00")){
            result = "7:00 PM";
        }
        if (comboBoxTime.equals("20:00")){
            result = "8:00 PM";
        }
        if (comboBoxTime.equals("21:00")){
            result = "9:00 PM";
        }
        if (comboBoxTime.equals("22:00")){
            result = "10:00 PM";
        }
        if (comboBoxTime.equals("23:00")){
            result = "11:00 PM";
        }
        return result;
    }

    @FXML
    public static String timeConverter(String comboBoxTime) {
        String result = null;
        if (comboBoxTime.equals("12:00 AM")){
            result = "00:00:00";
        }
        if (comboBoxTime.equals("1:00 AM")){
            result = "01:00:00";
        }
        if (comboBoxTime.equals("2:00 AM")){
            result = "02:00:00";
        }
        if (comboBoxTime.equals("3:00 AM")){
            result = "03:00:00";
        }
        if (comboBoxTime.equals("4:00 AM")){
            result = "04:00:00";
        }
        if (comboBoxTime.equals("5:00 AM")){
            result = "05:00:00";
        }
        if (comboBoxTime.equals("6:00 AM")){
            result = "06:00:00";
        }
        if (comboBoxTime.equals("7:00 AM")){
            result = "07:00:00";
        }
        if (comboBoxTime.equals("8:00 AM")){
            result = "08:00:00";
        }
        if (comboBoxTime.equals("9:00 AM")){
            result = "09:00:00";
        }
        if (comboBoxTime.equals("10:00 AM")){
            result = "10:00:00";
        }
        if (comboBoxTime.equals("11:00 AM")){
            result = "11:00:00";
        }
        if (comboBoxTime.equals("12:00 PM")){
            result = "12:00:00";
        }
        if (comboBoxTime.equals("1:00 PM")){
            result = "13:00:00";
        }
        if (comboBoxTime.equals("2:00 PM")){
            result = "14:00:00";
        }
        if (comboBoxTime.equals("3:00 PM")){
            result = "15:00:00";
        }
        if (comboBoxTime.equals("4:00 PM")){
            result = "16:00:00";
        }
        if (comboBoxTime.equals("5:00 PM")){
            result = "17:00:00";
        }
        if (comboBoxTime.equals("6:00 PM")){
            result = "18:00:00";
        }
        if (comboBoxTime.equals("7:00 PM")){
            result = "19:00:00";
        }
        if (comboBoxTime.equals("8:00 PM")){
            result = "20:00:00";
        }
        if (comboBoxTime.equals("9:00 PM")){
            result = "21:00:00";
        }
        if (comboBoxTime.equals("10:00 PM")){
            result = "22:00:00";
        }
        if (comboBoxTime.equals("11:00 PM")){
            result = "23:00:00";
        }
        return result;
    }


    /**
     * dateTimeFormatter accepts a local time and date and returns a timestamp corresponding to the UTC equivalent for storage on the database.
     * @param lt local time.
     * @param ld local date.
     * @return
     */
    @FXML
    public static Timestamp dateTimeFormatter(LocalTime lt,LocalDate ld) {
        String formattedString = null;
        ZoneId setZoneID = ZoneId.of("UTC");
        LocalDateTime mldt = LocalDateTime.of(ld,lt);
        ZoneId mzid = ZoneId.systemDefault();
        ZoneId utcZoneID = ZoneId.of("UTC");
        ZonedDateTime myZDT = ZonedDateTime.of(mldt,utcZoneID);


        formattedString = myZDT.toLocalDate().toString() + " " + lt +":00";
//        System.out.println(formattedString);
        return Timestamp.valueOf(formattedString);
    }

    /**
     * initSelectedContact takes in an appointment and returns the String value of the corresponding contact.
     * @param selectedAppointment
     * @return
     * @throws SQLException
     */
    @FXML
    public static String initSelectedContact(Appointments selectedAppointment) throws SQLException {
        int selectedContactID = selectedAppointment.getContactID();
        selectedContact = ContactsQuery.getContactName(selectedContactID);
        return selectedContact;
    }

    /**
     * initAllContacts queries the database for an observable list of all contacts and all contact names and stores them in an observable list.
     * @throws SQLException
     */
    @FXML
    public static void initAllContacts() throws SQLException {
        allContacts = ContactsQuery.allContacts();
        allContactNames = ContactsQuery.allContactNames();
    }

    /**
     * cancelButton returns the user to mainScene.fxml without making any changes to the records on the database.
     * @param event - user click.
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void cancelButton(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainScene.fxml"));
        MainSceneController controller = fxmlLoader.getController();
        controller.initAllAppts(allAppointments);
        controller.initUserID(userID);
        controller.initAllCustomers();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * initTimeDifference captures the users current timezone and compares it to the EST timezone, the difference is saved to an int value and used to adjust the current time before performing checks.
     */
    public static void initTimeDifference() {

        String thisTimeZone = TimeZone.getDefault().getID();
        ZoneId thisZone = ZoneId.of(thisTimeZone);

        String businessTimeZone3 = String.valueOf(ZoneId.of("America/New_York"));

        ZoneId businessTimeZone = ZoneId.of("America/New_York");

        TimeZone businessTimeZone2 = TimeZone.getTimeZone("America/New_York");



        String startDateTimeStr = null;
        String endDateTimeStr = null;
        ZoneId setZoneID = ZoneId.of("America/New_York");
        LocalDateTime startLDT = LocalDateTime.now();
        LocalDateTime endLDT = LocalDateTime.now();

        ZoneId mzid = ZoneId.systemDefault();
        ZoneId estZoneID = ZoneId.of("America/New_York");
        ZonedDateTime startZDT = ZonedDateTime.of(startLDT,estZoneID);
        ZonedDateTime endZDT = ZonedDateTime.of(endLDT,estZoneID);
        ZonedDateTime startRawZDT = ZonedDateTime.of(startLDT,mzid);
        ZonedDateTime endRawZDT = ZonedDateTime.of(endLDT,mzid);

//        DateFormat altFormat = new SimpleDateFormat("hh:mm");
//        altFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));

        LocalTime adjustedStartTime = LocalTime.now();
        LocalTime adjustedEndTime = LocalTime.now();

//        LocalTime adjustedStartTime = startZDT.toLocalTime();
//        LocalTime adjustedEndTime = endZDT.toLocalTime();

        LocalTime businessStartTime = LocalTime.of(8,0);
        LocalTime businessEndTime = LocalTime.of(22,0);

        ZonedDateTime businessTimestamp = ZonedDateTime.now(estZoneID);
        ZonedDateTime nowTimestamp = ZonedDateTime.now();

        ZoneOffset zoneOffsetAppt = thisZone.getRules().getOffset(LocalDateTime.now(ZoneId.from(nowTimestamp))); // THIS
        ZoneOffset zoneOffsetBusiness = businessTimeZone.getRules().getOffset(Instant.from(businessTimestamp)); // THIS

        String strZOA = String.valueOf(zoneOffsetAppt);

        long result = zoneOffsetAppt.compareTo(zoneOffsetBusiness) / 3600;

        OffsetTime offsetStart = adjustedStartTime.atOffset(zoneOffsetAppt);
        OffsetTime offsetEnd = adjustedEndTime.atOffset(zoneOffsetAppt);
        OffsetTime offsetBusinessStart = businessStartTime.atOffset(zoneOffsetBusiness);
        OffsetTime offsetBusinessEnd = businessEndTime.atOffset(zoneOffsetBusiness);

        timeDifference = result;

        System.out.println("offset compare to: " + result);

    }

    /**
     * timeCheck performs various checks and returns true if this starttime and endtime are acceptable to add to the database.
     * @param lt local start time.
     * @param ld local start date.
     * @param ltEnd local end time.
     * @param ldEnd local end date.
     * @param timeDifference the time difference value from the previous method.
     * @return
     * @throws ParseException
     */
    public static boolean timeCheck(LocalTime lt, LocalDate ld, LocalTime ltEnd, LocalDate ldEnd,long timeDifference) throws ParseException {
        boolean result = true;

        String thisTimeZone = TimeZone.getDefault().getID();


        Date date2 = new Date();
        long time2 = date2.getTime();
//        TimeZone.setDefault(TimeZone.getTimeZone("EST"));
        Timestamp ts2 = new Timestamp(time2);


        String startDateTimeStr = null;
        String endDateTimeStr = null;
        ZoneId setZoneID = ZoneId.of("America/New_York");
        LocalDateTime startLDT = LocalDateTime.of(ld,lt);
        LocalDateTime endLDT = LocalDateTime.of(ldEnd,ltEnd);

        ZoneId mzid = ZoneId.systemDefault();
        ZoneId estZoneID = ZoneId.of("America/New_York");
        ZonedDateTime startZDT = ZonedDateTime.of(startLDT,estZoneID);
        ZonedDateTime endZDT = ZonedDateTime.of(endLDT,estZoneID);
        ZonedDateTime startRawZDT = ZonedDateTime.of(startLDT,mzid);
        ZonedDateTime endRawZDT = ZonedDateTime.of(endLDT,mzid);


        DayOfWeek startDayOfWeek = DayOfWeek.from(startZDT);
        DayOfWeek endDayOfWeek = DayOfWeek.from(endZDT);


        LocalTime adjustedStartTime = startZDT.toLocalTime();
        LocalTime adjustedEndTime = endZDT.toLocalTime();


        LocalTime businessStartTime = LocalTime.of(8,0);
        LocalTime businessEndTime = LocalTime.of(22,0);

        LocalTime correctedLocalStartTime = adjustedStartTime.plusHours(timeDifference);
        LocalTime correctedLocalEndTime = adjustedEndTime.plusHours(timeDifference);

        System.out.println("corrected local start time: " + correctedLocalStartTime);



        int compareStartToStartingTime = correctedLocalStartTime.compareTo(businessStartTime);
        int compareStartToEndingTime = correctedLocalStartTime.compareTo(businessEndTime);
        int compareEndToEndingTime = correctedLocalEndTime.compareTo(businessEndTime);



//        if (startDayOfWeek == sunday || startDayOfWeek == saturday || endDayOfWeek == sunday || endDayOfWeek == saturday) {
//            result = false;
//            System.out.println("Day of week check failed.");
//            errorTextLabel.setVisible(true);
//            errorTextLabel.setText("Appointments must be between business hours, 8AM-10PM EST.");
//
//        }

        if (compareStartToStartingTime < 0) {
            result = false;
            System.out.println("Start time outside business hours.");
//            errorTextLabel.setVisible(true);
//            errorTextLabel.setText("Appointments must be between business hours, 8AM-10PM EST.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Starting Date Error");
            alert.setHeaderText("Error");
            alert.setContentText("Appointments must be between business hours, 8AM-10PM EST.");
            alert.show();


        }

        if (compareStartToEndingTime > 0) {
            result = false;
            System.out.println("Start time outside business hours..");
//            errorTextLabel.setVisible(true);
//            errorTextLabel.setText("Appointments must be between business hours, 8AM-10PM EST.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Date Error");
            alert.setHeaderText("Error");
            alert.setContentText("Appointments must be between business hours, 8AM-10PM EST.");
            alert.show();

        }

        if (compareEndToEndingTime > 0) {
            result = false;
            System.out.println("End time outside business hours.");
//            errorTextLabel.setVisible(true);
//            errorTextLabel.setText("Appointments must be between business hours, 8AM-10PM EST.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Date Error");
            alert.setHeaderText("Error");
            alert.setContentText("Appointments must be between business hours, 8AM-10PM EST.");
            alert.show();

        }


        startDateTimeStr = startZDT.toLocalDate().toString() + " " + lt +":00";
        endDateTimeStr = endZDT.toLocalDate().toString() + " " + lt +":00";

        Timestamp startDateTime = Timestamp.valueOf(startDateTimeStr);
        Timestamp endDateTime = Timestamp.valueOf(endDateTimeStr);


        if (endDateTime.before(startDateTime)) {
            result = false;
            System.out.println("end datetime before start datetime check failed.");
//            errorTextLabel.setVisible(true);
//            errorTextLabel.setText("Appointment end time must be after its' starting time.");
            Alert alert = new Alert(Alert.AlertType.WARNING);alert.setTitle("Date Error");
            alert.setHeaderText("Error");
            alert.setContentText("Appointment End DateTime must be after its starting time.");
            alert.show();

        }

        if (startDateTime.before(ts2)) {
            result = false;
            System.out.println("current time after start datetime check failed.");
//            errorTextLabel.setVisible(true);
//            errorTextLabel.setText("Cannot schedule appointment for a time that has already passed.");
            Alert alert = new Alert(Alert.AlertType.WARNING);alert.setTitle("Date Error");
            alert.setHeaderText("Error");
            alert.setContentText("Appointment start time cannot be in the past.");
            alert.show();
        }

//        if (startDateTime.equals(endDateTime)) {
//            result = false;
//            System.out.println("start and end time identical.");
//            System.out.println(startDateTime);
//            System.out.println(endDateTime);
////            errorTextLabel.setVisible(true);
////            errorTextLabel.setText("Cannot schedule appointment for a time that has already passed.");
//            Alert alert = new Alert(Alert.AlertType.WARNING);alert.setTitle("Date Error");
//            alert.setHeaderText("Error");
//            alert.setContentText("An Appointment cannot have the same start and end time.");
//            alert.show();
//        }

        return result;

    }

    /**
     * overlapCheck performs a check if there's an existing appointment within this timeframe.
     * @param startDateTime the submitted startdate time.
     * @param endDateTime the submitted enddate time.
     * @return true if all the checks pass.
     */
    public static boolean overlapCheck(Timestamp startDateTime, Timestamp endDateTime) {
        boolean result = true;

        for (Appointments appointment : allAppointmentsForThisUserID) {
            boolean loopCheck1 = false;
            boolean loopCheck2 = false;

            Timestamp recordedStart = appointment.getStart();
            Timestamp recordedEnd = appointment.getEnd();

            int rcStartToStartDateTime = recordedStart.compareTo(startDateTime); // negative if observedList startTime is before submitted start time.
            int startDateTimeToRcStart = startDateTime.compareTo(recordedStart);

            int rcStartToEndDateTime = recordedStart.compareTo(endDateTime); // positive if observedList startTime is after submitted end time.
            int endDateTimeToRcStart = endDateTime.compareTo(recordedStart);

            int rcEndToStartDateTime = recordedEnd.compareTo(startDateTime); // negative if observedList endTime is before submitted end time.
            int startDateTimeToRcEnd = startDateTime.compareTo(recordedEnd);

            int rcEndToEndDateTime = recordedEnd.compareTo(endDateTime);
            int endDateTimeToRcEnd = endDateTime.compareTo(recordedEnd);

            if (rcStartToStartDateTime < 0 && rcEndToStartDateTime <= 0) {
                loopCheck1 = true;
            } else if (rcStartToEndDateTime >= 0 && rcEndToEndDateTime > 0) {
                loopCheck2 = true;
            }

            if (loopCheck1 == false && loopCheck2 == false) {
                int apptID = appointment.getApptID();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Date Error");
                alert.setHeaderText("Error");
                alert.setContentText("This appointment has a scheduling overlap with appointment ID: " + apptID);
                alert.show();
                result = false;
                break;
            }

        }


        return result;
    }

    /**
     * submitButton commits the changes to the updated record on the database, if it passes the quality checks.
     * @param event
     * @throws IOException
     * @throws SQLException
     * @throws ParseException
     */
    @FXML
    void submitButton(ActionEvent event) throws IOException, SQLException, ParseException {
        boolean passCheck1 = false;

        errorTextLabel.setVisible(false);
        titleError.setVisible(false);
        locationError.setVisible(false);
        contactError.setVisible(false);
        startDateError.setVisible(false);
        startTimeError.setVisible(false);
        endDateError.setVisible(false);
        endTimeError.setVisible(false);
        customerIDError.setVisible(false);
        userIDError.setVisible(false);
        descriptionError.setVisible(false);
        typeError.setVisible(false);

        String title = titleText.getText();
        String location = locationText.getText();
//        String contact = contactComboBox.getValue().toString();
//        LocalDate startDate = startDateText.getValue();
//        String startTime = startTimeComboBox.getValue().toString();
//        LocalDate endDate = endDateText.getValue();
//        String endTime = endTimeComboBox.getValue().toString();
        String type = typeComboBox.getValue();
        String customerID = customerIDText.getText();
        int userIDNew = Integer.parseInt(userIDText.getText());
        String descriptionText = descriptionFieldText.getText();

        if (title.equals("")) {
            titleError.setVisible(true);
            errorTextLabel.setVisible(true);
            errorTextLabel.setText("* Required Field");
        }

        if (location.equals("")) {
            locationError.setVisible(true);
            errorTextLabel.setVisible(true);
            errorTextLabel.setText("* Required Field");
        }

        if (contactComboBox.getValue() == null) {
            contactError.setVisible(true);
            errorTextLabel.setVisible(true);
            errorTextLabel.setText("* Required Field");
        }

        if (startDateText.getValue() == null) {
            startDateError.setVisible(true);
            errorTextLabel.setVisible(true);
            errorTextLabel.setText("* Required Field");
        }

        if (startTimeComboBox.getValue() == null) {
            startTimeError.setVisible(true);
            errorTextLabel.setVisible(true);
            errorTextLabel.setText("* Required Field");
        }

        if (endDateText.getValue() == null) {
            endDateError.setVisible(true);
            errorTextLabel.setVisible(true);
            errorTextLabel.setText("* Required Field");
        }

        if (endTimeComboBox.getValue() == null) {
            endTimeError.setVisible(true);
            errorTextLabel.setVisible(true);
            errorTextLabel.setText("* Required Field");
        }

        if (customerID.equals("")) {
            customerIDError.setVisible(true);
            errorTextLabel.setVisible(true);
            errorTextLabel.setText("* Required Field");
        }

        if (userIDText.equals("")) {
            userIDError.setVisible(true);
            errorTextLabel.setVisible(true);
            errorTextLabel.setText("* Required Field");
        }

        if (descriptionText.equals("")) {
            descriptionError.setVisible(true);
            errorTextLabel.setVisible(true);
            errorTextLabel.setText("* Required Field");
        }

        if (typeComboBox.getValue() == null){
            typeError.setVisible(true);
            errorTextLabel.setVisible(true);
            errorTextLabel.setText("* Required Field");
        }

        Date date = new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);

        String testTime = timeConverter(startTimeComboBox.getValue());
        String endTime = timeConverter(endTimeComboBox.getValue());
        LocalTime ltStart = LocalTime.parse(testTime);
        LocalTime ltEnd = LocalTime.parse(endTime);
        LocalDate ldStart = startDateText.getValue();
        LocalDate ldEnd = endDateText.getValue();

//        LocalDateTime mldt = LocalDateTime.of(ldStart,ltStart);
//        ZoneId mzid = ZoneId.systemDefault();
//        ZonedDateTime myZDT = ZonedDateTime.of(mldt,mzid);

        boolean passCheck2 = timeCheck(ltStart,ldEnd,ltEnd,ldEnd,timeDifference);

        Timestamp startDateTime = dateTimeFormatter(ltStart,ldStart);
        Timestamp endDateTime = dateTimeFormatter(ltEnd,ldEnd);

        boolean passCheck3 = overlapCheck(startDateTime,endDateTime);

        String userName = UsersQuery.getUserName(userID);

        String contactName = contactComboBox.getValue();
        int contactID = ContactsQuery.getContactID(contactName);

        int realCustomerID = Integer.parseInt(customerID);


        if (!title.equals("")
                && !location.equals("")
                && contactComboBox.getValue() != null
                && startDateText.getValue() != null
                && startTimeComboBox.getValue() != null
                && endDateText.getValue() != null
                && endTimeComboBox.getValue() != null
                && !customerID.equals("")
                && !userIDText.equals("")
                && !descriptionText.equals("")
                && typeComboBox.getValue() != null) {
            passCheck1 = true;
        }



        if (passCheck1 == true && passCheck2 == true) {
            System.out.println(contactID);
            AppointmentsQuery.updateAppointment(title,descriptionText,location,type,startDateTime,endDateTime,selectedCreatedTime,userName,ts,userName,realCustomerID,userIDNew,contactID,selectedApptID);
            allAppointments = AppointmentsQuery.allAppointmentsByUserID(userID);
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainScene.fxml"));
            MainSceneController controller = fxmlLoader.getController();
            controller.initAllAppts(allAppointments);
            controller.initUserID(userID);
            controller.initAllCustomers();

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactComboBox.setItems(allContactNames);
        startTimeComboBox.setItems(allTimesForComboBox);
        endTimeComboBox.setItems(allTimesForComboBox);
        typeComboBox.setItems(allTypesForComboBox);

        if (selectedAppointment != null) {
//
//                InHouse c = (InHouse) selectedPart;
//                inHouseRadioButton.setSelected(true);
//                textToToggle.setText("Machine ID");
                this.appointmentIDText.setText(String.valueOf(selectedAppointment.getApptID()));
                this.titleText.setText(String.valueOf(selectedAppointment.getTitle()));
                this.locationText.setText(String.valueOf(selectedAppointment.getLocation()));
                this.contactComboBox.setValue(selectedContact);
                this.customerIDText.setText(String.valueOf(selectedAppointment.getCustomerID()));
                this.userIDText.setText(String.valueOf(selectedAppointment.getUserID()));
                this.descriptionFieldText.setText(String.valueOf(selectedAppointment.getDescription()));
                this.startDateText.setValue(selectedStartDate);
                this.endDateText.setValue(selectedEndDate);
                this.startTimeComboBox.setValue(selectedStartLocalTime);
                this.endTimeComboBox.setValue(selectedEndLocalTime);
                this.typeComboBox.setValue(selectedType);

            }

    }
}
