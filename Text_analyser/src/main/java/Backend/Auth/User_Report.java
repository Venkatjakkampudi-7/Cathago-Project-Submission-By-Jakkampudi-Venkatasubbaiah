package Backend.Auth;

import backend_processes.Documents;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;

public class User_Report extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Documents doc = null;
        String outcome = null;
        try
        {
            HttpSession session = request.getSession(false);
        
            int userid = (Integer) session.getAttribute("userid");
            
            doc = new Documents(userid);
            outcome = doc.Read_Scans_History();
            
            response.setContentType("text/plain");
            response.setHeader("Content-Disposition", "attachment; filename=\"downloaded_file.txt\"");

            try (PrintWriter out = response.getWriter()) 
            {
                out.print(outcome);
            }
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
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
