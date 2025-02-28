package backend_processes;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.security.SecureRandom;
import java.math.BigInteger;

public class Users extends DBConnection{
    private String Firstname;
    private String Lastname;
    private String Username;
    private String Emailid;
    private String Password;
    private String Secretkey;
    private int credits;
    private int userid;
    private Connection con;
    
    // Method to generate a random key
    private String generateRandomKey(int length) {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32).substring(0, length);
    }
    
    public Users(){
        this.con = this.getConnection();
    }
    
    public Users(String Firstname, String Lastname, String Username, String Emailid, String Password) {
        this.Firstname = Firstname;
        this.Lastname = Lastname;
        this.Username = Username;
        this.Emailid = Emailid;
        this.Password = Password;
        this.Secretkey = generateRandomKey(10);
        
        this.con = this.getConnection();
    }
    
    public String Read_top_users(){
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        
        try{
            String privillage = "user";
            // Reading the users
            String sql = "SELECT firstname,lastname, (SELECT count(*) from scanlist where userid = us.userid) + (SELECT COUNT(*) from rescanlist re inner join scanlist sl on sl.scanid = re.scanid where sl.userid = us.userid) as count from users us where us.privillage = ? order by count desc;";
            try(PreparedStatement pst = this.con.prepareStatement(sql))
            {
                pst.setString(1, privillage);
                
                ResultSet rs = pst.executeQuery();
                while(rs.next())
                {
                    // Building the json
                    JsonObjectBuilder jsonObject = Json.createObjectBuilder()
                        .add("firstname", rs.getString("firstname"))
                        .add("lastname", rs.getString("lastname"))
                        .add("count", rs.getString("count"));

                    jsonArray.add(jsonObject);
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return jsonArray.build().toString();
    }
    
    // Function to Read users
    public String Read_users(){
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        
        try{
            int ccusage = 0;
            String privillage = "user";
            // Reading the users
            String sql = "SELECT us.userid,us.slabid,firstname,lastname,accountstatus from subscription_slab ss inner join users us on us.slabid = ss.slabid where privillage = ?;";
            try(PreparedStatement pst = this.con.prepareStatement(sql))
            {
                pst.setString(1, privillage);
                
                ResultSet rs = pst.executeQuery();
                while(rs.next())
                {
                    this.userid = rs.getInt("userid");
                    ccusage = this.Get_credit_count();

                    // Building the json
                    JsonObjectBuilder jsonObject = Json.createObjectBuilder()
                        .add("userid", rs.getInt("userid"))
                        .add("slabid", rs.getInt("slabid"))
                        .add("firstname", rs.getString("firstname"))
                        .add("lastname", rs.getString("lastname"))
                        .add("accountstatus", rs.getString("accountstatus"))
                        .add("creditsleft",ccusage);

                    jsonArray.add(jsonObject);
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return jsonArray.build().toString();
    }
    // End of the Function to Read Users
    
    // Function to Fetch User
    public String Fetch_users(String skey){
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        
        try{
            int ccusage = 0;
            String privillage = "user";
            // Reading the users
            String sql = "SELECT us.userid,us.slabid,firstname,lastname,accountstatus from subscription_slab ss inner join users us on us.slabid = ss.slabid where privillage = ? and (firstname like '"+skey+"%' or lastname like '"+skey+"%');";
            try(PreparedStatement pst = this.con.prepareStatement(sql))
            {
                pst.setString(1, privillage);
                
                ResultSet rs = pst.executeQuery();
                while(rs.next())
                {
                    this.userid = rs.getInt("userid");
                    ccusage = this.Get_credit_count();

                    // Building the json
                    JsonObjectBuilder jsonObject = Json.createObjectBuilder()
                        .add("userid", rs.getInt("userid"))
                        .add("slabid", rs.getInt("slabid"))
                        .add("firstname", rs.getString("firstname"))
                        .add("lastname", rs.getString("lastname"))
                        .add("accountstatus", rs.getString("accountstatus"))
                        .add("creditsleft",ccusage);

                    jsonArray.add(jsonObject);
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return jsonArray.build().toString();
    }
    
    public String Know_user(int profileid){
        String outcome = null;
        try{
            String query = "SELECT firstname,lastname,username,emailid,slab_name,credit_count,(SELECT count(*) from scanlist where userid = ?) as scancount,date_format(creationtime,'%D %M %Y') as joined from users us inner join subscription_slab ss on ss.slabid = us.slabid where userid = ?;";
            try(PreparedStatement pst = this.con.prepareStatement(query))
            {
                pst.setInt(1,profileid);
                pst.setInt(2,profileid);

                ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    outcome = "{\"firstname\":\""+rs.getString("firstname")+"\", \"lastname\":\""+rs.getString("lastname")+"\", \"username\":\""+rs.getString("username")+"\", \"emailid\":\""+rs.getString("emailid")+"\", \"packname\":\""+rs.getString("slab_name")+"\", \"creditlimit\":"+rs.getInt("credit_count")+", \"scancount\":"+rs.getInt("scancount")+", \"joined\":\""+rs.getString("joined")+"\"}";
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return outcome;
    }
    // End of the Function to Fetch User
    
    // Function to Load User Profile
    public String Read_profile(int profileid){
        String outcome = null;
        try{
            String query = "SELECT firstname,lastname,username,emailid,accountstatus from users where userid = ?;";
            try(PreparedStatement pst = this.con.prepareStatement(query))
            {
                pst.setInt(1,profileid);

                ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    outcome = "{\"firstname\":\""+rs.getString("firstname")+"\", \"lastname\":\""+rs.getString("lastname")+"\", \"username\":\""+rs.getString("username")+"\", \"emailid\":\""+rs.getString("emailid")+"\", \"accstatus\":\""+rs.getString("accountstatus")+"\"}";
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return outcome;
    }
    // End of the Function to Load User Profile
    
    // Function to Create User
    public int Create_user(){
        int outcome = -1;
        try{
            String query = "INSERT into users (firstname,lastname,username,emailid,encrypted_password,secret_key,creationtime) values (?,?,?,?,AES_ENCRYPT(?,?),?,NOW());";
            try(PreparedStatement pst = this.con.prepareStatement(query))
            {
                pst.setString(1, this.Firstname);
                pst.setString(2, this.Lastname);
                pst.setString(3, this.Username);
                pst.setString(4, this.Emailid);
                pst.setString(5, this.Password);
                pst.setString(6, this.Secretkey);
                pst.setString(7, this.Secretkey);

                int rowsAffected = pst.executeUpdate();

                if(rowsAffected > 0){
                    outcome = 1;
                }
                else{
                    outcome = -1;
                }
                return outcome;
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return outcome;
    }
    // End of the Function to Create User
    
    public int Get_slabid(int userid){
        int rs = 0;
        try
        {
            String check = "SELECT slabid from users where userid = ?;";
            try(PreparedStatement cpst = this.con.prepareStatement(check))
            {
                cpst.setInt(1, userid);

                ResultSet rset = cpst.executeQuery();
                if(rset.next())
                {
                    rs = rset.getInt("slabid");
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return rs;
    }
    
    // Update pack
    public int Update_slab(int userid,int slabid){
        int rs = 0;
        try{
            
            if(this.Get_slabid(userid) == slabid)
            {
                return 0;
            }
            
            String query = "UPDATE users set slabid = ? where userid = ?;";
            try(PreparedStatement pst = this.con.prepareStatement(query))
            {
                pst.setInt(1, slabid);
                pst.setInt(2, userid);

                pst.executeUpdate();

                if(slabid == this.Get_slabid(userid)){
                    rs = 1;
                }
                else{
                    rs = 0;
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return rs;
    }
    // End of the Function to update the pack
    
    public String Get_accountstatus(int userid){
        String accountstatus = "";
        try{
            String check = "SELECT accountstatus from users where userid = ?;";
            try(PreparedStatement cpst = this.con.prepareStatement(check))
            {
                cpst.setInt(1, userid);

                ResultSet rset = cpst.executeQuery();
                if(rset.next()){
                    accountstatus = rset.getString("accountstatus");
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return accountstatus;
    }
    
    // Update account status
    public int Update_accountstatus(int userid,String status){
        int rs = 0;
        try{
            
            if(status.equals(this.Get_accountstatus(userid))){
                return 0;
            }
            
            String query = "UPDATE users set accountstatus = ? where userid = ?;";
            try(PreparedStatement pst = this.con.prepareStatement(query))
            {
                pst.setString(1,status);
                pst.setInt(2, userid);

                pst.executeUpdate();

                if(status.equals(this.Get_accountstatus(userid))){
                    rs = 1;
                }
                else{
                    rs = 0;
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return rs;
    }
    // End of the function to update the account status
    
    public int Get_credit_count(){
        try{
            String reqstatus = "granted";
            
            String sql = "SELECT ("
                + " (SELECT COALESCE(credit_count+granted_credits, 0)"
                + "     FROM subscription_slab ss"
                + "     INNER JOIN users us ON ss.slabid = us.slabid"
                + "     WHERE us.userid = ?)"
                + " - (SELECT COALESCE(COUNT(*), 0)"
                + "     FROM scanlist sl"
                + "     WHERE sl.userid = ?"
                + "     AND DATE(sl.scantime) = CURDATE())"
                + " - (SELECT COALESCE(COUNT(*), 0)"
                + "     FROM rescanlist rsl"
                + "     INNER JOIN scanlist sl ON sl.scanid = rsl.scanid"
                + "     WHERE sl.userid = ?"
                + "     AND DATE(rsl.thetime) = CURDATE())"
                + " ) AS count;";

            try(PreparedStatement pst = this.con.prepareStatement(sql))
            {
                pst.setInt(1, this.userid);
                pst.setInt(2, this.userid);
                pst.setInt(3,this.userid);

                ResultSet rs = pst.executeQuery();

                if(rs.next())
                {
                    this.credits = rs.getInt("count");
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        
        return this.credits;
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