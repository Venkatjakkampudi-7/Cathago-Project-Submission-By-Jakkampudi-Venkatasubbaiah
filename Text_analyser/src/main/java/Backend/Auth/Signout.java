package Backend.Auth;

import backend_processes.Logout;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class Signout extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        
        HttpSession session = request.getSession(false);
        
        Logout lt = null;
        
        try
        {
            lt = new Logout();
            
            int userid = (Integer) session.getAttribute("userid");
            lt.addlog(userid);
            
            if (session != null) {
                session.invalidate();
            }
            
            response.setContentType("application/json");
            response.getWriter().write("{\"Status\" : \"User Signed out !\"}");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(lt != null)
            {
                lt.Close_connection();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
