package Backend.Auth;

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

public class Login extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);

        String outcome = null;
        
        if (session != null && session.getAttribute("userid") != null) {
            // User is already logged in
            outcome = "{\"Status\":\"Logged In !\", \"Privillage\":\""+session.getAttribute("privillage").toString()+"\", \"account_status\":\""+session.getAttribute("account_status").toString()+"\"}";
        } else {
            // User is not logged in
            outcome = "{\"Status\":\"Login Required !\"}";
        }
        
        response.setContentType("application/json");                                // Setting the Response Content type as json
        response.getWriter().write(outcome);  // Generating the json response as Registration is made successfully
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Security se = null;
        HttpSession session = null;
        int userid = 0;
        String privillage = null;
        String accountstatus = null;
        
        try
        {
            // Read JSON data from request body
            InputStream inputStream = request.getInputStream();                         // Inputstream object for attaining the input stream
            JsonReader jsonReader = Json.createReader(inputStream);                     // Json reader object for reading the input stream with Json.createReader
            JsonObject jsonObject = jsonReader.readObject();                            // Reading the json object
            jsonReader.close();                                                         // Closing the json reader object

            // Extract data
            String username = jsonObject.getString("username", "");                     // Retriving the username
            String password = jsonObject.getString("password", "");                     // Retriving the password

            se = new Security(username,password);
        
            String outcome = se.signin();
            
            if(!outcome.equals("Invalid credentials"))
            {
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
            else{
                outcome = "{\"Status\":\"Invalid credentials\"}";
            }
            
            // Send response
            response.setContentType("application/json");                                // Setting the Response Content type as json
            response.getWriter().write(outcome);  // Generating the json response as Registration is made successfully
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            se.Close_connection();                                  // Closing the connection
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
