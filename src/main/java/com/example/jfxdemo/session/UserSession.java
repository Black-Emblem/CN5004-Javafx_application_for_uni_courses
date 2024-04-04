package com.example.jfxdemo.session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.jfxdemo.database.dbconn.dbconn;

public final class UserSession {

    private static UserSession instance;

    private int uid = -1;
    private String email;
    private String name;
    private String surname;
    private String role;
    private int currentSemester;


    private UserSession(int uid, String email, String name, String surname, String role, int currentSemester) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.currentSemester = currentSemester;
    }


    public static UserSession getInstance(int uid, String email, String name, String surname, String role, int currentSemester) {
        if(instance == null) {
            instance = new UserSession(uid, email, name, surname, role, currentSemester);
        }
        return instance;
    }

    public static UserSession getInstance() {
        return instance;
    }

    public static void setInstance(UserSession instance) {
        UserSession.instance = instance;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public int getCurrentSemester() {
        return currentSemester;
    }

    public void setCurrentSemester(int currentSemester) {
        this.currentSemester = currentSemester;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static void cleanUserSession()
    {
        instance.uid = -1;
        instance.email = "";
        instance.name = "";
        instance.surname = "";
        instance.role = "";
        instance.currentSemester = 0;
    }

    public static void generateSession(int id) throws SQLException
    {
        PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("select ID, role, email, name, surname, currentSemester from users where ID = ?");
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        while (rs.next())
        {
            UserSession.getInstance(rs.getInt("ID"),rs.getString("email"),rs.getString("name"),rs.getString("surname"),rs.getString("role"),rs.getInt("currentSemester"));
        }
    }

    @Override
    public String toString()
    {
        return "UserSession{" +
                "ID='" + uid + '\'' +
                "email='" + email + '\'' +
                "name='" + name + '\'' +
                "surname='" + surname + '\'' +
                "currentSemester='" + currentSemester + '\'' +
                ", role=" + role +
                '}';
    }
}