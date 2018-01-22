package projet.ihm;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import projet.ihm.controller.DetailedController;
import projet.ihm.model.Incident;
import projet.ihm.model.Urgency;

import java.io.IOException;

public class Const
{
    public static void goTo (@NotNull String fileName, @NotNull Stage stage)
    {
        try
        {
            //noinspection ConstantConditions
            Parent root = FXMLLoader.load(Const.class.getClassLoader().getResource(fileName));
            Scene scene = new Scene(root, 1080, 720);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void openDetailed_newWindow (String newTitle, Incident incident)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(Const.class.getClassLoader().getResource("DetailedView.fxml"));
            DetailedController controller = new DetailedController();

            controller.setIncident(incident);
            loader.setController(controller);

            Parent root = loader.load();
            Scene scene = new Scene(root, 780, 557);
            Stage stage = new Stage();
            stage.setTitle(newTitle);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void setUpUrgencyFactory (@NotNull TableColumn<Incident, String> urgencyCol)
    {
        urgencyCol.setCellFactory(p -> new TableCell<Incident, String>()
        {
            @Override
            protected void updateItem (String item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item == null || empty)
                {
                    setText(null);
                    setStyle("");
                }
                else
                {
                    setText("");

                    switch (Urgency.valueOf(item))
                    {
                        case Mineure:
                            setStyle("-fx-background-color: #01e110");
                            break;

                        case Faible:
                            setStyle("-fx-background-color: #008c1f");
                            break;

                        case Moderee:
                            setStyle("-fx-background-color: #005eff");
                            break;

                        case Grande:
                            setStyle("-fx-background-color: #ff7700");
                            break;

                        case Majeure:
                            setStyle("-fx-background-color: #ff0000");
                            break;

                        default:
                            setStyle("");
                            break;
                    }
                }
            }
        });
    }
}
