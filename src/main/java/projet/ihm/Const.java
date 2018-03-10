package projet.ihm;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import projet.ihm.controller.DetailedController;
import projet.ihm.model.incidents.Incident;
import projet.ihm.model.incidents.Urgency;

import java.io.IOException;

import static javafx.scene.paint.Color.*;

public class Const
{

    public static final double LOGIN_WIDTH  = 500;
    public static final double LOGIN_HEIGHT = 325;

    public static final double MAIN_WIDTH  = 1200;
    public static final double MAIN_HEIGHT = 700;

    public static final double ADD_WIDTH  = 1200;
    public static final double ADD_HEIGHT = 700;

    public static final double DETAIL_WIDTH  = 780;
    public static final double DETAIL_HEIGHT = 450;

    public static final double TableViewBorder = 2;

    private static final Color lightGreen = rgb(1, 225, 16, 1);
    private static final Color darkGreen  = rgb(0, 140, 31, 1);
    private static final Color blue       = rgb(0, 94, 255, 1);
    private static final Color orange     = rgb(255, 119, 0, 1);
    private static final Color red        = rgb(255, 0, 0, 1);
    private static final Color grey       = rgb(176, 176, 176, 1);

    @SuppressWarnings ("ConstantConditions")
    public static void goTo (@NotNull String fileName, @NotNull Stage stage, double width, double height, boolean resizable)
    {
        try
        {
            Rectangle2D screen = Screen.getPrimary().getVisualBounds();
            Parent root = FXMLLoader.load(Const.class.getClassLoader().getResource(fileName));
            Scene scene = new Scene(root, width, height);

            addStyleSheet(scene);

            stage.setMinWidth(0);
            stage.setMinHeight(0);

            stage.setScene(scene);
            stage.sizeToScene();
            stage.setResizable(resizable);
            stage.setX((screen.getWidth() - width) / 2);
            stage.setY((screen.getHeight() - height) / 2);

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
            Scene scene = new Scene(root, DETAIL_WIDTH, DETAIL_HEIGHT);
            addStyleSheet(scene);

            Stage stage = new Stage();

            stage.setTitle(newTitle);
            stage.setScene(scene);
            stage.sizeToScene();
            stage.setMinWidth(DETAIL_WIDTH);
            stage.setMinHeight(DETAIL_HEIGHT);
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

                Color color;

                if (item == null || empty)
                {
                    setStyle("");
                    setGraphic(null);
                    return;
                }
                else
                {
                    switch (Urgency.getFromLabel(item))
                    {
                        case Mineure:
                            color = lightGreen;
                            break;

                        case Faible:
                            color = darkGreen;
                            break;

                        case Moderee:
                            color = blue;
                            break;

                        case Grande:
                            color = orange;
                            break;

                        case Majeure:
                            color = red;
                            break;

                        default:
                            color = grey;
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
        //noinspection ConstantConditions
        scene.getStylesheets().add(Const.class.getClassLoader().getResource("styles.css").toString());
    }
}
