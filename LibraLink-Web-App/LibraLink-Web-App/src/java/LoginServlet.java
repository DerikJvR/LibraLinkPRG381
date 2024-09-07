import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("pwd");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Load the PostgreSQL driver
            Class.forName("org.postgresql.Driver");

            // Establish the connection
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LibraLinkDB", "postgres", "Dexter@15");

            // Query to check user credentials
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);

            rs = stmt.executeQuery();

            if (rs.next()) {
                // User found, login successful
                request.getRequestDispatcher("login-success.jsp").forward(request, response);
                
                // Run the Desktop Application (LibraLink-Library-Management)
                /*try {
                    // Specify the full path to the Java executable and the classpath
                    String command = "C:\\Program Files\\Java\\jdk-<version>\\bin\\java -cp C:\\Users\\tnthe\\OneDrive\\Desktop\\LibraLink Management\\LibraLink Management\\src\\main Main";

                    // Execute the command
                    Process process = Runtime.getRuntime().exec(command);

                    // Optionally, handle process output
                    process.waitFor(); // Wait for the process to complete
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }*/

                
            } else {
                // User not found, login failed
                request.getRequestDispatcher("login-error.jsp").forward(request, response);
            }

        } catch (ClassNotFoundException e) {
            // Handle driver not found error
            e.printStackTrace();
            request.getRequestDispatcher("login-error.jsp").forward(request, response);

        } catch (SQLException e) {
            // Handle SQL related errors
            e.printStackTrace();
            request.getRequestDispatcher("login-error.jsp").forward(request, response);

        } finally {
            // Close resources in the finally block
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
