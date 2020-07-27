/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author suitcase
 */
public class Collection implements java.io.Serializable {

    private static final long serialVersionUID = -8088132331695852971L;

    private String id;
    private Calendar createdOn;
    private Calendar lastModifiedOn;

    private List<LangString> titleList;


    private List<Book> bookList;

    /**
     * Creates a Collection object.
     */
    public Collection() {
        titleList = new ArrayList<LangString>();
        bookList = new ArrayList<Book>();
    }

    /**
     * Creates a Collection object using specified values.
     *
     * @param id            The collection's identifier.
     */
    public Collection(String id) {
        this.id = id;
        
        createdOn = null;
        lastModifiedOn = null;

        titleList = new ArrayList<LangString>();
        
        bookList = new ArrayList<Book>();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public Calendar getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Calendar createdOn) {
        this.createdOn = createdOn;
    }

    public Calendar getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Calendar lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public List<LangString> getTitleList() {
        return titleList;
    }

    public void addTitleList(List<LangString> titleList) {
        this.titleList.addAll(titleList);
    }

    public void addTitle(LangString title) {
        titleList.add(title);
    }

    public void addTitle(String value, String lang) {
        LangString title = new LangString(value, lang);
        titleList.add(title);
    }

    
    

    public List<Book> getBookList() {
        return bookList;
    }

    public void addBookList(List<Book> bookList) {
        this.bookList.addAll(bookList);
    }

    public void addBook(Book book) {
        bookList.add(book);
    }

    public void addBook(String bookId, String title) {
        Book book = new Book(bookId, title);
        bookList.add(book);
    }
}
