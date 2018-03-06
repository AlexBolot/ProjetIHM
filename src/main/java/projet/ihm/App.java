package projet.ihm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import projet.ihm.model.Incident;
import projet.ihm.model.users.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static projet.ihm.Const.*;

/**
 Hello world!
 */
public class App extends Application
{
    public static void main (String[] args)
    {
        if (args != null)
        {
            List<String> argsList = Arrays.asList(args);

            if (argsList.contains("clearIncidents")) Incident.writeToSave(new ArrayList<>());
            if (argsList.contains("clearUsers")) User.writeToSave(new ArrayList<>());
            if (argsList.contains("addIncidents"))
            {
                //TODO Add incidents examples
            }
            if (argsList.contains("addUsers"))
            {
                User.writeToSave(new ArrayList<User>()
                {{
                    add(new User("bob", "bob".hashCode(), User.Preset.Agent));
                    add(new User("alice", "alice".hashCode(), User.Preset.Admin));
                    add(new User("marcel", "marcel".hashCode(), User.Preset.SimpleUser));
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
            primaryStage.show();
        }
        catch (NullPointerException | IOException e)
        {
            e.printStackTrace();
        }
    }
}
