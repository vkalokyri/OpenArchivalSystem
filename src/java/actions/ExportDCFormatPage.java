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
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Book;

/**
 *
 * @author ganest
 */
public class ExportDCFormatPage implements Action {

    /** Page to be used to display data */
   private String view;
   
   public boolean execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
      	
        res.setContentType("application/x-download");
        res.setCharacterEncoding("UTF-8");
        System.out.println("Title = "+req.getParameter("title"));
        res.setHeader("Content-Disposition", "attachment; filename=" + req.getParameter("title") +".xml");

        String bookId = req.getParameter("bookId");
        String collectionId = req.getParameter("collectionId");

        String bookInDC = DAOFactory.getDAOFactory(DAOFactory.EXIST).getBookDAO().getBookInDCFormat(bookId, collectionId);				
        System.out.println("HERE"+bookInDC);
        res.getWriter().write(bookInDC);    

        
      return false;
   }
   /** Return the page name (and path) to display the view */
   public Object getModel() {
       return null;
   }

   /** Return the page name (and path) to display the view */
   public String getView() {
       return view;
   }
   
   private Calendar getCalendarInstance(String strdate) throws ParseException{
       DateFormat formatter ; 
       Date date ; 
       formatter = new SimpleDateFormat("dd-MM-yyyy");
       date = (Date)formatter.parse(strdate); 
       Calendar cal=Calendar.getInstance();
       cal.setTime(date);
       return cal;
   }

}
