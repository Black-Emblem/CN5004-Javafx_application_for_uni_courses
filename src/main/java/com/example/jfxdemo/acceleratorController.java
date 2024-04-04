package com.example.jfxdemo;

import com.example.jfxdemo.session.UserSession;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import static com.example.jfxdemo.database.dbf.*;


public class acceleratorController
{

    @FXML
    private TextField log_email;
    @FXML
    private PasswordField log_pass;
    @FXML
    private Text textE;
    @FXML
    private Text textP;

    @FXML
    protected void createAccount(ActionEvent event) throws IOException
    {
        Stage  stage;
        Parent scene;
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("createAccount.fxml")));
        stage.setScene(new Scene(scene));
    }

    @FXML
    private void authorise(ActionEvent event) throws IOException, SQLException
    {
        String email = log_email.getText();
        String pass = log_pass.getText();



        if (verifyEmail(email))
        {
            textE.setText("");
            int uid = verifyCredentials(email, pass);
            if (-1 != uid) {
                UserSession.generateSession(uid);
                String role = getRole(uid);
                System.out.println("login successful user: " + uid);
                System.out.println("user role: " + role);


                if (role.equals("S"))
                {
                    Stage stage;
                    Parent scene;
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainstudentmenu.fxml")));
                    stage.setScene(new Scene(scene));
                }
                else if (role.equals("T"))
                {
                    Stage stage;
                    Parent scene;
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainteachermenu.fxml")));
                    stage.setScene(new Scene(scene));
                }
                else if (role.equals("A"))
                {
                    Stage stage;
                    Parent scene;
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainadminmenu.fxml")));
                    stage.setScene(new Scene(scene));
                }
            }
            else
            {
                System.out.println("login failed");
                textP.setText("Invalid Password");
            }
        }
        else
        {
            System.out.println("login failed");
            textE.setText("Invalid Email");
        }
    }

}