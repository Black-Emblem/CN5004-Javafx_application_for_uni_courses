package com.example.jfxdemo;

import com.example.jfxdemo.session.AssignmentSession;
import com.example.jfxdemo.session.CourseSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static com.example.jfxdemo.database.dbconn.dbconn;

public class mainAdminController {

    @FXML
    public TextField eField;
    @FXML
    public Text text;
    @FXML
    private ListView<String> cList;
    @FXML
    private ListView<String> aList;


    public void initialize() throws  SQLException
    {
        //setup course and assignment list
        PreparedStatement sta = (PreparedStatement)dbconn().prepareStatement("select * from courses");
        ResultSet rsa = sta.executeQuery();
        while (rsa.next())
        {
            cList.getItems().add("Course: " + rsa.getInt("ID") + " Semester: " +rsa.getInt("semester"));
        }
        PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select * from assignments");
        ResultSet rsc = stc.executeQuery();
        while (rsc.next())
        {
            PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("select * from courses");
            ResultSet rs = st.executeQuery();
            while (rs.next())
            {
                if (rs.getInt("ID") == rsc.getInt("courseID")) {
                    aList.getItems().add("Course: " + rs.getInt("ID") + " Assignment: " + rsc.getInt("ID"));
                }
            }
        }
        PreparedStatement stb = (PreparedStatement)dbconn().prepareStatement("select * from assignments where courseID is null ");
        ResultSet rsb = stb.executeQuery();
        while (rsb.next())
        {
            aList.getItems().add("Course: null" + " Assignment: " + rsb.getInt("ID"));
        }
    }

    @FXML
    protected void toProfile(ActionEvent event) throws IOException
    {
        Stage stage;
        Parent scene;
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("userProfile.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    public void gotoCourse(ActionEvent actionEvent) throws SQLException, IOException {
        PreparedStatement sta = (PreparedStatement)dbconn().prepareStatement("select * from courses");
        ResultSet rsa = sta.executeQuery();
        while (rsa.next())
    {
        cList.getItems().add("Course: " + rsa.getInt("ID") + " Semester: " +rsa.getInt("semester"));
            if (cList.getSelectionModel().getSelectedItem().equals("Course: " + rsa.getInt("ID") + " Semester: " +rsa.getInt("semester")))
            {
                CourseSession.getInstance(rsa.getInt("ID"));
                Stage stage;
                Parent scene;
                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CoursesAview.fxml")));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
    }

    @FXML
    public void gotoAssignments(ActionEvent actionEvent) throws SQLException, IOException {
        CourseSession.getInstance(-1);
        PreparedStatement sta = (PreparedStatement)dbconn().prepareStatement("select * from courses");
        ResultSet rsa = sta.executeQuery();
        while (rsa.next())
        {
            PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select * from assignments where courseID = ?");
            stc.setInt(1, rsa.getInt("ID"));
            ResultSet rsc = stc.executeQuery();
            while (rsc.next())
            {
                if (aList.getSelectionModel().getSelectedItem().equals("Course: " + rsa.getInt("ID") + " Assignment: " + rsc.getInt("ID")))
                {
                    AssignmentSession.getInstance(rsc.getInt("ID"));
                    Stage stage;
                    Parent scene;
                    stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AssignmentAview.fxml")));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
            }
        }
        PreparedStatement stb = (PreparedStatement)dbconn().prepareStatement("select * from assignments where courseID is null ");
        ResultSet rsb = stb.executeQuery();
        while (rsb.next())
        {
            if (aList.getSelectionModel().getSelectedItem().equals("Course: null" + " Assignment: " + rsb.getInt("ID")))
            {
                AssignmentSession.getInstance(rsb.getInt("ID"));
                Stage stage;
                Parent scene;
                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AssignmentAview.fxml")));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
    }
    @FXML
    public void newCourse(ActionEvent actionEvent) {
        try {
            PreparedStatement stb = (PreparedStatement)dbconn().prepareStatement("INSERT into courses(name, description, semester) VALUES ('Course nane','desc...',0) ");

            stb.executeUpdate();

            PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select ID from courses order by ID desc limit 1");
            ResultSet rsc = stc.executeQuery();
            while (rsc.next())
            {
                CourseSession.getInstance(rsc.getInt("ID"));
                Stage stage;
                Parent scene;
                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CoursesAview.fxml")));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } catch (SQLException | IOException e ) {
            System.out.println("Submit not successful");
        }
    }

    @FXML
    public void newAssignments(ActionEvent actionEvent) {
        try {
            PreparedStatement stb = (PreparedStatement)dbconn().prepareStatement("INSERT into assignments(name, description, grade, type, courseid, dodate) VALUES ('name','desc',null,'type',1,'2010-10-10 00:00:00') ");

            stb.executeUpdate();

            PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select ID from assignments order by ID desc limit 1");
            ResultSet rsc = stc.executeQuery();
            while (rsc.next())
            {
                CourseSession.getInstance(-1);
                AssignmentSession.getInstance(rsc.getInt("ID"));
                Stage stage;
                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AssignmentAview.fxml")))));
                stage.show();
            }
        } catch (SQLException | IOException e ) {
            System.out.println("Submit not successful");
        }
    }

    @FXML
    public void newUser(ActionEvent actionEvent) throws SQLException {
        try {
            PreparedStatement stb = (PreparedStatement)dbconn().prepareStatement("INSERT into users() VALUES () ");
            stb.executeUpdate();
            PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select ID from users order by ID desc limit 1");
            ResultSet rsc = stc.executeQuery();
            while (rsc.next())
            {
                text.setText("Email was send to: " + eField.getText() + " with the ID: " + rsc.getInt("ID"));
            }
        } catch (SQLException e ) {
            System.out.println("Submit not successful");
        }
    }
}
