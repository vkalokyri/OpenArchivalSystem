/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.impl.exist.ExistDAOFactory;

/**
 * The Data Access Object Factory for the db.
 *
 * @author suitcase
 */

public abstract class DAOFactory {


    /** The exist DAO type which is supported by the factory. */
    public static final int EXIST = 1;


    /**
     * Creates a Collection Data Access Object.
     *
     * @return              A Collection Data Access Object.
     */
    public abstract CollectionDAO getCollectionDAO();

    /**
     * Creates a Book Data Access Object.
     *
     * @return              A Book Data Access Object.
     */
    public abstract BookDAO getBookDAO();

    /**
     * Creates a DAO Factory.
     *
     * @param whichFactory  The type of the factory to be created.
     * @return              A DAO Factory.
     */
    public static DAOFactory getDAOFactory(int whichFactory) {
        switch(whichFactory) {
            case EXIST:
                return ExistDAOFactory.getInstance();
            default:
                return null;
        }
    }

}

