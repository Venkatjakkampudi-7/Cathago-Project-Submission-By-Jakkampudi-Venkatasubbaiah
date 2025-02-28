package backend_processes;

import java.sql.Connection;
import java.sql.SQLException;

public class Dbconnection_test {
    public static void main(String args[]) throws SQLException{
        DBConnection db = new DBConnection();
        Connection con = db.getConnection();
        if(con == null){
            throw new SQLException("Database connection is NULL! Check your connection initialization.");
        }
    }
}
