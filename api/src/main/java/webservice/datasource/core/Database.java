package webservice.datasource.core;

import webservice.datasource.util.DatabaseProperties;
import webservice.datasource.util.SqlLoader;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private DatabaseProperties dbProperties;
    private SqlLoader sqlLoader;
    private static Connection connection;
    private Logger log = Logger.getLogger(getClass().getName());
    private PreparedStatement statement;

    public Database(String resource) {
        this.sqlLoader = new SqlLoader(resource);
        this.dbProperties = new DatabaseProperties();
        getConnection();
    }

    public Database() {

    }

    public Database query(String sql, String[] parameters) {
        try {
            PreparedStatement statement = connection.prepareStatement(sqlLoader.get(sql));

            if (parameters != null) {
                for (int i = 1; i <= parameters.length; i++) {
                    statement.setString(i, parameters[i - 1]);
                }
            }

            this.statement = statement;

        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        return this;
    }

    public ResultSet executeQuery() {
        try {
            return statement.executeQuery();
        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        return null;
    }

    public ResultSet execute() {
        ResultSet result = null;
        try {
            if (statement.execute()) {
                result = statement.getResultSet();
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        return result;
    }

    public int executeUpdate() {
        try {
            return statement.executeUpdate();
        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        return 0;
    }


    private void getConnection() {
        try {
            Class.forName(dbProperties.getDriver());

            if (connection == null) {
                connection = DriverManager.getConnection(dbProperties.getConnectionString());
            }

        } catch (SQLException | ClassNotFoundException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }


//    private void closeConnection() {
//        try {
//            connection.close();
//        } catch (SQLException e) {
//            log.log(Level.SEVERE, e.getMessage());
//        }
//    }


    public void setDbProperties(DatabaseProperties dbProperties) {
        this.dbProperties = dbProperties;
    }

    public static void setConnection(Connection connection) {
        Database.connection = connection;
    }

    public void setSqlLoader(SqlLoader sqlLoader) {
        this.sqlLoader = sqlLoader;
    }

    public void setLog(Logger log) {
        this.log = log;
    }
}
