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

import static java.time.temporal.ChronoUnit.MINUTES;
import static projet.ihm.Const.goTo;
import static projet.ihm.Const.setUpUrgencyFactory;
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

            if (building == Building.NotPrecised)
            {
                ddlRoom.getItems().clear();
                ddlRoom.setDisable(true);
                ddlRoom.getSelectionModel().select(Room.NotPrecised);
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

        urgencySlider.setValue(3);

        datePicker.setValue(LocalDate.now());
        timePicker.setValue(LocalTime.now());

        tableView.getItems().clear();
        tableView.getItems().addAll(Incident.readFromSave());

        titleCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().title()));
        typeCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().type().toString()));
        urgencyCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().urgency().toString()));
        setUpUrgencyFactory(urgencyCol);
        locationCol.setCellValueFactory(p -> {

            String building = (p.getValue().building() == null) ? "" : p.getValue().building().toString();
            String room = (p.getValue().room() == null) ? "" : p.getValue().room().toString();

            return new SimpleStringProperty(building + " " + room);
        });
        dateTimeCol.setCellValueFactory(p -> {

            String date = (p.getValue().date() == null) ? "" : p.getValue().date().format(DateTimeFormatter.ofPattern("dd/MM/uu"));
            String time = (p.getValue().time() == null) ? "" : p.getValue().time().truncatedTo(MINUTES).toString();

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
                urgency = NotPrecised;
        }

        Incident incident = new Incident(title, author, description, type, building, room, urgency, date, time);

        incident.addToSave();

        goBackToMain();
    }

    private void goBackToMain ()
    {
        goTo("MainView.fxml", (Stage) tableView.getScene().getWindow());
    }
}
