/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.impl.exist;

import dao.BookDAO;
import dao.CollectionDAO;
import gr.tuc.music.xmldb.connector.ExistConnector;
import gr.tuc.music.xmldb.connector.ExistConnectorException;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import model.Book;
import model.Collection;
import model.LangString;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlCalendar;
import org.apache.xmlbeans.XmlOptions;
import org.netbeans.xml.schema.myBookListSchema.CollectionListDocument;
import org.netbeans.xml.schema.myBookSchema.CollectionDocument;



/**
 * The Collection Data Access Object implementation for the Exist XML database.
 *
 * @author suitcase
 */
public class ExistCollectionDAO implements CollectionDAO {

     /**
     * Inserts a collection that contains only metadata information and no
     * records to the db.
     *
     * @param collection                A collection.
     * 
     * @return                          The stored collection.
     *                                  
     * @throws DBOperationException     If the operation failed to be executed.
     */

    @Override
    public Collection insertCollection(Collection collection) {
        
        String collectionId = insertCollection(collection, new ArrayList<Book>());
        
        return collection;
    }
    
   /**
     * Inserts a collection having a specific book list to the db.
     
     * @param collection                A collection.
     * @param bookList                The collection's book list.
     * 
     * @return                          The identifier of the stored collection.
     * @throws DBOperationException     If the operation failed to be executed.
     */
    @Override
    public String insertCollection(Collection collection, List<Book> bookList){


        collection.setId(UUID.randomUUID().toString());    
        collection.setCreatedOn(Calendar.getInstance());
        collection.setLastModifiedOn(Calendar.getInstance());
        
        // create a new CollectionDocument instance
        CollectionDocument xmlCollectionDocument = CollectionDocument.Factory.newInstance();

        // add "collection" element to the document
        org.netbeans.xml.schema.myBookSchema.CollectionDocument.Collection coll = xmlCollectionDocument.addNewCollection();

        // add "header" element to the collection
        org.netbeans.xml.schema.myBookSchema.HeaderInfo xmlCollectionHeader = coll.addNewHeader();

        // add "metadata" element to the collection
        org.netbeans.xml.schema.myBookSchema.CollectionDocument.Collection.Metadata xmlCollectionMetadata = coll.addNewMetadata();

        ////// Collection Header Initialization //////

        // add "id" element to the collection header
        xmlCollectionHeader.setId(collection.getId());

        // add "created" element to the collection header       
        xmlCollectionHeader.addNewCreated().setDate(collection.getCreatedOn());
       
        // add "lastModified" element to the collection header
        xmlCollectionHeader.addNewLastModified().setDate(collection.getLastModifiedOn());

        // add "status" element to the collection header
        xmlCollectionHeader.setStatus("IN_USE");

        ////// Collection Metadata Initialization //////
        org.netbeans.xml.schema.myBookSchema.LangString xmlLangString = null;

        for(LangString langString : collection.getTitleList()) {
            xmlLangString = xmlCollectionMetadata.addNewTitle(); 
            xmlLangString.setStringValue(langString.getValue());
            xmlLangString.setLang(langString.getLang());      
        }

        ////// Collection Books Initialization //////
        if(bookList!=null){
            org.netbeans.xml.schema.myBookSchema.Book[] bookArray = new org.netbeans.xml.schema.myBookSchema.Book[bookList.size()];
            int counter = 0;
         
        
            for(Book book : bookList) {
                System.out.println(bookList.size());
                org.netbeans.xml.schema.myBookSchema.Book xmlBook = org.netbeans.xml.schema.myBookSchema.Book.Factory.newInstance();
                org.netbeans.xml.schema.myBookSchema.HeaderInfo xmlBookHeader = xmlBook.addNewHeader();
                xmlBookHeader.setId(UUID.randomUUID().toString());
                xmlBookHeader.addNewCreated().setDate(Calendar.getInstance());
                xmlBookHeader.addNewLastModified().setDate(Calendar.getInstance());
                xmlBookHeader.setStatus("IN_USE");
                org.netbeans.xml.schema.myBookSchema.Book.Metadata xmlBookMetadata = xmlBook.addNewMetadata();
                 
                if(book.getIsbn()!=null)
                   xmlBookMetadata.setIsbn(book.getIsbn());
                if (book.getTitleList()!=null){
                    for (LangString title : book.getTitleList()){
                        org.netbeans.xml.schema.myBookSchema.LangString xmlbookTitle = xmlBookMetadata.addNewTitle();
                        xmlbookTitle.setStringValue(title.getValue());
                        xmlbookTitle.setLang(title.getLang());
                    }
                 }
                 if (book.getCreatorList()!=null){
                    for (String creator : book.getCreatorList()){
                        xmlBookMetadata.addCreator(creator);
                    }
                 }
                 if (book.getPublisherList()!=null){
                    for (String publisher : book.getPublisherList()){
                        xmlBookMetadata.addPublisher(publisher);
                    }
                }
                if (book.getEditorList()!=null){
                    for (String editor : book.getEditorList()){
                        xmlBookMetadata.addEditor(editor);
                    }
                } 
                if(book.getCopyrightDate()!=null)
                    xmlBookMetadata.setCopyrightDate(book.getCopyrightDate());
                if(book.getPublicationDate()!=null)
                    xmlBookMetadata.setPublicationDate(book.getPublicationDate()); 
                if(book.getReprintingDate()!=null)
                    xmlBookMetadata.setReprintingDate(book.getReprintingDate());
                if (book.getCommentsList()!=null){
                    for (String comments : book.getCommentsList()){
                        xmlBookMetadata.addComments(comments);
                    }
                }
                if (book.getDescriptionList()!=null){
                    for (LangString description : book.getDescriptionList()){
                        org.netbeans.xml.schema.myBookSchema.LangString xmlbookDescription = xmlBookMetadata.addNewDescription();
                        xmlbookDescription.setStringValue(description.getValue());
                        xmlbookDescription.setLang(description.getLang());
                    }
                 }
                 if (book.getPublicationPlace()!=null){
                    for (String publicationPlace : book.getPublicationPlace()){
                        xmlBookMetadata.addPublicationPlace(publicationPlace);
                    }
                 }   
                 if(book.getNoOfPages()!=0){
                    BigInteger bi = BigInteger.valueOf(book.getNoOfPages());
                    xmlBookMetadata.setNoOfPages(bi);
                 }
                 if (book.getType()!=null){
                    for (String type : book.getType()){
                        xmlBookMetadata.addType(type);
                    }
                 }
                 if(book.getRights()!=null)
                    xmlBookMetadata.setRights(book.getRights());
                 if(book.getFormat()!=null)
                    xmlBookMetadata.setFormat(book.getFormat());
                 if(book.getUri()!=null){
                    org.netbeans.xml.schema.myBookSchema.Book.Metadata.ObjectUri objectUri = xmlBookMetadata.addNewObjectUri(); 
                    objectUri.setStringValue(book.getUri());
                 }
                bookArray[counter]=xmlBook;
                counter++;
            }
            coll.addNewBooks().setBookArray(bookArray);
        }else{
            coll.addNewBooks();
        }

        // encode the string as UTF-8
        String xml = null;
        try {
            xml = new String(xmlCollectionDocument.toString().getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ExistCollectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        } 
        String documentName = collection.getId() + ".xml"; 

        // get a connector to the Exist-DB
        ExistConnector connector = ExistDAOFactory.getConnector();
        try {
            connector.storeXmlDocument(documentName, xml, ExistDAOFactory.METADATA_STORAGE_LOCATION, true);
        } catch (ExistConnectorException ex) {
            Logger.getLogger(ExistCollectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return collection.getId();
    }

    @Override
    public boolean deleteCollection(String collectionId) {
        boolean result = false;
        
        String query = null;
        XmlCalendar xmlCurrentTime = new XmlCalendar(Calendar.getInstance().getTime());
        Collection oldCollection = getCollection(collectionId);

        // get a connector to the Exist-DB
        ExistConnector connector = ExistDAOFactory.getConnector();

        query = ("declare default element namespace 'http://xml.netbeans.org/schema/MyBookSchema';\n" +
                 "(# exist:batch-transaction #) {\n" +
                 " update insert <deleted date=\"" + xmlCurrentTime.toString() + "\"/> following collection(\""+ ExistDAOFactory.METADATA_STORAGE_LOCATION +"\")/Collection[header/id='" + collectionId + "' and header/status!='DELETED']/header/lastModified,\n" +
                 " update value collection(\""+ ExistDAOFactory.METADATA_STORAGE_LOCATION +"\")/Collection[header/id=\"" + collectionId + "\" and header/status!='DELETED']/header/status with 'DELETED'\n" +
                 "}");
        
        try {
            connector.executeQuery(query);
        } catch (ExistConnectorException ex) {
            Logger.getLogger(ExistCollectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        ExistBookDAO bookDAO = new ExistBookDAO();
        List<Book> bookList = oldCollection.getBookList();

        if(bookList!=null){
            for(Book book : bookList) {
                String bookId = book.getBookId();
                System.out.println("BookID= "+ bookId);
                // delete the records from the collection
                bookDAO.deleteBook(bookId, collectionId);
            }

            result = true;
        }        
        
        return result;
    }

    @Override
    public Collection getCollection(String collectionId) {

        ExistConnector connector = ExistDAOFactory.getConnector();

        String query = ("declare default element namespace 'http://xml.netbeans.org/schema/MyBookSchema';\n" +
                        "for $c in collection(\""+ ExistDAOFactory.METADATA_STORAGE_LOCATION + "\")/Collection[header/id='" + collectionId + "' and header/status!='DELETED']\n" +
                        "return $c");                                                                     
      //  System.out.println("getCollection"+query);
        String result = null;
        try {
            result = connector.executeQuery(query);
           // System.out.println(result);
        } catch (ExistConnectorException ex) {
            Logger.getLogger(ExistCollectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Bind the Resulted Document...
        Collection collection = new Collection();
        
        CollectionDocument colDoc = CollectionDocument.Factory.newInstance();
        try {
            colDoc = CollectionDocument.Factory.parse(result);
            //Collection header
            collection.setId(colDoc.getCollection().getHeader().getId());
            collection.setLastModifiedOn(colDoc.getCollection().getHeader().getLastModified().getDate());
            collection.setCreatedOn(colDoc.getCollection().getHeader().getCreated().getDate());
            //Collection metadata
            org.netbeans.xml.schema.myBookSchema.LangString[] xmlTitles = colDoc.getCollection().getMetadata().getTitleArray();
            for(int i=0; i<xmlTitles.length; i++){
                collection.addTitle(xmlTitles[i].getStringValue(), xmlTitles[i].getLang());
            }
            //Collection books
            org.netbeans.xml.schema.myBookSchema.Book[] xmlBooks = colDoc.getCollection().getBooks().getBookArray();
            for(int i=0; i<xmlBooks.length; i++){
                //Book header
                Book book = new Book();
                System.out.println("In GetCollection id= " + xmlBooks[i].getHeader().getId());
                book.setBookId(xmlBooks[i].getHeader().getId());
                book.setCollectionId(colDoc.getCollection().getHeader().getId());
                book.setCreatedOn(xmlBooks[i].getHeader().getCreated().getDate());
                book.setLastModifiedOn(xmlBooks[i].getHeader().getLastModified().getDate());
                //Book metadata
                org.netbeans.xml.schema.myBookSchema.LangString[] xmlBookTitles = xmlBooks[i].getMetadata().getTitleArray();
                for(int j=0; j<xmlBookTitles.length; j++){
                    book.addTitle(xmlBookTitles[j].getStringValue(), xmlBookTitles[j].getLang());
                }
                
                org.netbeans.xml.schema.myBookSchema.LangString[] xmlBookDescription = xmlBooks[i].getMetadata().getDescriptionArray();
                for(int j=0; j<xmlBookDescription.length; j++){
                   book.addDescription(xmlBookDescription[j].getStringValue(), xmlBookDescription[j].getLang());
                }
                
                String[] xmlBookComments = xmlBooks[i].getMetadata().getCommentsArray();
                for(int j=0; j<xmlBookComments.length; j++){
                    book.addComment(xmlBookComments[j]);
                }  
                
                String[] xmlCreator = xmlBooks[i].getMetadata().getCreatorArray();
                for(int j=0; j<xmlCreator.length; j++){
                    book.addCreator(xmlCreator[j]);
                }
                String[] xmlEditor = xmlBooks[i].getMetadata().getEditorArray();
                for(int j=0; j<xmlEditor.length; j++){
                    book.addEditor(xmlEditor[j]);
                }
                String[] xmlPlace = xmlBooks[i].getMetadata().getPublicationPlaceArray();
                for(int j=0; j<xmlPlace.length; j++){
                    book.addPublicationPlace(xmlPlace[j]);
                }
                
                String[] xmlPublisher = xmlBooks[i].getMetadata().getPublisherArray();
                for(int j=0; j<xmlPublisher.length; j++){
                    book.addPublisher(xmlPublisher[j]);
                }
                String[] xmlType = xmlBooks[i].getMetadata().getTypeArray();
                for(int j=0; j<xmlType.length; j++){
                    book.addType(xmlType[j]);
                }
                book.setCopyrightDate(xmlBooks[i].getMetadata().getCopyrightDate());
                book.setFormat(xmlBooks[i].getMetadata().getFormat());
                book.setIsbn(xmlBooks[i].getMetadata().getIsbn());
                if(xmlBooks[i].getMetadata().getNoOfPages()!=null) 
                    book.setNoOfPages(xmlBooks[i].getMetadata().getNoOfPages().intValue());
                if(xmlBooks[i].getMetadata().getObjectUri()!=null)
                    book.setUri(xmlBooks[i].getMetadata().getObjectUri().getStringValue());
                book.setPublicationDate(xmlBooks[i].getMetadata().getPublicationDate());
                book.setReprintingDate(xmlBooks[i].getMetadata().getReprintingDate());
                book.setRights(xmlBooks[i].getMetadata().getRights());
                collection.addBook(book);
            }
            
        } catch (XmlException ex) {
            //handle exception    
            ex.printStackTrace();
        } 

        return collection;
    }

    @Override
    public String getCollectionInDCFormat(String collectionId) {
        
        ExistConnector connector = ExistDAOFactory.getConnector();

        String query = ("declare default element namespace 'http://xml.netbeans.org/schema/MyBookSchema';\n" +
                        "for $c in collection(\""+ ExistDAOFactory.METADATA_STORAGE_LOCATION + "\")/Collection[header/id='" + collectionId + "' and header/status!='DELETED']\n" +
                        "return $c");                                                                     
      
        String result = null;
        try {
            result = connector.executeQuery(query);
        } catch (ExistConnectorException ex) {
            Logger.getLogger(ExistCollectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println(result);
        
         try {
            StringReader reader = new StringReader(result);
            StringWriter writer = new StringWriter();
            
            // create a transform factory instance
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            // create a transformer for the stylesheet
            Transformer transformer = transformerFactory.newTransformer(new StreamSource(new File("src/java/schemas/collection2dc.xsl")));
                        
            
            // transform the source XML
            transformer.transform(new javax.xml.transform.stream.StreamSource(reader), new javax.xml.transform.stream.StreamResult(writer));
                        
            String tresult = writer.toString();
            System.out.println(tresult);
            
            reader = new StringReader(tresult);
            writer = new StringWriter();
            transformer = transformerFactory.newTransformer(new StreamSource(new File("src/java/schemas/pretty-print.xsl")));
            
            // transform the source XML
            transformer.transform(new javax.xml.transform.stream.StreamSource(reader), new javax.xml.transform.stream.StreamResult(writer));
            tresult = writer.toString();
            System.out.println(tresult);
            
        } catch(TransformerException ex) {
            System.out.println("The XML data failed to be transformed.");            
        }
        
        return null;
    }

    @Override
    public List<Collection> getCollectionList() {
        List<Collection> collectionList = new ArrayList<Collection>();

        // get a connector to the Exist-DB
        ExistConnector connector = ExistDAOFactory.getConnector();
        ExistBookDAO bookDao = new ExistBookDAO();

        String query = ("declare default element namespace 'http://xml.netbeans.org/schema/MyBookListSchema';\n" +
                     " declare namespace myb='http://xml.netbeans.org/schema/MyBookSchema';\n" +
                     "declare function myb:change-element-ns ($elements as element()*, $newns as xs:string, $prefix as xs:string) as element()? {\n" +
                                                    "    for $element in $elements \n" +
                                                    "    return \n" +
                                                    "        element {QName ($newns, fn:concat($prefix, if ($prefix = '') then '' else ':', fn:local-name($element)))}\n" +
                                                    "                {$element/@*, $element/node()}\n" +
                                                    "};"+
                     "    <CollectionList>\n" +
                     "        {for $c in collection('" + ExistDAOFactory.METADATA_STORAGE_LOCATION + "')/myb:Collection[myb:header/myb:status!='DELETED']\n" +
                     "        return \n" +
                     "            <Collection>\n" +
                     "              <header>\n   "+
                     "                {for $h in $c/myb:header/myb:* return myb:change-element-ns($h, 'http://xml.netbeans.org/schema/MyBookListSchema', '')}\n" +
                     "              </header>\n"+
                     "              <metadata>\n" +
                     "                {for $m in $c/myb:metadata/myb:* return myb:change-element-ns($m, 'http://xml.netbeans.org/schema/MyBookListSchema', '')}\n" +
                     "              </metadata>\n" +
                     "            </Collection>}\n" +
                     "    </CollectionList>");
        String queryResult = null;
        //System.out.println(query);
        try {
            queryResult = connector.executeQuery(query);
            //System.out.println(queryResult);
        } catch (ExistConnectorException ex) {
            Logger.getLogger(ExistCollectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Bind the Resulted Document...    
        org.netbeans.xml.schema.myBookListSchema.CollectionListDocument.CollectionList.Collection xmlCollection = null;

        try {
            CollectionListDocument xmlCollectionList = CollectionListDocument.Factory.parse(queryResult);
            
            for(int i=0; i<xmlCollectionList.getCollectionList().getCollectionArray().length; i++){
                Collection collection = new Collection();
                xmlCollection = xmlCollectionList.getCollectionList().getCollectionArray(i);
                collection.setId(xmlCollection.getHeader().getId());
                collection.setCreatedOn(xmlCollection.getHeader().getCreated().getDate());
                collection.setLastModifiedOn(xmlCollection.getHeader().getLastModified().getDate()); 
                
                org.netbeans.xml.schema.myBookListSchema.LangString[] xmlTitles = xmlCollection.getMetadata().getTitleArray();
                for(int j=0; j<xmlTitles.length; j++){
                    collection.addTitle(xmlTitles[j].getStringValue(), xmlTitles[j].getLang());
                }
                List<Book> bookList= bookDao.getBookList(collection.getId());
                collection.addBookList(bookList);
                collectionList.add(collection);
            }
 
        } catch (XmlException ex) {
            Logger.getLogger(ExistCollectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return collectionList;
    }

    @Override
    public void updateCollection(String collectionId, Collection collection) {
        

        org.netbeans.xml.schema.myBookSchema.CollectionDocument.Collection.Metadata xmlCollectionMetadata = org.netbeans.xml.schema.myBookSchema.CollectionDocument.Collection.Metadata.Factory.newInstance();
        
        
        org.netbeans.xml.schema.myBookSchema.LangString[] xmlLangString = new org.netbeans.xml.schema.myBookSchema.LangString[collection.getTitleList().size()];
        for(int i=0; i<collection.getTitleList().size(); i++){
            xmlLangString[i] = org.netbeans.xml.schema.myBookSchema.LangString.Factory.newInstance();
            xmlLangString[i].setStringValue(collection.getTitleList().get(i).getValue());
            xmlLangString[i].setLang(collection.getTitleList().get(i).getLang());
        }
        xmlCollectionMetadata.setTitleArray(xmlLangString);
        /*
         * Avoid xml:fragment as the root element of the elements.
         */
        XmlOptions options = new XmlOptions();
        options.setSaveSyntheticDocumentElement(new QName("http://xml.netbeans.org/schema/MyBookSchema", "metadata"));
        options.setUseDefaultNamespace();


        // encode the string as UTF-8
        String xml = null;
        try {
            xml = new String(xmlCollectionMetadata.xmlText(options).toString().getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ExistCollectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        XmlCalendar xmlCurrentTime = new XmlCalendar(Calendar.getInstance().getTime());


        // get a connector to the Exist-DB
        ExistConnector connector = ExistDAOFactory.getConnector();
        //System.out.println(xml);

        String query = ("declare default element namespace 'http://xml.netbeans.org/schema/MyBookSchema';\n" +
                 "(# exist:batch-transaction #) {\n" +
                 "    update replace collection('" + ExistDAOFactory.METADATA_STORAGE_LOCATION + "')/Collection[header/id='" + collectionId + "' and header/status!='DELETED']/header/lastModified with <lastModified date='" + xmlCurrentTime.toString() + "'/>,\n" +
                 "    update replace collection('" + ExistDAOFactory.METADATA_STORAGE_LOCATION + "')/Collection[header/id='" + collectionId + "' and header/status!='DELETED']/metadata with " + xml + "\n" +
                 "}");
        try {
            connector.executeQuery(query);
        } catch (ExistConnectorException ex) {
            Logger.getLogger(ExistCollectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void insertCollectionBooks(String collectionId, List<Book> bookList) {
        
        BookDAO bookDAO = new ExistBookDAO();
        if(bookList!=null){
            for(Book book : bookList) {
                bookDAO.insertBook(book, collectionId);
            }   
        }    
    }   
}
