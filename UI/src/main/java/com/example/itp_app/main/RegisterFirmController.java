package com.example.itp_app.main;
import com.example.itp_app.POJO.Employee;
import com.example.itp_app.utilities.Requester;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class RegisterFirmController {
    @FXML
    public Button registerButton;
    @FXML
    public Button addButton;
    @FXML
    private TextField firmNameField;
    @FXML
    private TextField firmCifField;
    @FXML
    public Label firmAdminLabel;
    @FXML
    private TextField employeeUsernameField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private TableView<Employee> employeesTable;
    @FXML
    private TableColumn<Employee, String> usernameColumn;
    @FXML
    private TableColumn<Employee, String> roleColumn;
    public Requester requester = new Requester();
    public ObservableList<Employee> employees = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        roleComboBox.getItems().add("ADMIN");
        roleComboBox.setValue("ADMIN");
        roleComboBox.setPromptText("Select role");

        addButton.setDisable(true);

        employeeUsernameField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        roleComboBox.valueProperty().addListener((observable, oldValue, newValue) -> checkFields());

        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        roleColumn.setCellValueFactory(cellData -> cellData.getValue().roleProperty());

        employeesTable.setItems(employees);
    }

    private void checkFields() {
        String username = employeeUsernameField.getText();
        String role = roleComboBox.getValue();
        addButton.setDisable(username.isEmpty() || role == null);
    }

    @FXML
    private void onAddEmployee() {
        String username = employeeUsernameField.getText();
        String role = roleComboBox.getValue();
        String firm = firmNameField.getText();

        if (username.isEmpty() || role == null) {
            System.out.println("Username and role must be filled.");
            return;
        }

        Employee newEmployee = new Employee(username, role, firm);
        employees.add(newEmployee);
        employeeUsernameField.clear();
        roleComboBox.getSelectionModel().clearSelection();

        if ("ADMIN".equals(role)) {
            firmAdminLabel.setText(username);
            roleComboBox.getItems().clear();
            roleComboBox.getItems().add("INSPECTOR");
        }
        roleComboBox.setPromptText("Select role");
        addButton.setDisable(true);
    }

    @FXML
    private void onRegister() {
        System.out.println(serializeFirmAndEmployees());
        Map<String, String> queryParams = new HashMap<>();
            queryParams.put("requirement", firmCifField.getText());

        String query = queryParams.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String requestUri = "URL/employees/registerFirm?" + query;
        requester.sendPostRequest(requestUri, serializeFirmAndEmployees(),this::handleResponse);
    }

    public void handleResponse(HttpResponse<String> responseBody) {
        try {
            System.out.println(responseBody.body());
            if (responseBody.statusCode() == 200) {
                String response = responseBody.body();
                Platform.runLater(() -> openInterface(response));
            } else {
                System.out.println("It cannot register firm");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openInterface(String response) {
        Gson gson = new Gson();
        JsonObject firmJsonObject = gson.fromJson(response,JsonObject.class);
        List<Map<String,String>> employeesData = new ArrayList<>();

        for (var employeeElement : firmJsonObject.getAsJsonArray("employeeCredentialsList")) {
            JsonObject employee = employeeElement.getAsJsonObject();
            String password = employee.getAsJsonObject("wallet").get("privateKey").getAsString();
            String username = employee.get("username").getAsString();
            Map<String,String> employeeKV = new HashMap<>();

            System.out.println("Username: " + username + ", Password: " + password);

            employeeKV.put(username,password);
            employeesData.add(employeeKV);
        }
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/itp_app/employee_views/admin_ui.fxml"));
            Parent root = fxmlLoader.load();
            Stage userStage = new Stage();
            userStage.setScene(new Scene(root, 600, 400));
            userStage.initStyle(StageStyle.UNDECORATED);
            userStage.show();
            Stage currentStage = (Stage) registerButton.getScene().getWindow();
            currentStage.close();

            openSecondStage(employeesData);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void openSecondStage(List<Map<String, String>> employeesData) {
        Stage secondStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        for (Map<String, String> data : employeesData) {
            StringBuilder details = new StringBuilder();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                details.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
            }
            if (details.length() > 2) {
                details.setLength(details.length() - 2);
            }
            Label infoLabel = new Label(details.toString());
            layout.getChildren().add(infoLabel);
        }

        Scene secondScene = new Scene(layout, 300, 200);
        secondStage.setScene(secondScene);


        secondStage.setWidth(350);
        secondStage.setHeight(250);

        secondStage.setTitle("Employee Details");
        secondStage.show();
    }


    public String serializeFirmAndEmployees() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();

        rootNode.put("firmName", firmNameField.getText());

        if (employees != null && !employees.isEmpty()) {
            ArrayNode employeesArray = rootNode.putArray("employeeCredentialsList");
            for (Employee employee : employees) {
                ObjectNode employeeNode = objectMapper.createObjectNode();
                employeeNode.put("username", employee.getUsername());
                employeeNode.put("role", employee.getRole());
                employeesArray.add(employeeNode);
            }
        } else {
            System.out.println("Warning: No employees data available to serialize.");
        }

        try {
            return objectMapper.writeValueAsString(rootNode);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }


}
