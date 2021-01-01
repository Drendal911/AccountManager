package sample;

import DBClasses.DBHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Account Manager");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
        DBHelper db = new DBHelper();
        HashMap<String, String> hashMap = new HashMap<>();
        db.loadDriver();


        try {
            db.makeQuery("select * from users;");
            ResultSet resultSet = db.getResult();
            resultSet.first();

            hashMap.put(resultSet.getString("UserName"), resultSet.getString("Password"));
            resultSet.close();

            System.out.println(hashMap);

        }catch (SQLException | NullPointerException e) {
            //System.out.println("NullPointerException");
        }


    }
}
