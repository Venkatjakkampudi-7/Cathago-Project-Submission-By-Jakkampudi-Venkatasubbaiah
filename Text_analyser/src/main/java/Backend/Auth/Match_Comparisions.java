package Backend.Auth;

import backend_processes.Documents;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Match_Comparisions extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int scanid = Integer.parseInt(request.getParameter("doc1"));
        int vsscanid = Integer.parseInt(request.getParameter("doc2"));
        
        String outcome = null;
        Documents doc = new Documents();
        
        try
        {
            outcome = doc.Get_Comparisions(scanid,vsscanid);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(doc != null){
                doc.Close_connection();
            }
        }
        
        // Send response
        response.setContentType("application/json");                // Setting the Response Content type as json
        response.getWriter().write(outcome);                        // Generating the json response as Registration is made successfully
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
