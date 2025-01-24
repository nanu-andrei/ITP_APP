package com.example.itp_app.utilities;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class FXMLoader {
    public static Node loadFxml(String mode, String fxml) throws IOException {

        String path = null;
        if(Objects.equals(mode, "main")) // transform to enum
        {
            path = "/com/example/itp_app/main_views/" + fxml;
        } else if (Objects.equals(mode, "user")) {
             path = "/com/example/itp_app/user_views/"+ fxml;
        } else if (Objects.equals(mode,"employee")) {
            path = "/com/example/itp_app/employee_views/"+ fxml;
        }

        URL url = FXMLoader.class.getResource(path);

        if (url == null) {
            throw new FileNotFoundException("FXML file not found: " + path);
        }

        FXMLLoader loader = new FXMLLoader(url);
        System.out.println(loader.getLocation());
        return loader.load();
    }
}


