/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package actions;

import controller.Action;
import dao.DAOFactory;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Collection;

/**
 *
 * @author ganest
 */
public class ShowCollectionUpdatePage implements Action {

    /** Page to be used to display data */
   private String view;
   Collection collection;
   
   public boolean execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
      	
       
       String collId = req.getParameter("collId");
       if(!collId.equals("-1")){
       
           HttpSession session = req.getSession();
           List current = (ArrayList)session.getAttribute("CurrentTree");
           if(current==null){
                current=new ArrayList();
                current.add(0, "-1");
                current.add(1, "-1");
                session.setAttribute("CurrentTree", current);
            }
           current.add(0,collId);

           String [] titles=req.getParameterValues("title");      
           String [] langTitles = req.getParameterValues("titleLang");


           collection = new Collection();
           for(int i=0; i<titles.length; i++)
                collection.addTitle(titles[i], langTitles[i]);       	

            view = "./views/ShowCollectionView.jsp";

            DAOFactory.getDAOFactory(DAOFactory.EXIST).getCollectionDAO().updateCollection(collId, collection);
       }else{
           String [] titles=req.getParameterValues("title");      
           String [] langTitles = req.getParameterValues("titleLang");
           collection = new Collection();
           for(int i=0; i<titles.length; i++)
                collection.addTitle(titles[i], langTitles[i]);   
           
           view = "./views/ShowCollectionView.jsp";

           DAOFactory.getDAOFactory(DAOFactory.EXIST).getCollectionDAO().insertCollection(collection);
       }
       
        
      return true;
   }
   /** Return the page name (and path) to display the view */
   public Object getModel() {
       return collection;
   }

   /** Return the page name (and path) to display the view */
   public String getView() {
       return view;
   }
   
   private Calendar getCalendarInstance(String strdate) throws ParseException{
       DateFormat formatter ; 
       Date date ; 
       formatter = new SimpleDateFormat("dd-MMM-yy");
       date = (Date)formatter.parse(strdate); 
       Calendar cal=Calendar.getInstance();
       cal.setTime(date);
       return cal;
   }

}
