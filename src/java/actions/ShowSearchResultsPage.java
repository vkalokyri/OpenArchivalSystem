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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Book;
import model.Collection;

/**
 *
 * @author ganest
 */
public class ShowSearchResultsPage implements Action {

    /** Page to be used to display data */
   private String view;
   List<Book> bookList = new ArrayList<Book>();
   
   public boolean execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
      
       HttpSession session = req.getSession();
       List current = (ArrayList)session.getAttribute("CurrentTree");
       if(current==null){
            current=new ArrayList();
            current.add(0, "-1");
            current.add(1, "-1");
            session.setAttribute("CurrentTree", current);
        }
       
           
       String [] titles=req.getParameterValues("title");      
       String [] langTitles = req.getParameterValues("titleLang");
       String [] creators = req.getParameterValues("creator");
       String isbn = req.getParameter("isbn");
       String [] publisher = req.getParameterValues("publisher");
       String[] editor = req.getParameterValues("editor");
       String copyDate = req.getParameter("copyDate");
       String publicDate = req.getParameter("publicDate");
       String reprintDate = req.getParameter("reprintDate");
       String [] description = req.getParameterValues("description");
       String [] descriptLang = req.getParameterValues("descriptionLang");
       String noOfPages = req.getParameter("noOfPages");
       String [] publicPlace = req.getParameterValues("publicPlace");
       String [] type = req.getParameterValues("type");
       String rights = req.getParameter("rights");
       String format = req.getParameter("format");
       String uri = req.getParameter("uri");
       String[] comments = req.getParameterValues("comments");
              
       Book book = new Book();       
       for(int i=0; i<titles.length; i++)
            book.addTitle(titles[i], langTitles[i]);       
       for(int i=0; i<creators.length; i++)
            book.addCreator(creators[i]);       
       for(int i=0; i<publisher.length; i++)
            book.addPublisher(publisher[i]);  
      for(int i=0; i<editor.length; i++)
            book.addEditor(editor[i]);
       book.setIsbn(isbn);
        try {
            if(!publicDate.equals(""))
                book.setPublicationDate(getCalendarInstance(publicDate));
            if(!copyDate.equals(""))
                book.setCopyrightDate(getCalendarInstance(copyDate));
            if(!reprintDate.equals(""))
                book.setReprintingDate(getCalendarInstance(reprintDate));
        } catch (ParseException ex) {
            Logger.getLogger(ShowSearchResultsPage.class.getName()).log(Level.SEVERE, null, ex);
        }       
       for(int i=0; i<description.length; i++)
            book.addDescription(description[i],descriptLang[i]);
       if(!noOfPages.equals(""))
            book.setNoOfPages(Integer.parseInt(noOfPages));
       for(int i=0; i<type.length; i++)
            book.addType(type[i]);
       for(int i=0; i<publicPlace.length; i++)
            book.addPublicationPlace(publicPlace[i]);
       book.setRights(rights);
       book.setFormat(format);
       book.setUri(uri);
      for(int i=0; i<comments.length; i++)
            book.addComment(comments[i]);
       
      bookList = DAOFactory.getDAOFactory(DAOFactory.EXIST).getBookDAO().searchBook(book);
       
      view = "./views/ShowSearchResultsView.jsp";
        
        
      return true;
   }
   /** Return the page name (and path) to display the view */
   public Object getModel() {
       return bookList;
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
