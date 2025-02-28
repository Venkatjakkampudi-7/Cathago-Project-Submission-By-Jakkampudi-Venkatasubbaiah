package Document_Processor;

import backend_processes.DBConnection;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// This is responsible for document preprocessing
public class Pre_Processor extends DBConnection {
    private Connection con;
    private int docid;
    private static String DOC_DIRECTORY = "D:/Cathago Test/App-data/";
    private String docpath;
    private List<String> Docdata;
    private Map<String, Integer> Docblueprint;
    
    public Pre_Processor(int docid) throws IOException{
        this.docid = docid;
        this.con = this.getConnection();
        
        this.Docblueprint = new HashMap<>();
        this.Docdata = new ArrayList<>();
        
        try{
            String query = "SELECT docpath from scanlist where scanid = ?;";
            try(PreparedStatement pst = this.con.prepareStatement(query))
            {
                pst.setInt(1,this.docid);

                ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    this.docpath = this.DOC_DIRECTORY;
                    this.docpath += rs.getString("docpath");
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        
        int Docflag = this.Check_Docs();
        
        // Checking weather the file is saved or not
        if(Docflag > 0)
        {
            return;                 // Case when the file is already saved
        }
        else if(Docflag == 0){
            this.Load_File();               // Retriving the document into string
            this.Initiate_Tokenisation();   // Tokenising the document line by line and word by word
            this.Save_Processed_File();     // Saving the processed file into the database for further processing
        }
    }
    
    public void Load_File() throws IOException{
        this.Docdata = Files.readAllLines(Paths.get(this.docpath));
    }
    
    public void Initiate_Tokenisation(){
        for(String piece : this.Docdata)
        {
            piece = piece.toLowerCase();                        // Step-1: Converting the data into lowercase
            piece = piece.replaceAll("[^a-zA-Z0-9 ]", "");      // Step-2: Removing special characters

            String[] chunks = piece.split("\\s+");              // Step-3: Partitioning by considering the space as delimiter 

            for (String token : chunks) {
                if (!token.isEmpty()) {
                    this.Docblueprint.put(token, this.Docblueprint.getOrDefault(token, 0) + 1);
                }
            }
        }
    }
    
    public int Check_Docs(){
        int rs = 0;
        try{
            // Testing weather the file is saved or not
            String check = "SELECT COUNT(*) AS COUNT from doc_data where docid = ?;";
            try(PreparedStatement ptest = this.con.prepareStatement(check))
            {
                ptest.setInt(1, this.docid);

                ResultSet rtest = ptest.executeQuery();
                if(rtest.next()){
                    if(rtest.getInt("COUNT") > 0){
                        rs = rtest.getInt("COUNT");
                    }
                }
                // End of testing
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return rs;
    }
    
    public void Save_Processed_File()
    {
        try
        {
            if(this.Check_Docs() > 0){
                return;
            }
            
            // Process for saving the file
            String query = "INSERT IGNORE INTO doc_data (docid,keyword,frequency) values (?,?,?);";
            try(PreparedStatement pst = this.con.prepareStatement(query))
            {
                for (Map.Entry<String, Integer> entry : this.Docblueprint.entrySet()) 
                {
                    pst.setInt(1, this.docid);      
                    pst.setString(2, entry.getKey()); 
                    pst.setInt(3, entry.getValue());  
                    pst.addBatch(); 
                }

                pst.executeBatch();
                // End of saving the file
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
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