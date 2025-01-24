package com.example.itp_app.User;

import com.example.itp_app.POJO.Request;
import com.example.itp_app.utilities.Requester;
import com.example.itp_app.utilities.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class UserController {
    @FXML
    public JFXButton inbox;
    @FXML
    public JFXButton request;
    @FXML
    public JFXButton account;
    @FXML
    public Button exit;
    @FXML
    public AnchorPane layout2;
    public Requester requester = new Requester();
    public JsonObject userData;

    public void initialize() {
        userData = SessionManager.getInstance().getResponse("userData");
    }

    @FXML
    public void onInboxPress() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("userId", userData.get("id").getAsString());

        String query = queryParams.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        requester.sendGetRequest("http://localhost:8080/users/inbox?" + query, response -> {
            System.out.println(response.statusCode());
            if (response.statusCode() == 200) {
                System.out.println(response.body());
                List<JsonObject> jsonObjects = new Gson().fromJson(response.body(), new TypeToken<List<JsonObject>>() {
                }.getType());
                List<Request> requests = new ArrayList<>();
                for(JsonObject jsonObject: jsonObjects)
                {
                    JsonObject requestInfo = jsonObject.getAsJsonObject("requestInfo");
                    JsonObject requestData = jsonObject.getAsJsonObject("requestData");

                    String name = requestInfo.has("userData") ? requestInfo.getAsJsonObject("userData").get("firstName").getAsString() : "";
                    String firmName = requestInfo.has("firm") ? requestInfo.getAsJsonObject("firm").get("firmName").getAsString() : "";
                    String vin = requestData.has("details") ? requestData.getAsJsonObject("details").get("vin").getAsString() : "";
                    String manufacturer = requestData.has("details") ? requestData.getAsJsonObject("details").get("manufacturer").getAsString() : "";
                    String model = requestData.has("details") ? requestData.getAsJsonObject("details").get("model").getAsString() : "";
                    String productionYear = requestData.has("details") ? requestData.getAsJsonObject("details").get("productionYear").getAsString() : "";
                    String fuelType = requestData.has("details") ? requestData.getAsJsonObject("details").get("fuelType").getAsString() : "";
                    String plateNumber = requestData.has("details") ? requestData.getAsJsonObject("details").get("plateNumber").getAsString() : "";
                    String color = requestData.has("details") ? requestData.getAsJsonObject("details").get("color").getAsString() : "";
                    String status = requestInfo.has("status") ? requestInfo.get("status").getAsString() : "";

                    System.out.println(name +" "+firmName+" "+vin+" "+manufacturer+" "+model+" "+plateNumber+" "+productionYear+" "+fuelType+" "+color);
                    Request request1 = new Request(name,vin,manufacturer,model,productionYear,fuelType,plateNumber,color,firmName,status);
                    if (request1 != null) {
                        requests.add(request1);
                    } else {
                        System.out.println("Failed to convert");
                    }
                }

                Platform.runLater(() -> {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/itp_app/user_views/user_inbox.fxml"));
                    System.out.println(loader.getLocation());
                    try {
                        Parent root = loader.load();
                        layout2.getChildren().clear();
                        layout2.getChildren().add(root);
                        InboxController inboxController = loader.getController();
                        inboxController.updateTableView(requests);

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
            } else {

                System.out.println("Error for now");
            }
        });

    }

    @FXML
    public void onRequestPress(ActionEvent e) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/itp_app/user_views/user_request.fxml"));
        try {
            Parent root = loader.load();
            layout2.getChildren().clear();
            layout2.getChildren().add(root);
            RequestController requestController = loader.getController();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    @FXML
    public void onAccountPress(ActionEvent e) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/itp_app/user_views/user_account.fxml"));
        try {
            Parent root = loader.load();
            layout2.getChildren().clear();
            layout2.getChildren().add(root);
            AccountController accountController = loader.getController();
            accountController.onPressAccount();



        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    public void exitAction(ActionEvent e) {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }

}