package backend_processes;

import java.sql.Connection;
import java.sql.SQLException;

public interface Databaseconnection {
    Connection getConnection() throws SQLException;
}