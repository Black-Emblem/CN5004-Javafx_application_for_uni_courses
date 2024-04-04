package com.example.jfxdemo;

import com.example.jfxdemo.session.AssignmentSession;
import com.example.jfxdemo.session.CourseSession;
import com.example.jfxdemo.session.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.example.jfxdemo.database.dbconn.dbconn;

public class CoursesController {

    @FXML
    private TextArea courseDescription;
    @FXML
    private Text CourseString;
    @FXML
    private ListView<String> AssignmentList;
    @FXML
    private StackedBarChart chart;
    @FXML
    private Text textG;
    @FXML
    private Text text;
    private double grade;


    @FXML
    public void initialize() throws SQLException
    {

        XYChart.Series series1 = new XYChart.Series();
        int a = 100;
        PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("select * from courses where ID = ? ");
        st.setInt(1, CourseSession.getInstance().getCid());
        ResultSet rs = st.executeQuery();
        while (rs.next())
        {
            CourseString.setText(rs.getString("name"));
            courseDescription.setText(rs.getString("description"));
            courseDescription.setWrapText(true);
            courseDescription.setEditable(false);
            text.setText("Semester: "+rs.getString("semester"));
            series1.setName("Course ID: " + String.valueOf(rs.getInt("ID")));

            PreparedStatement stb = (PreparedStatement)dbconn().prepareStatement("select * from assignments where courseID = ?");
            stb.setInt(1,rs.getInt("ID"));
            ResultSet rsb = stb.executeQuery();
            while (rsb.next())
            {
                AssignmentList.getItems().add("Assignment: " + rsb.getString("ID") + " Name: " + rsb.getString("name"));

                PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select * from assignmentrelations where assignmentsID = ? and userID = ? and grade IS NOT NULL ");
                stc.setInt(1,rsb.getInt("ID"));
                stc.setInt(2, UserSession.getInstance().getUid());
                ResultSet rsc = stc.executeQuery();
                while (rsc.next())
                {
                    a -= rsb.getInt("grade");
                    grade += rsc.getInt("grade")*( rsb.getInt("grade") * 0.01);
                }
            }
            PreparedStatement std = (PreparedStatement)dbconn().prepareStatement("select * from courserelations where courseID = ? and userID = ? and grade IS NOT NULL ");
            std.setInt(1,rs.getInt("ID"));
            std.setInt(2, UserSession.getInstance().getUid());
            ResultSet rsd = std.executeQuery();
            while (rsd.next())
            {
                series1.getData().add(new XYChart.Data(String.valueOf(rs.getInt("ID")), (rsd.getInt("grade")*( a *0.01))));
                grade += rsd.getInt("grade")*( a *0.01);
            }
            a=100;
        }
        chart.getData().add(series1);

        //initialize grade chart
        for(XYChart.Series series : getData()){
            chart.getData().add(series);
        }
        if (grade >= 95.0){
            textG.setText("Grade: A+ "+grade+"%");
        }
        else if (grade >= 90.0){
            textG.setText("Grade: A  "+grade+"%");
        }
        else if (grade >= 80.0){
            textG.setText("Grade: B  "+grade+"%");
        }
        else if (grade >= 70.0){
            textG.setText("Grade: C  "+grade+"%");
        }
        else if (grade >= 60.0){
            textG.setText("Grade: D  "+grade+"%");
        }
        else if (grade < 60.0){
            textG.setText("Grade: F  "+grade+"%");
        }
    }
    private ObservableList<XYChart.Series> getData() throws SQLException {
        var list = FXCollections.<XYChart.Series>observableArrayList();
        ArrayList<List> arrayList = new ArrayList<>();

        PreparedStatement stb = (PreparedStatement)dbconn().prepareStatement("select * from assignments where courseID = ?");
        stb.setInt(1,CourseSession.getInstance().getCid());
        ResultSet rsb = stb.executeQuery();
        while (rsb.next())
        {
            PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select * from assignmentrelations where assignmentsID = ? and userID = ? and grade IS NOT NULL ");
            stc.setInt(1,rsb.getInt("ID"));
            stc.setInt(2, UserSession.getInstance().getUid());
            ResultSet rsc = stc.executeQuery();
            while (rsc.next()) {
                arrayList.add(Arrays.asList(rsb.getString("name") , String.valueOf(CourseSession.getInstance().getCid()), (rsc.getInt("grade") * (rsb.getInt("grade") * 0.01))));
            }
        }

        for (List obs : arrayList){
            list.add(new XYChart.Series((String) obs.get(0), FXCollections.observableArrayList(new XYChart.Data<>((String) obs.get(1), (Number) obs.get(2)))));
        }
        return list;
    }

    @FXML
    protected void toMain(ActionEvent event) throws IOException
    {
        CourseSession.deleteInstance();
        Stage stage;
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainstudentmenu.fxml")))));
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
                    scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Assignment.fxml")));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
            }
        }
    }

    public void submit(ActionEvent actionEvent) {
    }
}
