package edu.unm.dao;


import edu.unm.entity.Elector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

/**
 * Standard SQLite implementation of {@link ElectorDAO}
 */
public class ElectorSQLiteDAO extends AbstractSQLiteDAO implements ElectorDAO {

    /**
     * SQL query to create the elector database table if it does not yet exist.
     */
    private static final String CREATE_ELECTOR_TABLE
            = "CREATE TABLE IF NOT EXISTS electors (\n"
            + "       social_number TEXT PRIMARY KEY,\n"
            + "       name TEXT NOT NULL,\n"
            + "       date_of_birth TEXT NOT NULL\n"
            + ");";

    /**
     * SQL query to obtain all users from the {@code users} table.
     */
    private static final String ALL_ELECTORS_QUERY
            = "select /* ALL_ELECTORS_QUERY */\n"
            + "          social_number,\n"
            + "          name,\n"
            + "          date_of_birth\n"
            + "from      electors;";

    private static final String FIND_ELECTOR_BY_SOCIAL_QUERY
            = "select /* ELECTOR_BY_SOCIAL */\n"
            + "          social_number,\n"
            + "          name,\n"
            + "          date_of_birth\n"
            + "from      electors\n"
            + "where     social_number = ?";

    /**
     * SQL query to insert a new user into the {@code users} table
     */
    private static final String NEW_ELECTOR_QUERY
            = "insert into electors( /* NEW_ELECTOR */\n"
            + "            social_number,\n"
            + "            name,\n"
            + "            date_of_birth)\n"
            + "values (?, ?, ?);";

    /**
     * SQL query to update a user from the {@code users} table
     */
    private static final String UPDATE_ELECTOR_QUERY
            = "update electors SET /* UPDATE_ELECTOR */\n"
            + "             social_number = ?,\n"
            + "             name = ?\n"
            + "             date_of_birth = ?\n"
            + "where        social_number = ?;";

    /**
     * SQL query to remove a user from the {@code users} table
     */
    private static final String REMOVE_ELECTOR_QUERY
            = "delete from electors /* REMOVE_ELECTOR */\n"
            + "where       social_number = ?;";

    private static final String ELECTOR_COUNT_QUERY
            = "select count(*) as total from electors;";

    public ElectorSQLiteDAO(Connection conn) {
        super(conn);
        initialSetup();
    }

    private static Logger getLogger() {
        return Logger.getLogger(ElectorSQLiteDAO.class.getName());
    }

    /**
     * Mapper method that maps a row of the ResultSet into a {@link Elector} object.
     *
     * @param rs query result row
     * @return the mapped {@link Elector} object
     * @throws SQLException if obtaining data from ResultSet fails
     */
    private static Elector mapElector(ResultSet rs) throws SQLException {
        String name = rs.getString(1);
        String socialNumber = rs.getString(2);
        Date dob = rs.getDate(3);


        Elector elector = new Elector(name, socialNumber, dob);
        //user.setHashedPIN(hashedPIN);
        //user.setAdmin(admin);

        return elector;
    }

    @Override
    public void initialSetup() {
        int userCount = 0;
        try (Connection conn = DAOUtils.getConnection()) {
            ResultSet rs = conn.getMetaData().getTables(null, null, "electors", new String[] {"TABLE"});
            if (!rs.next()) { // table does not exist
                PreparedStatement ps = conn.prepareStatement(CREATE_ELECTOR_TABLE);
                ps.execute();
            }

            rs = conn.getMetaData().getTables(null, null, "users", new String[] {"TABLE"});
            if (rs.next()) {
                getLogger().log(INFO, "[SQLStats] Electors table successfully created.");

                PreparedStatement ps = conn.prepareStatement(ELECTOR_COUNT_QUERY);
                rs = ps.executeQuery();
                userCount = rs.getInt("total");

            }
        } catch (SQLException e) {
            getLogger().log(WARNING, "[SQLStats] User table failed to be created. {0}",
                    e.getMessage().trim());
        }

    }

