package com.example.jfxdemo;

import com.example.jfxdemo.session.AssignmentSession;
import com.example.jfxdemo.session.CourseSession;
import com.example.jfxdemo.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static com.example.jfxdemo.database.dbconn.dbconn;

public class CourseAviewController {

    @FXML
    public AnchorPane toAssessment;
    @FXML
    public Button assignment;
    @FXML
    public Button DC;
    @FXML
    public Text AssignmentString;
    @FXML
    public ToggleButton edit;

    @FXML
    public TextField semester;
    @FXML
    public ListView participants;

    @FXML
    public TextField grade;
    @FXML
    public TextField selected1;
    @FXML
    public ListView Users;
    @FXML
    public TextField selected;
    @FXML
    public PasswordField selected2;
    @FXML
    private TextArea courseDescription;
    @FXML
    private TextField courseString;
    @FXML
    private ListView<String> AssignmentList;
    @FXML
    private Text text;
    @FXML
    private Text textID;


    @FXML
    public void initialize() throws SQLException
    {
        textID.setText("ID: "+String.valueOf(CourseSession.getInstance().getCid()));
        PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("select * from courses where ID = ? ");
        st.setInt(1, CourseSession.getInstance().getCid());
        ResultSet rs = st.executeQuery();
        while (rs.next())
        {
            courseString.setText(rs.getString("name"));
            courseDescription.setText(rs.getString("description"));
            semester.setText(rs.getString("semester"));

            PreparedStatement stb = (PreparedStatement)dbconn().prepareStatement("select * from assignments where courseID = ?");
            stb.setInt(1,rs.getInt("ID"));
            ResultSet rsb = stb.executeQuery();
            while (rsb.next())
            {
                AssignmentList.getItems().add("Assignment: " + rsb.getString("ID") + " Name: " + rsb.getString("name"));
            }
        }
        PreparedStatement sta = (PreparedStatement)dbconn().prepareStatement("select userID from courserelations where courseID = ?");
        sta.setInt(1, CourseSession.getInstance().getCid());
        ResultSet rsa = sta.executeQuery();
        while (rsa.next())
        {
            PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select * from users where ID = ?");
            stc.setInt(1, rsa.getInt("userID"));
            ResultSet rsc = stc.executeQuery();
            while (rsc.next())
            {
                participants.getItems().add("ID: "+rsc.getString("ID")+" Name: "+rsc.getString("name")+" role: "+ rsc.getString("role"));
            }
        }
        PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select * from users");
        ResultSet rsc = stc.executeQuery();
        while (rsc.next())
        {
            Users.getItems().add("ID: "+rsc.getString("ID")+" Name: "+rsc.getString("name")+" role: "+ rsc.getString("role"));
        }

    }
    @FXML
    protected void toMain(ActionEvent event) throws IOException
    {
        CourseSession.deleteInstance();
        Stage stage;
        Parent scene;
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainadminmenu.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    protected void toAssignment(ActionEvent event) throws IOException, SQLException
    {
        PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("select * from courses where ID = ? ");
        st.setInt(1, CourseSession.getInstance().getCid());
        ResultSet rs = st.executeQuery();
        while (rs.next())
        {
            courseString.setText(rs.getString("name"));
            courseDescription.setText(rs.getString("description"));
            semester.setText(rs.getString("semester"));

            PreparedStatement stb = (PreparedStatement)dbconn().prepareStatement("select * from assignments where courseID = ?");
            stb.setInt(1,rs.getInt("ID"));
            ResultSet rsb = stb.executeQuery();
            while (rsb.next())
            {
                if (AssignmentList.getSelectionModel().getSelectedItem().equals("Assignment: " + rsb.getString("ID") + " Name: " + rsb.getString("name")))
                {
                    AssignmentSession.getInstance(rsb.getInt("ID"));
                    Stage stage;
                    Parent scene;
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AssignmentAview.fxml")));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
            }
        }
    }

    @FXML
    public void submit(ActionEvent actionEvent) {
        if (courseDescription.isDisabled()){
            semester.setDisable(false);
            courseDescription.setDisable(false);
            courseString.setDisable(false);
            text.setText("The form is now unlocked. Press edit again to submit changes.");
        }
        else {
            try {
                PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("UPDATE courses SET name = ?, description = ?, semester = ? WHERE ID = ?");
                st.setString(1, courseString.getText());
                st.setString(2, courseDescription.getText());
                st.setString(3, semester.getText());
                st.setInt(4, CourseSession.getInstance().getCid());
                st.executeUpdate();
                text.setText("Updated");
                semester.setDisable(true);
                courseDescription.setDisable(true);
                courseString.setDisable(true);


            } catch (SQLException e ) {
                text.setText("Submit not successful");
            }
        }
    }

    @FXML
    public void getSelected(ActionEvent actionEvent) throws SQLException {
        PreparedStatement sta = (PreparedStatement)dbconn().prepareStatement("select * from courserelations where courseID = ?");
        sta.setInt(1, CourseSession.getInstance().getCid());
        ResultSet rsa = sta.executeQuery();
        while (rsa.next())
        {
            PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select * from users where ID = ?");
            stc.setInt(1, rsa.getInt("userID"));
            ResultSet rsc = stc.executeQuery();
            while (rsc.next())
            {
                if (participants.getSelectionModel().getSelectedItem().equals("ID: "+rsc.getString("ID")+" Name: "+rsc.getString("name")+" role: "+ rsc.getString("role"))){
                    selected.setText(String.valueOf(rsc.getString("ID")));
                }
            }
        }
    }

    @FXML
    public void Delete(ActionEvent actionEvent) throws SQLException {
        if (selected2.isDisabled()){
            selected2.setDisable(false);
            text.setText("Enter your password to confirm this action");
            DC.setText("Confirm");
        }
        else {
            PreparedStatement sta = (PreparedStatement)dbconn().prepareStatement("select * from users where ID = ? and role = 'A'");
            sta.setInt(1, UserSession.getInstance().getUid());
            ResultSet rsa = sta.executeQuery();
            while (rsa.next())
            {
                if (selected2.getText().equals(rsa.getString("password"))){
                    try {
                        PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("DELETE FROM courses WHERE ID = ?");
                        st.setInt(1, CourseSession.getInstance().getCid());
                        st.executeQuery();
                        CourseSession.deleteInstance();
                        Stage stage;
                        Parent scene;
                        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainadminmenu.fxml")));
                        stage.setScene(new Scene(scene));
                        stage.show();
                    } catch(Exception e) {
                        selected2.setText("");
                        selected2.setDisable(true);
                        text.setText("Wrong password");
                        DC.setText("Delete Course");
                        System.out.println(e);
                    }
                }
                else {
                    selected2.setText("");
                    selected2.setDisable(true);
                    text.setText("Wrong password");
                    DC.setText("Delete Course");
                }
            }
        }
    }

    @FXML
    public void removeP(ActionEvent actionEvent) throws SQLException {
        PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select * from courserelations where courseID = ? and userID = ? limit 1");
        stc.setInt(1, CourseSession.getInstance().getCid());
        stc.setInt(2, Integer.parseInt(selected.getText()));
        ResultSet rsc = stc.executeQuery();
        while (rsc.next())
        {
            try {
                PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("DELETE FROM courserelations WHERE ID = ?");
                st.setInt(1, rsc.getInt("ID"));
                st.executeQuery();


                PreparedStatement std = (PreparedStatement)dbconn().prepareStatement("select * from users where ID = ?");
                std.setInt(1, Integer.parseInt(selected.getText()));
                ResultSet rsd = std.executeQuery();
                while (rsd.next())
                {
                    participants.getItems().remove("ID: "+rsd.getString("ID")+" Name: "+rsd.getString("name")+" role: "+ rsd.getString("role"));
                }
                selected.setText("");
            } catch(Exception e) {
                System.out.println(e);
            }
        }
    }

    @FXML
    public void getSelectedtwo(ActionEvent actionEvent) throws SQLException {
        PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select * from users");
        ResultSet rsc = stc.executeQuery();
        while (rsc.next())
        {
            if (Users.getSelectionModel().getSelectedItem().equals("ID: "+rsc.getString("ID")+" Name: "+rsc.getString("name")+" role: "+ rsc.getString("role"))) {
                selected1.setText(String.valueOf(rsc.getString("ID")));
            }
        }

    }
    @FXML
    public void addP(ActionEvent actionEvent) throws SQLException {
        PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select * from users where ID = ?");
        stc.setInt(1, Integer.parseInt(selected1.getText()));
        ResultSet rsc = stc.executeQuery();
        while (rsc.next())
        {
            try {
                PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("INSERT into courserelations(userID, courseID) VALUES (?,?)");
                st.setInt(1, rsc.getInt("ID"));
                st.setInt(2, CourseSession.getInstance().getCid());
                st.executeUpdate();

                PreparedStatement stb = (PreparedStatement)dbconn().prepareStatement("select userID from courserelations where courseID = ? and userID = ?");
                stb.setInt(1, CourseSession.getInstance().getCid());
                stb.setInt(2, Integer.parseInt(selected1.getText()));
                ResultSet rsb = stb.executeQuery();
                while (rsb.next())
                {
                    PreparedStatement std = (PreparedStatement)dbconn().prepareStatement("select * from users where ID = ?");
                    std.setInt(1, rsb.getInt("userID"));
                    ResultSet rsd = std.executeQuery();
                    while (rsd.next())
                    {
                        participants.getItems().add("ID: "+rsd.getString("ID")+" Name: "+rsd.getString("name")+" role: "+ rsd.getString("role"));
                    }
                }
                selected1.setText("");
            } catch(Exception e) {
                System.out.println(e);
            }
        }
    }
}
