/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package actions;

import controller.Action;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 *
 * @author ganest
 */
public class NewBookPage implements Action {

    /** Page to be used to display data */
   private String view;
   
   public boolean execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
      	
       String collId = req.getParameter("collId");
       
       
       HttpSession session = req.getSession();
       List current = (ArrayList)session.getAttribute("CurrentTree");
       if(current==null){
            current=new ArrayList();
            current.add(0, "-1");
            current.add(1, "-1");
            session.setAttribute("CurrentTree", current);
        }
       current.add(0,collId);
       
       
        view = "./views/NewBookView.jsp";
        
      return true;
   }
   /** Return the page name (and path) to display the view */
   public Object getModel() {
       return null;
   }

   /** Return the page name (and path) to display the view */
   public String getView() {
       return view;
   }

}
