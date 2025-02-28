package Backend.Auth;

import backend_processes.Users;
import backend_processes.Documents;
import backend_processes.Credits;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class Profile extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Users us = null;
        Documents docs = null;
        Credits cs = null;
        
        try
        {
            String outcome = null;
            
            HttpSession session = request.getSession(false);
        
            int req = Integer.parseInt(request.getParameter("req"));
            int userid = (Integer) session.getAttribute("userid");

            switch(req){
                case 1:
                    us = new Users();
                    outcome = us.Read_profile(userid);
                    
                    break;
                case 2:
                    docs = new Documents(userid);
                    outcome = docs.Read_previous_Scans();
                    
                    break;
                case 3:
                        cs = new Credits(userid);
                        int ccount = cs.Get_credit_count();

                        if(ccount == 0){
                            boolean reqstatus = cs.Get_credit_request_status();
                            outcome = "{\"credits\" : "+cs.Get_credit_count()+",\"reqstatus\":"+reqstatus+"}";
                        }
                        else{
                            outcome = "{\"credits\" : "+cs.Get_credit_count()+"}";
                        }

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
            if(docs != null){
                docs.Close_connection();
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