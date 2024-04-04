package com.example.jfxdemo;

import com.example.jfxdemo.session.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.example.jfxdemo.database.dbconn.dbconn;

public class UserProfileController {

    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField pEmailField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private PasswordField passField;
    @FXML
    private ChoiceBox genderField;
    @FXML
    private DatePicker bDayField;
    @FXML
    private BarChart avarGrade;

    private int a = 0;

    @FXML
    public void initialize() throws SQLException
    {
        //initialize info form
        avarGrade.getData().clear();
        StringConverter converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd");
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        bDayField.setConverter(converter);
        bDayField.setPromptText("yyyy-mm-dd");
        PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("select * from users where ID = ? ");
        st.setInt(1, UserSession.getInstance().getUid());
        ResultSet rs = st.executeQuery();
        while (rs.next())
        {
            idField.setText(String.valueOf(rs.getInt("ID")));
            nameField.setText(rs.getString("name"));
            surnameField.setText(rs.getString("surname"));
            pEmailField.setText(rs.getString("pEmail"));
            emailField.setText(rs.getString("email"));
            phoneField.setText(rs.getString("phone"));
            passField.setText(rs.getString("password"));
            genderField.setValue("Other");
            genderField.setValue("Male");
            genderField.setValue("Female");
            switch(rs.getString("gender")) {
                case "Other":
                    genderField.getSelectionModel().isSelected(0);
                    break;
                case "Male":
                    genderField.getSelectionModel().isSelected(1);
                    break;
                case "Female":
                    genderField.getSelectionModel().isSelected(2);
                    break;
            }
            bDayField.setValue(LocalDate.parse(String.valueOf(rs.getDate("birthday"))));
        }

        //initialize grade chart
        for(XYChart.Series series : getData()){
            avarGrade.getData().add(series);
        }
    }

    private ObservableList<XYChart.Series> getData() throws SQLException {
        var list = FXCollections.<XYChart.Series>observableArrayList();
        ArrayList<List> arrayList = new ArrayList<>();
        ArrayList<List> List = new ArrayList<>();
        int[][] averageGrade = new int[10][20];
        for (int i = 0; i <= (UserSession.getInstance().getCurrentSemester()+1); i++) {
            PreparedStatement stb = (PreparedStatement) dbconn().prepareStatement("select * from courses where semester = ?");
            stb.setInt(1, (i+1));
            ResultSet rsb = stb.executeQuery();
            while (rsb.next()) {
                PreparedStatement sta = (PreparedStatement) dbconn().prepareStatement("select * from courserelations where userID = ? and courseID = ? ");
                sta.setInt(1, UserSession.getInstance().getUid());
                sta.setInt(2, rsb.getInt("ID"));
                ResultSet rsa = sta.executeQuery();
                while (rsa.next()) {
                    PreparedStatement stc = (PreparedStatement) dbconn().prepareStatement("select * from grade where courseID = ? and userID = ?");
                    stc.setInt(1, rsb.getInt("ID"));
                    stc.setInt(2, UserSession.getInstance().getUid());
                    ResultSet rsc = stc.executeQuery();
                    while (rsc.next()) {
                        averageGrade[i][a] = rsc.getInt("grade");
                        a++;
                    }
                }
            }
        }

        for (int i = 0; i < UserSession.getInstance().getCurrentSemester(); i++) {
            int first = 0;
            int second = 0;
            for (int j = 0; j < 20; j++) {
                if (averageGrade[i][j] != 0){
                    first += averageGrade[i][j];
                    second++;
                }
            }
            if (second == 0){
                first = first/1;
            }
            else {
                first = first/second;
            }
            arrayList.add(Arrays.asList("Semester: "+(i+1), "Semester: "+ (i+1),first));
        }

        for (List obs : arrayList){
            list.add(new XYChart.Series((String) obs.get(0), FXCollections.observableArrayList(new XYChart.Data<>((String) obs.get(1), (Number) obs.get(2)))));
        }
        return list;
    }
    @FXML
    protected void toMain(ActionEvent event) throws IOException
    {
        if (UserSession.getInstance().getRole().equals("S")){
            Stage stage;
            Parent scene;
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainstudentmenu.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        } else if (UserSession.getInstance().getRole().equals("T")) {
            Stage stage;
            Parent scene;
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainteachermenu.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        } else if (UserSession.getInstance().getRole().equals("A")) {
            Stage stage;
            Parent scene;
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainadminmenu.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    public void unlock(ActionEvent actionEvent) throws IOException, SQLException {
        Parent scene;
        Stage newWindow = new Stage();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("popup.fxml")));
        newWindow.setTitle("Personal info form");
        newWindow.setScene(new Scene(scene));
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
        newWindow.show();
        initialize();
    }
}
