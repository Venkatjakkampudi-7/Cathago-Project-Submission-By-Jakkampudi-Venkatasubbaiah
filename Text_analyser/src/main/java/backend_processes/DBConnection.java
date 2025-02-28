package backend_processes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection implements Databaseconnection {
        // The private final variables
    private static final String HOST = "jdbc:mysql://localhost:3306/";      // String for the host name
    private static final String DB_NAME = "textanalyser";                   // String for the dbname
    private static final String USER = "root";                              // String for the username
    private static final String PASSWORD = "Tarzan2003";                    // String for the password
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";   // MySQL JDBC driver

    @Override
    public Connection getConnection() {                                     // Connection Function
        try {
            // Ensure the MySQL JDBC driver is loaded
            Class.forName(JDBC_DRIVER);

            // Use a more complete connection string with SSL turned off for local connections
            String connectionUrl = HOST + DB_NAME 
                                 + "?useSSL=false" 
                                 + "&allowPublicKeyRetrieval=true" 
                                 + "&serverTimezone=UTC";
            return DriverManager.getConnection(connectionUrl, USER, PASSWORD);
        } catch (SQLException e) {
            // Handle SQLException more gracefully and print stack trace for debugging
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // Handle ClassNotFoundException if the JDBC driver is not found
            System.err.println("JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        }
        return null;  // Return null if connection fails
    }
}