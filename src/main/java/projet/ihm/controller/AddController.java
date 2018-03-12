package projet.ihm.controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import com.sun.javafx.scene.control.skin.TableViewSkin;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import projet.ihm.model.incidents.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MINUTES;
import static projet.ihm.Const.*;
import static projet.ihm.model.incidents.Status.TODO;
import static projet.ihm.model.incidents.Urgency.*;
import static projet.ihm.model.users.User.currentLoggedIn;

public class AddController
{
    //region --------------- FXML Attributes ---------------

    @FXML
    private AnchorPane sideAnchorPane;

    @FXML
    private TableView<Incident> tableView;

    @FXML
    private TableColumn<Incident, String> urgencyCol;
    @FXML
    private TableColumn<Incident, String> typeCol;
    @FXML
    private TableColumn<Incident, String> titleCol;
    @FXML
    private TableColumn<Incident, String> locationCol;
    @FXML
    private TableColumn<Incident, String> dateTimeCol;
    @FXML
    private TableColumn<Incident, String> statusCol;

    @FXML
    private TextField txtTitle;
    @FXML
    private Label     txtAuthor;
    @FXML
    private TextArea  txtDescription;

    @FXML
    private ComboBox<String> ddlRoom;
    @FXML
    private ComboBox<String> ddlBuilding;
    @FXML
    private ComboBox<String> ddlType;

    @FXML
    private Slider urgencySlider;

    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private JFXTimePicker timePicker;

    @FXML
    private Label lblUrgency1;
    @FXML
    private Label lblUrgency2;
    @FXML
    private Label lblUrgency3;
    @FXML
    private Label lblUrgency4;
    @FXML
    private Label lblUrgency5;

    //endregion

    private HashMap<Double, Label> labelMap;

    private static boolean activeView = false;

