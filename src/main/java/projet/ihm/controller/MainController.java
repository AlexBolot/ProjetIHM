package projet.ihm.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import projet.ihm.model.Incident;

import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.MINUTES;
import static projet.ihm.Const.*;

public class MainController
{
    public TableView<Incident>           tableView;
    public TableColumn<Incident, String> urgencyCol;
    public TableColumn<Incident, String> typeCol;
    public TableColumn<Incident, String> titleCol;
    public TableColumn<Incident, String> locationCol;
    public TableColumn<Incident, String> dateTimeCol;
    public Button                        btnDetailed;
    private Button                       btnDeco;


    @FXML
    public void initialize ()
    {
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

        btnDetailed.setVisible(false);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) btnDetailed.setVisible(true);
            else btnDetailed.setVisible(false);
        });
    }

    public void Add_onClick ()
    {
        goTo("AddView.fxml", (Stage) tableView.getScene().getWindow(), MAIN_WIDTH, MAIN_HEIGHT);
    }

    public void Detailed_onClick ()
    {
        Incident incident = tableView.getSelectionModel().getSelectedItem();

        openDetailed_newWindow("Detailed view", incident);
    }

    public void Deco_onClick() {
        goTo("Login.fxml", (Stage) tableView.getScene().getWindow(), LOGIN_WIDTH, LOGIN_HEIGHT);
    }
}
