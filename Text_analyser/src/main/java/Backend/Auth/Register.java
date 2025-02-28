package Backend.Auth;

// Importing the Users class to work with the registrations
import backend_processes.Users;
import backend_processes.Security;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;

public class Register extends HttpServlet {
    
    // Does the Job for the Registration Part
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        Users us = null;
        Security se = null;
        HttpSession session = null;
        int userid = 0;
        String privillage = null;
        String accountstatus = null;
        String outcome = null;
        
        try
        {
            // Read JSON data from request body
            InputStream inputStream = request.getInputStream();                         // Inputstream object for attaining the input stream
            JsonReader jsonReader = Json.createReader(inputStream);                     // Json reader object for reading the input stream with Json.createReader
            JsonObject jsonObject = jsonReader.readObject();                            // Reading the json object
            jsonReader.close();                                                         // Closing the json reader object

            // Extract data
            String firstname = jsonObject.getString("firstname", "");                   // Retriving the username
            String lastname = jsonObject.getString("lastname", "");                     // Retriving the password
            String username = jsonObject.getString("username", "");                     // Retriving the username
            String emailid = jsonObject.getString("emailid", "");                       // Retriving the username
            String password = jsonObject.getString("password", "");                     // Retriving the password
            String confirmedPassword = jsonObject.getString("confirmedPassword", "");   // Retriving the confirmed password

            // Print data (for debugging)
            System.out.println("Username: " + username);                                // Username
            System.out.println("Password: " + password);                                // Password
            System.out.println("Confirmed Password: " + confirmedPassword);             // Confirmed Password
            
            us = new Users(firstname,lastname,username,emailid,password);

            int reprocess = us.Create_user();
            
            if(reprocess == 1){
            
                // Signing the user
                se = new Security(username,password);
                outcome = se.signin();

                userid = se.Get_userid();
                privillage = se.Get_privillage();
                accountstatus = se.Get_account_status();

                if(accountstatus.equals("active")){

                    session = request.getSession(true);
                    session.setMaxInactiveInterval(60*60);
                    
                    session.setAttribute("userid", userid);
                    session.setAttribute("privillage", privillage);
                    session.setAttribute("account_status", accountstatus);
                }
            }
            else if(reprocess == -1)
            {
                outcome = "{\"Status\":\"Failed to register !\"}";
            }
            
            // Send response
            response.setContentType("application/json");                                // Setting the Response Content type as json
            response.getWriter().write(outcome);                                        // Generating the json response as Registration is made successfully
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(us != null)
            {
                us.Close_connection();
            }
        }
     }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}