/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package actions;

import controller.Action;
import dao.BookDAO;
import dao.CollectionDAO;
import dao.DAOFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Book;
import model.Collection;
import model.LangString;

/**
 *
 * @author ganest
 */
public class ShowBookPage implements Action {

    /** Page to be used to display data */
   private String view;
   Book book;
   
   public boolean execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
      	
       String collId = req.getParameter("collId");
       String bookId = req.getParameter("bookId");
       
       HttpSession session = req.getSession();
       List current = (ArrayList)session.getAttribute("CurrentTree");
       if(current==null){
            current=new ArrayList();
            current.add(0, "-1");
            current.add(1, "-1");
            session.setAttribute("CurrentTree", current);
        }
       current.add(0,collId);
       current.add(1,bookId);
       
       
        view = "./views/ShowBookView.jsp";
	System.out.println(bookId+" kai   "+ collId);	
        book = DAOFactory.getDAOFactory(DAOFactory.EXIST).getBookDAO().getBook(bookId, collId);
        System.out.println(book.getTitleList().get(0).getValue());
        
        
      return true;
   }
   /** Return the page name (and path) to display the view */
   public Object getModel() {
       return book;
   }

   /** Return the page name (and path) to display the view */
   public String getView() {
       return view;
   }

}
