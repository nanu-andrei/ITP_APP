package com.example.itp_app.User;

import com.example.itp_app.utilities.Requester;
import com.example.itp_app.utilities.SessionManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class RequestController  {
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private TextField name, VIN, model, producer, productionYear, color , plateNr, fuelType, designatedFirm;
    @FXML
    private Label error;
    public JsonObject userData;

    @FXML
    private Button submitButton;
    @FXML
    private void initialize()
    {
        userData = SessionManager.getInstance().getResponse("userData");
    }
    public Requester requester = new Requester();

    @FXML
    public void setSubmitButton() throws JsonProcessingException {
        System.out.println(serializeData());
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("firm", designatedFirm.getText());

        String query = queryParams.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        requester.sendPostRequest("URL/users/requests?"+query, serializeData() ,this::handleResponse);
    }

    private void handleResponse(HttpResponse<String> response) {
        if(response.statusCode() == 200)
        {
            Platform.runLater(()->error.setText("The request is was submitted"));

        }
        else
        {
            Platform.runLater(()->error.setText("The request is wrong, try again."));
        }
    }


    private String serializeData() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode request = objectMapper.createObjectNode();
        ObjectNode requestInfo = objectMapper.createObjectNode();
        ObjectNode requestData = objectMapper.createObjectNode();


        JsonNode userDataNode = objectMapper.readTree(userData.toString());
        requestInfo.set("userData", userDataNode);


        ObjectNode details = objectMapper.createObjectNode();
        details.put("vin",VIN.getText());
        details.put("manufacturer",producer.getText());
        details.put("model",model.getText());
        details.put("productionYear",productionYear.getText());
        details.put("fuelType",fuelType.getText());
        details.put("plateNumber",plateNr.getText());
        details.put("color",color.getText());

        requestData.set("details",details);

        request.set("requestInfo",requestInfo);
        request.set("requestData",requestData);

        try {
            return objectMapper.writeValueAsString(request);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

}
