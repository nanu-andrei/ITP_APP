package com.example.itp_app.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class ItpApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ItpApplication.class.getResource("/com/example/itp_app/main_views/start.fxml"));
        System.out.println(fxmlLoader.getLocation());
        Parent root = fxmlLoader.load();
        ItpAppController itpAppController = fxmlLoader.getController();
        stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root, 300, 400);
        stage.setTitle("ItpApp!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
