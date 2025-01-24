package com.example.itp_app.Employee;

import com.example.itp_app.POJO.Employee;
import com.example.itp_app.utilities.Requester;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.TextField;

import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class AddController {

    @FXML
    public TextField name;
    @FXML
    public TextField role;
    @FXML
    public TextField firm;
    @FXML
    public Button addButton;
    @FXML
    public Button removeButton;
    @FXML
    public Button updateButton;

    private Requester requester = new Requester();

    @FXML
    public void setAddButton()
    {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("firmName", firm.getText());
        System.out.println("Firm name:"+firm.getText());

        String query = queryParams.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + customURLEncode(entry.getValue()))
                .collect(Collectors.joining("&"));

        requester.sendPostRequest("URL/employees/admin/addInspector?"+query, serializeData(), this::handleAdd );
    }

    private static String customURLEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private void handleAdd(HttpResponse<String> stringHttpResponse) {
        System.out.println(stringHttpResponse.body());
    }

    @FXML
    public  void setRemoveButton()
    {
        Map<String, String> queryParams2 = new HashMap<>();
        queryParams2.put("username", name.getText());
        String query2 = queryParams2.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + customURLEncode(entry.getValue()))
                .collect(Collectors.joining("&"));
        requester.sendDeleteRequest("URL/employees/admin/removeInspector?"+query2, this::handleRemove);

    }

    private void handleRemove(HttpResponse<String> stringHttpResponse) {
    }

    @FXML
    public void updateButton()
    {
        requester.sendPostRequest("URL/employee/updateEmployee", serializeData(), this::handleUpdate);

    }

    private void handleUpdate(HttpResponse<String> stringHttpResponse) {
    }

    private String serializeData() {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode employeesArray = objectMapper.createArrayNode();

        ObjectNode employeeNode = objectMapper.createObjectNode();
        employeeNode.put("username",name.getText() );
        employeeNode.put("role", role.getText());
        employeesArray.add(employeeNode);

        try {
            return objectMapper.writeValueAsString(employeesArray);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }
}

