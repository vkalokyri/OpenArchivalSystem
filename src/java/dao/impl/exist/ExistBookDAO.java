/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.impl.exist;

import dao.BookDAO;
import gr.tuc.music.xmldb.connector.ExistConnector;
import gr.tuc.music.xmldb.connector.ExistConnectorException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
import model.LangString;
import org.apache.xmlbeans.XmlCalendar;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.netbeans.xml.schema.myBookListSchema.BookListDocument;


/**
 *
 * @author suitcase
 */
public class ExistBookDAO implements BookDAO {

    @Override
    public Book insertBook(Book book, String collectionId) {
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
         
        XmlOptions options = new XmlOptions();
        options.setSaveSyntheticDocumentElement(new QName("http://xml.netbeans.org/schema/MyBookSchema", "book"));
        options.setUseDefaultNamespace();

        // encode the string as UTF-8
        String xml = null;
        try {
            xml = new String(xmlBook.xmlText(options).toString().getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ExistCollectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        String query = ("declare default element namespace 'http://xml.netbeans.org/schema/MyBookSchema';\n" +
                        "(# exist:batch-transaction #) {\n" +
                        " update insert "+ xml +" into collection('" + ExistDAOFactory.METADATA_STORAGE_LOCATION + "')/Collection[header/id='" + collectionId + "' and header/status!='DELETED']/books \n"+
                        "}");

        ExistConnector connector = ExistDAOFactory.getConnector();
        Book newBook = null;
        try {
            connector.executeQuery(query);
            newBook = getBook(xmlBookHeader.getId(), collectionId);
        } catch (ExistConnectorException ex) {
            Logger.getLogger(ExistCollectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newBook;
    }

    @Override
    public Book getBook(String bookId, String collectionId) {

            
        // get a connector to the Exist-DB
        ExistConnector connector = ExistDAOFactory.getConnector();

        String query = ("declare default element namespace 'http://xml.netbeans.org/schema/MyBookListSchema';\n" +
                     " declare namespace myb='http://xml.netbeans.org/schema/MyBookSchema';\n" +
                     " declare function myb:change-element-ns ($elements as element()*, $newns as xs:string, $prefix as xs:string) as element()? {\n" +
                                                    "    for $element in $elements \n" +
                                                    "    return \n" +
                                                    "        element {QName ($newns, fn:concat($prefix, if ($prefix = '') then '' else ':', fn:local-name($element)))}\n" +
                                                    "                {$element/@*, $element/node()}\n" +
                                                    "};"+
                     "<BookList>\n" +
                     "{for $b in collection('" + ExistDAOFactory.METADATA_STORAGE_LOCATION + "')/myb:Collection[myb:header/myb:status!='DELETED']/myb:books/myb:book[myb:header/myb:id='" + bookId + "' and myb:header/myb:status!='DELETED']\n" +
                     "        return \n" +
                     "            <Book>\n" +
                     "              <header>\n   "+
                     "                {for $h in $b/myb:header/myb:* return myb:change-element-ns($h, 'http://xml.netbeans.org/schema/MyBookListSchema', '')}\n" +
                     "              </header>\n"+
                     "              <metadata>\n" +
                     "                {for $m in $b/myb:metadata/myb:* return myb:change-element-ns($m, 'http://xml.netbeans.org/schema/MyBookListSchema', '')}\n" +
                     "              </metadata>\n" +
                     "            </Book>}\n" +
                    "</BookList>");                                                                    
       
        String queryResult = null;
        try {
            queryResult = connector.executeQuery(query);
            //System.out.println("getBookResultQuery = "+queryResult);
        } catch (ExistConnectorException ex) {
            Logger.getLogger(ExistBookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Book book = new Book();
        //Bind the Resulted Document...    
        org.netbeans.xml.schema.myBookListSchema.BookListDocument.BookList.Book xmlBook = null;

        try {
            BookListDocument xmlBookList = BookListDocument.Factory.parse(queryResult);
            //System.out.println(xmlBookList.getBookList().getBookArray().length);
            if(xmlBookList.getBookList().getBookArray().length==1){
                xmlBook = xmlBookList.getBookList().getBookArray(0);
                book.setBookId(xmlBook.getHeader().getId());
                book.setCreatedOn(xmlBook.getHeader().getCreated().getDate());
                book.setLastModifiedOn(xmlBook.getHeader().getLastModified().getDate()); 
                
                book.setIsbn(xmlBook.getMetadata().getIsbn());
                if(xmlBook.getMetadata().getObjectUri()!=null)
                    book.setUri(xmlBook.getMetadata().getObjectUri().getStringValue());
                book.setRights(xmlBook.getMetadata().getRights());
                book.setFormat(xmlBook.getMetadata().getFormat());
                book.setCopyrightDate(xmlBook.getMetadata().getCopyrightDate());
                book.setPublicationDate(xmlBook.getMetadata().getPublicationDate());
                book.setReprintingDate(xmlBook.getMetadata().getReprintingDate());
                if(xmlBook.getMetadata().getNoOfPages()!=null)
                    book.setNoOfPages(xmlBook.getMetadata().getNoOfPages().intValue());
                
                
                org.netbeans.xml.schema.myBookListSchema.LangString[] xmlTitles = xmlBook.getMetadata().getTitleArray();
                for(int j=0; j<xmlTitles.length; j++){
                    book.addTitle(xmlTitles[j].getStringValue(), xmlTitles[j].getLang());
                }
                org.netbeans.xml.schema.myBookListSchema.LangString[] xmlDescription = xmlBook.getMetadata().getDescriptionArray();
                for(int j=0; j<xmlDescription.length; j++){
                    book.addDescription(xmlDescription[j].getStringValue(), xmlDescription[j].getLang());
                }
                String[] xmlCreator = xmlBook.getMetadata().getCreatorArray();
                for(int j=0; j<xmlCreator.length; j++){
                    book.addCreator(xmlCreator[j]);
                }
                String[] xmlPublisher = xmlBook.getMetadata().getPublisherArray();
                for(int j=0; j<xmlPublisher.length; j++){
                    book.addPublisher(xmlPublisher[j]);
                }
                String[] xmlEditor = xmlBook.getMetadata().getEditorArray();
                for(int j=0; j<xmlEditor.length; j++){
                    book.addEditor(xmlEditor[j]);
                }
                String[] xmlComments = xmlBook.getMetadata().getCommentsArray();
                for(int j=0; j<xmlComments.length; j++){
                    book.addComment(xmlComments[j]);
                }
                String[] xmlPublicationPlace = xmlBook.getMetadata().getPublicationPlaceArray();
                for(int j=0; j<xmlPublicationPlace.length; j++){
                    book.addPublicationPlace(xmlPublicationPlace[j]);
                }
                String[] xmlType = xmlBook.getMetadata().getTypeArray();
                for(int j=0; j<xmlType.length; j++){
                    book.addType(xmlType[j]);
                }               
                
            }            
 
        } catch (XmlException ex) {
            Logger.getLogger(ExistCollectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return book;
    }

    @Override
    public String getBookInDCFormat(String bookId, String collectionId) {
        
       String result = null;
       ExistConnector connector = ExistDAOFactory.getConnector();

        String query = ("declare default element namespace 'http://xml.netbeans.org/schema/MyBookListSchema';\n" +
                     " declare namespace myb='http://xml.netbeans.org/schema/MyBookSchema';\n" +
                     " declare function myb:change-element-ns ($elements as element()*, $newns as xs:string, $prefix as xs:string) as element()? {\n" +
                                                    "    for $element in $elements \n" +
                                                    "    return \n" +
                                                    "        element {QName ($newns, fn:concat($prefix, if ($prefix = '') then '' else ':', fn:local-name($element)))}\n" +
                                                    "                {$element/@*, $element/node()}\n" +
                                                    "};"+
                     "\n" +
                     "for $b in collection('" + ExistDAOFactory.METADATA_STORAGE_LOCATION + "')/myb:Collection[myb:header/myb:status!='DELETED']/myb:books/myb:book[myb:header/myb:id='" + bookId + "' and myb:header/myb:status!='DELETED']\n" +
                     "        return \n" +
                     "            <Book>\n" +
                     "              <header>\n   "+
                     "                {for $h in $b/myb:header/myb:* return myb:change-element-ns($h, 'http://xml.netbeans.org/schema/MyBookListSchema', '')}\n" +
                     "              </header>\n"+
                     "              <metadata>\n" +
                     "                {for $m in $b/myb:metadata/myb:* return myb:change-element-ns($m, 'http://xml.netbeans.org/schema/MyBookListSchema', '')}\n" +
                     "              </metadata>\n" +
                     "            </Book>\n" +
                    "");                                                                    
       
        String queryResult = null;
        try {
            queryResult = connector.executeQuery(query);
        } catch (ExistConnectorException ex) {
            Logger.getLogger(ExistBookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        try {
            StringReader reader = new StringReader(queryResult);
            StringWriter writer = new StringWriter();
            
            // create a transform factory instance
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            // create a transformer for the stylesheet
            Transformer transformer = transformerFactory.newTransformer(new StreamSource(new File("/Users/suitcase/NetBeansProjects/Assignment1/src/java/schemas/book2dc.xsl")));
                        
            
            // transform the source XML
            transformer.transform(new javax.xml.transform.stream.StreamSource(reader), new javax.xml.transform.stream.StreamResult(writer));
                        
            result = writer.toString();            
            
            reader = new StringReader(result);
            writer = new StringWriter();
            transformer = transformerFactory.newTransformer(new StreamSource(new File("/Users/suitcase/NetBeansProjects/Assignment1/src/java/schemas/pretty-print.xsl")));
            
            // transform the source XML
            transformer.transform(new javax.xml.transform.stream.StreamSource(reader), new javax.xml.transform.stream.StreamResult(writer));
            result = writer.toString();
            System.out.println(result);
            
        } catch(TransformerException ex) {
            System.out.println("The XML data failed to be transformed.");            
        }
        return result;
    }
    

    @Override
    public Book updateBook(String bookId, Book book, String collectionId) {
 

        org.netbeans.xml.schema.myBookSchema.Book xmlBook = org.netbeans.xml.schema.myBookSchema.Book.Factory.newInstance();
        //org.netbeans.xml.schema.myBookSchema.HeaderInfo xmlBookHeader = xmlBook.addNewHeader();
        
       // xmlBookHeader.setId(book.getBookId());
        //xmlBookHeader.addNewCreated().setDate(book.getCreatedOn());
        //xmlBookHeader.addNewLastModified().setDate(Calendar.getInstance());
        //xmlBookHeader.setStatus("IN_USE");
        
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
         String xml =null;
         XmlOptions options = new XmlOptions();
         options.setSaveSyntheticDocumentElement(new QName("http://xml.netbeans.org/schema/MyBookSchema", "metadata"));
         options.setUseDefaultNamespace();

             //xml = new String(xmlBook.toString().getBytes(), "UTF-8");
         
        // encode the string as UTF-8
        //String xmlMetadata = null;
        try {
            xml = new String(xmlBookMetadata.xmlText(options).toString().getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ExistCollectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        XmlCalendar xmlCurrentTime = new XmlCalendar(Calendar.getInstance().getTime());
        // get a connector to the Exist-DB
        ExistConnector connector = ExistDAOFactory.getConnector(); 

        //String query = ("declare default element namespace 'http://xml.netbeans.org/schema/MyBookSchema';\n" +
          //       "(# exist:batch-transaction #) {\n" +
            //     "    update replace collection('" + ExistDAOFactory.METADATA_STORAGE_LOCATION + "')/Collection[header/id='" + collectionId + "' ]/books/book[header/id='" + bookId + "' and header/status!='DELETED']/header/lastModified with <lastModified date='" + xmlCurrentTime.toString() + "'/>,\n" +
              //   "    update replace collection('" + ExistDAOFactory.METADATA_STORAGE_LOCATION + "')/Collection[header/id='" + collectionId + "' ]/books/book[header/id='" + bookId + "' and header/status!='DELETED']/metadata with " + xmlMetadata + "\n" +
                // "}");
        System.out.println(xml);
        String query = ("declare default element namespace 'http://xml.netbeans.org/schema/MyBookSchema';\n" +
                "(# exist:batch-transaction #) {\n" +
                "   update replace collection('" + ExistDAOFactory.METADATA_STORAGE_LOCATION + "')/Collection[header/id='" + collectionId + "']/books/book[header/id='" + bookId + "' and header/status!='DELETED']/metadata with " + xml +" , \n"+
                "   update replace collection('" + ExistDAOFactory.METADATA_STORAGE_LOCATION + "')/Collection[header/id='" + collectionId + "']/books/book[header/id='" + bookId + "' and header/status!='DELETED']/header/lastModified with <lastModified date='" + xmlCurrentTime.toString() + "'/>\n" +
                "}");
        System.out.println(query);
        try {
            connector.executeQuery(query);
        } catch (ExistConnectorException ex) {
            Logger.getLogger(ExistBookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return getBook(bookId, collectionId);
            
    }


    @Override
    public boolean deleteBook(String bookId, String collectionId) {
        boolean result = false;

        XmlCalendar xmlCurrentTime = new XmlCalendar(Calendar.getInstance().getTime());

        // get a connector to the Exist-DB
        ExistConnector connector = ExistDAOFactory.getConnector();

        String query = ("declare default element namespace 'http://xml.netbeans.org/schema/MyBookSchema';\n" +
                        "(# exist:batch-transaction #) {\n" +
                        " update insert <deleted date='" + xmlCurrentTime.toString() + "'/> following collection('" + ExistDAOFactory.METADATA_STORAGE_LOCATION + "')/Collection[header/id='" + collectionId + "']/books/book[header/id='" + bookId + "' and header/status!='DELETED']/header/lastModified,\n" +
                        " update value collection('" + ExistDAOFactory.METADATA_STORAGE_LOCATION + "')/Collection[header/id='" + collectionId + "']/books/book[header/id='" + bookId + "' and header/status!='DELETED']/header/status with 'DELETED'\n" +
                        "}");
        
        /*String query2 = ("declare default element namespace 'http://xml.netbeans.org/schema/MyBookSchema';\n" +
                        " for $b in collection('" + ExistDAOFactory.METADATA_STORAGE_LOCATION + "')/Collection[header/id='" + collectionId + "']/books/book[header/id='" + bookId + "' and header/status!='DELETED']\n" +
                        " return $b");*/
                        
        try {
            connector.executeQuery(query);
        } catch (ExistConnectorException ex) {
            Logger.getLogger(ExistBookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }
    
    

    @Override
    public List<Book> getBookList(String collectionId) {
        List<Book> bookList = new ArrayList<Book>();

        // get a connector to the Exist-DB
        ExistConnector connector = ExistDAOFactory.getConnector();

        String query = ("declare default element namespace 'http://xml.netbeans.org/schema/MyBookListSchema';\n" +
                     " declare namespace myb='http://xml.netbeans.org/schema/MyBookSchema';\n" +
                     "declare function myb:change-element-ns ($elements as element()*, $newns as xs:string, $prefix as xs:string) as element()? {\n" +
                                                    "    for $element in $elements \n" +
                                                    "    return \n" +
                                                    "        element {QName ($newns, fn:concat($prefix, if ($prefix = '') then '' else ':', fn:local-name($element)))}\n" +
                                                    "                {$element/@*, $element/node()}\n" +
                                                    "};"+
                     "    <BookList>\n" +
                     "        {for $b in collection('" + ExistDAOFactory.METADATA_STORAGE_LOCATION + "')/myb:Collection[myb:header/myb:id='" + collectionId + "' and myb:header/myb:status!='DELETED']/myb:books/myb:book[myb:header/myb:status!='DELETED'] \n" +
                     "        return \n"+
                     "            <Book>\n" +
                     "              <header>\n   "+
                     "                  {for $h in $b/myb:header/myb:* return myb:change-element-ns($h, 'http://xml.netbeans.org/schema/MyBookListSchema', '')}\n" +
                     "                {for $h in $b/myb:header/myb:* return myb:change-element-ns($h, 'http://xml.netbeans.org/schema/MyBookListSchema', '')}\n" +
                     "              </header>\n"+
                     "              <metadata>\n" +
                     "                {for $m in $b/myb:metadata/myb:* return myb:change-element-ns($m, 'http://xml.netbeans.org/schema/MyBookListSchema', '')}\n" +
                     "              </metadata>\n" +
                     "            </Book>}\n" +
                     "    </BookList>");
        String queryResult = null;
        //System.out.println(query);
        try {
            queryResult = connector.executeQuery(query);
            //System.out.println(queryResult);
        } catch (ExistConnectorException ex) {
            Logger.getLogger(ExistCollectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Bind the Resulted Document...    

        try {
            BookListDocument xmlBookList = BookListDocument.Factory.parse(queryResult);
           
            for(int i=0; i<xmlBookList.getBookList().getBookArray().length; i++){
                Book book = new Book();
                org.netbeans.xml.schema.myBookListSchema.BookListDocument.BookList.Book xmlBook = xmlBookList.getBookList().getBookArray(i);
                book.setBookId(xmlBook.getHeader().getId());
                book.setCreatedOn(xmlBook.getHeader().getCreated().getDate());
                book.setLastModifiedOn(xmlBook.getHeader().getLastModified().getDate()); 
                
                book.setIsbn(xmlBook.getMetadata().getIsbn());
                if(xmlBook.getMetadata().getObjectUri()!=null)
                    book.setUri(xmlBook.getMetadata().getObjectUri().getStringValue());
                book.setRights(xmlBook.getMetadata().getRights());
                book.setFormat(xmlBook.getMetadata().getFormat());
                book.setCopyrightDate(xmlBook.getMetadata().getCopyrightDate());
                book.setPublicationDate(xmlBook.getMetadata().getPublicationDate());
                book.setReprintingDate(xmlBook.getMetadata().getReprintingDate());
                if(xmlBook.getMetadata().getNoOfPages()!=null)
                    book.setNoOfPages(xmlBook.getMetadata().getNoOfPages().intValue());
                
                
                org.netbeans.xml.schema.myBookListSchema.LangString[] xmlTitles = xmlBook.getMetadata().getTitleArray();
                for(int j=0; j<xmlTitles.length; j++){
                    book.addTitle(xmlTitles[j].getStringValue(), xmlTitles[j].getLang());
                }
                
                org.netbeans.xml.schema.myBookListSchema.LangString[] xmlDescription = xmlBook.getMetadata().getDescriptionArray();
                for(int j=0; j<xmlDescription.length; j++){
                    book.addDescription(xmlDescription[j].getStringValue(), xmlDescription[j].getLang());
                }
                String[] xmlCreator = xmlBook.getMetadata().getCreatorArray();
                for(int j=0; j<xmlCreator.length; j++){
                    book.addCreator(xmlCreator[j]);
                }
                String[] xmlPublisher = xmlBook.getMetadata().getPublisherArray();
                for(int j=0; j<xmlPublisher.length; j++){
                    book.addPublisher(xmlPublisher[j]);
                }
                String[] xmlEditor = xmlBook.getMetadata().getEditorArray();
                for(int j=0; j<xmlEditor.length; j++){
                    book.addEditor(xmlEditor[j]);
                }
                String[] xmlComments = xmlBook.getMetadata().getCommentsArray();
                for(int j=0; j<xmlComments.length; j++){
                    book.addComment(xmlComments[j]);
                }
                String[] xmlPublicationPlace = xmlBook.getMetadata().getPublicationPlaceArray();
                for(int j=0; j<xmlPublicationPlace.length; j++){
                    book.addPublicationPlace(xmlPublicationPlace[j]);
                }
                String[] xmlType = xmlBook.getMetadata().getTypeArray();
                for(int j=0; j<xmlType.length; j++){
                    book.addType(xmlType[j]);
                }
                bookList.add(book);
            }
 
        } catch (XmlException ex) {
            Logger.getLogger(ExistCollectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bookList;
    }
       
    private static String readFileAsString(String filePath)
    throws java.io.IOException{
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

    @Override
    public List<Book> searchBook(Book searchBook) {
        List<Book> bookList = new ArrayList<Book>();
        
        ExistConnector connector = ExistDAOFactory.getConnector();
        
        String query = ("declare default element namespace 'http://xml.netbeans.org/schema/MyBookListSchema';\n" +
                     " declare namespace myb='http://xml.netbeans.org/schema/MyBookSchema';\n" +
                     "declare function myb:change-element-ns ($elements as element()*, $newns as xs:string, $prefix as xs:string) as element()? {\n" +
                                                    "    for $element in $elements \n" +
                                                    "    return \n" +
                                                    "        element {QName ($newns, fn:concat($prefix, if ($prefix = '') then '' else ':', fn:local-name($element)))}\n" +
                                                    "                {$element/@*, $element/node()}\n" +
                                                    "};"+
                     "    <BookList>\n" +
                     "        {for $b in collection('" + ExistDAOFactory.METADATA_STORAGE_LOCATION + "')/myb:Collection[myb:header/myb:status!='DELETED']/myb:books/myb:book[myb:header/myb:status!='DELETED' and (myb:metadata/myb:title[contains(.,'"+searchBook.getTitleList().get(0).getValue() +"')]/text() or myb:metadata/myb:creator[contains(.,'"+searchBook.getCreatorList().get(0) +"')]/text() or myb:metadata/myb:isbn[contains(.,'"+searchBook.getIsbn() +"')]/text()  or myb:metadata/myb:publisher[contains(.,'"+searchBook.getPublisherList().get(0) +"')]/text() or myb:metadata/myb:editor[contains(.,'"+searchBook.getEditorList().get(0) +"')]/text() or myb:metadata/myb:description[contains(.,'"+searchBook.getDescriptionList().get(0).getValue() +"')]/text()  or myb:metadata/myb:publicationPlace[contains(.,'"+searchBook.getPublicationPlace().get(0) +"')]/text() or   myb:metadata/myb:rights[contains(.,'"+searchBook.getRights() +"')]/text() or myb:metadata/myb:format[contains(.,'"+searchBook.getFormat() +"')]/text() or myb:metadata/myb:comments[contains(.,'"+searchBook.getCommentsList().get(0) +"')]/text())] \n" +
                     "        return \n"+
                     "            <Book>\n" +
                     "              <header>\n   "+
                     "                  {for $h in $b/myb:header/myb:* return myb:change-element-ns($h, 'http://xml.netbeans.org/schema/MyBookListSchema', '')}\n" +                                    
                     "              </header>\n"+
                     "              <metadata>\n" +
                     "                {for $m in $b/myb:metadata/myb:* return myb:change-element-ns($m, 'http://xml.netbeans.org/schema/MyBookListSchema', '')}\n" +
                     "              </metadata>\n" +
                     "            </Book>}\n" +
                     "    </BookList>");
        
        String queryResult = null;
        System.out.println(query);
        try {
            queryResult = connector.executeQuery(query);
            //System.out.println(queryResult);
        } catch (ExistConnectorException ex) {
            Logger.getLogger(ExistCollectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println(queryResult);
        
        try {
            BookListDocument xmlBookList = BookListDocument.Factory.parse(queryResult);
           
            for(int i=0; i<xmlBookList.getBookList().getBookArray().length; i++){
                Book book = new Book();
                org.netbeans.xml.schema.myBookListSchema.BookListDocument.BookList.Book xmlBook = xmlBookList.getBookList().getBookArray(i);
                book.setBookId(xmlBook.getHeader().getId());
                book.setCreatedOn(xmlBook.getHeader().getCreated().getDate());
                book.setLastModifiedOn(xmlBook.getHeader().getLastModified().getDate()); 
                
                book.setIsbn(xmlBook.getMetadata().getIsbn());
                if(xmlBook.getMetadata().getObjectUri()!=null)
                    book.setUri(xmlBook.getMetadata().getObjectUri().getStringValue());
                book.setRights(xmlBook.getMetadata().getRights());
                book.setFormat(xmlBook.getMetadata().getFormat());
                book.setCopyrightDate(xmlBook.getMetadata().getCopyrightDate());
                book.setPublicationDate(xmlBook.getMetadata().getPublicationDate());
                book.setReprintingDate(xmlBook.getMetadata().getReprintingDate());
                if(xmlBook.getMetadata().getNoOfPages()!=null)
                    book.setNoOfPages(xmlBook.getMetadata().getNoOfPages().intValue());
                
                
                org.netbeans.xml.schema.myBookListSchema.LangString[] xmlTitles = xmlBook.getMetadata().getTitleArray();
                for(int j=0; j<xmlTitles.length; j++){
                    book.addTitle(xmlTitles[j].getStringValue(), xmlTitles[j].getLang());
                }
                
                org.netbeans.xml.schema.myBookListSchema.LangString[] xmlDescription = xmlBook.getMetadata().getDescriptionArray();
                for(int j=0; j<xmlDescription.length; j++){
                    book.addDescription(xmlDescription[j].getStringValue(), xmlDescription[j].getLang());
                }
                String[] xmlCreator = xmlBook.getMetadata().getCreatorArray();
                for(int j=0; j<xmlCreator.length; j++){
                    book.addCreator(xmlCreator[j]);
                }
                String[] xmlPublisher = xmlBook.getMetadata().getPublisherArray();
                for(int j=0; j<xmlPublisher.length; j++){
                    book.addPublisher(xmlPublisher[j]);
                }
                String[] xmlEditor = xmlBook.getMetadata().getEditorArray();
                for(int j=0; j<xmlEditor.length; j++){
                    book.addEditor(xmlEditor[j]);
                }
                String[] xmlComments = xmlBook.getMetadata().getCommentsArray();
                for(int j=0; j<xmlComments.length; j++){
                    book.addComment(xmlComments[j]);
                }
                String[] xmlPublicationPlace = xmlBook.getMetadata().getPublicationPlaceArray();
                for(int j=0; j<xmlPublicationPlace.length; j++){
                    book.addPublicationPlace(xmlPublicationPlace[j]);
                }
                String[] xmlType = xmlBook.getMetadata().getTypeArray();
                for(int j=0; j<xmlType.length; j++){
                    book.addType(xmlType[j]);
                }
                bookList.add(book);
            }
 
        } catch (XmlException ex) {
            Logger.getLogger(ExistCollectionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bookList;
    }

}
