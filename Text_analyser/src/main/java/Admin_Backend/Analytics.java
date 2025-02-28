package Admin_Backend;

import backend_processes.Users;
import backend_processes.Documents;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Analytics extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Users us = null;
        Documents ds = null;
        
        String outcome = null;
        
        try
        {
            int req = Integer.parseInt(request.getParameter("req"));

            switch(req)
            {
                case 1:
                    // Case for reading the top users
                    us = new Users();
                    outcome = us.Read_top_users();

                    break;

                case 2:
                    // Case for reading the credit requests
                    ds = new Documents();
                    outcome = ds.Get_most_searched();

                    break;

                case 3:
                    // Case for reading the profile
                    ds = new Documents();
                    outcome = ds.Show_all_scans();

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
            if(ds != null){
                ds.Close_connection();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
