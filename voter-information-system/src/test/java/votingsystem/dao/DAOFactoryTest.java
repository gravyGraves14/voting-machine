package votingsystem.dao;

import edu.unm.Elector;
import edu.unm.*;
import votingsystem.UnitTest;
import org.junit.jupiter.api.Assertions;

/**
 * created by:
 * author: MichaelMillar
 */
public class DAOFactoryTest {

    @UnitTest
    public void testCreatNotNull() {
        Class<ElectorDAO> daoClass = ElectorDAO.class;
        ElectorDAO dao1 = DAOFactory.create(daoClass);
        ElectorDAO dao2 = DAOFactory.create(daoClass, DAOUtils.getConnection());
        ElectorDAO dao3 = DAOFactory.create(daoClass, DAOUtils::getConnection);

        Assertions.assertNotNull(dao1);
        Assertions.assertNotNull(dao2);
        Assertions.assertNotNull(dao3);
    }

    @UnitTest
    public void testCreateEquals() {
        Class<ElectorDAO> daoClass = ElectorDAO.class;
        ElectorDAO dao1 = DAOFactory.create(daoClass);
        ElectorDAO dao2 = DAOFactory.create(daoClass, DAOUtils.getConnection());
        ElectorDAO dao3 = DAOFactory.create(daoClass, DAOUtils::getConnection);

        Assertions.assertTrue(dao1 instanceof ElectorSQLiteDAO);
        Assertions.assertTrue(dao2 instanceof ElectorSQLiteDAO);
        Assertions.assertTrue(dao3 instanceof ElectorSQLiteDAO);
    }

}
