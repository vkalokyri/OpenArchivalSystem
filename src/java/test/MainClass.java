/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import dao.impl.exist.ExistBookDAO;
import dao.impl.exist.ExistCollectionDAO;
import java.util.ArrayList;
import java.util.List;
import model.Book;
import model.Collection;
import model.LangString;

/**
 *
 * @author suitcase
 */
public class MainClass {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Collection collection = new Collection();
        collection.addTitle("coll2", "en");
        collection.addTitle("coll", "el"); 
        
        Book book1= new Book();
        book1.addTitle("Title1", "en");
        book1.setIsbn("gdfhjsagjk837487236r72");
        book1.setNoOfPages(229);
        List list = new ArrayList<LangString>();
        list.add(new LangString("Perfect book","English"));
        list.add(new LangString("Τέλειο βιβλίο","Eλληνικά"));
        book1.setDescriptionList(list);
        List list2 = new ArrayList<String>();
        list2.add("Valia Kalokyri");
        list2.add("Βάλια Καλοκύρη");
        book1.setDescriptionList(list);
        book1.setCreatorList(list2);
        
        Book book2= new Book();
        book2.addTitle("Title2", "en");
        book2.setIsbn("isbn");
        book2.setNoOfPages(500);
        list = new ArrayList<LangString>();
        list.add(new LangString("Description","English"));
        book2.setDescriptionList(list);
        list2 = new ArrayList<String>();
        list2.add("Valia");
        book2.setDescriptionList(list);
        book2.setCreatorList(list2);
        
        List bookList = new ArrayList<Book>();
        bookList.add(book1);
        bookList.add(book2);
        
        ExistCollectionDAO dao = new ExistCollectionDAO();
        ExistBookDAO bdao = new ExistBookDAO();
        bdao.searchBook(book2);
        //bdao.getBookInDCFormat("1157b55f-ef0c-4891-8c06-4d0598670201","04e071fa-3a43-41b7-b8cf-e669465f3691");
       // List<Book> bookList2 = bdao.getBookList("5e89566b-072d-4878-ae93-d1a6eb171b29");        
        //System.out.println(bookList2.size());
        //System.out.println(bookList2.get(0).getTitleList().get(0).getValue());
        //System.out.println(bookList2.get(0).getDescriptionList().get(0).getValue());
        //Book book = bdao.getBook("4b154ce1-8f2c-43af-b989-4e2b3b129ced", "04e071fa-3a43-41b7-b8cf-e669465f3691");
        //System.out.println(book.getDescriptionList().get(0).getValue());
        //System.out.println(book.getCreatorList().get(0));
        //bdao.updateBook("59867c1b-e3ef-43ec-8f11-4160f6b76f5b", book2, "c52786f1-4596-4443-a8b3-63131d8923f7");
        //dao.insertCollection(collection,bookList);
        //dao.updateCollection("1d9b4f69-59f2-4f17-bfa5-0cbcb0e42512",collection);
        //dao.deleteCollection("d23a3057-34ea-44ea-83e2-c418bd416997");
        //dao.insertCollectionBooks("0e5b9c17-13fd-466e-b133-da3b227e88c5", bookList);
       // List<Collection> collist = dao.getCollectionList();
        //System.out.println(collist.get(0).getTitleList().get(0).getValue());
        //System.out.println(collist.get(0).getTitleList().get(0).getLang());
        //Book book= bdao.getBook("ec0f4b56-bfff-4ac9-a5c2-a9833d977024", "0e5b9c17-13fd-466e-b133-da3b227e88c5");
        //System.out.println(book.getDescriptionList().get(1).getValue());
        //System.out.println(book.getCreatorList().get(0));
        //bdao.deleteBook("8dd8bfa6-6852-41d9-bbe2-855303242dd9", "1016f73b-3023-40d7-9e7a-3617e20b993d");
        //dao.getCollection("0cd679c2-53e5-43fb-b165-e3d8e3dcb399");
    }
}