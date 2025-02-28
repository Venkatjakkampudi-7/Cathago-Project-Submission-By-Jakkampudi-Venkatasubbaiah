package Admin_Backend;

import backend_processes.Credits;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class Credit_Manager extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Credits cs = null;
        
        try
        {
            String outcome = null;

            // Read JSON data from request body
            int req = Integer.parseInt(request.getParameter("req"));

            cs = new Credits();

            switch(req)
            {
                case 1:
                    // Case for reading the credit requests
                    outcome = cs.Read_credit_requests();
                    break;

                case 2:
                    // Case for Fetching credit requests
                    String skey = request.getParameter("skey");
                    outcome = cs.Fetch_credit_requests(skey);
                    break;

                default:
                    outcome = "{\"Status\" : \"Invalid Request !\"}";
            }

            // Closing the connection
            cs.Close_connection();

            // Send response
            response.setContentType("application/json");                                // Setting the Response Content type as json
            response.getWriter().write(outcome);  // Generating the json response as Registration is made successfully
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(cs != null){
                cs.Close_connection();
            }
        }
    }
    
    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Credits cs = null;
        
        try
        {
            String outcome = null;
            
            // Read JSON data from request body
            InputStream inputStream = request.getInputStream();                         // Inputstream object for attaining the input stream
            JsonReader jsonReader = Json.createReader(inputStream);                     // Json reader object for reading the input stream with Json.createReader
            JsonObject jsonObject = jsonReader.readObject();                            // Reading the json object
            jsonReader.close();                                                         // Closing the json reader object

            int userid = jsonObject.getInt("userid");
            String reqstatus = jsonObject.getString("reqstatus", "");
            int creditcount = jsonObject.getInt("creditcount");
            int requestid = jsonObject.getInt("requestid");

            cs = new Credits(userid);

            int val = cs.Process_credit_request(reqstatus,creditcount,requestid);

            // Closing the connection
            cs.Close_connection();

            if(val == 1)
            {
                outcome = "{\"Status\" : \"Request Updated !\"}";
            }
            else
            {
                outcome = "{\"Status\" : \"Failed to update request !\"}";
            }

            // Send response
            response.setContentType("application/json");                                // Setting the Response Content type as json
            response.getWriter().write(outcome);  // Generating the json response as Registration is made successfully
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(cs != null){
                cs.Close_connection();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}