package com.example.jfxdemo;

import com.example.jfxdemo.grade.Grade;
import com.example.jfxdemo.session.AssignmentSession;
import com.example.jfxdemo.session.CourseSession;
import com.example.jfxdemo.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.jfxdemo.database.dbconn.dbconn;
import static com.example.jfxdemo.grade.Grade.*;



public class mainStudentController
{
    @FXML
    private ListView<String> cList;
    @FXML
    private ListView<String> aList;
    @FXML
    private LineChart gradeGraph;
    @FXML
    private NumberAxis yAxis ;
    @FXML
    private CategoryAxis xAxis ;


    @FXML
    public void initialize() throws  SQLException
    {

        //setup course list
        PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("select courseID from courserelations where userID = ?");
        st.setInt(1, UserSession.getInstance().getUid());
        ResultSet rs = st.executeQuery();
        while (rs.next())
        {
            PreparedStatement sta = (PreparedStatement)dbconn().prepareStatement("select * from courses where ID = ? and semester < ? ");
            sta.setInt(1, rs.getInt("courseID"));
            sta.setInt(2, (UserSession.getInstance().getCurrentSemester() + 1));
            ResultSet rsa = sta.executeQuery();
            while (rsa.next())
            {
                cList.getItems().add("Course: " + rsa.getInt("ID") + " Semester: " +rsa.getInt("semester"));
            }
        }
        //setup assignment list
        PreparedStatement stb = (PreparedStatement)dbconn().prepareStatement("select assignmentsID from assignmentrelations where userID = ?");
        stb.setInt(1, UserSession.getInstance().getUid());
        ResultSet rsb = stb.executeQuery();
        while (rsb.next())
        {
            PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select * from assignments where ID = ?");
            stc.setInt(1, rsb.getInt("assignmentsID"));
            ResultSet rsc = stc.executeQuery();
            while (rsc.next())
            {
                PreparedStatement std = (PreparedStatement)dbconn().prepareStatement("select * from courses where ID = ? and semester < ? ");
                std.setInt(1, rsc.getInt("courseID"));
                std.setInt(2, (UserSession.getInstance().getCurrentSemester() + 1));
                ResultSet rsd = std.executeQuery();
                while (rsd.next())
                {
                    aList.getItems().add("Course: " + rsd.getInt("ID") + " Assignment: " + rsc.getInt("ID"));
                }
            }
        }


        //setup course grade graph
        clearCGrades();
        clearAGrades();
        PreparedStatement ste = (PreparedStatement)dbconn().prepareStatement("select courseID from courserelations where userID = ?");
        ste.setInt(1, UserSession.getInstance().getUid());
        ResultSet rse = ste.executeQuery();
        while (rse.next())
        {
            PreparedStatement sta = (PreparedStatement)dbconn().prepareStatement("select * from courses where ID = ? and semester < ? ");
            sta.setInt(1, rse.getInt("courseID"));
            sta.setInt(2, (UserSession.getInstance().getCurrentSemester() + 1));
            ResultSet rsa = sta.executeQuery();
            while (rsa.next())
            {
                PreparedStatement std = (PreparedStatement)dbconn().prepareStatement("select * from grade where courseID = ? and userID = ?");
                std.setInt(1, rsa.getInt("ID"));
                std.setInt(2, UserSession.getInstance().getUid());
                ResultSet rsd = std.executeQuery();
                while (rsd.next())
                {
                    addCGrade(rsd.getInt("grade"),rsd.getInt("courseID") ,-1 , String.valueOf(rsd.getDate("date")));
                }
                PreparedStatement stf = (PreparedStatement)dbconn().prepareStatement("select * from assignmentrelations where grade IS NOT NULL and userID = ?");
                stf.setInt(1, UserSession.getInstance().getUid());
                ResultSet rsf = stf.executeQuery();
                while (rsf.next())
                {
                    PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select * from assignments where courseID = ? and  ID = ?");
                    stc.setInt(1, rsa.getInt("ID"));
                    stc.setInt(2, rsf.getInt("assignmentsID"));
                    ResultSet rsc = stc.executeQuery();
                    while (rsc.next())
                    {
                        addAGrade(rsf.getInt("grade"),-1 ,rsc.getInt("ID") , String.valueOf(rsc.getDate("doDate")));
                    }
                }
            }
        }
        sortCGrade();
        sortAGrade();
        xAxis.setLabel("Date");
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(100);
        yAxis.setTickUnit(5);
        XYChart.Series<Number, Number> courses = new XYChart.Series<Number, Number>();
        XYChart.Series<Number, Number> assignments = new XYChart.Series<Number, Number>();
        gradeGraph.setTitle("Grades");
        courses.setName("Course grades");
        for (int i = 0; i < getCLength(); i++)
        {
            courses.getData().add(new XYChart.Data(getSpecificCDate(i),getSpecificCGrade(i)));
        }
        assignments.setName("Assignment grades");
        for (int i = 0; i < getALength(); i++)
        {
            assignments.getData().add(new XYChart.Data(getSpecificADate(i), getSpecificAGrade(i)));
        }
        gradeGraph.getData().add(courses);
        gradeGraph.getData().add(assignments);
    }

    @FXML
    protected void toCourse(ActionEvent event) throws IOException, SQLException {
        PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("select courseID from courserelations where userID = ?");
        st.setInt(1, UserSession.getInstance().getUid());
        ResultSet rs = st.executeQuery();
        while (rs.next())
        {
            PreparedStatement sta = (PreparedStatement)dbconn().prepareStatement("select * from courses where ID = ? and semester < ? ");
            sta.setInt(1, rs.getInt("courseID"));
            sta.setInt(2, (UserSession.getInstance().getCurrentSemester() + 1));
            ResultSet rsa = sta.executeQuery();
            while (rsa.next())
            {
                if (cList.getSelectionModel().getSelectedItem().equals("Course: " + rsa.getInt("ID") + " Semester: " +rsa.getInt("semester")))
                {

                    CourseSession.getInstance(rsa.getInt("ID"));
                    Stage stage;
                    Parent scene;
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Courses.fxml")));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
            }
        }
    }

    @FXML
    protected void toAssignment(ActionEvent event) throws IOException, SQLException
    {
        CourseSession.getInstance(-1);
        PreparedStatement stb = (PreparedStatement)dbconn().prepareStatement("select assignmentsID from assignmentrelations where userID = ?");
        stb.setInt(1, UserSession.getInstance().getUid());
        ResultSet rsb = stb.executeQuery();
        while (rsb.next())
        {
            PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select * from assignments where ID = ?");
            stc.setInt(1, rsb.getInt("assignmentsID"));
            ResultSet rsc = stc.executeQuery();
            while (rsc.next())
            {
                PreparedStatement std = (PreparedStatement)dbconn().prepareStatement("select * from courses where ID = ? and semester < ? ");
                std.setInt(1, rsc.getInt("courseID"));
                std.setInt(2, (UserSession.getInstance().getCurrentSemester() + 1));
                ResultSet rsd = std.executeQuery();
                while (rsd.next())
                {
                    if (aList.getSelectionModel().getSelectedItem().equals("Course: " + rsd.getInt("ID") + " Assignment: " + rsc.getInt("ID")))
                    {
                        AssignmentSession.getInstance(rsc.getInt("ID"));
                        Stage stage;
                        Parent scene;
                        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Assignment.fxml")));
                        stage.setScene(new Scene(scene));
                        stage.show();
                    }
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

}