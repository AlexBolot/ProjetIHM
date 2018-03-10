package projet.ihm.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import projet.ihm.model.incidents.Incident;
import projet.ihm.model.incidents.Status;
import projet.ihm.model.users.User;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MINUTES;
import static projet.ihm.model.users.Privileges.Privilege.UpdateStatus;

public class DetailedController
{
    //region --------------- FXML Attributes ---------------

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
    private TextArea  txtDescription;
    @FXML
    private TextField txtBuildingRoom;
    @FXML
    private TextField txtStatus;

    @FXML
    private ChoiceBox<String> ddlStatus;

    @FXML
    private Button btnSubmit;

    //endregion

    private Incident incident;

    public void setIncident (Incident incident)
    {
        this.incident = incident;
    }

    @FXML
    public void initialize ()
    {
        ArrayList<String> statusLabels = Arrays.stream(Status.values()).map(Status::label).collect(Collectors.toCollection(ArrayList::new));

        ddlStatus.getItems().clear();
        ddlStatus.getItems().addAll(statusLabels);

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

            txtStatus.setText(incident.status().label());
            ddlStatus.getSelectionModel().select(incident.status().label());
        }

        boolean canUpdateStatus = User.currentLoggedIn.hasPrivilege(UpdateStatus);

        txtStatus.setVisible(!canUpdateStatus);
        ddlStatus.setVisible(canUpdateStatus);
        btnSubmit.setVisible(canUpdateStatus);
    }

    public void Submit_onClick ()
    {
        if (incident != null)
        {
            ArrayList<Incident> list = Incident.readFromSave();

            for (Incident i : list)
            {
                if (i.ID() == incident.ID())
                {
                    Status newStatus = Status.getFromLabel(ddlStatus.getSelectionModel().getSelectedItem());
                    i.setStatus(newStatus);
                }
            }

            Incident.writeToSave(list);
        }

        Close_onClick();
    }

    public void Close_onClick ()
    {
        Stage stage = (Stage) txtTitle.getScene().getWindow();
        stage.close();
    }
}
