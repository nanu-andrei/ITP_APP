package com.example.itp_app.Employee;

import com.example.itp_app.POJO.Request;
import com.example.itp_app.User.InboxController;
import com.example.itp_app.utilities.FXMLoader;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminController {
    @FXML
    public JFXButton addInspector;
    @FXML
    public JFXButton viewRequests;
    @FXML
    public JFXButton assignContracts;
    @FXML
    public Button exit;
    @FXML
    public AnchorPane admin;

    public JsonObject employeeData;

    public Requester requester = new Requester();
    public void initialize() {
        employeeData = SessionManager.getInstance().getResponse("employeeData");
    }

    @FXML
    public void exitAction(ActionEvent e)
    {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void setAssignContractsAction()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/itp_app/employee_views/assign_ui.fxml"));

        try{
            Parent root = loader.load();
            admin.getChildren().clear();
            admin.getChildren().add(root);
            AssignController assignController = loader.getController();


        }catch (IOException ex){ex.printStackTrace();}
    }
    @FXML
    public void onViewRequestsAction()
    {
        JsonObject firm = employeeData.getAsJsonObject("firm");
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("firmName", firm.get("firmName").getAsString());
        String query = queryParams.entrySet().stream().
                map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        System.out.println(query);
        requester.sendGetRequest("URL/employees/admin/viewRequests?" + query, response -> {
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
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/itp_app/employee_views/view_requests.fxml"));
                    System.out.println(loader.getLocation());
                    try {
                        Parent root = loader.load();
                        admin.getChildren().clear();
                        admin.getChildren().add(root);
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
    @FXML
    public void onAddInspectorAction()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/itp_app/employee_views/add_remove_ui.fxml"));

        try{
            Parent root = loader.load();
            admin.getChildren().clear();
            admin.getChildren().add(root);
            AddController addController = loader.getController();


        }catch (IOException ex){ex.printStackTrace();}
    }


}
