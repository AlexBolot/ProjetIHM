package projet.ihm.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import projet.ihm.model.Account;

public class LoginController {

    @FXML
    private TextField login;

    @FXML
    private TextField password;

    @FXML
    void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER){
            checkIdentifiers(login.getText(), password.getText().hashCode());
        }
    }

    @FXML
    void initialize() {
        assert login != null : "fx:id=\"login\" was not injected: check your FXML file 'Login.fxml'.";
        assert password != null : "fx:id=\"password\" was not injected: check your FXML file 'Login.fxml'.";
    }

    private void checkIdentifiers(String login, int passwordHash) {

        for (Account account : Account.accounts) {

            if (account.getName().equals(login)) {
                if (account.getPasswordHash() == passwordHash) {
                    //OK
                }
                //mettre mot de passe en rouge
            }
        }
        //mettre login et mot de passe en rouge
    }
}
