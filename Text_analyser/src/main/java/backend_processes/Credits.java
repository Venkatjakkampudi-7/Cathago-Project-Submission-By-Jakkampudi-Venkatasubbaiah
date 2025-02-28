package backend_processes;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Credits extends DBConnection {
    private int userid;
    private int credits;
    private int requestid;
    private Connection con;
    
    public Credits(){
        con = this.getConnection();
    }
    
    public Credits(int userid){
        this.userid = userid;
        con = this.getConnection();
    }
    
    // Admin Actions
    
    // Read credit history : Reads the previous credit requests for an user to display for a specific user
    public String Read_credit_history(){
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        try{
            String reqstatus = "requested";
            
            // Reading the pending requests
            String sql = "SELECT date_format(request_time,'%D %M %Y') as thedate, "
                    + "date_format(request_time,'%r') as thetime, credits_granted, "
                    + "status from credit_requests cr "
                    + "inner join users us on us.userid = cr.userid "
                    + "where us.userid = ? and status != ?;";
            try(PreparedStatement pst = this.con.prepareStatement(sql))
            {
                pst.setInt(1,this.userid);
                pst.setString(2,reqstatus);

                ResultSet rs = pst.executeQuery();
                while(rs.next()){

                    // Building the json
                    JsonObjectBuilder jsonObject = Json.createObjectBuilder()
                        .add("date", rs.getString("thedate"))
                        .add("time", rs.getString("thetime"))  // Fixed column name
                        .add("credits", rs.getInt("credits_granted"))    // Fixed column name
                        .add("status",rs.getString("status"));

                    jsonArray.add(jsonObject);
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        
        return jsonArray.build().toString();
    }
    // End of the function for reading credit history
    
    // Read credit requests : Reads Credit request to display on admin panel
    public String Read_credit_requests(){
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        
        try{
            String reqstatus = "requested";
            int ccusage = 0;
            
            // Reading the pending requests
            String sql = "SELECT requestid,us.userid,firstname,lastname from credit_requests cr inner join users us on us.userid = cr.userid where status = ?;";
            try(PreparedStatement pst = this.con.prepareStatement(sql))
            {
                pst.setString(1,reqstatus);

                ResultSet rs = pst.executeQuery();
                while(rs.next())
                {
                    // Reading the usage
                    String currentusage = "SELECT count(*) as count from scanlist where userid = ?;";
                    try(PreparedStatement usage = this.con.prepareStatement(currentusage))
                    {
                        usage.setInt(1,rs.getInt("userid"));

                        ResultSet usres = usage.executeQuery();
                        if(usres.next()){
                            ccusage = usres.getInt("count");
                        }

                        // Building the json
                        JsonObjectBuilder jsonObject = Json.createObjectBuilder()
                            .add("requestid", rs.getInt("requestid"))
                            .add("userid", rs.getInt("userid"))
                            .add("firstname", rs.getString("firstname")) 
                            .add("lastname", rs.getString("lastname"))  
                            .add("usage",ccusage);

                        jsonArray.add(jsonObject);
                    }
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        
        return jsonArray.build().toString();
    }
    // End of the function for reading the credit requests
    
    // Fetch credit requests : Reads Credit request on search to display on admin panel
    public String Fetch_credit_requests(String key){
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        
        try{
            String reqstatus = "requested";
            int ccusage = 0;
            
            // Reading the pending requests
            String sql = "SELECT requestid,us.userid,firstname,lastname from credit_requests cr inner join users us on us.userid = cr.userid where (status = ?) and (firstname like \""+key+"%\" or lastname like \""+key+"%\");";
            try(PreparedStatement pst = this.con.prepareStatement(sql))
            {
                pst.setString(1,reqstatus);

                ResultSet rs = pst.executeQuery();
                while(rs.next())
                {
                    // Reading the usage
                    String currentusage = "SELECT count(*) as count from scanlist where userid = ?;";
                    PreparedStatement usage = this.con.prepareStatement(currentusage);
                    usage.setInt(1,rs.getInt("userid"));

                    ResultSet usres = usage.executeQuery();
                    if(usres.next()){
                        ccusage = usres.getInt("count");
                    }

                    // Building the json
                    JsonObjectBuilder jsonObject = Json.createObjectBuilder()
                        .add("requestid", rs.getInt("requestid"))
                        .add("userid", rs.getInt("userid"))
                        .add("firstname", rs.getString("firstname"))  
                        .add("lastname", rs.getString("lastname"))   
                        .add("usage",ccusage);

                    jsonArray.add(jsonObject);
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return jsonArray.build().toString();
    }
    // End of function for fetching the credit requests
    
    // Process credit request : Updates the request depends upon the user status
    public int Process_credit_request(String reqstatus,int creditcount,int requestid){
        int rs = 0;
        try{
            this.requestid = requestid;
            if(this.Get_credit_request_status()){
                String sql = "UPDATE credit_requests set status = ?, credits_granted = ? where requestid = ?;";
                try(PreparedStatement pst = this.con.prepareStatement(sql))
                {
                    pst.setString(1,reqstatus);
                    pst.setInt(2,creditcount);
                    pst.setInt(3,requestid);

                    int affectedrows = pst.executeUpdate();
                    if(affectedrows > 0){
                        rs = 1;
                        System.out.println("Credit request updated !");
                    }
                }
                
                if(reqstatus.equals("granted"))
                {
                
                    String granted_credits = "UPDATE users set granted_credits = granted_credits+? where userid = ?;";
                    try(PreparedStatement gst = this.con.prepareStatement(granted_credits))
                    {
                        gst.setInt(1, creditcount);
                        gst.setInt(2, userid);

                        int affecteds = gst.executeUpdate();
                        if(affecteds > 0){
                            rs = 1;
                            System.out.println("Additional credits granted !");
                        }
                    }
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return rs;
    }
    // End of the function for processing the credit requests
    
    // Add credits
    public int Add_credits(int userid,int creditcount){
        int rs = 0;
        try{
            String status = "granted";
            
            String query = "INSERT into credit_requests (userid,request_time,status,credits_granted) values (?,NOW(),?,?);";
            try(PreparedStatement pst = this.con.prepareStatement(query))
            {
                pst.setInt(1,userid);
                pst.setString(2,status);
                pst.setInt(3,creditcount);

                int affectedrows = pst.executeUpdate();
                if(affectedrows > 0){
                    rs = 1;
                }
            }
            
            String granted_credits = "UPDATE users set granted_credits = granted_credits+? where userid = ?;";
            try(PreparedStatement gst = this.con.prepareStatement(granted_credits))
            {
                gst.setInt(1, creditcount);
                gst.setInt(2, userid);
                
                int affectedrows = gst.executeUpdate();
                if(affectedrows > 0){
                    rs = 1;
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return rs;
    }
    // End of the function for adding credits
    
    // End of Admin actions
    
    // Get credit status : returns weather admin has taken action for the credits
    public boolean Get_credit_request_status(){
        boolean status = false;
        try{
            String sts = "requested";
            String query = "SELECT status from credit_requests where requestid = ?;";
            try(PreparedStatement pst = this.con.prepareStatement(query))
            {
                pst.setInt(1, this.requestid);

                ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    if(rs.getString("status").equals(sts)){
                        status = true;
                    }
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return status;
    }
    // End of function for attaining the credit request status
    
    // Profile and credit request actions
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
    
    // Request credits : requests the admin for credits
    public int Request_credits(){
        int reqstatus = 0;
        try
        {
            // Testing before making the credit request
            String teststatus = "requested";
            String testquery = "SELECT count(*) as count from credit_requests where userid = ? and status = ?;";
            try(PreparedStatement testpst = this.con.prepareStatement(testquery))
            {
                testpst.setInt(1,this.userid);
                testpst.setString(2,teststatus);

                ResultSet testres = testpst.executeQuery();
                if(testres.next()){
                    if(testres.getInt("count") > 0){
                        return 0;
                    }
                }
            }
            // End of the test section
            
            // Requesting credits
            String creditrequest = "INSERT INTO credit_requests (userid,request_time) values (?,NOW());";
            try(PreparedStatement pst = this.con.prepareStatement(creditrequest))
            {
                pst.setInt(1, this.userid);

                int affectedrows = pst.executeUpdate();
                if(affectedrows > 0){
                    reqstatus = 1;
                }
                else{
                    reqstatus = -1;
                }
            }
            // End of requesting the credits
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        
        return reqstatus;
    }
    // End of Profile and credit request actions
    
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