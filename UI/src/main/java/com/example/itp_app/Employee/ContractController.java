package com.example.itp_app.Employee;

import com.example.itp_app.POJO.Request;
import com.example.itp_app.utilities.Requester;
import com.example.itp_app.utilities.SessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ContractController {
    @FXML
    public TextField vinTextField;
    @FXML
    public TextField modelTextField;
    @FXML
    public TextField productionYearTextField;
    @FXML
    public TextField fuelTypeTextField;
    @FXML
    public TextField plateNumberTextField;
    @FXML
    public TextField colorTextField;
    @FXML
    public TextField cubicVolumeTextField;
    @FXML
    public TextField pollutionCoefficientTextField;
    @FXML
    public TextField observationsTextField;
    @FXML
    public TextField manufacturerTextField;
    @FXML
    public Button completeButton;

    public final Requester requester = new Requester();

    @FXML
    public void setCompleteButtonAction()
    {
        Map<String, String> queryParams = new HashMap<>();
        Map<String, String> queryParams2 = new HashMap<>();
        Map<String, String> queryParams3 = new HashMap<>();

        queryParams.put("cubicVolume", cubicVolumeTextField.getText());
        queryParams2.put("pollutionCoefficient",pollutionCoefficientTextField.getText());
        queryParams2.put("observations",observationsTextField.getText());


        String query = queryParams.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        String query2= queryParams2.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        String query3= queryParams2.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        requester.sendPostRequest("URL/employees/inspector/completeContract?"+query + "&" + query2 + "&" + query3, serializeData() , this::handler);
    }

    private void handler(HttpResponse<String> response) {
        try {

            if (response.statusCode() == 200) {
                Platform.runLater(() -> {
                    vinTextField.clear();
                    productionYearTextField.clear();
                    modelTextField.clear();
                    fuelTypeTextField.clear();
                    manufacturerTextField.clear();
                    plateNumberTextField.clear();
                    colorTextField.clear();
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setContractDetails(Request currentContract) {
        if (currentContract != null) {
            Platform.runLater(() -> {
                vinTextField.setText(currentContract.getVIN());
                productionYearTextField.setText(currentContract.getProductionYear());
                modelTextField.setText(currentContract.getModel());
                fuelTypeTextField.setText(currentContract.getFuelType());
                manufacturerTextField.setText(currentContract.getManufacturer());
                plateNumberTextField.setText(currentContract.getPlateNumber());
                colorTextField.setText(currentContract.getColor());
            });
        }
    }

    public String serializeData()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode request = objectMapper.createObjectNode();
        request.put("vin", vinTextField.getText());
        request.put("model", modelTextField.getText());
        request.put("productionYear", productionYearTextField.getText());
        request.put("fuelType", fuelTypeTextField.getText());
        request.put("plateNumber", plateNumberTextField.getText());
        request.put("color", colorTextField.getText());
        try {
            return objectMapper.writeValueAsString(request);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }
}
