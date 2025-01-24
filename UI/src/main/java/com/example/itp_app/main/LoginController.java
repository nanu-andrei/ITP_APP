package com.example.itp_app.main;
import com.example.itp_app.User.UserController;
import com.example.itp_app.utilities.Requester;
import com.example.itp_app.utilities.SessionManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class LoginController {

    public Stage UserStage;
    public UserController userController;

    @FXML
    public JFXButton log;
    @FXML
    public TextField nameField;
    @FXML
    public TextField passwordField;
    @FXML
    public Button exitButton;
    @FXML
    public Label testLabel;
    @FXML
    public Button backButton;
    private Requester requester = new Requester();
    @FXML
    public void onLogAction() {
        requester.sendPostRequest("URL/users/login", serializeUser(), this::handleResponse);
    }

    private void handleResponse(HttpResponse<String> responseBody) {
        try {
            System.out.println(responseBody.body());
            if (responseBody.statusCode() == 200) {
                SessionManager.getInstance().saveResponse("userData", responseBody.body());
                Platform.runLater(this::openUserInterface);
            } else {
                Platform.runLater(() -> testLabel.setText("Credentials Invalid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String serializeUser() {
        var objectMapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("name", nameField.getText());
        data.put("password", passwordField.getText());

        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }
    public void exitButtonAction()
    {
       Stage stage = (Stage)exitButton.getScene().getWindow();
       stage.close();

    }

    private void openUserInterface() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/itp_app/user_views/user_ui.fxml"));
            Parent root = fxmlLoader.load();
            Stage userStage = new Stage();
            userStage.setScene(new Scene(root, 600, 400));
            userStage.initStyle(StageStyle.UNDECORATED);
            userStage.show();
            Stage currentStage = (Stage) log.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void onPressBack() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ItpApplication.class.getResource("/com/example/itp_app/main_views/start.fxml"));

            Parent root = fxmlLoader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Previous Scene");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
