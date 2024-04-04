package com.example.jfxdemo;

        import com.example.jfxdemo.session.UserSession;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.control.Button;
        import javafx.scene.control.ChoiceBox;
        import javafx.scene.control.DatePicker;
        import javafx.scene.control.TextField;
        import javafx.scene.text.Text;
        import javafx.stage.Stage;
        import java.io.IOException;
        import java.sql.Date;
        import java.sql.PreparedStatement;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.util.Objects;

        import static com.example.jfxdemo.database.dbconn.dbconn;

public class createAccountController
{

    @FXML
    private TextField pEmail;
    @FXML
    private TextField uPass;
    @FXML
    private TextField uName;
    @FXML
    private TextField uSurname;
    @FXML
    private TextField uID;
    @FXML
    private TextField pNum;
    @FXML
    private DatePicker bDay;
    @FXML
    private ChoiceBox uGender;
    @FXML
    private Text text;


    @FXML
    protected void onSubmit(ActionEvent event) throws IOException, SQLException {
        PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("select * from users where ID = ? and email IS NUll");
        st.setInt(1, Integer.parseInt(uID.getText()));
        ResultSet rs = st.executeQuery();
        while (rs.next())
        {
            try {
                PreparedStatement sta = (PreparedStatement)dbconn().prepareStatement("UPDATE users SET role = ?, email = ?, pEmail = ?, password = ?, name = ? , surname = ?, phone = ?, birthday = ?, gender = ?, currentSemester = ? WHERE ID = ?");
                sta.setString(1, "S");
                sta.setString(2, uName.getText()+uID.getText()+"@qduni.edu.gr");
                sta.setString(3, pEmail.getText());
                sta.setString(4, uPass.getText());
                sta.setString(5, uName.getText());
                sta.setString(6, uSurname.getText());
                sta.setString(7, pNum.getText());
                sta.setDate(8, Date.valueOf(bDay.getValue()));
                sta.setString(9, uGender.getSelectionModel().toString());
                sta.setInt(10, 1);
                sta.setInt(11, Integer.parseInt(uID.getText()));
                sta.executeUpdate();

                UserSession.generateSession(Integer.parseInt(uID.getText()));Stage stage;
                Parent scene;
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainstudentmenu.fxml")));
                stage.setScene(new Scene(scene));
            } catch (SQLException e ) {
                System.out.println("not successful");
                text.setText("ERROR please contact your representatives");
            }
        }
    }

    @FXML
    protected void returnHome(ActionEvent event) throws IOException
    {
        Stage stage;
        Parent scene;
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("accelerator.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}