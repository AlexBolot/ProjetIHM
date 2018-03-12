package projet.ihm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import projet.ihm.model.incidents.Building;
import projet.ihm.model.incidents.Incident;
import projet.ihm.model.incidents.Room;
import projet.ihm.model.users.User;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static projet.ihm.Const.*;
import static projet.ihm.model.incidents.Building.*;
import static projet.ihm.model.incidents.IncidentType.*;
import static projet.ihm.model.incidents.Room.*;
import static projet.ihm.model.incidents.Status.*;
import static projet.ihm.model.incidents.Urgency.*;

/**
 Hello world!
 */
public class App extends Application
{
    //clearIncidents addIncidents clearUsers addUsers
    public static void main (String[] args)
    {
        if (args != null)
        {
            List<String> argsList = Arrays.asList(args);

            if (argsList.contains("clearIncidents")) Incident.writeToSave(new ArrayList<>());
            if (argsList.contains("clearUsers")) User.writeToSave(new ArrayList<>());
            if (argsList.contains("addIncidents"))
            {
                Incident.writeToSave(new ArrayList<Incident>()
                {{
                    add(new Incident("On manque de feutres",
                                     "bob",
                                     "Il n'y a plus de feutres rouges et verts dans la salle...",
                                     Fournitures));

                    add(new Incident("Vitre brisée amphi Est",
                                     "alice",
                                     "Un pigeon a volé droit dans la vitre...",
                                     VitreBrisee,
                                     BatE,
                                     E134,
                                     Moderee,
                                     LocalDate.now(),
                                     LocalTime.now(),
                                     TODO));

                    add(new Incident("Lampe clignotte au plafond",
                                     "alice",
                                     "Un néon clignotte au plafond de la salle, c'est désagréable...",
                                     LampeCassee,
                                     BatO,
                                     O318,
                                     Faible,
                                     LocalDate.now(),
                                     LocalTime.now(),
                                     DOING));

                    add(new Incident("Lampadaire cassé",
                                     "marcel",
                                     "Le lampadaire du parking ne s'allume plus...",
                                     LampeCassee,
                                     Parking,
                                     P1,
                                     Mineure,
                                     LocalDate.now(),
                                     LocalTime.now(),
                                     DOING));

                    add(new Incident("Stationnement gênant !",
                                     "bob",
                                     "Voiture immatriculée BR-542-KX m'empèche de déplacer ma voiture !!",
                                     VoitureMalGaree,
                                     Parking,
                                     P2,
                                     Majeure,
                                     LocalDate.now(),
                                     LocalTime.now(),
                                     TODO));

                    add(new Incident("Loreum ipsum... !",
                            "thomas",
                            "",
                            Fournitures,
                            Building.NONE,
                            Room.NONE,
                            Majeure,
                            LocalDate.now(),
                            LocalTime.now(),
                            TODO));
                }});
            }
            if (argsList.contains("addUsers"))
            {
                User.writeToSave(new ArrayList<User>()
                {{
                    add(new User("gérard", "gérard".hashCode(), User.Preset.Agent));
                    add(new User("jeanne", "jeanne".hashCode(), User.Preset.Admin));
                    add(new User("mathieu", "mathieu".hashCode(), User.Preset.SimpleUser));
                }});
            }
        }

        launch(args);
    }

    @Override
    public void start (Stage primaryStage)
    {
        try
        {
            //noinspection ConstantConditions
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Login.fxml"));
            primaryStage.setTitle("Déclaration d'incident Polytech");
            Scene scene = new Scene(root, LOGIN_WIDTH, LOGIN_HEIGHT);

            primaryStage.setMinWidth(LOGIN_WIDTH);
            primaryStage.setMinHeight(LOGIN_HEIGHT);

            addStyleSheet(scene);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        }
        catch (NullPointerException | IOException e)
        {
            e.printStackTrace();
        }
    }
}
