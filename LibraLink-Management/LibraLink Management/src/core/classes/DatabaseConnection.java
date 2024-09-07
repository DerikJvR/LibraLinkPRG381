package core.classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DRIVER = "org.apache.derby.jdbc.ClientDriver";
    private static final String JDBC_URL = "jdbc:derby://localhost:1527/LibraryDB;user=app;password=app";
    
    // Method to get a new database connection
    public static Connection getConnection() throws SQLException {
        try {
            // Load the Derby driver if not already loaded
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load database driver", e);
        }
        // Return a new connection
        return DriverManager.getConnection(JDBC_URL);
    }
    
    public static void addBook(String title, int authorID, String isbn, int publishedYear, int copies, String category) {
    // Check if the book with the same ISBN already exists
    String checkSql = "SELECT COUNT(*) FROM Books WHERE ISBN = ?";
    String insertSql = "INSERT INTO Books (TITLE, AUTHOR_ID, ISBN, PUBLISHED_YEAR, COPIES_AVAILABLE, CATEGORY) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = getConnection();
         PreparedStatement checkStmt = conn.prepareStatement(checkSql);
         PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

        // Check for existing book
        checkStmt.setString(1, isbn);
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
            System.out.println("Book with ISBN " + isbn + " already exists.");
            return;
        }

        // Set parameters for the insert statement
        insertStmt.setString(1, title);
        insertStmt.setInt(2, authorID);
        insertStmt.setString(3, isbn);
        insertStmt.setInt(4, publishedYear);
        insertStmt.setInt(5, copies);
        insertStmt.setString(6, category);

        // Execute the insert statement
        int rowsAffected = insertStmt.executeUpdate();
        System.out.println("Book added successfully. Rows affected: " + rowsAffected);

    } catch (SQLException e) {
        System.err.println("Failed to add book: " + e.getMessage());
        e.printStackTrace();
    }
}
    
    public static void addAuthor(int authorID, String authorName, String authorSurname) {
    // Check if the book with the same ISBN already exists
    
    String insertSql = "INSERT INTO AUTHORS (AUTHOR_ID, Name, Surname) VALUES (?, ?, ?)";

    try (Connection conn = getConnection();
         
         PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

     
        // Set parameters for the insert statement
        insertStmt.setInt(1, authorID);
        insertStmt.setString(2, authorName);
        insertStmt.setString(3, authorSurname);
        

        // Execute the insert statement
        int rowsAffected = insertStmt.executeUpdate();
        System.out.println("Book added successfully. Rows affected: " + rowsAffected);

    } catch (SQLException e) {
        System.err.println("Failed to add book: " + e.getMessage());
        e.printStackTrace();
    }
}
    
    public static void addBorrower( int bookID, int userID, java.sql.Date borrowDate, java.sql.Date dueDate, java.sql.Date returnDate) {
        // Check if the book with the same bookID exists
        String checkSql = "SELECT COUNT(*) FROM Books WHERE Book_ID = ?";
        String insertSql = "INSERT INTO Borrowers ( Book_ID, User_ID, Borrow_Date, Due_Date, Return_Date) VALUES ( ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            // Check if the book exists by bookID
            checkStmt.setInt(1, bookID);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("Book with ID " + bookID + " does not exist.");
                return;
            }

          //  borrowDate = sdf.parse(dateString);

            // Set parameters for the insert statement
            
            insertStmt.setInt(1, bookID);
            insertStmt.setInt(2, userID);
            insertStmt.setDate(3, borrowDate);  // Using java.sql.Date
            insertStmt.setDate(4, dueDate);     // Using java.sql.Date
            insertStmt.setDate(5, returnDate);  // Using java.sql.Date or null

            // Execute the insert statement
            int rowsAffected = insertStmt.executeUpdate();
            System.out.println("Borrower added successfully. Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            System.err.println("Failed to add Borrower: " + e.getMessage());
            e.printStackTrace();
        }
    }
}