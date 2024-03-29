package DBClasses;

import java.sql.*;

public class DBHelper {
    private static final String databaseName = "account_manager";
    private static final String DB_URL = "jdbc:mysql://192.168.0.189/" + databaseName +
            "?autoReconnect=true&useSSL=false";
    private static final String username = "acctmgr";
    private static final String password = "acctmgr_password";
    Connection dbConnect;

    public DBHelper() {
    }


    /* METHODS *********************************************************************************************************
    ********************************************************************************************************************
    *******************************************************************************************************************/

/*
    public void loadDriver() {
        try {
            //load the jdbc driver
            Class.forName(driver);
        }catch (Exception ex) {
            System.out.println("Something went wrong. JDBC Driver not loaded.");
        }
    }

 */

    //Connects to the database
    public Connection makeConnection() throws ClassNotFoundException, SQLException, Exception {
        dbConnect = DriverManager.getConnection(DB_URL, username, password);
        return dbConnect;
    }

    public Integer numOfColumns(String table) throws SQLException {
        dbConnect = DriverManager.getConnection(DB_URL, username, password);
        Statement stmt = dbConnect.createStatement();
        //Code to get the number of columns in a table
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM " + table + " where 1=2;");
        ResultSetMetaData rsmd = resultSet.getMetaData();
        resultSet.close();
        stmt.close();
        dbConnect.close();
        return rsmd.getColumnCount();
    }
/*
    public Integer numOfRows(String table) throws SQLException {
        dbConnect = DriverManager.getConnection(DB_URL, username, password);
        Statement stmt = dbConnect.createStatement();
        //Code to get the number of columns in a table
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM " + table);
        resultSet.last();
        int size = resultSet.getRow();
        resultSet.beforeFirst();
        resultSet.close();
        stmt.close();
        dbConnect.close();
        return size;
    }

 */

}


