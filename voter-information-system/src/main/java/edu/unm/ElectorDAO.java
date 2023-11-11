package edu.unm;


import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * A DAO to retrieve user information from a database.
 */
public interface ElectorDAO extends BaseDAO {

    /**
     * This method attempts to create the database structure and default
     * user if the database has not been previously setup. If the database
     * is already setup, this method will not do anything.
     */
    void initialSetup();

    /**
     * This method attempts to add a new user to the database.
     *
     * @param user {@link Elector} to add to the {@code users} table.
     * @return true if user was successfully added
     * @throws SQLException if database query could not be executed
     */
    boolean addElector(Elector user) throws SQLException;

    /**
     * This method attempts to update a new user to the database.
     *
     * @param user {@link Elector} to update in the {@code users} table.
     * @return true if user was successfully updated
     * @throws SQLException if database query could not be executed
     */
    boolean updateElector(Elector user) throws SQLException;

    /**
     * This method attempts to add a remove user to the database.
     *
     * @param user {@link Elector} to remove to the {@code users} table.
     * @return true if user was successfully removed
     * @throws SQLException if database query could not be executed
     */
    boolean removeElector(Elector user) throws SQLException;

    /**
     * Method to obtain a list of all registered users of the home safe.
     * @return returns a list of {@link Elector} DTOs
     */
    List<Elector> listAllElectors() throws SQLException;

    /**
     * Method to obtain a single {@link Elector} from the {@code users} table.
     *
     * @param name primary key name of user to get
     * @return an {@link Optional} object which may or may not contain a
     *         {@link Elector} object.
     * @throws SQLException if database query could not be executed
     */
    Optional<Elector> getElectorBySocial(String name) throws SQLException;
}
