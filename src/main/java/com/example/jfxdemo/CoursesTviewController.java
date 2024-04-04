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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.example.jfxdemo.database.dbconn.dbconn;

public class CoursesTviewController {

    @FXML
    public AnchorPane toAssessment;
    @FXML
    public Button assignment;
    @FXML
    public Text AssignmentString;
    @FXML
    public ToggleButton edit;
    @FXML
    public TextField semester;
    @FXML
    public ListView participants;
    @FXML
    public TextField selectedS;
    @FXML
    public TextField grade;
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
    }
    @FXML
    protected void toMain(ActionEvent event) throws IOException
    {
        CourseSession.deleteInstance();
        Stage stage;
        Parent scene;
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainteachermenu.fxml")));
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
                    scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AssignmentTview.fxml")));
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
    public void submitG(ActionEvent actionEvent) throws SQLException {
        PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select * from users where ID = ?");
        stc.setInt(1, Integer.parseInt(selectedS.getText()));
        ResultSet rsc = stc.executeQuery();
        while (rsc.next())
        {
            if (rsc.getString("role").equals("S")){
                PreparedStatement sta = (PreparedStatement)dbconn().prepareStatement("select * from courserelations where courseID = ?");
                sta.setInt(1, CourseSession.getInstance().getCid());
                ResultSet rsa = sta.executeQuery();
                while (rsa.next())
                {
                    try {
                        PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("UPDATE courserelations SET grade = ? WHERE ID = ?");
                        st.setString(1, grade.getText());
                        st.setInt(2, rsa.getInt("ID"));
                        st.executeUpdate();
                    } catch (SQLException e ) {
                        System.out.println("Submit not successful");
                    }
                }
            }
        }
    }

    @FXML
    public void createFinalGrades(ActionEvent actionEvent) throws SQLException {
        double localGrade = 0;
        PreparedStatement sta = (PreparedStatement)dbconn().prepareStatement("select * from courserelations where courseID = ?");
        sta.setInt(1, CourseSession.getInstance().getCid());
        ResultSet rsa = sta.executeQuery();
        while (rsa.next())
        {
            PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select * from users where ID = ? and role = 'S'");
            stc.setInt(1, rsa.getInt("userID"));
            ResultSet rsc = stc.executeQuery();
            while (rsc.next())
            {
                int a = 100;
                PreparedStatement stb = (PreparedStatement)dbconn().prepareStatement("select * from assignments where courseID = ?");
                stb.setInt(1, rsa.getInt("courseID"));
                ResultSet rsb = stb.executeQuery();
                while (rsb.next())
                {
                    PreparedStatement std = (PreparedStatement)dbconn().prepareStatement("select * from assignmentrelations where assignmentsID = ? and userID = ? ");
                    std.setInt(1, rsb.getInt("ID"));
                    std.setInt(2, rsc.getInt("ID"));
                    ResultSet rsd = std.executeQuery();
                    while (rsd.next()) {
                        a -= rsb.getInt("grade");
                        localGrade +=(rsd.getInt("grade") * (rsb.getInt("grade") * 0.01));
                    }
                }
                localGrade += (rsa.getInt("grade")*( a *0.01));
                a = 100;
                try {
                    PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("INSERT into grade(grade,userID,courseID) VALUES (?,?,?) ");
                    st.setInt(1, (int) localGrade);
                    st.setInt(2, rsc.getInt("ID"));
                    st.setInt(3, rsa.getInt("courseID"));
                    st.executeUpdate();
                    System.out.println("");
                    System.out.println("Great success!");
                    System.out.println("user: "+rsc.getInt("ID")+" Grade: "+localGrade);
                    System.out.println("");
                } catch (SQLException e ) {
                    System.out.println("Submit not successful");
                }
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
                    if (rsc.getString("role").equals("S")){
                        selectedS.setText(String.valueOf(rsc.getString("ID")));
                        grade.setText(String.valueOf(rsa.getInt("grade")));
                    }
                }
            }
        }
    }

    @FXML
    public void createReport(ActionEvent actionEvent) throws SQLException {
        FileChooser output = exportFile(new FileChooser(), new String[]{".txt"});
        File outputFile = output.showSaveDialog(assignment.getScene().getWindow());


        // Get the final content of the dot file
        if (outputFile != null)
        {
            //createDotGraph(generateTreeDot(),outputFile.getAbsolutePath(),"txt");
            saveSystem(outputFile,CreateFile());
            System.out.println("File created!");
            System.out.println("Filename: " + outputFile.getName());
            System.out.println("AbsolutePath: " + outputFile.getAbsolutePath());
        }
        else
        {
            System.out.println("Aborted");
        }
    }

    private FileChooser exportFile(FileChooser var, String[] extension)
    {
        var.setInitialFileName("Course " +CourseSession.getInstance().getCid()+" Student report Date " + LocalDate.now());
        for (String str : extension)
        {
            var.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                    String.format("%s File", str),
                    String.format("*%s", str)));
        }
        return var;
    }
    private void saveSystem(File file, String content)
    {
        try
        {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.write(content);
            printWriter.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    private String CreateFile() throws SQLException {


        String txt = "Student Report\n"+ "Course: "+ CourseSession.getInstance().getCid() +"\n" + "Date: " + LocalDate.now() + "\n\n";

        double localGrade = 0;
        PreparedStatement sta = (PreparedStatement)dbconn().prepareStatement("select * from courserelations where courseID = ?");
        sta.setInt(1, CourseSession.getInstance().getCid());
        ResultSet rsa = sta.executeQuery();
        while (rsa.next())
        {
            PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select * from users where ID = ? and role = 'S'");
            stc.setInt(1, rsa.getInt("userID"));
            ResultSet rsc = stc.executeQuery();
            while (rsc.next())
            {
                int a = 100;
                txt += "User ID: " + rsc.getInt("ID") + " Name: " + rsc.getString("name") + "\n";
                txt += "Assignments\n{\n";
                PreparedStatement stb = (PreparedStatement)dbconn().prepareStatement("select * from assignments where courseID = ?");
                stb.setInt(1, rsa.getInt("courseID"));
                ResultSet rsb = stb.executeQuery();
                while (rsb.next())
                {
                    PreparedStatement std = (PreparedStatement)dbconn().prepareStatement("select * from assignmentrelations where assignmentsID = ? and userID = ? ");
                    std.setInt(1, rsb.getInt("ID"));
                    std.setInt(2, rsc.getInt("ID"));
                    ResultSet rsd = std.executeQuery();
                    while (rsd.next()) {
                        a -= rsb.getInt("grade");
                        localGrade +=(rsd.getInt("grade") * (rsb.getInt("grade") * 0.01));
                        txt += "Assignment ID: " + rsb.getInt("ID") + " Type: " + rsb.getString("type") + " Grade: " + rsd.getInt("grade") + " Weight: " + rsb.getInt("grade") + "% Calculated Grade: " + rsd.getInt("grade") * (rsb.getInt("grade") * 0.01) + "\n";
                    }
                }
                txt += "}\n";
                localGrade += (rsa.getInt("grade")*( a *0.01));
                txt += "Course ID: " + CourseSession.getInstance().getCid() + " Grade: " + rsa.getInt("grade") + " Weight: " + a + "% Calculated Grade: " + rsa.getInt("grade") * (a*0.01) + "\n";
                txt += "Total Grade: " + localGrade+"\n\n";
                a = 100;
            }
        }
        return txt;
    }
}
