package Admin_Backend;

import backend_processes.Users;
import backend_processes.Credits;
import backend_processes.Documents;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.InputStream;

public class User_Manager extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Users us = null;
        Credits cs = null;
        Documents ds = null;
        
        try
        {
            String outcome = null;

            int req = Integer.parseInt(request.getParameter("req"));
            int userid = 0;

            switch(req)
            {
                case 1:
                    // Case for reading the credit requests
                    us = new Users();
                    outcome = us.Read_users();
                    
                    break;

                case 2:
                    // Case for reading the credit requests
                    String skey = request.getParameter("skey");

                    us = new Users();
                    outcome = us.Fetch_users(skey);
                    
                    break;

                case 3:
                    // Case for reading the profile
                    userid = Integer.parseInt(request.getParameter("userid"));

                    us = new Users();
                    outcome = us.Know_user(userid);
                    
                    break;

                case 4:
                    // Case for reading the scan history
                    userid = Integer.parseInt(request.getParameter("userid"));

                    ds = new Documents(userid);
                    outcome = ds.Read_previous_Scans();
                    
                    break;

                case 5:
                    // Case for reading the credit history
                    userid = Integer.parseInt(request.getParameter("userid"));

                    cs = new Credits(userid);
                    outcome = cs.Read_credit_history();
                    
                    break;

                default:
                    outcome = "{\"Status\" : \"Invalid Request !\"}";
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
            if(us != null){
                us.Close_connection();
            }
            if(cs != null){
                cs.Close_connection();
            }
            if(ds != null){
                ds.Close_connection();
            }
        }
    }
    
    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Users us = null;   // User object
        Credits cs = null; // Credit object
        
        try
        {
            String outcome = "{";

            // Read JSON data from request body
            InputStream inputStream = request.getInputStream();                         // Inputstream object for attaining the input stream
            JsonReader jsonReader = Json.createReader(inputStream);                     // Json reader object for reading the input stream with Json.createReader
            JsonObject jsonObject = jsonReader.readObject();                            // Reading the json object
            jsonReader.close();                                                         // Closing the json reader object

            int userid = jsonObject.getInt("userid");
            int creditcount = jsonObject.getInt("creditcount");
            int slabid = jsonObject.getInt("slabid");
            String accstatus = jsonObject.getString("accstatus", "");

            int cu = 0; // credit update flag
            int su = 0; // pack update flag
            int au = 0; // account update flag

            // Processing the credit adjustments
            if(creditcount > 0){
                cs = new Credits();
                cu = cs.Add_credits(userid, creditcount);
                if(cu > 0){
                    outcome += "\"creditstatus\":\"updated\",";
                }
                else if(cu == 0){
                    outcome += "\"creditstatus\":\"no change\",";
                }
            }
            else{
                outcome += "\"creditstatus\":\"no change\",";
            }
            // End of processing the credit adjustments

            // Processing the pack and the account
            us = new Users();
            su = us.Update_slab(userid, slabid);                // Updating the pack
            au = us.Update_accountstatus(userid, accstatus);    // Updating the account

            if(su > 0){
                outcome += "\"packstatus\":\"updated\",";
            }
            else if(su == 0){
                outcome += "\"packstatus\":\"no change\",";
            }

            if(au > 0){
                outcome += "\"accountstatus\":\"updated\"}";
            }
            else if(au == 0){
                outcome += "\"accountstatus\":\"no change\"}";
            }
            // End of processing the pack and the account

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
            if(us != null){
                us.Close_connection();
            }
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
