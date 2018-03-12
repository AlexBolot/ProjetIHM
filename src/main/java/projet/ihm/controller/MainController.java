package projet.ihm.controller;

import com.sun.javafx.scene.control.skin.TableViewSkin;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import projet.ihm.model.incidents.Incident;
import projet.ihm.model.incidents.Urgency;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MINUTES;
import static javafx.scene.control.SelectionMode.MULTIPLE;
import static projet.ihm.Const.*;
import static projet.ihm.model.incidents.Urgency.getFromLabel;
import static projet.ihm.model.users.Privileges.Privilege.AddIncident;
import static projet.ihm.model.users.Privileges.Privilege.DeleteIncident;
import static projet.ihm.model.users.User.currentLoggedIn;

public class MainController
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
    private Button btnDetailed;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;

    //endregion

    private static boolean activeView = false;

    @FXML
    public void initialize ()
    {
        activeView = true;

        //region --> init TableView { data, selection_mode, listeners }
        tableView.getItems().clear();
        tableView.getItems().addAll(Incident.readFromSave());
        tableView.getSelectionModel().setSelectionMode(MULTIPLE);
        tableView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            int selectionSize = tableView.getSelectionModel().getSelectedItems().size();
            btnDelete.setText(selectionSize > 1 ? "Supprimer incidents" : "Supprimer incident");
        });
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) btnDetailed.setVisible(true);
            else btnDetailed.setVisible(false);

            int selectionSize = tableView.getSelectionModel().getSelectedItems().size();
            btnDelete.setText(selectionSize > 1 ? "Supprimer incidents" : "Supprimer incident");
        });
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

        //region --> init buttons visibility
        btnDetailed.setVisible(false);
        btnAdd.setVisible(currentLoggedIn != null && currentLoggedIn.hasPrivilege(AddIncident));
        btnDelete.setVisible(currentLoggedIn != null && currentLoggedIn.hasPrivilege(DeleteIncident));
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

    public void refresh(){
        tableView.getItems().clear();
        tableView.getItems().addAll(Incident.readFromSave());
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
        List<Button> buttons = sideAnchorPane.getChildren().stream().filter(Button.class::isInstance).map(Button.class::cast).collect(
                Collectors.toList());

        double minHeight = buttons.stream().mapToDouble(Button::getMinHeight).sum();

        minHeight += (buttons.size() - 1) * buttons.get(0).getMinHeight();
        minHeight += scene.getY();
        minHeight += scene.getWindow().getHeight() - scene.getHeight() - scene.getY();

        return AnchorPane.getTopAnchor(sideAnchorPane) + minHeight + AnchorPane.getBottomAnchor(sideAnchorPane);
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

    public void Add_onClick ()
    {
        activeView = false;

        goTo("AddView.fxml", (Stage) tableView.getScene().getWindow(), ADD_WIDTH, ADD_HEIGHT, true);
    }

    public void Detailed_onClick ()
    {
        activeView = false;

        openDetailed_newWindow("Vue détaillée", tableView.getSelectionModel().getSelectedItem(), this);
    }

    public void Deco_onClick ()
    {
        activeView = false;

        goTo("Login.fxml", (Stage) tableView.getScene().getWindow(), LOGIN_WIDTH, LOGIN_HEIGHT, false);
    }

    public void Delete_onClick ()
    {
        if (currentLoggedIn == null || !currentLoggedIn.hasPrivilege(DeleteIncident)) return;

        ArrayList<Incident> incidents = new ArrayList<>(tableView.getItems());
        tableView.getSelectionModel().getSelectedItems().forEach(incidents::remove);

        Incident.writeToSave(incidents);

        tableView.getItems().clear();
        tableView.getItems().addAll(Incident.readFromSave());
    }

    //endregion
}
