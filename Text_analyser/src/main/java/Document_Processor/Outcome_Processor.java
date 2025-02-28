package Document_Processor;

import backend_processes.DBConnection;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import java.math.BigDecimal;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Outcome_Processor extends DBConnection {
    private Connection con;
    private int docid;
    private List<Integer> matchingindexes;
    private Map<Integer,Double> combinationaloutcome;
    
    public Outcome_Processor(int docid){
        this.con = this.getConnection();
        this.docid = docid;
    }
    
    public List<Integer> Previous_Matches(){
        return this.matchingindexes;
    }
    
    public boolean first_go(){
        boolean first = true;
        try{
            String query = "SELECT COALESCE(count(distinct docid),0) as count from doc_data;";
            try(PreparedStatement pst = this.con.prepareStatement(query))
            {
                ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    if(rs.getInt("count") > 0){
                        first = false;
                    }
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return first;
    }
    
    public void Save_Results(int Type, HashMap<Integer,Double> outcomes){
        try{
            
            int i=0;
            
            // Process for saving the file
            String query = "";
            if(Type == 1){
                query = "INSERT IGNORE INTO similarity_scores (scanid,vsscanid,score,test_date) values (?,?,?,NOW());";
            }
            else if(Type == 2){
                query = "INSERT IGNORE INTO semantic_scores (scanid,vsscanid,score,test_date) values (?,?,?,NOW());";
            }
            try(PreparedStatement pst = this.con.prepareStatement(query))
            {
                for (Map.Entry<Integer, Double> entry : outcomes.entrySet()) 
                {
                    if(i < 20){
                        pst.setInt(1, this.docid);      
                        pst.setInt(2, entry.getKey()); 
                        pst.setDouble(3, entry.getValue());  
                        pst.addBatch(); 
                        i++;
                    }
                }

                pst.executeBatch();
                // End of saving the file
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    public String Get_Doc_Scores(){
        if(first_go()){
            return "{\"Status\":\"No docs to compare !\"}";
        }
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        
        try{
            String sql = "SELECT DISTINCT"
                        + " REPLACE((SELECT docpath FROM scanlist WHERE scanid = ss.vsscanid), 'Documents/', '') AS filename,"
                        + " (SELECT docpath FROM scanlist WHERE scanid = ss.vsscanid) AS docpathone, "
                        + " (SELECT docpath FROM scanlist WHERE scanid = ss.scanid) AS docpathtwo, "
                        + " ss.vsscanid, "
                        + " ROUND(COALESCE(se.score * ss.score, 0), 4) AS fscore, "
                        + " ROUND(COALESCE(ss.score, 0), 4) AS similarity_score, "
                        + " ROUND(COALESCE(se.score, 0), 4) AS semantic_score "
                        + " FROM "
                        + " similarity_scores ss "
                        + " LEFT JOIN "
                        + " semantic_scores se ON se.scanid = ss.scanid AND ss.vsscanid = se.vsscanid "
                        + " WHERE "
                        + " ss.scanid = ? "
                        + " AND ss.score >= 0.5 "
                        + " AND (SELECT docpath FROM scanlist WHERE scanid = ss.vsscanid) != (SELECT docpath FROM scanlist WHERE scanid = ss.scanid) "
                        + " ORDER BY "
                        + " similarity_score DESC "
                        + " LIMIT 20;";
            
            try(PreparedStatement pst = this.con.prepareStatement(sql))
            {
                pst.setInt(1,this.docid);

                ResultSet rs = pst.executeQuery();
                while(rs.next()){
                    
                    // Building the json
                    JsonObjectBuilder jsonObject = Json.createObjectBuilder()
                        .add("filename", rs.getString("filename"))
                        .add("scanid", this.docid)
                        .add("vsscanid", rs.getInt("vsscanid"))
                        .add("similarity_score", rs.getDouble("similarity_score"))  
                        .add("semantic_score", rs.getDouble("semantic_score"))    
                        .add("fscore",rs.getDouble("fscore"));

                    jsonArray.add(jsonObject);
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        
        return jsonArray.build().toString();
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