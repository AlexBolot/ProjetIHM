package projet.ihm.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import projet.ihm.model.users.User;

import static projet.ihm.Const.*;

public class LoginController
{
    @FXML
    private AnchorPane loginPane;

    @FXML
    private TextField login;

    @FXML
    private TextField password;

    @FXML
    private Button submit;

    @FXML
    void onKeyPressed (KeyEvent event)
    {
        if (event.getCode() == KeyCode.ENTER) checkIdentifiers();
    }

    @FXML
    void onSubmit ()
    {
        checkIdentifiers();
    }

    @FXML
    void initialize ()
    {
        assert loginPane != null : "fx:id=\"loginPane\" was not injected: check your FXML file 'Login.fxml'.";
        assert login != null : "fx:id=\"login\" was not injected: check your FXML file 'Login.fxml'.";
        assert password != null : "fx:id=\"password\" was not injected: check your FXML file 'Login.fxml'.";
        assert submit != null : "fx:id=\"submit\" was not injected: check your FXML file 'Login.fxml'.";
    }

    private void checkIdentifiers ()
    {
        int passwordHash = password.getText().trim().hashCode();
        String loginName = login.getText().trim();
        boolean accountFound = false;

        for (User user : User.readFromSave())
        {
            if(user.name().equals(loginName))
            {
                accountFound = true;

                if(user.passwordHash() == passwordHash)
                {
                    Stage stage = (Stage) loginPane.getScene().getWindow();

                    User.currentLoggedIn = user;

                    goTo("MainView.fxml", stage, MAIN_WIDTH, MAIN_HEIGHT, true);
                }
                else
                {
                    password.getStyleClass().add("missedField");
                }
            }
        }

        if (!accountFound)
        {
            login.getStyleClass().add("missedField");
            password.getStyleClass().add("missedField");
        }
    }
}
