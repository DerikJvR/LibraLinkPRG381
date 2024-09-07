package core.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserData {
    public static ResultSet getUsers() throws SQLException {
        String query = "SELECT USER_ID, NAME, SURNAME, EMAIL, PHONE, REGISTRATION_DATE FROM USERS";
        
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error executing query: " + e.getMessage(), e);
        }
        
        return resultSet;
    }
}