/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.impl.exist;

import dao.BookDAO;
import dao.CollectionDAO;
import dao.DAOFactory;
import gr.tuc.music.xmldb.connector.ExistConnector;
import gr.tuc.music.xmldb.connector.ExistConnectorException;
import gr.tuc.music.xmldb.connector.impl.ExistRpcConnector;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author suitcase
 */
public class ExistDAOFactory extends DAOFactory {
    
   
    private static ExistConnector connector = null;

    public static String EXISTDB_LOCATION = "http://localhost:8081/exist";
    public static String EXISTDB_USERNAME = "admin";
    public static String EXISTDB_PASSWORD = "admin123#@!";
    
    public static String EXISTDB_XMLRPC_LOCATION = "http://localhost:8081/exist/xmlrpc";
    public static String EXISTDB_REST_LOCATION = "http://localhost:8081/exist/rest/";
    
    public static String METADATA_STORAGE_LOCATION = "/metadata";
    public static String CONTENT_STORAGE_LOCATION = "/content";



    /**
     * Creates an exist type DAO factory object.
     */
    private ExistDAOFactory() {
        try {
            Properties properties = new Properties();          
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("dao.properties");
            
            if(is != null) {
                String stringValue = null;
                int intValue = 0;
                
                properties.load(is);
            
                stringValue = properties.getProperty("EXISTDB_LOCATION");
                if(stringValue != null) {
                    EXISTDB_LOCATION = stringValue;
                    EXISTDB_XMLRPC_LOCATION = EXISTDB_LOCATION + "/xmlrpc";
                    EXISTDB_REST_LOCATION = EXISTDB_LOCATION + "/rest/";                    
                }
                
                stringValue = properties.getProperty("EXISTDB_USERNAME");
                if(stringValue != null) {
                    EXISTDB_USERNAME = stringValue;                   
                }
                
                stringValue = properties.getProperty("EXISTDB_PASSWORD");
                if(stringValue != null) {
                    EXISTDB_PASSWORD = stringValue;                    
                }
                
                stringValue = properties.getProperty("METADATA_STORAGE_LOCATION");
                if(stringValue != null) {
                    METADATA_STORAGE_LOCATION = stringValue;                    
                }
                
                stringValue = properties.getProperty("CONTENT_STORAGE_LOCATION");
                if(stringValue != null) {
                    CONTENT_STORAGE_LOCATION = stringValue;                    
                }               
                
            }
            
        } catch (IOException ex) {
            // use the default parameter values
        }
    }
    

    // External signature methods
    //////////////////////////////////
    
    public static ExistDAOFactory getInstance() {
        return ExistDAOFactoryHolder.INSTANCE;
    }

    /**
     * Creates an exist type CollectionDAO.
     *
     * @return          A Collection Data Access Object.
     */
    public CollectionDAO getCollectionDAO() {
        return new ExistCollectionDAO();
    }
    
    
    /**
     * Creates an exist type BookDAO.
     *
     * @return          A Book Data Access Object.
     */
    public BookDAO getBookDAO() {
        return new ExistBookDAO();
    }
    
    /**
     * ExistDAOFactoryHolder is loaded on the first execution of 
     * ExistDAOFactory.getInstance() or the first access to 
     * ExistDAOFactoryHolder.INSTANCE, not before.
     */
    private static class ExistDAOFactoryHolder { 
        public static final ExistDAOFactory INSTANCE = new ExistDAOFactory();
    }
    
    /**
     * Gets a connector in order to provide instant access to an Exist XML
     * Database instance.
     *
     * @return          The connector to the Exist XML Database instance.
     */
    public static ExistConnector getConnector() {
        if(connector == null) {
            try {
                connector = new ExistRpcConnector(EXISTDB_XMLRPC_LOCATION, EXISTDB_USERNAME, EXISTDB_PASSWORD);
            } catch (ExistConnectorException ex) {
                ex.printStackTrace();
            }
        }

        return connector;
    }
}