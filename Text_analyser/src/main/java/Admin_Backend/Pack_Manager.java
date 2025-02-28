package Admin_Backend;

import backend_processes.Pack;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class Pack_Manager extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Pack pk = null;
        
        try
        {
            String outcome = null;

            // Read JSON data from request body
            int req = Integer.parseInt(request.getParameter("req"));

            pk = new Pack();

            switch(req){
                case 1:
                    // Case for reading the credit requests
                    outcome = pk.Read_packs();
                    break;

                case 2:
                    // Case for Fetching credit requests
                    String skey = request.getParameter("skey");
                    outcome = pk.Fetch_packs(skey);
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
            if(pk != null){
                pk.Close_connection();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Pack pk = null;
        
        try
        {
            String outcome = null;
            
            // Read JSON data from request body
            InputStream inputStream = request.getInputStream();                         // Inputstream object for attaining the input stream
            JsonReader jsonReader = Json.createReader(inputStream);                     // Json reader object for reading the input stream with Json.createReader
            JsonObject jsonObject = jsonReader.readObject();                            // Reading the json object
            jsonReader.close();                                                         // Closing the json reader object

            String packname = jsonObject.getString("packname", "");
            int creditcount = Integer.parseInt(jsonObject.getString("creditcount"));

            pk = new Pack();

            int val = pk.Create_pack(packname,creditcount);
            if(val == 1)
            {
                outcome = "{\"Status\" : \"Pack Created !\"}";
            }
            else
            {
                outcome = "{\"Status\" : \"Failed to create pack !\"}";
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
            if(pk != null){
                pk.Close_connection();
            }
        }
    }
    
    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Pack pk = null;
        
        try
        {
            String outcome = null;
            
            // Read JSON data from request body
            InputStream inputStream = request.getInputStream();                         // Inputstream object for attaining the input stream
            JsonReader jsonReader = Json.createReader(inputStream);                     // Json reader object for reading the input stream with Json.createReader
            JsonObject jsonObject = jsonReader.readObject();                            // Reading the json object
            jsonReader.close();                                                         // Closing the json reader object

            String aipower = jsonObject.getString("aipower","");
            String packstatus = jsonObject.getString("packstatus","");
            int creditlimit = jsonObject.getInt("creditlimit");
            int packid = jsonObject.getInt("packid");

            pk = new Pack();

            int val = pk.Update_packs(packid,creditlimit,aipower,packstatus);
            if(val == 1)
            {
                outcome = "{\"Status\" : \"Pack Updated !\"}";
            }
            else
            {
                outcome = "{\"Status\" : \"Failed to update pack !\"}";
            }

            pk.Close_connection();

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
            if(pk != null){
                pk.Close_connection();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}