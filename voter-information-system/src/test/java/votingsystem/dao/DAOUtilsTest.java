package votingsystem.dao;

import votingsystem.UnitTest;
import edu.unm.*;
import org.junit.jupiter.api.Assertions;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * created by:
 * author: MichaelMillar
 */
public class DAOUtilsTest {

    @UnitTest
    public void testGetConnection() {
        Connection conn = DAOUtils.getConnection();
        Assertions.assertNotNull(conn);
    }

    @UnitTest
    public void testAsSqlTimestamp() {
        LocalDateTime dt = LocalDateTime.now();
        Timestamp ts = new Timestamp(dt.getYear() - 1900,
                                     dt.getMonthValue() - 1,
                                     dt.getDayOfMonth(),
                                     dt.getHour(),
                                     dt.getMinute(),
                                     dt.getSecond(),
                                     dt.getNano());

        Assertions.assertEquals(ts, Timestamp.valueOf(dt));
    }

}
