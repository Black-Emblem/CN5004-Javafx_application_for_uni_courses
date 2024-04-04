package com.example.jfxdemo;

import com.example.jfxdemo.session.AssignmentSession;
import com.example.jfxdemo.session.CourseSession;
import com.example.jfxdemo.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static com.example.jfxdemo.database.dbconn.dbconn;

public class mainTeacherController {

    @FXML
    private ListView<String> cList;
    @FXML
    private ListView<String> aList;


    public void initialize() throws  SQLException
    {
        //setup course and assignment list
        PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("select courseID from courserelations where userID = ?");
        st.setInt(1, UserSession.getInstance().getUid());
        ResultSet rs = st.executeQuery();
        while (rs.next())
        {
            PreparedStatement sta = (PreparedStatement)dbconn().prepareStatement("select * from courses where ID = ?");
            sta.setInt(1, rs.getInt("courseID"));
            ResultSet rsa = sta.executeQuery();
            while (rsa.next())
            {
                cList.getItems().add("Course: " + rsa.getInt("ID") + " Semester: " +rsa.getInt("semester"));
                PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select * from assignments where courseID = ?");
                stc.setInt(1, rsa.getInt("ID"));
                ResultSet rsc = stc.executeQuery();
                while (rsc.next())
                {
                    aList.getItems().add("Course: " + rsa.getInt("ID") + " Assignment: " + rsc.getInt("ID"));
                }
            }
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
    protected void createGrades(ActionEvent event) throws IOException, SQLException {
        PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("select * from courserelations where userID = ? ");
        st.setInt(1, UserSession.getInstance().getUid());
        ResultSet rs = st.executeQuery();
        while (rs.next())
        {
            PreparedStatement sta = (PreparedStatement)dbconn().prepareStatement("select * from courses where ID = ?");
            sta.setInt(1, rs.getInt("ID"));
            ResultSet rsa = sta.executeQuery();
            while (rsa.next())
            {
                PreparedStatement stb = (PreparedStatement)dbconn().prepareStatement("select * from courserelations where courseID = ?");
                stb.setInt(1, rsa.getInt("ID"));
                ResultSet rsb = stb.executeQuery();
                while (rsb.next())
                {
                    PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select * from users where ID = ? and role = 'S'");
                    stc.setInt(1, rsb.getInt("userID"));
                    ResultSet rsc = stc.executeQuery();
                    while (rsc.next())
                    {
                        PreparedStatement std = (PreparedStatement)dbconn().prepareStatement("select * from grade where userID = ? and courseID = ?");
                        std.setInt(1, rsb.getInt("userID"));
                        std.setInt(2, rsa.getInt("courseID"));
                        ResultSet rsd = std.executeQuery();
                        System.out.println(rsd.getRow());
                    }
                }
            }
        }
    }

    public void gotoCourse(ActionEvent actionEvent) throws SQLException, IOException {
        PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("select courseID from courserelations where userID = ?");
        st.setInt(1, UserSession.getInstance().getUid());
        ResultSet rs = st.executeQuery();
        while (rs.next())
        {
            PreparedStatement sta = (PreparedStatement)dbconn().prepareStatement("select * from courses where ID = ?");
            sta.setInt(1, rs.getInt("courseID"));
            ResultSet rsa = sta.executeQuery();
            while (rsa.next())
            {
                if (cList.getSelectionModel().getSelectedItem().equals("Course: " + rsa.getInt("ID") + " Semester: " +rsa.getInt("semester")))
                {
                    CourseSession.getInstance(rsa.getInt("ID"));
                    Stage stage;
                    Parent scene;
                    stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CoursesTview.fxml")));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
            }
        }
    }

    public void gotoAssignments(ActionEvent actionEvent) throws SQLException, IOException {
        CourseSession.getInstance(-1);
        PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("select courseID from courserelations where userID = ?");
        st.setInt(1, UserSession.getInstance().getUid());
        ResultSet rs = st.executeQuery();
        while (rs.next())
        {
            PreparedStatement sta = (PreparedStatement)dbconn().prepareStatement("select * from courses where ID = ?");
            sta.setInt(1, rs.getInt("courseID"));
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
                        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AssignmentTview.fxml")));
                        stage.setScene(new Scene(scene));
                        stage.show();
                    }
                }
            }
        }
    }

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
                scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CoursesTview.fxml")));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } catch (SQLException | IOException e ) {
            System.out.println("Submit not successful");
        }
    }

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
                stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AssignmentTview.fxml")))));
                stage.show();
            }
        } catch (SQLException | IOException e ) {
            System.out.println("Submit not successful");
        }
    }
}
