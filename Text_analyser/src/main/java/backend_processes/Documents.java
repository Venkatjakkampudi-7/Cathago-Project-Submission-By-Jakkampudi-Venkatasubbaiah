package backend_processes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import jakarta.servlet.http.Part;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import java.nio.file.Path;
import java.nio.file.Paths;
import jakarta.json.JsonObjectBuilder;
import java.sql.Statement;

public class Documents extends DBConnection
{
    private Connection con;
    private int userid;
    private static String UPLOAD_DIRECTORY = "D:/Cathago Test/App-data/Documents/";
    private static String RETRIVAL_DIRECTORY = "D:/Cathago Test/App-data/";
    private int docid;
    private int credits;
    
    private Credits cre;
    
    public Documents(){
        con = this.getConnection();
    }
    
    public Documents(int userid)
    {
        con = this.getConnection();
        this.userid = userid;
        
        if(this.Get_credit_count() == 0){
            return;
        }
    }
    
    public Documents(int userid,int scanid)
    {
        con = this.getConnection();
        this.userid = userid;
        this.docid = scanid;
        
        if(this.Get_credit_count() == 0){
            return;
        }
    }
    
    // Create a rescan : Adds a rescan and deducts credits from the user
    public int Re_scan(){
        int key = 0;
        try{
            String query = "INSERT into rescanlist (scanid,thetime) values (?,NOW());";
            try(PreparedStatement pst = this.con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
            {
                pst.setInt(1,this.docid);

                int Affectedrows = pst.executeUpdate();

                if(Affectedrows > 0)
                {
                    key = this.docid;
                    this.Check_for_additional_charge();
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return key;
    }
    // End of rescan
    
    // Create Document : Moves the uploaded document and stores the path into the database.
    private void Create_document(String destination){
        try{
            String query = "INSERT into scanlist (userid,docpath,scantime) values (?,?,NOW());";
            try(PreparedStatement pst = this.con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
            {
                pst.setInt(1,this.userid);
                pst.setString(2,destination);

                int Affectedrows = pst.executeUpdate();

                if(Affectedrows > 0)
                {
                    // Get the generated scanid (primary key)
                    ResultSet generatedKeys = pst.getGeneratedKeys();
                    if (generatedKeys.next()) 
                    {
                        this.docid = generatedKeys.getInt(1);  // Retrieve the auto-incremented scanid
                        this.Check_for_additional_charge();
                    }
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    // End of the Function to creat the document
    
    private void Check_for_additional_charge(){
        try{
            String check = "SELECT"
                + " (SELECT COALESCE(COUNT(*), 0)"
                + "     FROM scanlist sl"
                + "     WHERE sl.userid = ?"
                + "     AND DATE(sl.scantime) = CURDATE() )"
                + " + SELECT COALESCE(COUNT(*), 0)"
                + "     FROM rescanlist rsl"
                + "     INNER JOIN scanlist sl ON sl.scanid = rsl.scanid"
                + "     WHERE sl.userid = ?"
                + "     AND DATE(rsl.thetime) = CURDATE()"
                + " ) AS todaycount,"
                + " (SELECT credit_count"
                + "     FROM users us"
                + "     INNER JOIN subscription_slab ss ON ss.slabid = us.slabid"
                + "     WHERE us.userid = ?) AS ccount;";
            
            try(PreparedStatement checkstmt = this.con.prepareStatement(check))
            {
                checkstmt.setInt(1, this.userid);
                checkstmt.setInt(2, this.userid);
                checkstmt.setInt(3, this.userid);

                ResultSet cset = checkstmt.executeQuery();
                if(cset.next()){
                    if(cset.getInt("todaycount") > cset.getInt("ccount")){
                        // This means all the free credits for today are exhusted
                        int creditsleft = this.cre.Get_credit_count();
                        
                        if(creditsleft > 0)
                        {
                            String query = "UPDATE users set granted_credits = granted_credits-1 where userid = ?;";
                            try(PreparedStatement pst = this.con.prepareStatement(query))
                            {
                                pst.setInt(1, this.userid);

                                pst.executeUpdate();
                            }
                        }
                    }
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    // Move Document : Moves the file and returns the file path.
    public int Move_document(Part filePart, String fileName) throws IOException {
        
        // Ensure UPLOAD_DIRECTORY is properly defined
        final String UPLOAD_DIRECTORY = "D:\\Cathago Test\\App-data\\Documents";
        File uploadDir = new File(UPLOAD_DIRECTORY);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Ensure filePart is not null
        if (filePart == null) {
            System.out.println("File part is null. No file received.");
            return -1;
        }

        // Create the file instance
        File file = new File(uploadDir, fileName);

        // Copy the uploaded file contents to the destination
        try (InputStream fileContent = filePart.getInputStream()) 
        {
            Files.copy(fileContent, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File successfully moved to: " + file.getAbsolutePath());
        } 
        catch (IOException e) 
        {
            throw new IOException("Error while saving file: " + e.getMessage(), e);
        }

        // Store relative path in DB
        String finalpath = "Documents/" + fileName;
        
        this.Create_document(finalpath);                // Creating the record for storing the file info into the database
        
        return this.docid;                              // Returning the document id generated after saving the document
    }
    // End of the Function for Moving the document
    
    public String Read_previous_Scans(){
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        try{
            String query = "SELECT scanid,REPLACE(docpath, 'Documents/', '') AS filename,docpath,date_format(scantime,\"%d-%m-%Y\") as thedate,date_format(scantime,\"%r\") as thetime from scanlist where userid = ? order by scantime desc;";
            try(PreparedStatement pst = this.con.prepareStatement(query))
            {
                pst.setInt(1,this.userid);

                ResultSet rs = pst.executeQuery();
                int count = 1;
                while (rs.next()) {
                    JsonObjectBuilder jsonObject = Json.createObjectBuilder()
                        .add("sno", count)
                        .add("sid", rs.getInt("scanid"))
                        .add("filename", rs.getString("filename"))
                        .add("date", rs.getString("thedate"))
                        .add("time", rs.getString("thetime"));

                    jsonArray.add(jsonObject);
                    count++;
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        
        return jsonArray.build().toString();
    }
    
    // Function for writing the scan history
    public String Read_Scans_History(){
        StringBuilder outcome = new StringBuilder();
        try{
            String query = "SELECT scanid,REPLACE(docpath, 'Documents/', '') AS filename,docpath,date_format(scantime,\"%d-%m-%Y\") as thedate,date_format(scantime,\"%r\") as thetime from scanlist where userid = ? order by scantime desc;";
            try(PreparedStatement pst = this.con.prepareStatement(query))
            {
                pst.setInt(1,this.userid);

                ResultSet rs = pst.executeQuery();
                int count = 0;
                
                // Header Section
                String header = String.format(
                    "                                          Scan History                                          %n"+
                    "+----------------------------------------------------------------------------------------------+%n"+
                    "| %-5s | %-50s | %-12s | %-16s |%n" +
                    "|----------------------------------------------------------------------------------------------|%n",
                    "No.", "Filename", "Date", "Time");

                outcome.append(header);
                while (rs.next()) {
                    count++;
                    String filename = rs.getString("filename");
                    String thedate = rs.getString("thedate");
                    String thetime = rs.getString("thetime");

                    String row = String.format(
                            "| %-5d | %-50s | %-12s | %-16s |%n",
                            count, filename, thedate, thetime
                    );
                    outcome.append(row);
                }

                // Footer Section
                if (count > 0) {
                    outcome.append("+----------------------------------------------------------------------------------------------+");
                    outcome.append(String.format("%nTotal Scans: %d", count));
                } else {
                    outcome.append(String.format("No Scans found.%n"));
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return outcome.toString();
    }
    // End of the function for writing the scan history
    
    // Function to show all the scans
    public String Show_all_scans(){
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        try{
            String query = "SELECT scanid,REPLACE(docpath, 'Documents/', '') AS filename,docpath,date_format(scantime,\"%d-%m-%Y\") as thedate,date_format(scantime,\"%r\") as thetime from scanlist order by scantime desc;";
            try(PreparedStatement pst = this.con.prepareStatement(query))
            {
                ResultSet rs = pst.executeQuery();
                int count = 1;
                while (rs.next()) {
                    JsonObjectBuilder jsonObject = Json.createObjectBuilder()
                        .add("sno", count)
                        .add("sid", rs.getInt("scanid"))
                        .add("filename", rs.getString("filename"))
                        .add("date", rs.getString("thedate"))
                        .add("time", rs.getString("thetime"));

                    jsonArray.add(jsonObject);
                    count++;
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        
        return jsonArray.build().toString();
    }
    // End of the Function to show all the scans
    
    // Function to show top searched topics
    public String Get_most_searched(){
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        try{
            String sql = "SELECT " +
                    "    dd1.keyword, " +
                    "    COALESCE(SUM(dd1.frequency), 0) AS frequency " +
                    "FROM " +
                    "    doc_data dd1 " +
                    "INNER JOIN " +
                    "    doc_data dd2 ON dd1.keyword = dd2.keyword " +
                    "INNER JOIN " +
                    "    ( " +
                    "        SELECT scanid, vsscanid, score " +
                    "        FROM similarity_scores " +
                    "        WHERE score >= 0.5 " +
                    "        ORDER BY score DESC " +
                    "        LIMIT 1000 " +
                    "    ) AS ss ON dd1.docid = ss.scanid AND dd2.docid = ss.vsscanid " +
                    "LEFT JOIN " +
                    "    stopwords sw ON dd1.keyword = sw.stopword " +
                    "WHERE " +
                    "    sw.stopword IS NULL " +
                    "GROUP BY " +
                    "    dd1.keyword " +
                    "ORDER BY " +
                    "    frequency DESC LIMIT 10;";
            
            try(PreparedStatement pst = this.con.prepareStatement(sql))
            {
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    JsonObjectBuilder jsonObject = Json.createObjectBuilder()
                        .add("keyword", rs.getString("keyword"))
                        .add("count", rs.getInt("frequency"));

                    jsonArray.add(jsonObject);
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        
        return jsonArray.build().toString();
    }
    // End of function to show top searched topics
    
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
    
    public String Get_Comparisions(int scanid,int vsscanid) throws IOException {
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();

        try{
            String sql = "SELECT (SELECT docpath from scanlist where scanid = ?) as doc1,(SELECT docpath from scanlist where scanid = ?) as doc2;";
            try(PreparedStatement pst = this.con.prepareStatement(sql)){
                pst.setInt(1, scanid);
                pst.setInt(2, vsscanid);

                ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    JsonObjectBuilder jsonObject = Json.createObjectBuilder()
                        .add("actualdoc", Get_Documentdata(rs.getString("doc1")))
                        .add("vsdocs", Get_Documentdata(rs.getString("doc2")));

                    jsonArray.add(jsonObject);
                }
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }

        return jsonArray.build().toString();
    }

    public String Get_Documentdata(String thepath) throws IOException {
        System.out.println("Get_Documentdata called with path: " + thepath); // Log the input path
    
        StringBuilder content = new StringBuilder();
        Path uploadDir = Paths.get(this.RETRIVAL_DIRECTORY);
        Path filePath = uploadDir.resolve(thepath).normalize();
    
        System.out.println("Resolved file path: " + filePath.toString()); // Log the resolved path
    
        if (!Files.exists(filePath)) {
            System.out.println("File does not exist: " + filePath.toString()); // Log if the file is missing
            return ""; // Or throw a more specific exception
        }
    
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toString()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        } catch(IOException e){
            System.out.println("IOException inside Get_Documentdata");
            e.printStackTrace();
            throw e;
        }
    
        System.out.println("File content read successfully."); // Log successful read
        return content.toString();
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