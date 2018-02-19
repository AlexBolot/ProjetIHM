package projet.ihm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import projet.ihm.model.Account;

import static projet.ihm.Const.*;


public class LoginController {

    @FXML
    private AnchorPane loginPane;

    @FXML
    private TextField login;

    @FXML
    private TextField password;


    @FXML
    private Button submit;

    @FXML
    void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER){
            checkIdentifiers();
        }
    }

    @FXML
    void onSubmit(ActionEvent event) {
        checkIdentifiers();
    }

    @FXML
    void initialize() {
        assert loginPane != null : "fx:id=\"loginPane\" was not injected: check your FXML file 'Login.fxml'.";
        assert login != null : "fx:id=\"login\" was not injected: check your FXML file 'Login.fxml'.";
        assert password != null : "fx:id=\"password\" was not injected: check your FXML file 'Login.fxml'.";
        assert submit != null : "fx:id=\"submit\" was not injected: check your FXML file 'Login.fxml'.";
    }

    private void checkIdentifiers() {

        int passwordHash = password.getText().hashCode();
        boolean accountNotFound = true;

        for (Account account : Account.accounts) {

            if (account.getName().equals(login.getText())) {
                accountNotFound = false;

                if (account.getPasswordHash() == passwordHash){
                    Stage stage = (Stage) loginPane.getScene().getWindow();

                    goTo("MainView.fxml", stage, MAIN_WIDTH, MAIN_HEIGHT);
                }else{
                    password.getStyleClass().add("missedField");

                }
            }

        }
        if (accountNotFound){
            login.getStyleClass().add("missedField");
            password.getStyleClass().add("missedField");
        }

    }
}
