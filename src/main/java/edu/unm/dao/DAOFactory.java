package edu.unm.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Factory class that is used to instantiate all implementation specific DAO's.
 */
public abstract class DAOFactory {

    private static Logger getLogger() {
        return Logger.getLogger(DAOFactory.class.getName());
    }

    /**
     * Build and return an instance of the requested DAO, wired with the database
     * connection from the default application context.
     *
     * @param daoClass a type token specifying the kind of DAO to be created
     * @param <T>      the type of the DAO interface
     * @return         a DAO implementation of the requested interface type
     */
    public static <T extends BaseDAO> T create(Class<T> daoClass) {
        return create(daoClass, DAOUtils::getConnection);
    }

    /**
     * Build and return an instance of the requested DAO, wired with the provided
     * database connection.
     *
     * @param daoClass a type token specifying the kind of DAO to be created
     * @param conn     the database connection to use
     * @param <T>      a DAO implementation of the requested interface type
     * @return         a DAO implementation of the requested interface type
     */
    public static <T extends BaseDAO> T create(Class<T> daoClass, Connection conn) {
        return create(daoClass, () -> conn);
    }

    /**
     * Implementation for the DAO creation pattern. The database connection is
     * retrieved lazily once the implementation class and constructor has been
     * resolved.
     *
     * @param daoClass           a type token specifying the kind of DAO to be created
     * @param connectionSupplier a supplier for the database connection to use
     * @param <T>                the type of the DAO interface
     * @return         a DAO implementation of the requested interface type
     */
    public static <T extends BaseDAO> T create(
            Class<T> daoClass, Supplier<Connection> connectionSupplier) {
        try {
            Class<T> implementationClass = getImplementationClass(daoClass);

            try {
                return implementationClass
                        .getDeclaredConstructor(Connection.class)
                        .newInstance(connectionSupplier.get());
            } catch (NoSuchMethodException e) {
                getLogger().severe("No suitable constructor for class " +
                        implementationClass.getName());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                getLogger().severe("Exception while creating DAO implementation class " +
                        implementationClass.getName());
            }
        } catch (ClassNotFoundException e) {
            getLogger().severe(e.getMessage());
        }

        return null;
    }

    /**
     * Given a class token for a DAO interface class, returns a class object for
     * the respective implementation class, for example, {@link ElectorSQLiteDAO}.
     * This simply injects an {@code "SQLite"} in front of the DAO suffix that
     * all DAO interfaces should have, and finds the corresponding class on the
     * classpath using reflection.
     *
     * @param daoClass a class token specifying the DAO interface for which to
     *                 find an implementation.
     * @param <T>      the type of the DAO interface
     * @param <I>      the type of the DAO implementation class
     * @return         a suitable class implementing the requested DAO interface
     * @throws ClassNotFoundException if no suitable implementation for the
     *                                interface exists.
     */
    private static <T extends  BaseDAO, I extends T>
      Class<I> getImplementationClass(Class<T> daoClass) throws ClassNotFoundException {
        String interfaceName = daoClass.getName();

        if (!interfaceName.endsWith("DAO")) {
            throw new ClassNotFoundException("No matching implementation for " + interfaceName);
        }

        String implementationClassName = interfaceName
                .substring(0, interfaceName.length() - 3)
                .concat("SQLiteDAO");
        Class<?> implementationClass;

        try {
            implementationClass = daoClass
                    .getClassLoader()
                    .loadClass(implementationClassName);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Implementation class " +
                    implementationClassName + " does not implement interface " +
                    interfaceName);
        }

        if (!daoClass.isAssignableFrom(implementationClass)) {
            throw new ClassNotFoundException("Implementation class " +
                    implementationClassName + " does not implement interface " +
                    interfaceName);
        }

        // This unchecked cast is valid, since implementationClass is a subclass of T
        @SuppressWarnings("unchecked")
            Class<I> castImplementationClass = (Class<I>) implementationClass;

        return castImplementationClass;
    }
}
