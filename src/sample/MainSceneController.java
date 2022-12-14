package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <code>MainSceneController</code> acts as the controller for the main scene after logging in.
 * @author Andrew Hobbs
 */

public class MainSceneController implements Initializable {

    @FXML
    private AnchorPane scenePane;

    Stage stage;

    @FXML
    private Label errorLabel;

    @FXML
    private Button exitButton;

    @FXML
    private Button updateCustomer;

    @FXML
    private Button addCustomerButton;

    @FXML
    private Button addAppointmentButton;

    @FXML
    private Button updateAppointmentButton;

    @FXML
    private Button viewAllAppointmentsButton;

    @FXML
    private TableView<Appointments> appointmentsTable;

    @FXML
    private TableColumn<Appointments, java.sql.Timestamp> startTime;

    @FXML
    private TableColumn<Appointments, Integer> apptID;

    @FXML
    private Button reportsButton;

    @FXML
    private Button deleteAppointmentButton;

    Locale currentLocale = Locale.getDefault();

    public static ObservableList<Appointments> allAppointments;

    public static int userID;

    public static String userName;

    @FXML
    private TableColumn<Customers, String> customersAddressColumn;

    @FXML
    private TableColumn<Customers, String> customersNameColumn;

    @FXML
    private TableColumn<Customers, String> customersPhoneColumn;

    @FXML
    private TableView<Customers> customersTable;

    @FXML
    private Button deleteCustomerButton;

    @FXML
    private TextField appointmentSearchText;

    @FXML
    private TextField customerSearchText;

    /**
     * initUserID - retrieves the cooresponding UserName that would match the userID.
     * @param userIDNumber - the userID number sent in from login controller.
     * @throws SQLException
     */
    @FXML
    public static void initUserID(int userIDNumber) throws SQLException {
        userID = userIDNumber;
        userName = UsersQuery.getUserName(userID);
    }

    /**
     * initAllAppts - accepts the ObservedList of appointments from the login controller.
     * @param allApts - all apts refers to the empty observable list declared.
     */
    @FXML
    public static void initAllAppts(ObservableList allApts) {
        allAppointments = allApts;
    }

    /**
     * allCustomers is an observableList of all customers .
     */
    @FXML
    public static ObservableList<Customers> allCustomers;

    /**
     * initAllCustomers queries the database for all Customers and stores them in an observable list.
     * @throws SQLException
     */
    @FXML static void initAllCustomers() throws SQLException {
        allCustomers = CustomersQuery.getAllCustomers();
    }

    /**
     * intAllCustomers
     * @param allCustomersFromModify - an observable list of the updated observable list of customers.
     */
    @FXML
    static void initAllCustomersExisting(ObservableList allCustomersFromModify) {
        allCustomers = allCustomersFromModify;
    }

    @FXML
    private Label userNameLabel;


