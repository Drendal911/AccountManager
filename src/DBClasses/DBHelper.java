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
    private ResultSet rs;

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

    //This class will handle different query types
    public void makeQuery (String q) throws SQLException {
        try {
            dbConnect = DriverManager.getConnection(DB_URL, username, password);
            Statement stmt = dbConnect.createStatement();
            String type = q.toLowerCase().substring(0,6);

            switch (type) {
                case ("select") -> rs = stmt.executeQuery(q);
                case ("delete") -> stmt.executeUpdate(q);
                case ("update") -> stmt.executeUpdate(q);
                case ("insert") -> stmt.executeUpdate(q);
            }
        }
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public ResultSet getResult() {
        return rs;
    }

    public Integer numOfColumns(String table) throws SQLException {
        dbConnect = DriverManager.getConnection(DB_URL, username, password);
        Statement stmt = dbConnect.createStatement();
        //Code to get the number of columns in a table
        rs = stmt.executeQuery("SELECT * FROM " + table + " where 1=2;");
        ResultSetMetaData rsmd = rs.getMetaData();
        rs.close();
        stmt.close();
        dbConnect.close();
        return rsmd.getColumnCount();
    }

}