    @FXML
    public void initialize ()
    {
        activeView = true;

        //region --> init TableView data
        tableView.getItems().clear();
        tableView.getItems().addAll(Incident.readFromSave());
        //endregion

        //region --> init Adding Fields
        txtTitle.setText("");
        txtDescription.setText("");
        txtAuthor.setText(currentLoggedIn == null ? "" : currentLoggedIn.name());

        ddlType.getItems().clear();
        ddlType.getItems().addAll(IncidentType.labels());

        ddlBuilding.getItems().clear();
        ddlBuilding.getItems().addAll(Building.labels());
        ddlBuilding.setOnAction(event -> {

            Building building = Building.getFromLabel(ddlBuilding.getSelectionModel().getSelectedItem());

            if (building == Building.NONE)
            {
                ddlRoom.getItems().clear();
                ddlRoom.setDisable(true);
                ddlRoom.getSelectionModel().select(Room.NONE.label());
            }
            else
            {
                ArrayList<Room> interestingRooms = new ArrayList<>(Arrays.asList(Room.values()));
                interestingRooms.removeIf(room -> room.building() != building && room.building() != null);

                ArrayList<String> list = interestingRooms.stream().map(Room::label).collect(Collectors.toCollection(ArrayList::new));

                ddlRoom.getItems().clear();
                ddlRoom.getItems().addAll(list);
                ddlRoom.setDisable(false);
            }
        });

        labelMap = new HashMap<Double, Label>()
        {{
            put(1d, lblUrgency1);
            put(2d, lblUrgency2);
            put(3d, lblUrgency3);
            put(4d, lblUrgency4);
            put(5d, lblUrgency5);
        }};

        urgencySlider.setValue(3);
        urgencySlider.valueProperty().addListener(event -> {
            double value = urgencySlider.getValue();

            labelMap.forEach((aDouble, label) -> {
                if (aDouble == value)
                {
                    label.setScaleX(1.4);
                    label.setScaleY(1.4);
                }
                else
                {
                    label.setScaleX(1);
                    label.setScaleY(1);
                }
            });
        });

        datePicker.setValue(LocalDate.now());
        timePicker.setValue(LocalTime.now());
        timePicker.setIs24HourView(true);
        //endregion

        //region --> init Columns CellFactories
        titleCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().title()));
        typeCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().type().label()));
        urgencyCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().urgency().label()));
        urgencyCol.setComparator((o1, o2) -> {
            Urgency u1 = getFromLabel(o1);
            Urgency u2 = getFromLabel(o2);

            return Integer.compare(u1.urgencyLevel(), u2.urgencyLevel());
        });
        setUpUrgencyFactory(urgencyCol);
        locationCol.setCellValueFactory(p -> {

            String building = (p.getValue().building() == null) ? "" : p.getValue().building().label();
            String room = (p.getValue().room() == null) ? "" : p.getValue().room().label();

            return new SimpleStringProperty(building + " - " + room);
        });
        dateTimeCol.setCellValueFactory(p -> {

            String date = (p.getValue().date() == null) ? "" : p.getValue().date().format(DateTimeFormatter.ofPattern("dd/MM/uu"));
            String time = (p.getValue().time() == null) ? "" : p.getValue().time().truncatedTo(MINUTES).toString();

            return new SimpleStringProperty(date + " " + time);
        });
        statusCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().status().label()));
        //endregion

        //noinspection Duplicates
        Platform.runLater(() -> {
            autoFitTable();
            resizeTitleCol();

            Stage stage = (Stage) tableView.getScene().getWindow();

            stage.setMinWidth(getMinWidth());
            stage.setMinHeight(getMinHeight());

            stage.widthProperty().addListener((observable, oldValue, newValue) -> {
                if (activeView)
                {
                    double minWidth = getMinWidth();

                    if (stage.getMinWidth() > minWidth) stage.setMinWidth(minWidth);
                    if (newValue.doubleValue() < minWidth) stage.setWidth(minWidth);
                    resizeTitleCol();
                }
            });
        });
    }

    //Méthode pas très lisible mais j'ai pas vraiment mieux à offrir...
    private boolean checkMissedField (Control control)
    {
        if (control instanceof TextField)
        {
            TextField textField = (TextField) control;

            if (textField.getText().isEmpty())
            {
                textField.getStyleClass().add("missedField");
                return true;
            }
            else
            {
                textField.getStyleClass().removeAll("missedField");
                return false;
            }
        }

        if (control instanceof ComboBox)
        {
            ComboBox comboBox = (ComboBox) control;

            if (comboBox.getSelectionModel().isEmpty())
            {
                comboBox.getStyleClass().add("missedField");
                return true;
            }
            else
            {
                comboBox.getStyleClass().removeAll("missedField");
                return false;
            }
        }

        return false;
    }

    //region --------------- GUI methods -------------------

    @SuppressWarnings ("Duplicates")
    private double getMinWidth ()
    {
        double minWidth = 0;

        minWidth += urgencyCol.getWidth();
        minWidth += typeCol.getWidth();
        minWidth += titleCol.getMinWidth();
        minWidth += locationCol.getWidth();
        minWidth += dateTimeCol.getWidth();
        minWidth += statusCol.getWidth();
        minWidth += TableViewBorder;

        return AnchorPane.getLeftAnchor(tableView) + minWidth + AnchorPane.getRightAnchor(tableView);
    }

    private double getMinHeight ()
    {
        Scene scene = tableView.getScene();

        double minHeight = sideAnchorPane.getHeight();

        minHeight += scene.getY();
        minHeight += scene.getWindow().getHeight() - scene.getHeight() - scene.getY();

        return (AnchorPane.getTopAnchor(sideAnchorPane) * 2) + minHeight;
    }

    @SuppressWarnings ("Duplicates")
    private void autoFitTable ()
    {
        tableView.getColumns().forEach(column -> {
            try
            {
                Method method = TableViewSkin.class.getDeclaredMethod("resizeColumnToFitContent", TableColumn.class, int.class);
                method.setAccessible(true);
                method.invoke(tableView.getSkin(), column, -1);
            }
            catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
            {
                e.printStackTrace();
            }
        });
    }

    @SuppressWarnings ("Duplicates")
    private void resizeTitleCol ()
    {
        double sizeLeft = tableView.getWidth();

        sizeLeft -= urgencyCol.getWidth();
        sizeLeft -= typeCol.getWidth();
        sizeLeft -= locationCol.getWidth();
        sizeLeft -= dateTimeCol.getWidth();
        sizeLeft -= statusCol.getWidth();
        sizeLeft -= TableViewBorder;

        sizeLeft = Math.max(sizeLeft, titleCol.getMinWidth());

        titleCol.setPrefWidth(sizeLeft);
    }

    //endregion

    //region --------------- OnClick Methods ---------------

    public void Cancel_onClick ()
    {
        goBackToMain();
    }

    public void Submit_onClick ()
    {
        boolean missingField;

        txtTitle.getStyleClass().add("missedField");

        missingField = checkMissedField(txtTitle);
        missingField |= checkMissedField(txtAuthor);
        missingField |= checkMissedField(ddlType);

        String title = txtTitle.getText().trim();
        String author = txtAuthor.getText().trim();
        String description = txtDescription.getText().trim();

        Room room = Room.getFromLabel(ddlRoom.getValue());
        Building building = Building.getFromLabel(ddlBuilding.getValue());
        IncidentType type = IncidentType.getFromLabel(ddlType.getValue());

        LocalDate date = datePicker.getValue();
        LocalTime time = timePicker.getValue();

        int urgencyLevel = (int) urgencySlider.getValue();

        Urgency urgency;

        switch (urgencyLevel)
        {
            case 1:
                urgency = Mineure;
                break;
            case 2:
                urgency = Faible;
                break;
            case 3:
                urgency = Moderee;
                break;
            case 4:
                urgency = Grande;
                break;
            case 5:
                urgency = Majeure;
                break;
            default:
                urgency = NONE;
        }

        if (missingField) return;

        Incident incident = new Incident(title, author, description, type, building, room, urgency, date, time, TODO);

        incident.addToSave();

        goBackToMain();
    }

    private void goBackToMain ()
    {
        activeView = false;
        goTo("MainView.fxml", (Stage) tableView.getScene().getWindow(), MAIN_WIDTH, MAIN_HEIGHT, true);
    }

    //endregion
}