    @FXML
    void exitProgram(ActionEvent event) throws IOException {
        errorLabel.setVisible(false);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("You're about to exit");
        alert.setContentText("Are you sure you want to exit the program?");

        if(alert.showAndWait().get() == ButtonType.OK) {
            stage = (Stage) scenePane.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * initUpcomingApptCheck checks if the user that just logged in has an upcoming appointment within 15 minutes.
     */
    @FXML
    static void initUpcomingApptCheck() {

        String formattedString = null;
        LocalDateTime mldt = LocalDateTime.now();
        ZoneId mzid = ZoneId.systemDefault();
        ZonedDateTime myZDT = ZonedDateTime.of(mldt,mzid);

        LocalDateTime correctedLocalTime = mldt.plusMinutes(15);

//        formattedString = myZDT.toLocalDate().toString() + " " + LocalTime.now();
//        Timestamp correctedLocal = Timestamp.valueOf(formattedString);


        LocalTime currentTimestamp = LocalTime.now();
        for (Appointments appointment : allAppointments) {
            Timestamp storedAppointmentStart = appointment.getStart();
            LocalDateTime apptStart = storedAppointmentStart.toLocalDateTime();
            int upcomingApptID = appointment.getApptID();

            DateTimeFormatter correctedFormat = DateTimeFormatter.ofPattern("E, dd/MM HH:mm ");

            int timeComparison = correctedLocalTime.compareTo(apptStart);

            if (timeComparison >= 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Upcoming appointment.");
                alert.setHeaderText("Appointment ID: " + upcomingApptID);
                alert.setContentText("You have an upcoming appointment at " + apptStart.format(correctedFormat));
                alert.show();
            }
        }

    }

    /**
     * switchToAddCustomer - changes the scene to addCustomer.fxml
     * @param event - user click.
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void switchToAddCustomer(ActionEvent event) throws IOException, SQLException {
        errorLabel.setVisible(false);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("addCustomer.fxml"));
        AddCustomerController controller = fxmlLoader.getController();
        controller.initCountriesForComboBox();
        controller.initUserID(userID);


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * switchToUpdateCustomer - changes the scene to updateCustomer.fxml
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void switchToUpdateCustomer(ActionEvent event) throws IOException, SQLException {
        errorLabel.setVisible(false);
        Customers selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("updateCustomer.fxml"));
            UpdateCustomerController controller = fxmlLoader.getController();
            controller.initSelectedCustomer(selectedCustomer);
            controller.initCountriesForComboBox();
            controller.initUserID(userID);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * switchToAddAppointment - changes to scene to addAppointment.fxml
     * @param event - user click.
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void switchToAddAppointment(ActionEvent event) throws IOException, SQLException {
        errorLabel.setVisible(false);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("addAppointment.fxml"));
        AddAppointmentController controller = fxmlLoader.getController();
        controller.initAllAppts(allAppointments);
        controller.initUserID(userID);
        controller.initAllContacts();
        controller.initApptTimes();
        controller.initAllTypes();
        controller.initTimeDifference();


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * switchToUpdateAppointment - changes the scene to updateAppointment.fxml
     * @param event - user click.
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void switchToUpdateAppointment(ActionEvent event) throws IOException, SQLException {
        errorLabel.setVisible(false);
        Appointments selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("updateAppointment.fxml"));
            UpdateAppointmentController controller = fxmlLoader.getController();

            controller.initSelectedAppointment(selectedAppointment);
            controller.initAllAppts(allAppointments);
            controller.initUserID(userID);
            controller.initAllContacts();
            controller.initApptTimes();
            controller.initAllTypes();
            controller.initFormattedTimes(selectedAppointment);
            controller.initSelectedContact(selectedAppointment);
            controller.initSelectedType(selectedAppointment);
            controller.initTimeDifference();

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * switchToViewAllAppointments -  changes the scene to viewAllAppointments.fxml
     * @param event - user click.
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void switchToViewAllAppointments(ActionEvent event) throws IOException, SQLException {
        errorLabel.setVisible(false);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("viewAllAppointments.fxml"));
        viewAllAppointmentsController controller = fxmlLoader.getController();
        controller.initUserID(userID);
        controller.initAllAppointmentsForAllUsers();
        controller.initCurrentWeek();


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * switchToReports - changes the scene to reports.fxml
     * @param event - user click.
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void switchToReports(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("reports.fxml"));
        ReportsController controller = fxmlLoader.getController();
//        controller.initAddProductData(imsInventory);
        controller.initMonthsForComboBox();
        controller.initAllContacts();
        controller.initAllAppointmentsForAllUsers();
        controller.initAllUsers();


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * deleteAppointment - checks if the user has selected an appointment on the table, and removes it from the database.
     * @param event - user click.
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void deleteAppointment(MouseEvent event) throws IOException, SQLException {
        errorLabel.setVisible(false);
        Appointments appointment = appointmentsTable.getSelectionModel().getSelectedItem();
        String appointmentType = appointment.getType();
        int appointmentID = appointment.getApptID();
        if (appointment != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Appointment Deletion Confirmation");
            alert.setHeaderText("You are about to delete appointment " + appointmentID);
            alert.setContentText("Are you sure you want to delete this " + appointmentType + " appointment?");

            if(alert.showAndWait().get() == ButtonType.OK) {

                allAppointments.remove(appointment);
                AppointmentsQuery.deleteAppointment(appointment.getApptID());

            }

        }
    }

    /**
     * deleteCustomer - checks if the user has selected a user on the user table and deletes it from the database if there isn't an appointment scheduled for this customer.
     * @param event - mouse click.
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void deleteCustomer(MouseEvent event) throws IOException, SQLException {
        boolean passCheck = true;
        Customers customer = customersTable.getSelectionModel().getSelectedItem();
        int customerID = customer.getCustomerID();
        String customerName = customer.getCustomerName();
        if (customer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Appointment Deletion Confirmation");
            alert.setHeaderText("You are about to delete customer: " + customerName);
            alert.setContentText("Are you sure you want to delete this customer?");

            if(alert.showAndWait().get() == ButtonType.OK) {
                for(Appointments appointment : allAppointments) {
                    if(appointment.getCustomerID() == customerID) {
                        passCheck = false;
                        errorLabel.setVisible(true);
                        errorLabel.setText(customerName + " cannot be removed because of a conflicting appointment still scheduled.");
                    }
                }
                if(passCheck) {
                allCustomers.remove(customer);
                CustomersQuery.deleteCustomer(customerID);
                errorLabel.setVisible(true);
                errorLabel.setText(customerName + " has been deleted from customer records.");
                }
            }

        }
    }

    @Override
    /**
     * Two Lambda expressions are used in this method for dynamic filtering of the UserTable and AppointmentsTable. Text entered into search bars will display matches in the tabledata in real time.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userNameLabel.setVisible(true);
        userNameLabel.setText(userName);

        if (allAppointments!= null) {

            apptID.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("apptID"));
            startTime.setCellValueFactory(new PropertyValueFactory<Appointments, Timestamp>("start"));


            FilteredList<Appointments> filteredData = new FilteredList<>(allAppointments, b -> true);

            appointmentSearchText.textProperty().addListener((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate(Appointments -> {
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();

                    if (String.valueOf(Appointments.getStart()).indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (String.valueOf(Appointments.getApptID()).indexOf(searchKeyword)  > -1) {
                        return true;
                    } else
                        return false;
                });
            });

            SortedList<Appointments> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(appointmentsTable.comparatorProperty());
            appointmentsTable.setItems(sortedData);

        }

        if (allCustomers!= null) {

            customersNameColumn.setCellValueFactory(new PropertyValueFactory<Customers, String>("customerName"));
            customersAddressColumn.setCellValueFactory(new PropertyValueFactory<Customers, String>("address"));
            customersPhoneColumn.setCellValueFactory(new PropertyValueFactory<Customers, String>("phoneNumber"));

            FilteredList<Customers> filteredData = new FilteredList<>(allCustomers, b -> true);

            customerSearchText.textProperty().addListener((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate(Customers -> {
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();

                    if (Customers.getCustomerName().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Customers.getPhoneNumber().toLowerCase().indexOf(searchKeyword)  > -1) {
                        return true;
                    } else if (Customers.getAddress().toLowerCase().indexOf(searchKeyword)  > -1) {
                        return true;
                    } else
                        return false;
                });
            });

            SortedList<Customers> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(customersTable.comparatorProperty());
            customersTable.setItems(sortedData);
        }

    }
}
