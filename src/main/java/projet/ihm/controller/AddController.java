package projet.ihm.controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import projet.ihm.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static java.time.temporal.ChronoUnit.MINUTES;
import static projet.ihm.Const.*;
import static projet.ihm.model.Urgency.*;

public class AddController
{
    public TableView<Incident>           tableView;
    public TableColumn<Incident, String> urgencyCol;
    public TableColumn<Incident, String> typeCol;
    public TableColumn<Incident, String> titleCol;
    public TableColumn<Incident, String> locationCol;
    public TableColumn<Incident, String> dateTimeCol;

    public TextField txtTitle;
    public TextField txtAuthor;
    public TextArea  txtDescription;

    public ComboBox<Room>         ddlRoom;
    public ComboBox<Building>     ddlBuilding;
    public ComboBox<IncidentType> ddlType;

    public Slider        urgencySlider;
    public JFXDatePicker datePicker;
    public JFXTimePicker timePicker;

    public Label lblUrgency1;
    public Label lblUrgency2;
    public Label lblUrgency3;
    public Label lblUrgency4;
    public Label lblUrgency5;

    private HashMap<Double, Label> labelMap;

    @FXML
    public void initialize ()
    {
        txtTitle.setText("");
        txtDescription.setText("");
        txtAuthor.setText("");

        ddlType.getItems().clear();
        ddlType.getItems().addAll(IncidentType.values());

        ddlBuilding.getItems().clear();
        ddlBuilding.getItems().addAll(Building.values());
        ddlBuilding.setOnAction(event -> {

            Building building = ddlBuilding.getSelectionModel().getSelectedItem();

            if (building == Building.Non_Precisé)
            {
                ddlRoom.getItems().clear();
                ddlRoom.setDisable(true);
                ddlRoom.getSelectionModel().select(Room.Non_Precisé);
            }
            else
            {
                ArrayList<Room> interestingRooms = new ArrayList<>(Arrays.asList(Room.values()));
                interestingRooms.removeIf(room -> room.value() != building && room.value() != null);

                ddlRoom.getItems().clear();
                ddlRoom.getItems().addAll(interestingRooms);
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

        tableView.getItems().clear();
        tableView.getItems().addAll(Incident.readFromSave());

        titleCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().title()));
        typeCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().type().toString()));
        urgencyCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().urgency().toString()));

        setUpUrgencyFactory(urgencyCol);
        locationCol.setCellValueFactory(p ->

                                        {

                                            String building = (p.getValue().building() == null) ? "" : p.getValue().building().toString();
                                            String room = (p.getValue().room() == null) ? "" : p.getValue().room().toString();

                                            return new SimpleStringProperty(building + " " + room);
                                        });
        dateTimeCol.setCellValueFactory(p ->

                                        {

                                            String date = (p.getValue().date() == null) ? "" : p.getValue()
                                                                                                .date()
                                                                                                .format(DateTimeFormatter.ofPattern(
                                                                                                        "dd/MM/uu"));
                                            String time = (p.getValue().time() == null) ? "" : p.getValue()
                                                                                                .time()
                                                                                                .truncatedTo(MINUTES)
                                                                                                .toString();

                                            return new SimpleStringProperty(date + " " + time);
                                        });

        timePicker.setIs24HourView(true);
    }

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

        Room room = ddlRoom.getValue();
        Building building = ddlBuilding.getValue();
        IncidentType type = ddlType.getValue();

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
                urgency = Non_précisée;
        }

        if (missingField) return;

        Incident incident = new Incident(title, author, description, type, building, room, urgency, date, time);

        incident.addToSave();

        goBackToMain();
    }

    private void goBackToMain ()
    {
        goTo("MainView.fxml", (Stage) tableView.getScene().getWindow(), MAIN_WIDTH, MAIN_HEIGHT);
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
}
