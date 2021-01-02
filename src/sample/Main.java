package sample;

import DBClasses.DBHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Account Manager");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException {
        launch(args);
        DBHelper db = new DBHelper();
        HashMap<String, String> hashMap = new HashMap<>();
        db.loadDriver();

        //Test code to see if I could get the results of a resultset
        String table = "users";
        db.makeQuery("SELECT column_name FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'account_manager' " +
                "AND TABLE_NAME = '" + table + "'");
        ResultSet resultSet = db.getResult();
        resultSet.first();
        int count = 1;
        while (count <= db.numOfColumns(table)) {
            System.out.println(resultSet.getString(1));
            resultSet.next();
            count++;
        }
        resultSet.close();

    }
}

