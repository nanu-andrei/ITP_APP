package com.example.itp_app.Employee;

import com.example.itp_app.utilities.Requester;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AssignController {
    @FXML
    public TextField requestId;
    @FXML
    public TextField inspector;
    @FXML
    public TextField admin;
    @FXML
    public Label RequestId;
    @FXML
    public Label Inspector;
    @FXML
    public Label Admin;
    @FXML
    public Button assign;
    private Requester requester = new Requester();

    @FXML
    public void setAssign()
    {
        Map<String, String> queryParams = new HashMap<>();
        Map<String, String> queryParams2 = new HashMap<>();

        queryParams.put("requestId", requestId.getText());

        queryParams2.put("admin",admin.getText());

        String query = queryParams.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        String query2= queryParams2.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        requester.sendPostRequest("URL/employees/admin/assignContract?"+query, serializeData() , this::handleAssign);

    }

    private void dataParsing(HttpResponse<String> response) {
        System.out.println(response.body());
    }

    private String serializeData() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode data = objectMapper.createObjectNode();
        data.put("admin",admin.getText());
        data.put("inspector",inspector.getText());
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    private void handleAssign(HttpResponse<String> response) {
        if(response.statusCode()==200)
        {
            Platform.runLater(()-> System.out.println("Request Assigned"));
        }
        else
        {
            Platform.runLater(()-> System.out.println("Error Assigning Request"));
        }
    }

}
