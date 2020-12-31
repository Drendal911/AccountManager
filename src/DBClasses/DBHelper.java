package DBClasses;

import java.sql.*;

public class DBHelper {
    private static final String databaseName = "account_manager";
    private static final String DB_URL = "jdbc:mysql://192.168.0.189/" + databaseName +
            "?autoReconnect=true&useSSL=false";
    private static final String username = "acctmgr";
    private static final String password = "acctmgr_password";
    private static final String driver = "com.mysql.jdbc.Driver";

    Connection dbConnect;
    Statement stmt;
    ResultSet rs;

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

    public void dbExecStmt(String s) throws SQLException {
        try {
            dbConnect = DriverManager.getConnection(DB_URL, username, password);
            System.out.println("Connection Successful!");

            stmt = dbConnect.createStatement();
            stmt.execute(s);
            dbConnect.close();
            System.out.println("Database updated.");
        }catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    //DB call used with SELECT statements
    public ResultSet dbGetResSet(String s) throws SQLException {
        dbConnect = null;
        try {
            dbConnect = DriverManager.getConnection("jdbc:mysql://192.168.0.189/test?user=acctmgr_guest&password=" +
                    "acctmgr_password");
            stmt = dbConnect.createStatement();
            stmt.executeQuery(s);
            rs = stmt.getResultSet();
            dbConnect.close();

        }catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            dbConnect.rollback();
            dbConnect.close();
        }
        return rs;
    }
}
