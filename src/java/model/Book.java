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
public class Book implements java.io.Serializable {

    private static final long serialVersionUID = 5938782489537536151L;

    private String bookId;
    private String collectionId;
    private Calendar createdOn;
    private Calendar lastModifiedOn;
    
    private String isbn;
    private String uri;
    private Calendar copyrightDate;
    private Calendar publicationDate;
    private Calendar reprintingDate;
    private int noOfPages;
    private String rights;
    private String format;
    private List<LangString> titleList;
    private List<LangString> descriptionList;
    private List<String> creatorList;
    private List<String> publisherList;
    private List<String> editorList;
    private List<String> commentsList;
    private List<String> publicationPlace;
    private List<String> typeList;
    
    /**
     * Creates a Book object.
     */
    public Book() {
        bookId = null;
        collectionId = null;
        createdOn = null;        
        lastModifiedOn = null;
        uri = null;
        isbn = null;
        uri = null;        
        rights = null;
        format = null;
        noOfPages = 0;

        
        titleList = new ArrayList<LangString>();
        creatorList = new ArrayList<String>();
        typeList = new ArrayList<String>();
        descriptionList = new ArrayList<LangString>();
        publisherList = new ArrayList<String>();
        editorList = new ArrayList<String>();
        commentsList = new ArrayList<String>();
        publicationPlace = new ArrayList<String>();
    }

    /**
     * Creates a Book object using specified values.
     *
     * @param bookId          The book's identifier.
     * @param collectionId    The identifier of the collection where the
     *                        book belongs.
     */
    public Book(String bookId, String collectionId) {
    	this.bookId = bookId;
        this.collectionId = collectionId;

        createdOn = null;        
        lastModifiedOn = null;
        uri = null;
        isbn = null;
        uri = null;        
        rights = null;
        format = null;
        noOfPages = 0;

        
        titleList = new ArrayList<LangString>();
        creatorList = new ArrayList<String>();
        typeList = new ArrayList<String>();
        descriptionList = new ArrayList<LangString>();
        publisherList = new ArrayList<String>();
        editorList = new ArrayList<String>();
        commentsList = new ArrayList<String>();
        publicationPlace = new ArrayList<String>();
        
   
        
    }
    
        public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public List<String> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(List<String> commentsList) {
        this.commentsList = commentsList;
    }
    
    public void addComment(String comment) {
        commentsList.add(comment);
    }

    public Calendar getCopyrightDate() {
        return copyrightDate;
    }

    public void setCopyrightDate(Calendar copyrightDate) {
        this.copyrightDate = copyrightDate;
    }

    public Calendar getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Calendar createdOn) {
        this.createdOn = createdOn;
    }

    public List<String> getCreatorList() {
        return creatorList;
    }

    public void setCreatorList(List<String> creatorList) {
        this.creatorList = creatorList;
    }
    
    public void addCreator(String creator) {
        creatorList.add(creator);
    }

    public List<LangString> getDescriptionList() {
        return descriptionList;
    }

    public void setDescriptionList(List<LangString> descriptionList) {
        this.descriptionList = descriptionList;
    }
    
    
    public void addDescription(String value, String lang) {
        LangString description = new LangString(value, lang);
        descriptionList.add(description);
    }

    public List<String> getEditorList() {
        return editorList;
    }

    public void setEditorList(List<String> editorList) {
        this.editorList = editorList;
    }
    
    public void addEditor(String editor) {
        editorList.add(editor);
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Calendar getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Calendar lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public int getNoOfPages() {
        return noOfPages;
    }

    public void setNoOfPages(int noOfPages) {
        this.noOfPages = noOfPages;
    }

    public Calendar getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Calendar publicationDate) {
        this.publicationDate = publicationDate;
    }

    public List<String> getPublicationPlace() {
        return publicationPlace;
    }

    public void setPublicationPlace(List<String> publicationPlace) {
        this.publicationPlace = publicationPlace;
    }
    
    public void addPublicationPlace(String place) {
        publicationPlace.add(place);
    }

    public List<String> getPublisherList() {
        return publisherList;
    }

    public void setPublisherList(List<String> publisherList) {
        this.publisherList = publisherList;
    }
    
    public void addPublisher(String publisher) {
        publisherList.add(publisher);
    }

    public Calendar getReprintingDate() {
        return reprintingDate;
    }

    public void setReprintingDate(Calendar reprintingDate) {
        this.reprintingDate = reprintingDate;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public List<LangString> getTitleList() {
        return titleList;
    }

    public void setTitleList(List<LangString> titleList) {
        this.titleList = titleList;
    }
    
    public void addTitle(LangString title) {
        titleList.add(title);
    }
    
    
    public void addTitle(String value, String lang) {
        LangString title = new LangString(value, lang);
        titleList.add(title);
    }

    public List<String> getType() {
        return typeList;
    }

    public void setType(List<String> type) {
        this.typeList = type;
    }
    
    public void addType(String type) {
        typeList.add(type);
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    
    
}
