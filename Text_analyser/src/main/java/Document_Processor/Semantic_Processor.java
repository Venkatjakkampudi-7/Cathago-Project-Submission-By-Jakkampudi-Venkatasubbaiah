package Document_Processor;

import ai.onnxruntime.OrtException;
import backend_processes.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Semantic_Processor extends DBConnection {
    private Connection con;
    private int doc1;
    private int doc2;
    private Onxmodel onml;
    
    public Semantic_Processor(int docid1,int docid2){
        this.con = this.getConnection();
        this.doc1 = docid1;
        this.doc2 = docid2;
    }
    
    public String Get_doc_data(int docid){
        String docdata = "";
        try{
            String query = "SELECT keyword from doc_data where docid = ?;";
            try(PreparedStatement pst = this.con.prepareStatement(query))
            {
                pst.setInt(1, docid);

                ResultSet rs = pst.executeQuery();
                while(rs.next()){
                    docdata += rs.getString("keyword");
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return docdata;
    }
    
    public Double Compute_Semantic_Score() throws OrtException {
        Double score = 0.0;
        String doc1data = this.Get_doc_data(this.doc1);
        String doc2data = this.Get_doc_data(this.doc2);
        
        String modelPath = "D:\\Cathago Test\\NLP Model\\bert.onnx"; // Correct Windows path
        onml = new Onxmodel(modelPath);
        
        score = onml.compareDocuments(doc1data, doc2data);
        
        return score;
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