package core.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class OverdueBooksFetcher {

    public static ResultSet getOverdueBooks() throws SQLException {
        String query = "SELECT b.BORROWER_ID, b.BOOK_ID, u.EMAIL, bk.TITLE, b.BORROW_DATE, b.DUE_DATE " +
                       "FROM BORROWERS b " +
                       "JOIN USERS u ON b.USER_ID = u.USER_ID " +
                       "JOIN BOOKS bk ON b.BOOK_ID = bk.BOOK_ID " +
                       "WHERE b.RETURN_DATE IS NULL AND CURRENT_DATE > b.DUE_DATE";

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

    public static long calculateDaysOverdue(java.sql.Date dueDate) {
        return ChronoUnit.DAYS.between(dueDate.toLocalDate(), LocalDate.now());
    }
}
