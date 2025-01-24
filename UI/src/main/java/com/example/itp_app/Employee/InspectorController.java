package com.example.itp_app.Employee;

import com.example.itp_app.POJO.Request;
import com.example.itp_app.utilities.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class InspectorController {
    @FXML
    public JFXButton viewRequests;
    @FXML
    public JFXButton completeContract;
    @FXML
    public Button exit;
    @FXML
    public AnchorPane inspector;

    public Requester requester = new Requester();
    public JsonObject employeeData;
    private ObservableList<Request> requests = FXCollections.observableArrayList();
    private static final Logger logger = Logger.getLogger(InspectorController.class.getName());

    @FXML
    public void exitAction(ActionEvent e) {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }

    public void initialize() {
        employeeData = SessionManager.getInstance().getResponse("employeeData");
    }

    @FXML
    public void onCompleteContractAction() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/itp_app/employee_views/complete_contract.fxml"));

        try {
            Parent root = loader.load();
            inspector.getChildren().clear();
            inspector.getChildren().add(root);
            ContractController contractController = loader.getController();
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("username", employeeData.get("username").getAsString());

            String query = queryParams.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));
            requester.sendGetRequest("URL/employees/inspector/viewAssignedContracts?" + query, response -> {
                if (response.statusCode() == 200) {
                    System.out.println(response.body());
                    List<JsonObject> jsonObjects = getGson().fromJson(response.body(), new TypeToken<List<JsonObject>>() {}.getType());

                    for (JsonObject jsonObject : jsonObjects) {
                        JsonObject requestInfo = jsonObject.getAsJsonObject("requestInfo");
                        JsonObject requestData = jsonObject.getAsJsonObject("requestData");

                        logger.info("Deserializing Request:");
                        logger.info("Request Info: " + requestInfo);
                        logger.info("Request Data: " + requestData);

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
                        Request request1 = new Request(name,vin,manufacturer,model,productionYear,fuelType,plateNumber,color,firmName,status);
                        if (request1 != null) {
                            requests.add(request1);
                        } else {
                            System.out.println("Failed to convert");
                        }
                    }

                    Platform.runLater(() -> {
                        if (!requests.isEmpty()) {
                            SessionManager.getInstance().saveObject("currentContract", requests.get(0));
                            Request currentContract = requests.get(0);
                            System.out.println("Current Contract: " + (currentContract == null));

                            if (currentContract != null) {
                                contractController.setContractDetails(currentContract);
                            }

                            if (contractController.completeButton.isPressed()) {
                                contractController.setCompleteButtonAction();
                                requests.remove(0);
                                if (!requests.isEmpty()) {
                                    SessionManager.getInstance().saveObject("currentContract", requests.get(0));
                                } else {
                                    SessionManager.getInstance().removeResponse("currentContract");
                                }
                            }

                        } else {
                            System.out.println("No requests to process.");
                        }
                    });
                }
            });

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(SimpleStringProperty.class, new SimpleStringPropertyAdapter())
                .create();
    }

    private Request convertJsonToRequest(JsonObject jsonObject) {
        return getGson().fromJson(jsonObject, Request.class);
    }

    @FXML
    public void onViewAssignedRequestsAction() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("username", employeeData.get("username").getAsString());

        String query = queryParams.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        requester.sendGetRequest("URL/employees/inspector/viewAssignedContracts?" + query, response -> {
            if (response.statusCode() == 200) {
                System.out.println(response.body());
                List<JsonObject> jsonObjects = getGson().fromJson(response.body(), new TypeToken<List<JsonObject>>() {
                }.getType());

                for (JsonObject jsonObject : jsonObjects) {
                    JsonObject requestInfo = jsonObject.getAsJsonObject("requestInfo");
                    JsonObject requestData = jsonObject.getAsJsonObject("requestData");

                    logger.info("Deserializing Request:");
                    logger.info("Request Info: " + requestInfo);
                    logger.info("Request Data: " + requestData);

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
                    Request request1 = new Request(name, vin, manufacturer, model, productionYear, fuelType, plateNumber, color, firmName, status);
                    if (request1 != null) {
                        requests.add(request1);
                    } else {
                        System.out.println("Failed to convert");
                    }
                }


                Platform.runLater(() -> {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/itp_app/employee_views/view_requests.fxml"));
                    System.out.println(loader.getLocation());
                    try {
                        Parent root = loader.load();
                        inspector.getChildren().clear();
                        inspector.getChildren().add(root);
                        ViewAdminController viewAdminController = loader.getController();
                        viewAdminController.updateTableView(requests);

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
            } else {

                System.out.println("Error for now");
            }
        });
    }

    public void  loadInterface(String fxml) {
        try {
            Node content = FXMLoader.loadFxml("employee", fxml);
            System.out.println(content);
            inspector.getChildren().clear();
            inspector.getChildren().add(content);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
