package edu.unm.dao;


import edu.unm.entity.Elector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.text.ParseException;

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
            + "       ID TEXT PRIMARY KEY,\n"
            + "       FirstName TEXT NOT NULL,\n"
            + "       LastName TEXT NOT NULL,\n"
            + "       date_of_birth TEXT NOT NULL\n"
            + ");";

    /**
     * SQL query to obtain all users from the {@code users} table.
     */
    private static final String ALL_ELECTORS_QUERY
            = "select /* ALL_ELECTORS_QUERY */\n"
            + "          ID,\n"
            + "          firstName,\n"
            + "          lastName,\n"
            + "          date_of_birth\n"
            + "from      electors;";

    private static final String FIND_ELECTOR_BY_SOCIAL_QUERY
            = "select /* ELECTOR_BY_SOCIAL */\n"
            + "          ID,\n"
            + "          firstName,\n"
            + "          lastName,\n"
            + "          date_of_birth\n"
            + "from      electors\n"
            + "where     ID = ?";

    /**
     * SQL query to insert a new user into the {@code users} table
     */
    private static final String NEW_ELECTOR_QUERY
            = "insert into electors( /* NEW_ELECTOR */\n"
            + "            ID,\n"
            + "            firstName,\n"
            + "            lastName,\n"
            + "            date_of_birth)\n"
            + "values (?, ?, ?, ?);";

    /**
     * SQL query to update a user from the {@code users} table
     */
    private static final String UPDATE_ELECTOR_QUERY
            = "update electors SET /* UPDATE_ELECTOR */\n"
            + "             ID = ?,\n"
            + "            firstName,\n"
            + "            lastName,\n"
            + "             date_of_birth = ?\n"
            + "where        ID = ?;";

    /**
     * SQL query to remove a user from the {@code users} table
     */
    private static final String REMOVE_ELECTOR_QUERY
            = "delete from electors /* REMOVE_ELECTOR */\n"
            + "where       ID = ?;";

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
        String id = rs.getString(1);
        String firstName = rs.getString(2);
        String lastName = rs.getString(3);
        String date = rs.getString(4);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dob = null;

        try {
            java.util.Date parsedDate = dateFormat.parse(date);
            dob = new java.sql.Date(parsedDate.getTime());
        } catch (ParseException e) {
            // Handle parsing exception, e.g., log an error
            e.printStackTrace();
        }


        Elector elector = new Elector(firstName, lastName, id, dob);
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
            ps.setString(1, elector.getId());
            ps.setString(2, elector.getFirstName());
            ps.setString(3, elector.getLastName());
            String formattedDate = elector.getDob().toString();
            //ps.setDate(3, elector.getDob());
            ps.setString(4, formattedDate);
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
            ps.setString(1, elector.getId());
            ps.setString(2, elector.getFirstName());
            ps.setString(3, elector.getLastName());
            ps.setDate(4, elector.getDob());
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
            ps.setString(1, elector.getId());
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
    public Optional<Elector> getElectorBySocial(String id) throws SQLException {
        long start = System.currentTimeMillis();

        try (Connection conn = DAOUtils.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(FIND_ELECTOR_BY_SOCIAL_QUERY);
            ps.setString(1, id);
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

    public boolean isAlreadyRegistered(String id) throws SQLException {
        List<Elector> allRegisteredVoters = listAllElectors();

        for (int i = 0; i < allRegisteredVoters.size(); i++){
            Elector ele = allRegisteredVoters.get(i);
            if (ele.getId().equals(id)) return true;
        }
        return false;
    }


}
