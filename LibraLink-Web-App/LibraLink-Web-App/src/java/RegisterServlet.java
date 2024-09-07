import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("email");
        String password = request.getParameter("pwd");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String phone = request.getParameter("phone");
        String role = request.getParameter("LecturerSwitch") != null ? "Librarian" : "Student";
        
        Connection conn = null;
        PreparedStatement stmt = null;

        try {               
            // Load the PostgreSQL driver
            Class.forName("org.postgresql.Driver");
            
            // Establish the connection
           conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LibraLinkDB", "postgres", "Dexter@15");

            // Insert the data into the Users table
            String sql = "INSERT INTO users (username, password, name, surname, phone, email, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);  // No encryption applied as per your preference
            stmt.setString(3, name);
            stmt.setString(4, surname);
            stmt.setString(5, phone);
            stmt.setString(6, username);  // Using email as username
            stmt.setString(7, role);

            stmt.executeUpdate();

            // Redirect to a success page
            response.sendRedirect("register-success.jsp");

        } catch (ClassNotFoundException e) {
            // Handle driver not found error
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database driver not found: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);

        } catch (SQLException e) {
            // Handle SQL related errors
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);

        } finally {
            // Close resources in the finally block
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
