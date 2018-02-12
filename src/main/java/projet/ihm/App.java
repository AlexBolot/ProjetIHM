package projet.ihm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import projet.ihm.model.Incident;

import java.io.IOException;
import java.util.ArrayList;

import static projet.ihm.Const.*;

/**
 Hello world!
 */
public class App extends Application
{
    public static void main (String[] args)
    {
        if (args != null && args.length == 1 && args[0].equals("clear"))
            Incident.writeToSave(new ArrayList<>());

        launch(args);
    }

    @Override
    public void start (Stage primaryStage)
    {
        try
        {
            //noinspection ConstantConditions
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("MainView.fxml"));
            primaryStage.setTitle("Déclaration d'incident Polytech");
            Scene scene = new Scene(root, 1080, 720);
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
