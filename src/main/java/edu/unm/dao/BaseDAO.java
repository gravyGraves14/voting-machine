package edu.unm.dao;

/**
 * Basic interface from which all other DAO interfaces are derived. Used as the
 * upper bound for instances that can be created by the common {@link DAOFactory}
 */
public interface BaseDAO {

    /**
     * Commit the transaction and log any exception.
     */
    void commit();

    /**
     * Rollback the transaction and log any exception.
     */
    void rollback();

}
