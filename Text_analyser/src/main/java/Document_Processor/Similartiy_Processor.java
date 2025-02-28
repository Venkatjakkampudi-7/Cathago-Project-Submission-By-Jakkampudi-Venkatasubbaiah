package Document_Processor;

import backend_processes.DBConnection;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// This class finds the similarity between the files
public class Similartiy_Processor extends DBConnection{
    // Initalizing the requried variables
    private Connection con;
    private int docid;
    
    public Similartiy_Processor(int docid){
        this.con = this.getConnection();
        this.docid = docid;
    }
    
    public ArrayList<Integer> Fetch_Matching_Documents(){
        ArrayList<Integer> matchingindexes = new ArrayList<>();
        try{
            String query = "SELECT dd.docid,"
                    + " Round((SUM(dd.frequency * da.frequency)"
                    + " / ((SQRT(SUM(dd.frequency * dd.frequency)))"
                    + " * (SQRT(SUM(da.frequency * da.frequency))))"
                    + "),2) as score"
                    + " FROM doc_data dd"
                    + " INNER JOIN doc_data da ON dd.keyword = da.keyword"
                    + " WHERE da.docid = ?"
                    + " AND dd.docid != ?"
                    + " GROUP BY dd.docid"
                    + " ORDER BY score DESC"
                    + " LIMIT 100;";
            try(PreparedStatement pst = this.con.prepareStatement(query))
            {
                pst.setInt(1,this.docid);
                pst.setInt(2, this.docid);

                ResultSet rs = pst.executeQuery();
                while(rs.next()){
                    matchingindexes.add(rs.getInt("docid"));
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return matchingindexes;
    }
    
    public Double Existing_Score(int doc2){
        Double score = 0.0;
        try
        {
            String Scorestmt = "SELECT score from similarity_scores where scanid = ? and vsscanid = ?;";
            
            try(PreparedStatement pst = this.con.prepareStatement(Scorestmt))
            {
                pst.setInt(1, this.docid);
                pst.setInt(2, doc2);

                ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    score = rs.getDouble("score");
                }
                else{
                    score = -1.0;
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        
        return score;
    }
    
    // Process Document : Compares the document to the rest of the documents and returns the result
    public Double Generate_Score(int doc2)
    {
        Double score = 0.0;
        try
        {   
            String Scorestmt = "WITH Combination AS ("
                    + " SELECT"
                    + "     COALESCE(dd1.keyword, dd2.keyword) AS keyword,"
                    + "     COALESCE(dd1.frequency, 0) AS mag1,"
                    + "     COALESCE(dd2.frequency, 0) AS mag2"
                    + " FROM doc_data dd1"
                    + " LEFT JOIN doc_data dd2 ON dd1.keyword = dd2.keyword AND dd2.docid = ?"
                    + " WHERE dd1.docid = ?"
                    + " UNION"
                    + " SELECT"
                    + "     COALESCE(dd1.keyword, dd2.keyword) AS keyword,"
                    + "     COALESCE(dd1.frequency, 0) AS mag1,"
                    + "     COALESCE(dd2.frequency, 0) AS mag2"
                    + " FROM doc_data dd2"
                    + " LEFT JOIN doc_data dd1 ON dd1.keyword = dd2.keyword AND dd1.docid = ?"
                    + " WHERE dd2.docid = ?"
                    + " )"
                    + " SELECT "
                    + "     ROUND(SUM(mag1 * mag2) / (SQRT(SUM(mag1 * mag1)) * SQRT(SUM(mag2 * mag2))),2) AS score"
                    + " FROM Combination";
            
            try(PreparedStatement pst = this.con.prepareStatement(Scorestmt))
            {
                pst.setInt(1, this.docid);
                pst.setInt(2, doc2);
                pst.setInt(3, doc2);
                pst.setInt(4, this.docid);

                ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    score = rs.getDouble("score");
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        
        return score;
    }
    // End of the Function for Processing the document
    
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