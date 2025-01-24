package com.example.itp_app.main;

import com.example.itp_app.Employee.InspectorController;
import com.example.itp_app.utilities.Requester;
import com.example.itp_app.utilities.SessionManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient ;
import java.io.Serializable;
import java.net.URL;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SignupController implements Initializable, Serializable {

    @FXML
    public Button sign;
    @FXML
    public TextField firstNameField;
    @FXML
    public TextField lastNameField;
    @FXML
    public TextField cnpField;
    @FXML
    public DatePicker dob;
    @FXML
    public ChoiceBox<String> gender;
    @FXML
    public TextField userIdField;
    @FXML
    public TextField passwordField;
    public String genderV;
    @FXML
    public Label error;
    @FXML
    public Button backButton;

    public Requester requester = new Requester();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gender.getItems().addAll("M", "F");
    }

    @FXML
    public void onSignupAction() {
        String uid = userIdField.getText();
        String pass = passwordField.getText();
        String fname = firstNameField.getText();
        String lname = lastNameField.getText();
        String cnp = cnpField.getText();
        String birth = String.valueOf(dob.getValue());
        genderV = gender.getValue();


        if (uid.isEmpty() || pass.isEmpty() || fname.isEmpty() || lname.isEmpty() || cnp.isEmpty() || birth.isEmpty() || genderV.isEmpty()) {
            error.setText("No field should be empty");
        } else if (!uid.matches("^[a-zA-Z0-9_]+$")) {
            error.setText("Username can only include letters, digits, and underscores.");
        } else if (!pass.matches("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$")) {
            error.setText("Password must include at least one letter and one digit.");
        } else if (!fname.matches("^[a-zA-Z]+$")) {
            error.setText("First name can only include letters.");
        } else if (!lname.matches("^[a-zA-Z]+$")) {
            error.setText("Last name can only include letters.");
        } else if (!cnp.matches("^\\d+$")) {
            error.setText("CNP must be digits only.");
        } else {
            requester.sendPostRequest("URL/users/signup", serializeUser(), this::processSignupResponse);
        }
    }

    private void processSignupResponse(HttpResponse<String> responseBody) {
        if (responseBody.statusCode() == 200) {
            SessionManager.getInstance().saveResponse("userData", responseBody.body());
            Platform.runLater(this::navigateToNextPage);
        } else {
            Platform.runLater(() -> error.setText("Try Again"));
        }
    }

    private String serializeUser() {
        var objectMapper = new ObjectMapper();
        ObjectNode signupData = objectMapper.createObjectNode();

        ObjectNode sensitiveData = objectMapper.createObjectNode();
        sensitiveData.put("username", userIdField.getText());
        sensitiveData.put("password", passwordField.getText());

        ObjectNode userData = objectMapper.createObjectNode();
        userData.put("firstName", firstNameField.getText());
        userData.put("lastName", lastNameField.getText());
        userData.put("cnp", cnpField.getText());
        userData.put("dateOfBirth", String.valueOf(dob.getValue()));
        userData.put("gender", gender.getValue());

        signupData.set("sensitiveDataDTO", sensitiveData);
        signupData.set("dataDTO", userData);

        try {
            return objectMapper.writeValueAsString(signupData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }


    private void navigateToNextPage() {
        {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(ItpApplication.class.getResource("/com/example/itp_app/user_views/user_ui.fxml"));
                Parent root = fxmlLoader.load();
                Stage userStage = new Stage();
                userStage.setScene(new Scene(root, 600, 400));
                userStage.initStyle(StageStyle.UNDECORATED);
                userStage.show();
                Stage currentStage = (Stage) sign.getScene().getWindow();
                currentStage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
