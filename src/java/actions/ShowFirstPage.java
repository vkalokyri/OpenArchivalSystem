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
import model.Collection;
import model.LangString;

/**
 *
 * @author ganest
 */
public class ShowFirstPage implements Action {

    /** Page to be used to display data */
   private String view;
   Collection book;
   List<Collection> collectionList=new ArrayList<Collection>();


   
   public boolean execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
      
        view = "./views/index.jsp";
		
        collectionList = DAOFactory.getDAOFactory(DAOFactory.EXIST).getCollectionDAO().getCollectionList();
        HttpSession session = req.getSession();
        List<String> current = (ArrayList<String>)session.getAttribute("CurrentTree");
        if(current==null){
            current=new ArrayList();
            current.add(0, "-1");
            current.add(1, "-1");
            session.setAttribute("CurrentTree", current);
        }
        current.add(0, "-1");
        current.add(1, "-1");
        session.setAttribute("CurrentTree", current);
        
        for(int i=0;i< collectionList.size();i++ ){
            for(int j=0;j<collectionList.get(i).getBookList().size();j++)
                System.out.println("CollectionID="+collectionList.get(i).getId()+" kai bookID="+collectionList.get(i).getBookList().get(j).getBookId());
        }
        
      return true;
   }
   /** Return the page name (and path) to display the view */
   public Object getModel() {
       return collectionList;
   }

   /** Return the page name (and path) to display the view */
   public String getView() {
       return view;
   }

}
