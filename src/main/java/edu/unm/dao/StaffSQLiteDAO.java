package edu.unm.dao;


import edu.unm.entity.Staff;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

/**
 * Standard SQLite implementation of {@link StaffDAO}
 */
public class StaffSQLiteDAO extends AbstractSQLiteDAO implements StaffDAO {

    /**
     * SQL query to create the staff database table if it does not yet exist.
     */
    private static final String CREATE_STAFF_TABLE
            = "CREATE TABLE IF NOT EXISTS staff (\n"
            + "       ID TEXT PRIMARY KEY,\n"
            + "       FirstName TEXT NOT NULL,\n"
            + "       LastName TEXT NOT NULL,\n"
            + "       IsAdmin INTEGER NOT NULL,\n"
            + "       Password TEXT NOT NULL\n"
            + ");";

    /**
     * SQL query to obtain all users from the {@code users} table.
     */
    private static final String ALL_STAFF_QUERY
            = "select /* ALL_STAFF_QUERY */\n"
            + "          ID,\n"
            + "          firstName,\n"
            + "          lastName,\n"
            + "          isAdmin,\n"
            + "          password\n"
            + "from      staff;";

    private static final String FIND_STAFF_BY_ID_QUERY
            = "select /* STAFF_BY_ID */\n"
            + "          ID,\n"
            + "          firstName,\n"
            + "          lastName,\n"
            + "          isAdmin,\n"
            + "          password\n"
            + "from      staff\n"
            + "where     ID = ?";

    /**
     * SQL query to insert a new user into the {@code users} table
     */
    private static final String NEW_STAFF_QUERY
            = "insert into staff( /* NEW_STAFF */\n"
            + "            ID,\n"
            + "            firstName,\n"
            + "            lastName\n"
            + "            isAdmin,\n"
            + "            password)\n"
            + "values (?, ?, ?, ?, ?);";

    /**
     * SQL query to update a user from the {@code users} table
     */
    private static final String UPDATE_STAFF_QUERY
            = "update staff SET /* UPDATE_STAFF */\n"
            + "             ID = ?,\n"
            + "            firstName,\n"
            + "            lastName,\n"
            + "            isAdmin,\n"
            + "            password)\n"
            + "where        ID = ?;";

    /**
     * SQL query to remove a user from the {@code users} table
     */
    private static final String REMOVE_STAFF_QUERY
            = "delete from staff /* REMOVE_STAFF */\n"
            + "where       ID = ?;";

    private static final String STAFF_COUNT_QUERY
            = "select count(*) as total from staff;";

    public StaffSQLiteDAO(Connection conn) {
        super(conn);
        initialSetup();
    }

    private static Logger getLogger() {
        return Logger.getLogger(StaffSQLiteDAO.class.getName());
    }

    /**
     * Mapper method that maps a row of the ResultSet into a {@link Staff} object.
     *
     * @param rs query result row
     * @return the mapped {@link Staff} object
     * @throws SQLException if obtaining data from ResultSet fails
     */
    private static Staff mapStaff(ResultSet rs) throws SQLException {
        String firstName = rs.getString(1);
        String lastName = rs.getString(2);
        String id = rs.getString(3);
        int isAdmin = rs.getInt(4);
        String password = rs.getString(5);
        boolean admin = isAdmin != 0;
        Staff staff = new Staff(firstName, lastName, id, admin, password);

        return staff;
    }

    @Override
    public void initialSetup() {
        int userCount = 0;
        try (Connection conn = DAOUtils.getConnection()) {
            ResultSet rs = conn.getMetaData().getTables(null, null, "staff", new String[] {"TABLE"});
            if (!rs.next()) { // table does not exist
                PreparedStatement ps = conn.prepareStatement(CREATE_STAFF_TABLE);
                ps.execute();
            }

            rs = conn.getMetaData().getTables(null, null, "users", new String[] {"TABLE"});
            if (rs.next()) {
                getLogger().log(INFO, "[SQLStats] Staff table successfully created.");

                PreparedStatement ps = conn.prepareStatement(STAFF_COUNT_QUERY);
                rs = ps.executeQuery();
                userCount = rs.getInt("total");

            }

        } catch (SQLException e) {
            getLogger().log(WARNING, "[SQLStats] User table failed to be created. {0}",
                    e.getMessage().trim());
        }

    }

    @Override
    public boolean addStaff(Staff staff) throws SQLException {
        long start = System.currentTimeMillis();

        try (Connection conn = DAOUtils.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(NEW_STAFF_QUERY);
            ps.setString(1, staff.getId());
            ps.setString(2, staff.getFirstName());
            ps.setString(3, staff.getLastName());
            ps.setInt(4, staff.isAdmin() ? 1 : 0);
            ps.setString(5, staff.getPassword());
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
    public boolean updateStaff(Staff staff) throws SQLException {
        long start = System.currentTimeMillis();

        try (Connection conn = DAOUtils.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(UPDATE_STAFF_QUERY);
            ps.setString(1, staff.getId());
            ps.setString(2, staff.getFirstName());
            ps.setString(3, staff.getLastName());
            ps.setInt(4, staff.isAdmin() ? 1 : 0);
            ps.setString(5, staff.getPassword());
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
    public boolean removeStaff(Staff staff) throws SQLException {
        long start = System.currentTimeMillis();

        try (Connection conn = DAOUtils.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(REMOVE_STAFF_QUERY);
            ps.setString(1, staff.getId());
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
    public List<Staff> listAllStaff() throws SQLException {
        long start = System.currentTimeMillis();

        try (Connection conn = DAOUtils.getConnection()) {
            List<Staff> result = DAOUtils.queryForList(conn,
                    ALL_STAFF_QUERY,
                    null,
                    StaffSQLiteDAO::mapStaff);

            long dur = System.currentTimeMillis() - start;
            getLogger().log(INFO, "[SQLStats] ALL_USERS {0} row(s) in {1} ms.",
                    new Object[]{result.size(), dur});

            return result;
        } catch (SQLException e) {
            long dur = System.currentTimeMillis() - start;
            getLogger().log(WARNING, "[SQLStats] ALL_STAFF failed({0}) in {1} ms.",
                    new Object[]{e.getMessage().trim(), dur});
            throw e;
        }
    }

    @Override
    public Optional<Staff> getStaffById(String id) throws SQLException {
        long start = System.currentTimeMillis();

        try (Connection conn = DAOUtils.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(FIND_STAFF_BY_ID_QUERY);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapStaff(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            long dur = System.currentTimeMillis() - start;
            getLogger().log(WARNING, "[SQLStats] STAFF_BY_ID failed({0}) in {1} ms.",
                    new Object[]{e.getMessage().trim(), dur});
            throw e;
        }
    }
}
