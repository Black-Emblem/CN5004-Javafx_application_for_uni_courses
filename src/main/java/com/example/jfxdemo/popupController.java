package com.example.jfxdemo;

import com.example.jfxdemo.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.jfxdemo.database.dbconn.dbconn;

public class popupController {

    @FXML
    private TextField pEmail;
    @FXML
    private TextField phone;
    @FXML
    private PasswordField pass;
    @FXML
    private PasswordField passC;
    @FXML
    private Text text;
    @FXML
    private Button submit;
    @FXML
    private Button unlock;

    @FXML
    public void initialize() throws SQLException
    {
        PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("select * from users where ID = ? ");
        st.setInt(1, UserSession.getInstance().getUid());
        ResultSet rs = st.executeQuery();
        while (rs.next())
        {
            pEmail.setText(rs.getString("pEmail"));
            phone.setText(rs.getString("phone"));
        }
    }
    public void submit(ActionEvent actionEvent) {
        if (pass.getText().equals(passC.getText())){
            try {
                PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("UPDATE users SET pEmail = ?, password = ?, phone = ? WHERE ID = ?");
                st.setString(1, pEmail.getText());
                st.setString(2, pass.getText());
                st.setString(3, phone.getText());
                st.setInt(4, UserSession.getInstance().getUid());
                st.executeUpdate();
                text.setText("Updated");
                pEmail.setDisable(true);
                phone.setDisable(true);
                passC.setDisable(true);
                unlock.setDisable(false);
                submit.setDisable(true);
                pass.clear();
                passC.clear();


            } catch (SQLException e ) {
                text.setText("update not successful");
            }
        }
    }

    public void unlock(ActionEvent actionEvent) throws SQLException {
        PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("select password from users where ID = ? ");
        st.setInt(1, UserSession.getInstance().getUid());
        ResultSet rs = st.executeQuery();
        while (rs.next())
        {
            if(pass.getText().equals(rs.getString("password"))){
                pEmail.setDisable(false);
                phone.setDisable(false);
                passC.setDisable(false);
                unlock.setDisable(true);
                submit.setDisable(false);
                pass.clear();
                text.setText("");
            }
            else {
                text.setText("Wrong password!");
            }
        }
    }
}
