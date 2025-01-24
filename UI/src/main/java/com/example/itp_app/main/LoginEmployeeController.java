package com.example.itp_app.main;

import com.example.itp_app.Employee.AdminController;
import com.example.itp_app.Employee.InspectorController;
import com.example.itp_app.utilities.Requester;
import com.example.itp_app.utilities.SessionManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class LoginEmployeeController {
    public Stage EmployeeStage;
    private AdminController adminController;
    private InspectorController inspectorController;

    @FXML
    public JFXButton log2;
    @FXML
    public TextField nameField;
    @FXML
    public TextField walletField;

    @FXML
    public Button exitButton;
    @FXML
    public Label testLabel;
    @FXML
    public Button backButton;

    private Requester requester = new Requester();

    @FXML
    public void exitButtonAction(ActionEvent e) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void onEmployeeLogin()
    {
        requester.sendPostRequest("URL/employees/login", serializeUser(), this::handleResponse);
    }
    private void handleResponse(HttpResponse<String> responseBody) {
        try {
            System.out.println(responseBody.body());
            if (responseBody.statusCode() == 200) {
                String response = responseBody.body();
                SessionManager.getInstance().saveResponse("employeeData", response);
                Platform.runLater(() -> openEmployeeInterface(response));
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
        data.put("username", nameField.getText());
        data.put("privateKey", walletField.getText());

        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }
    private void openEmployeeInterface(String response) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            String role = rootNode.path("role").asText();

            if("ADMIN".equals(role)){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/itp_app/employee_views/admin_ui.fxml"));
                Parent root = fxmlLoader.load();
                Stage userStage = new Stage();
                userStage.setScene(new Scene(root, 600, 400));
                userStage.initStyle(StageStyle.UNDECORATED);
                userStage.show();
                Stage currentStage = (Stage) log2.getScene().getWindow();
                currentStage.close();
            }
            else
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/itp_app/employee_views/inspector_ui.fxml"));
                Parent root = fxmlLoader.load();
                Stage userStage = new Stage();
                userStage.setScene(new Scene(root, 600, 400));
                userStage.initStyle(StageStyle.UNDECORATED);
                userStage.show();
                Stage currentStage = (Stage) log2.getScene().getWindow();
                currentStage.close();
            }

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

