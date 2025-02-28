package backend_processes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.http.HttpSession;

public class Logout extends DBConnection{
    private Connection con;
    
    public Logout(){
        this.con = this.getConnection();
    }
    
    public void addlog(int userid){
        try{
            String status = "signout";
            String sql = "INSERT INTO log (userid,thetime,status) values (?,NOW(),?);";
            try(PreparedStatement pst = this.con.prepareStatement(sql))
            {
                pst.setInt(1, userid);
                pst.setString(2, status);
                
                pst.executeUpdate();
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    // Function to close the connection
    public void Close_connection(){
        if(this.con != null){
            try{
                if(!this.con.isClosed()){
                    this.con.close();
                    this.con = null;
                }
            }
            catch(SQLException se){
                se.printStackTrace();
            }
        }
    }
    // End of the function to close the connection
}