package Backend.Auth;

import backend_processes.Documents;
import backend_processes.Credits;

import Document_Processor.Pre_Processor;
import Document_Processor.Main_Processor;
import Document_Processor.Outcome_Processor;


import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.InputStream;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1MB
    maxFileSize = 1024 * 1024 * 1024, // 1GB
    maxRequestSize = 1024 * 1024 * 1024 // 1GB
)
public class ScanUpload extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int scanid = Integer.parseInt(request.getParameter("scanid"));
        
        String outcome = "";
        Outcome_Processor opr = new Outcome_Processor(scanid);
        
        try
        {
            outcome = opr.Get_Doc_Scores();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(opr != null){
                opr.Close_connection();
            }
        }
        
        // Send response
        response.setContentType("application/json");                // Setting the Response Content type as json
        response.getWriter().write(outcome);                        // Generating the json response as Registration is made successfully
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        Documents doc = null;
        Pre_Processor ppr = null;
        Main_Processor mpr = null;
        Outcome_Processor opr = null;
        
        try
        {
            String outcome = null;

            // Extract data
            HttpSession session = request.getSession(false);
        
            int userid = (Integer) session.getAttribute("userid");
            
            Part filePart = request.getPart("document");                    // Get uploaded file
            String fileName = filePart.getSubmittedFileName();              // Extract file name

            doc = new Documents(userid);

            if(doc.Get_credit_count() > 0)
            {
                int docid = doc.Move_document(filePart, fileName);         // Process to move and save the file while retriving the outcome

                ppr = new Pre_Processor(docid);

                // Does the rest of the processing only if there are more than one file
                if(ppr.first_go())
                {
                    ppr.Close_connection();
                    outcome = "{\"status\":\"This is first file !\",\"sid\":"+docid+"}";
                }
                else{
                    mpr = new Main_Processor(userid,docid);

                    opr = new Outcome_Processor(docid);
                    outcome = opr.Get_Doc_Scores();
                }
            }
            else
            {
                outcome = "{\"status\":\"Haha Nice try !\")";
            }

            // Send response
            response.setContentType("application/json");                // Setting the Response Content type as json
            response.getWriter().write(outcome);                        // Generating the json response as Registration is made successfully
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            // Close connections in a finally block
            if (doc != null) {
                doc.Close_connection();
            }
            if (ppr != null) {
                ppr.Close_connection();
            }
            if (mpr != null) {
                mpr.Close_connection();
            }
            if (opr != null) {
                opr.Close_connection();
            }
        }
    }
    
    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Documents doc = null;
        Pre_Processor ppr = null;
        Main_Processor mpr = null;
        Outcome_Processor opr = null;
        
        try
        {
            // Read JSON data from request body
            InputStream inputStream = request.getInputStream();                         // Inputstream object for attaining the input stream
            JsonReader jsonReader = Json.createReader(inputStream);                     // Json reader object for reading the input stream with Json.createReader
            JsonObject jsonObject = jsonReader.readObject();                            // Reading the json object
            jsonReader.close();                                                         // Closing the json reader object

            // Extract data
            HttpSession session = request.getSession(false);
        
            int usid = (Integer) session.getAttribute("userid");
            int docid = jsonObject.getInt("scanid");

            String outcome = "";
            int key = 0;
            
            doc = new Documents(usid,docid);

            if(doc.Get_credit_count() > 0)
            {
                key = doc.Re_scan();

                if(key == docid)
                {
                    ppr = new Pre_Processor(docid);

                    // Does the rest of the processing only if there are more than one file
                    if(ppr.first_go())
                    {
                        outcome = "{\"status\":\"This is first file !\",\"sid\":"+docid+"}";
                    }
                    else
                    {
                        mpr = new Main_Processor(usid,docid);

                        opr = new Outcome_Processor(docid);
                        outcome = opr.Get_Doc_Scores();
                    }
                }
            }
            else
            {
                outcome = "{\"status\":\"Haha Nice try !\",\"sid\":"+docid+",\"key\":"+key+"}";
            }

            // Send response
            response.setContentType("application/json");                // Setting the Response Content type as json
            response.getWriter().write(outcome);                        // Generating the json response as Registration is made successfully
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            // Close connections in a finally block
            if (doc != null) {
                doc.Close_connection();
            }
            if (ppr != null) {
                ppr.Close_connection();
            }
            if (mpr != null) {
                mpr.Close_connection();
            }
            if (opr != null) {
                opr.Close_connection();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}