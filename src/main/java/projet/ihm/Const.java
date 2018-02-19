package projet.ihm;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import projet.ihm.controller.DetailedController;
import projet.ihm.model.Incident;
import projet.ihm.model.Urgency;

import java.io.IOException;

public class Const
{

    public static final double LOGIN_WIDTH = 500;
    public static final double LOGIN_HEIGHT = 325;

    public static final double MAIN_WIDTH = 1080;
    public static final double MAIN_HEIGHT = 720;

    @SuppressWarnings ("ConstantConditions")
    public static void goTo (@NotNull String fileName, @NotNull Stage stage)
    {
        try
        {
            Parent root = FXMLLoader.load(Const.class.getClassLoader().getResource(fileName));
            Scene scene = new Scene(root, 1080, 720);

            scene.getStylesheets().add(Const.class.getClassLoader().getResource("styles.css").toString());

            addStyleSheet(scene);
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
            addStyleSheet(scene);
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

                Color color = null;

                if (item == null || empty)
                {
                    setStyle("");
                    setGraphic(null);
                    return;
                }
                else
                {
                    switch (Urgency.valueOf(item))
                    {
                        case Mineure:
                            color = Color.rgb(1, 225, 16, 1);
                            break;

                        case Faible:
                            color = Color.rgb(0, 140, 31, 1);
                            break;

                        case Moderee:
                            color = Color.rgb(0, 94, 255, 1);
                            break;

                        case Grande:
                            color = Color.rgb(255, 119, 0, 1);
                            break;

                        case Majeure:
                            color = Color.rgb(255, 0, 0, 1);
                            break;

                        default:
                            break;
                    }
                }

                Rectangle rect = new Rectangle();

                rect.heightProperty().bind(heightProperty().subtract(15));
                rect.widthProperty().bind(widthProperty().subtract(10));
                rect.setStroke(color);
                rect.setFill(color);

                setGraphic(rect);
                getStyleClass().add("padding5");
            }
        });
    }

    public static void addStyleSheet (@NotNull Scene scene)
    {
        scene.getStylesheets().add(Const.class.getClassLoader().getResource("styles.css").toString());
    }
}
