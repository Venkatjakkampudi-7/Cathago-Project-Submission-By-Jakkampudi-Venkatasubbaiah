package Document_Processor;

import ai.onnxruntime.OrtException;
import backend_processes.DBConnection;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main_Processor extends DBConnection {
    // Initalizing the requried variables
    private Connection con;
    private int userid;
    private int docid;
    private boolean aipower;
    private ArrayList<Integer> matchingindexes;
    private Map<Integer,Double> Combinationaloutcome;
    private LinkedHashMap<Integer,Double> Rankedlist;
    private Map<Integer,Double> Processedlist;
    private LinkedHashMap<Integer,Double> Finallist;
    private Similartiy_Processor spr;
    private Semantic_Processor sepr;
    private Outcome_Processor opr;
    
    public Main_Processor(int userid,int docid){
        this.con = this.getConnection();
        this.userid = userid;
        this.docid = docid;
        
        if(this.first_go()){
            return;
        }
        
        this.spr = new Similartiy_Processor(this.docid);
        this.opr = new Outcome_Processor(this.docid);
        
        // Function call to check for ai powered processing
        this.Aipower();
        
        this.matchingindexes = new ArrayList<>();                           // Initializing the array to store the matching indexes
        this.matchingindexes = spr.Fetch_Matching_Documents();              // Retriving the matching indexes
        
        this.Combinationaloutcome = new HashMap<>();
        this.Processedlist = new HashMap<>();
        
        // Processing for matches
        this.Combinational_Processing();
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
    
    // Function for generating combinational processing in the first go
    public void Combinational_Processing(){
        // Step-1 : Obtaining the scores from the cosine similarity for grading most relevent documents
        for (Integer docid : this.matchingindexes) 
        {
            this.Combinationaloutcome.put(docid, spr.Generate_Score(docid));
        }
        
        // Sorting the scores
        this.Rankdocs();
        
        // Saving the results into the database
        opr.Save_Results(1, this.Rankedlist);
        
        if(this.aipower)
        {
            // Step-3: Processing for the semantic analysis for getting the cosine similarity scores after sorting
            for(Map.Entry<Integer, Double> entry : this.Rankedlist.entrySet()){
                this.sepr = new Semantic_Processor(this.docid,entry.getKey());

                try 
                {
                    // Step-4: Obtaining results
                    this.Processedlist.put(entry.getKey(), sepr.Compute_Semantic_Score());
                } catch (OrtException ex) {
                    Logger.getLogger(Main_Processor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            // Sorting based on results
            this.Sortfinallist();

            // Saving the results into the database
            opr.Save_Results(2, this.Finallist);
        }
    }
    // End of the Function for first combination
    
    public void Rankdocs(){
        // Convert HashMap into a List
        List<Map.Entry<Integer, Double>> list = new ArrayList<>(this.Combinationaloutcome.entrySet());

        // Sorting in descending order
        list.sort(Map.Entry.<Integer, Double>comparingByValue().reversed());

        // Maintaining the order in a LinkedHashMap
        this.Rankedlist = new LinkedHashMap<>();
        for (Map.Entry<Integer, Double> entry : list) {
            this.Rankedlist.put(entry.getKey(), entry.getValue());
        }
    }
    
    public void Sortfinallist(){
        // Convert HashMap into a List
        List<Map.Entry<Integer, Double>> list = new ArrayList<>(this.Processedlist.entrySet());

        // Sorting in descending order
        list.sort(Map.Entry.<Integer, Double>comparingByValue().reversed());

        // Maintaining the order in a LinkedHashMap
        this.Finallist = new LinkedHashMap<>();
        for (Map.Entry<Integer, Double> entry : list) {
            this.Finallist.put(entry.getKey(), entry.getValue());
        }
    }
    
    private void Aipower(){
        try{
            String sql = "SELECT ai_assisted from users us inner join subscription_slab ss on ss.slabid = us.slabid where us.userid = ?;";
            try(PreparedStatement pst = this.con.prepareStatement(sql))
            {
                pst.setInt(1, this.userid);

                ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    if(rs.getString("ai_assisted").equals("yes")){
                        this.aipower = true;
                    }
                    else{
                        this.aipower = false;
                    }
                }
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
                if(this.spr != null){
                    this.spr.Close_connection();
                }
                
                if(this.opr != null){
                    this.opr.Close_connection();
                }
                
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