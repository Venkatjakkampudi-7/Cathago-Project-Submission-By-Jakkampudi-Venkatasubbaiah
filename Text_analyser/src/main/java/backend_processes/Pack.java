package backend_processes;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Pack extends DBConnection {
    private Connection con;
    
    public Pack(){
        con = this.getConnection();
    }
    
    public String Read_packs(){
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        
        try{
            int usercount = 0;
            
            // Reading the pending requests
            String sql = "SELECT slabid,slab_name,credit_count,ai_assisted,slab_status from subscription_slab;";
            try(PreparedStatement pst = this.con.prepareStatement(sql))
            {
                ResultSet rs = pst.executeQuery();
                while(rs.next())
                {
                    String privillage = "user";
                    // Reading the usage
                    String currentusage = "SELECT count(*) as count from users where slabid = ? and privillage = ?;";
                    try(PreparedStatement usage = this.con.prepareStatement(currentusage))
                    {
                        usage.setInt(1,rs.getInt("slabid"));
                        usage.setString(2, privillage);

                        ResultSet usres = usage.executeQuery();
                        if(usres.next()){
                            usercount = usres.getInt("count");
                        }

                        // Building the json
                        JsonObjectBuilder jsonObject = Json.createObjectBuilder()
                            .add("packid", rs.getInt("slabid"))
                            .add("slabname", rs.getString("slab_name"))  
                            .add("creditcount", rs.getString("credit_count"))
                            .add("ai_assisted", rs.getString("ai_assisted"))
                            .add("slabstatus",rs.getString("slab_status"))
                            .add("uscount",usercount);

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
    
    public String Fetch_packs(String skey){
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        
        try{
            int usercount = 0;
            
            // Reading the pending requests
            String sql = "SELECT slabid,slab_name,credit_count,slab_status from subscription_slab where slab_name Like '"+skey+"%';";
            try(PreparedStatement pst = this.con.prepareStatement(sql))
            {
            
                ResultSet rs = pst.executeQuery();
                while(rs.next())
                {
                    String privillage = "user";
                    // Reading the usage
                    String currentusage = "SELECT count(*) as count from users where slabid = ? and privillage = ?;";
                    try(PreparedStatement usage = this.con.prepareStatement(currentusage))
                    {
                        usage.setInt(1,rs.getInt("slabid"));
                        usage.setString(2, privillage);

                        ResultSet usres = usage.executeQuery();
                        if(usres.next()){
                            usercount = usres.getInt("count");
                        }

                        // Building the json
                        JsonObjectBuilder jsonObject = Json.createObjectBuilder()
                            .add("packid", rs.getInt("slabid"))
                            .add("slabname", rs.getString("slab_name"))  // Fixed column name
                            .add("creditcount", rs.getString("credit_count"))    // Fixed column name
                            .add("slabstatus",rs.getString("slab_status"))
                            .add("uscount",usercount);

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
    
    public int Create_pack(String packname,int creditlimit){
        int rs = 0;
        try{
            
            String sql = "INSERT into subscription_slab (slab_name,credit_count) values (?,?);";
            try(PreparedStatement pst = this.con.prepareStatement(sql))
            {
                pst.setString(1,packname);
                pst.setInt(2,creditlimit);

                int affectedrows = pst.executeUpdate();
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
    
    public int Update_packs(int packid,int creditlimit,String aipower,String status){
        int rs = 0;
        try{
            String sql = "UPDATE subscription_slab set credit_count = ?, ai_assisted = ? , slab_status = ? where slabid = ?;";
            try(PreparedStatement pst = this.con.prepareStatement(sql))
            {
                pst.setInt(1,creditlimit);
                pst.setString(2,aipower);
                pst.setString(3,status);
                pst.setInt(4,packid);

                int affectedrows = pst.executeUpdate();
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