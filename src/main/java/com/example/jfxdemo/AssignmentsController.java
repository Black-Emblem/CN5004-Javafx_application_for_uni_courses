package com.example.jfxdemo;

import com.example.jfxdemo.session.AssignmentSession;
import com.example.jfxdemo.session.CourseSession;
import com.example.jfxdemo.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static com.example.jfxdemo.database.dbconn.dbconn;

public class AssignmentsController {

    @FXML
    private TextArea assignmentDescription;
    @FXML
    private Text assignmentString;
    @FXML
    private Button goBack;
    @FXML
    private BarChart chart;
    @FXML
    private Text Type;
    @FXML
    private Text DueDate;
    @FXML
    private Text Course;




    @FXML
    public void initialize() throws SQLException
    {
        if (CourseSession.getInstance().isSet())
        {
            goBack.setText("Back to course");
        }
        else {
            goBack.setText("Back to main");
        }
        PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("select * from assignments where ID = ? ");
        st.setInt(1, AssignmentSession.getInstance().getaid());
        ResultSet rs = st.executeQuery();
        while (rs.next())
        {
            assignmentString.setText(rs.getString("name"));
            assignmentDescription.setText(rs.getString("description"));
            assignmentDescription.setWrapText(true);
            assignmentDescription.setEditable(false);
            Type.setText(rs.getString("type"));
            DueDate.setText(String.valueOf(rs.getDate("doDate")));
            Course.setText("ID: " + rs.getString("ID") + " Name: " + rs.getString("name"));
            PreparedStatement sta = (PreparedStatement)dbconn().prepareStatement("select grade from assignmentrelations where assignmentsID = ? and userID = ?");
            sta.setInt(1, rs.getInt("ID"));
            sta.setInt(2, UserSession.getInstance().getUid());
            ResultSet rsa = sta.executeQuery();
            XYChart.Series series1 = new XYChart.Series();
            series1.setName(rs.getString("name"));
            while (rsa.next())
            {
                series1.getData().add(new XYChart.Data(String.valueOf(rs.getInt("ID")), rsa.getInt("grade")));
            }

            chart.getData().add(series1);
        }
    }

    @FXML
    protected void goBack(ActionEvent event) throws IOException
    {
        if (CourseSession.getInstance().isSet())
        {
            AssignmentSession.deleteInstance();
            Stage stage;
            Parent scene;
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Courses.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else
        {
            CourseSession.deleteInstance();
            AssignmentSession.deleteInstance();
            Stage stage;
            Parent scene;
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainstudentmenu.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        }

    }
}
