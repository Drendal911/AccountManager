package DBClasses;

import java.sql.*;
import java.util.LinkedList;

public class DBHelper {
    private static final String databaseName = "account_manager";
    private static final String DB_URL = "jdbc:mysql://192.168.0.189/" + databaseName +
            "?autoReconnect=true&useSSL=false";
    private static final String username = "acctmgr";
    private static final String password = "acctmgr_password";
    private static final String driver = "com.mysql.jdbc.Driver";
    Connection dbConnect;

    public DBHelper() {
    }


    /* METHODS *********************************************************************************************************
    ********************************************************************************************************************
    *******************************************************************************************************************/


    public void loadDriver() {
        try {
            //load the jdbc driver
            Class.forName(driver);
        }catch (Exception ex) {
            System.out.println("Something went wrong. Driver not loaded.");
        }
    }

    //Connects to the database
    public Connection makeConnection() throws ClassNotFoundException, SQLException, Exception {
        dbConnect = DriverManager.getConnection(DB_URL, username, password);
        System.out.println("Connection Successful!");
        return dbConnect;
    }

    //Closes a connection to the database
    public void closeConnection() throws ClassNotFoundException, SQLException, Exception {
        dbConnect.close();
        System.out.println("Connection Closed.");
    }

    public Integer numOfColumns(String table) throws SQLException {
        //dbConnect = DriverManager.getConnection(DB_URL, username, password);
        Statement stmt = dbConnect.createStatement();
        //Code to get the number of columns in a table
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM " + table + " where 1=2;");
        ResultSetMetaData rsmd = resultSet.getMetaData();
        resultSet.close();
        stmt.close();
        dbConnect.close();
        return rsmd.getColumnCount();
    }

}


