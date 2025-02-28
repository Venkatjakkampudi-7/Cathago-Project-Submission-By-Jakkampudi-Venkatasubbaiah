package Backend.Auth;

import backend_processes.Credits;

import java.io.PrintWriter;
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

public class Credit_request extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Credit_request</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Credit_request at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Read JSON data from request body
        
        Credits cs = null;
        
        try
        {
            // Extract data
            HttpSession session = request.getSession(false);
        
            int userid = (Integer) session.getAttribute("userid");
            
            String outcome = null;
            int requeststatus = 0;

            cs = new Credits(userid);
        
            requeststatus = cs.Request_credits();
        
        
            if(requeststatus == 0){
                outcome = "{\"Status\" : \"Waiting for Admin to process your request !\"}";
            }
            else if(requeststatus == 1){
                outcome = "{\"Status\" : \"Requested Admin for credits !\"}";
            }
            else if(requeststatus == -1){
                outcome = "{\"Status\" : \"Failed to Request Admin for credits\"}";
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
            if(cs != null){
                cs.Close_connection();                                  // Closing the connection
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
