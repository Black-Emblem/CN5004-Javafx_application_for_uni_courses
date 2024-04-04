package com.example.jfxdemo.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.jfxdemo.database.dbconn.dbconn;

public class dbf {

    public static int translateEmailToID(String email) throws SQLException
    {
        Statement st = dbconn().createStatement();
        String sqlStr = "select ID from users where email = ?";
        PreparedStatement ps = dbconn().prepareStatement(sqlStr);
        ps.setString(1, email);
        ps.executeUpdate();
        ResultSet rs = st.executeQuery(sqlStr);
        while (rs.next()) {
            return rs.getInt("ID");
        }
        return -1;
    }

    public static boolean verifyEmail(String email) throws SQLException
    {
        Statement st = dbconn().createStatement();
        String sqlStr = "select email from users";
        ResultSet rs = st.executeQuery(sqlStr);
        while (rs.next()) {
            if (rs.getString("email").equals(email))
            {
                return true;
            }

        }
        return false;
    }

    public static int verifyCredentials(String email, String password) throws SQLException
    {
        PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("select * from users where email = ?");
        st.setString(1, email);
        ResultSet rs = st.executeQuery();
        while (rs.next())
        {
            if (rs.getString("email").equals(email) && rs.getString("password").equals(password))
            {
                return rs.getInt("ID");
            }
        }
        return -1;
    }

    public static String getRole(int uid) throws SQLException {
        PreparedStatement st = (PreparedStatement)dbconn().prepareStatement("select role from users where ID = ?");
        st.setInt(1, uid);
        ResultSet rs = st.executeQuery();
        while (rs.next())
        {
            return rs.getString("role");
        }
        return "!";
    }
}