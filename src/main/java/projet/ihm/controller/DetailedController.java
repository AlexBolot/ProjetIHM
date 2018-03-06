package projet.ihm.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import projet.ihm.model.Incident;

import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.MINUTES;

public class DetailedController
{
    @FXML
    private TextField txtType;

    @FXML
    private TextField txtTitle;

    @FXML
    private TextField txtAuthor;

    @FXML
    private TextField txtUrgency;

    @FXML
    private TextField txtDateHour;

    @FXML
    private TextArea txtDescription;

    @FXML
    private TextField txtBuildingRoom;

    private Incident incident;

    public void setIncident (Incident incident)
    {
        this.incident = incident;
    }

    @FXML
    public void initialize ()
    {
        if (incident != null)
        {
            txtTitle.setText(incident.title());
            txtDescription.setText(incident.description());
            txtType.setText(incident.type().toString());
            txtAuthor.setText(incident.author());

            String building = incident.building() == null ? "" : incident.building().toString();
            String room = incident.room() == null ? "" : incident.room().toString();

            txtBuildingRoom.setText(building + " " + room);
            txtUrgency.setText(incident.urgency().toString());

            String date = (incident.date() == null) ? "" : incident.date().format(DateTimeFormatter.ofPattern("dd/MM/uu"));
            String time = (incident.time() == null) ? "" : incident.time().truncatedTo(MINUTES).toString();

            txtDateHour.setText(date + " " + time);
        }
    }

    public void Close_onClick ()
    {
        Stage stage = (Stage) txtTitle.getScene().getWindow();
        stage.close();
    }
}
