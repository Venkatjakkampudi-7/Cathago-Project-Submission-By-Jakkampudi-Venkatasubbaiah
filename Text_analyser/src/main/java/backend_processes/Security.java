package backend_processes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Security extends DBConnection{
    private int userid;
    private String account_status;
    private String privillage;
    private String username;
    private String password;
    private Connection con;
    
    public Security(String username,String password)
    {
        this.username = username;
        this.password = password;

        // Establishing the connection
        this.con = this.getConnection();
    }
    
    // Function to log to history
    private String addlog()
    {
        String outcome = null;
        try
        {
            String logquery = "INSERT into log (userid,thetime) values (?,NOW());";
            try(PreparedStatement pst = this.con.prepareStatement(logquery))
            {
                pst.setInt(1, this.userid);

                int rowsAffected = pst.executeUpdate();

                if(rowsAffected > 0){
                    System.out.println("Login Successful ! Userid : "+this.userid);
                    outcome = "{ \"Status\" : \"Login Successful !\", \"Accountstatus\" : \""+this.account_status+"\", \"Privillage\" : \""+this.privillage+"\"}";
                }
                else{
                    outcome = "{ \"Staus \" : \"Failed to add log to history !\"}";
                }
            }
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
        return outcome;
    }
    // End of the function to add log
    
    // Function to Sign in the user
    public String signin()
    {
        String outcome = null;
        try{
            String skey_query = "SELECT userid,privillage,accountstatus FROM users WHERE username = ? AND CAST(AES_DECRYPT(encrypted_password, secret_key) AS CHAR) = ?;";
            try(PreparedStatement pst = this.con.prepareStatement(skey_query))
            {
                pst.setString(1, this.username);
                pst.setString(2, this.password);
                pst.execute();

                ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    // Retriving the userid
                    this.userid = rs.getInt("userid");
                    this.account_status = rs.getString("accountstatus");
                    this.privillage = rs.getString("privillage");
                    
                    if(this.account_status.equals("active"))
                    {
                        outcome = this.addlog();
                    }
                    else
                    {
                        outcome = "{ \"Status\" : \"Login Failure !\", \"Accountstatus\" : \""+this.account_status+"\", \"Privillage\" : \""+this.privillage+"\"}";
                    }
                }
                else
                {
                    outcome = "Invalid credentials";
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return outcome;
    }
    // End of the function to sign in the user
    
    public int Get_userid(){
        return this.userid;
    }
    
    public String Get_account_status(){
        return this.account_status;
    }
    
    public String Get_privillage(){
        return this.privillage;
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