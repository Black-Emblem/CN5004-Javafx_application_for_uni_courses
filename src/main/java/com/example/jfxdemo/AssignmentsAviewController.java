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
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

import static com.example.jfxdemo.database.dbconn.dbconn;

public class AssignmentsAviewController {

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
    public TextField selectedC;
    @FXML
    public ListView participants11;
    @FXML
    public ListView participants1;
    @FXML
    public TextField pass;
    @FXML
    public TextField selected1;
    @FXML
    public ListView Users;
    @FXML
    public TextField selected;
    @FXML
    private TextArea assignmentDescription;
    @FXML
    private TextField assignmentString;
    @FXML
    private Button goBack;
    @FXML
    private Button DC;




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
            participants1.getItems().add("ID: "+rs.getInt("courseID"));

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
        PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select * from users");
        ResultSet rsc = stc.executeQuery();
        while (rsc.next())
        {
            Users.getItems().add("ID: "+rsc.getString("ID")+" Name: "+rsc.getString("name")+" role: "+ rsc.getString("role"));
        }
        PreparedStatement std = (PreparedStatement)dbconn().prepareStatement("select * from courses");
        ResultSet rsd = std.executeQuery();
        while (rsd.next())
        {
            participants11.getItems().add("ID: "+rsd.getInt("ID"));
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
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CoursesAview.fxml")));
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
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainadminmenu.fxml")));
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
                    selected.setText(String.valueOf(rsc.getString("ID")));
                }
            }
        }
    }

    @FXML
    public void Delete(ActionEvent actionEvent) throws SQLException {
        if (pass.isDisabled()){
            pass.setDisable(false);
            text.setText("Enter your password to confirm this action");
            DC.setText("Confirm");
        }
        else {
            PreparedStatement sta = (PreparedStatement)dbconn().prepareStatement("select * from users where ID = ? and role = 'A'");
            sta.setInt(1, UserSession.getInstance().getUid());
            ResultSet rsa = sta.executeQuery();
            while (rsa.next())
            {
                if (pass.getText().equals(rsa.getString("password"))){
                    try {
                        PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("DELETE FROM assignmentrelations WHERE ID = ?");
                        st.setInt(1, AssignmentSession.getInstance().getaid());
                        st.executeQuery();
                        if (CourseSession.getInstance().isSet())
                        {
                            AssignmentSession.deleteInstance();
                            Stage stage;
                            Parent scene;
                            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CoursesAview.fxml")));
                            stage.setScene(new Scene(scene));
                            stage.show();
                        }
                        else
                        {
                            CourseSession.deleteInstance();
                            AssignmentSession.deleteInstance();
                            Stage stage;
                            Parent scene;
                            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainadminmenu.fxml")));
                            stage.setScene(new Scene(scene));
                            stage.show();
                        }
                    } catch(Exception e) {
                        pass.setText("");
                        pass.setDisable(true);
                        text.setText("Wrong password");
                        DC.setText("Delete assignment");
                        System.out.println(e);
                    }
                }
                else {
                    pass.setText("");
                    pass.setDisable(true);
                    text.setText("Wrong password");
                    DC.setText("Delete assignment");
                }
            }
        }
    }

    @FXML
    public void removeP(ActionEvent actionEvent) throws SQLException {
        PreparedStatement stc = (PreparedStatement)dbconn().prepareStatement("select * from assignmentrelations where assignmentsID = ? and userID = ? limit 1");
        stc.setInt(1, AssignmentSession.getInstance().getaid());
        stc.setInt(2, Integer.parseInt(selected.getText()));
        ResultSet rsc = stc.executeQuery();
        while (rsc.next())
        {
            try {
                PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("DELETE FROM assignmentrelations WHERE ID = ?");
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
                PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("INSERT into assignmentrelations(userID, assignmentsID) VALUES (?,?)");
                st.setInt(1, rsc.getInt("ID"));
                st.setInt(2, AssignmentSession.getInstance().getaid());
                st.executeUpdate();

                PreparedStatement stb = (PreparedStatement)dbconn().prepareStatement("select userID from assignmentrelations where assignmentsID = ? and userID = ?");
                stb.setInt(1, AssignmentSession.getInstance().getaid());
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

    @FXML
    public void getSelectedC(ActionEvent actionEvent) throws SQLException {
        PreparedStatement std = (PreparedStatement)dbconn().prepareStatement("select * from courses");
        ResultSet rsd = std.executeQuery();
        while (rsd.next())
        {
            if (participants11.getSelectionModel().getSelectedItem().equals("ID: "+rsd.getInt("ID"))){
                selectedC.setText(String.valueOf(rsd.getInt("ID")));
            }
        }
    }

    @FXML
    public void RemoveC(ActionEvent actionEvent) {
        try {
            PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("UPDATE assignments SET courseID = null WHERE ID = ?");
            st.setInt(1, AssignmentSession.getInstance().getaid());
            st.executeUpdate();
            text.setText("Updated");
            participants1.getItems().clear();
        } catch (SQLException e ) {
            text.setText("Submit not successful");
        }
    }

    @FXML
    public void addC(ActionEvent actionEvent) {
        try {
            PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("UPDATE assignments SET courseID = ? WHERE ID = ?");
            st.setInt(1, Integer.parseInt(selectedC.getText()));
            st.setInt(2, AssignmentSession.getInstance().getaid());
            st.executeUpdate();
            text.setText("Updated");
            participants1.getItems().clear();
            participants1.getItems().add("ID: "+selectedC.getText());
        } catch (SQLException e ) {
            text.setText("Submit not successful");
        }
    }
}