package core.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookData {
    public static ResultSet getBooks() throws SQLException {
        String query = "SELECT b.BOOK_ID, b.TITLE, (a.NAME || ' ' || a.SURNAME) AS AUTHOR, b.ISBN, b.PUBLISHED_YEAR, b.COPIES_AVAILABLE, b.CATEGORY " +
                       "FROM BOOKS b " +
                       "JOIN AUTHORS a ON b.AUTHOR_ID = a.AUTHOR_ID";
        
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