package core.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BorrowersData {

    // Method to get borrowers
    public static ResultSet getBorrowers() throws SQLException {
        String query = "SELECT b.BORROWER_ID, b.BOOK_ID, b.USER_ID, b.BORROW_DATE, b.DUE_DATE, b.RETURN_DATE " +
                       "FROM BORROWERS b";
        
        // Use try-with-resources to manage connection and statement
        try (Connection connection = DatabaseConnection.getConnection(); // Ensure the connection is opened
             PreparedStatement statement = connection.prepareStatement(query)) {
             
            return statement.executeQuery(); // Execute the query and return the ResultSet
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error executing query: " + e.getMessage(), e);
        }
    }
}