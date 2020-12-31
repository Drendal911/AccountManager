package sample;

import DBClasses.DBHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Account Manager");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException {
        launch(args);
        DBHelper db = new DBHelper();
        db.loadDriver();
        //db.dbExecStmt("create table users (UserID int not null, UserName varchar(30) not null, Password varchar(30) not null, primary key (UserID))");
    }
}
