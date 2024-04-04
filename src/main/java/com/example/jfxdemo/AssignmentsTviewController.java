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
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

import static com.example.jfxdemo.database.dbconn.dbconn;

public class AssignmentsTviewController {

    @FXML
    public ListView participants;
    @FXML
    public TextField grade;
    @FXML
    public TextField selectedS;
    @FXML
    public ImageView UserIcon;
    @FXML
    public Text textID;
    @FXML
    public TextField weight;
    @FXML
    public Text text;
    @FXML
    public ToggleButton edit;
    @FXML
    public TextField type;
    @FXML
    public DatePicker dueDate;
    @FXML
    public TextField course;
    @FXML
    private TextArea assignmentDescription;
    @FXML
    private TextField assignmentString;
    @FXML
    private Button goBack;




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
            textID.setText("Assignment ID: " + String.valueOf(rs.getInt("ID")));
            assignmentString.setText(rs.getString("name"));
            assignmentDescription.setText(rs.getString("description"));
            type.setText(rs.getString("type"));
            dueDate.setValue(LocalDate.parse(String.valueOf((rs.getDate("doDate")))));
            course.setText(rs.getString("ID"));
            weight.setText(String.valueOf(rs.getInt("grade")));

            PreparedStatement sta = (PreparedStatement)dbconn().prepareStatement("select userID from assignmentrelations where assignmentsID = ?");
            sta.setInt(1, AssignmentSession.getInstance().getaid());
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
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CoursesTview.fxml")));
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
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainteachermenu.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        }

    }

    @FXML
    public void submit(ActionEvent actionEvent) {
        if (assignmentDescription.isDisabled()){
            type.setDisable(false);
            assignmentString.setDisable(false);
            assignmentDescription.setDisable(false);
            dueDate.setDisable(false);
            course.setDisable(true);
            weight.setDisable(false);
            text.setText("The form is now unlocked. Press edit again to submit changes.");
        }
        else {
            try {
                PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("UPDATE assignments SET name = ?, description = ?, type = ?, doDate = ?, courseID = ?, grade = ? WHERE ID = ?");
                st.setString(1, assignmentString.getText());
                st.setString(2, assignmentDescription.getText());
                st.setString(3, type.getText());
                st.setDate(4, Date.valueOf(dueDate.getValue()));
                st.setInt(5, Integer.parseInt(course.getText()));
                st.setInt(6, Integer.parseInt(weight.getText()));
                st.setInt(7, AssignmentSession.getInstance().getaid());
                st.executeUpdate();
                text.setText("Updated");
                type.setDisable(true);
                assignmentString.setDisable(true);
                assignmentDescription.setDisable(true);
                dueDate.setDisable(true);
                course.setDisable(true);
                weight.setDisable(true);


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
                PreparedStatement sta = (PreparedStatement)dbconn().prepareStatement("select userID from assignmentrelations where assignmentsID = ?");
                sta.setInt(1, AssignmentSession.getInstance().getaid());
                ResultSet rsa = sta.executeQuery();
                while (rsa.next())
                {
                    try {
                        PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("UPDATE assignmentrelations SET grade = ? WHERE ID = ?");
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
    public void createReport(ActionEvent actionEvent) throws SQLException {
        FileChooser output = exportFile(new FileChooser(), new String[]{".txt"});
        File outputFile = output.showSaveDialog(goBack.getScene().getWindow());


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

    @FXML
    public void getSelected(ActionEvent actionEvent) throws SQLException {
        PreparedStatement sta = (PreparedStatement)dbconn().prepareStatement("select * from assignmentrelations where assignmentsID = ?");
        sta.setInt(1, AssignmentSession.getInstance().getaid());
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

    private FileChooser exportFile(FileChooser var, String[] extension)
    {
        var.setInitialFileName("Assignment " +AssignmentSession.getInstance().getaid()+" Student report Date " + LocalDate.now());
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


        String txt = "Student Report\n"+ "Assignments: "+ AssignmentSession.getInstance().getaid() +"\n" + "Date: " + LocalDate.now() + "\n\n";

        PreparedStatement std = (PreparedStatement)dbconn().prepareStatement("select * from assignmentrelations where assignmentsID = ?");
        std.setInt(1, AssignmentSession.getInstance().getaid());
        ResultSet rsd = std.executeQuery();
        while (rsd.next()) {
            PreparedStatement stb = (PreparedStatement)dbconn().prepareStatement("select * from assignments where ID = ?");
            stb.setInt(1, AssignmentSession.getInstance().getaid());
            ResultSet rsb = stb.executeQuery();
            while (rsb.next()) {
                PreparedStatement stc = (PreparedStatement) dbconn().prepareStatement("select * from users where ID = ? and role = 'S'");
                stc.setInt(1, rsd.getInt("userID"));
                ResultSet rsc = stc.executeQuery();
                while (rsc.next()) {
                    txt += "User ID: " + rsc.getInt("ID") + " Name: " + rsc.getString("name") + "\n";
                    txt += "Assignment ID: " + AssignmentSession.getInstance().getaid() + " Type: " + rsb.getString("type") + " Grade: " + rsd.getInt("grade") + " Weight: " + rsb.getInt("grade") + "% Calculated Grade: " + rsd.getInt("grade") * (rsb.getInt("grade") * 0.01) + "\n\n\n";
                }
            }
        }
        return txt;
    }
}