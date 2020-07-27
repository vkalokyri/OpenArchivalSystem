/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.Book;
import model.Collection;

/**
 *
 * @author suitcase
 */
public interface CollectionDAO {
    
    /**
     * Inserts a collection that contains only metadata information and no
     * books to the db.
     *
     * @param collection                A collection.

     * @return                          The stored collection
     *
     * @throws DBOperationException     If the operation failed to be executed.
     */
    public abstract Collection insertCollection(Collection collection);


    /**
     * Inserts a collection having a specific book list to the db.
     
     * @param collection                A collection.
     * @param bookList                The collection's book list.
     * 
     * @return                          The identifier of the stored collection.
     * @throws DBOperationException     If the operation failed to be executed.
     */
    public abstract String insertCollection(Collection collection, List<Book> bookList);

    /**
     * Deletes a collection from the db.
     *
     * @param collectionId              The collection's identifier.

     * @return                          True in case that the collection has
     *                                  been deleted successfully, otherwise
     *                                  false.
     * @throws DBOperationException     If the operation failed to be executed.
     */
    public abstract boolean deleteCollection(String collectionId);

    /**
     * Retrieves a collection from the db.
     *
     * @param collectionId              The collection's identifier.
     * @return                          The collection found.
     * @throws DBOperationException     If the operation failed to be executed.
     */
    public abstract Collection getCollection(String collectionId);

 

    /**
     * Retrieves a collection in DC format from the db.
     *
     * @param collectionId              The collection's identifier.
     * @return                          The DC collection.
     * @throws DBOperationException     If the operation failed to be executed.
     */
    public abstract String getCollectionInDCFormat(String collectionId);

    
    /**
     * Retrieves a list of the collections (only identifier, title and 
     * lastModified values) stored in the db.
     *
     * @return                          A list of collections.
     * @throws DBOperationException     If the operation failed to be executed.
     */
    public abstract List<Collection> getCollectionList();

    
    /**
     * Updates a collection's metadata information.
     *
     * @param collectionId              The collection's identifier.
     * @param collection                An updated collection.
     * 
     * @throws DBOperationException     If the operation failed to be executed.
     */
    public abstract void updateCollection(String collectionId, Collection collection);

    /**
     * Inserts records to a collection's record list.
     *
     * @param collectionId              The collection's identifier.
     * @param bookList                  A list of books to be inserted to the
     *                                  collection's book list.
     * 
     * @throws DBOperationException     If the operation failed to be executed.
     */
    public abstract void insertCollectionBooks(String collectionId, List<Book> bookList);
    
    
}
