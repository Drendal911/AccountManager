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
        primaryStage.setScene(new Scene(root, 600, 400));
        root.requestFocus();
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException {
        launch(args);
        DBHelper db = new DBHelper();
        HashMap<String, String> hashMap = new HashMap<>();
        db.loadDriver();
    }
}