    @Override
    public boolean addElector(Elector elector) throws SQLException {
        long start = System.currentTimeMillis();

        try (Connection conn = DAOUtils.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(NEW_ELECTOR_QUERY);
            ps.setString(1, elector.getSocialNumber());
            ps.setString(2, elector.getName());
            String formattedDate = elector.getDob().toString();
            //ps.setDate(3, elector.getDob());
            ps.setString(3, formattedDate);
            int result = ps.executeUpdate();

            long dur = System.currentTimeMillis() - start;
            if (result == 1) {
                getLogger().log(INFO, "[SQLStats] Successfully added user to table in {0} ms.", dur);
                return true;
            }
            getLogger().log(WARNING, "[SQLStats] Failed to add user to table in {0} ms.", dur);
        } catch (SQLException e) {
            long dur = System.currentTimeMillis() - start;
            getLogger().log(WARNING, "[SQLStats] Failed to add user to table in {0} ms. {1}",
                    new Object[]{dur, e.getMessage().trim()});
            throw e;
        }
        return false;
    }

    @Override
    public boolean updateElector(Elector elector) throws SQLException {
        long start = System.currentTimeMillis();

        try (Connection conn = DAOUtils.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(UPDATE_ELECTOR_QUERY);
            ps.setString(1, elector.getSocialNumber());
            ps.setString(2, elector.getName());
            ps.setDate(3, elector.getDob());
            int result = ps.executeUpdate();

            long dur = System.currentTimeMillis() - start;
            if (result == 1) {
                getLogger().log(INFO, "[SQLStats] Successfully updated user in {0} ms.", dur);
                return true;
            }
            getLogger().log(WARNING, "[SQLStats] Failed to update user in {0} ms.", dur);
        } catch (SQLException e) {
            long dur = System.currentTimeMillis() - start;
            getLogger().log(WARNING, "[SQLStats] Failed to update user in {0} ms. {1}",
                    new Object[]{dur, e.getMessage().trim()});
            throw e;
        }
        return false;
    }

    @Override
    public boolean removeElector(Elector elector) throws SQLException {
        long start = System.currentTimeMillis();

        try (Connection conn = DAOUtils.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(REMOVE_ELECTOR_QUERY);
            ps.setString(1, elector.getSocialNumber());
            int result = ps.executeUpdate();

            long dur = System.currentTimeMillis() - start;
            if (result == 1) {
                getLogger().log(INFO, "[SQLStats] Successfully removed user from table in {0} ms", dur);
                return true;
            }
            getLogger().log(WARNING, "[SQLStats] Failed to remove user from table in {0} ms.", dur);
        } catch (SQLException e) {
            long dur = System.currentTimeMillis() - start;
            getLogger().log(WARNING, "[SQLStats] Failed to remove user from table in {0} ms. {1}",
                    new Object[]{dur, e.getMessage().trim()});
            throw e;
        }
        return false;
    }

    @Override
    public List<Elector> listAllElectors() throws SQLException {
        long start = System.currentTimeMillis();

        try (Connection conn = DAOUtils.getConnection()) {
            List<Elector> result = DAOUtils.queryForList(conn,
                    ALL_ELECTORS_QUERY,
                    null,
                    ElectorSQLiteDAO::mapElector);

            long dur = System.currentTimeMillis() - start;
            getLogger().log(INFO, "[SQLStats] ALL_USERS {0} row(s) in {1} ms.",
                    new Object[]{result.size(), dur});

            return result;
        } catch (SQLException e) {
            long dur = System.currentTimeMillis() - start;
            getLogger().log(WARNING, "[SQLStats] ALL_ELECTORS failed({0}) in {1} ms.",
                    new Object[]{e.getMessage().trim(), dur});
            throw e;
        }
    }

    @Override
    public Optional<Elector> getElectorBySocial(String socialnumber) throws SQLException {
        long start = System.currentTimeMillis();

        try (Connection conn = DAOUtils.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(FIND_ELECTOR_BY_SOCIAL_QUERY);
            ps.setString(1, socialnumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapElector(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            long dur = System.currentTimeMillis() - start;
            getLogger().log(WARNING, "[SQLStats] ELECTOR_BY_SOCIAL failed({0}) in {1} ms.",
                    new Object[]{e.getMessage().trim(), dur});
            throw e;
        }
    }
}
