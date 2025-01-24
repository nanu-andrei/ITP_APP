package com.example.itp_app.Employee;

import com.example.itp_app.POJO.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ViewAdminController implements Initializable {
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public TableView<Request> requestTable;
    @FXML
    public TableColumn<Request, String> nameColumn;
    @FXML
    public TableColumn<Request, String> vinColumn;
    @FXML
    public TableColumn<Request, String> modelColumn;
    @FXML
    public TableColumn<Request,String> manufacturerColumn;
    @FXML
    public TableColumn<Request, String> yearColumn;
    @FXML
    public TableColumn<Request, String> colorColumn;
    @FXML
    public TableColumn<Request, String> fuelTypeColumn;
    @FXML
    public TableColumn<Request, String> plateNumberColumn;
    @FXML
    public TableColumn<Request,String> firmNameColumn;
    @FXML
    public TableColumn<Request, String> statusColumn;

    public final ObservableList<Request> requestData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
    }


    private void setupTableColumns() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        vinColumn.setCellValueFactory(cellData -> cellData.getValue().vinProperty());
        modelColumn.setCellValueFactory(cellData -> cellData.getValue().modelProperty());
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().productionYearProperty());
        colorColumn.setCellValueFactory(cellData -> cellData.getValue().colorProperty());
        fuelTypeColumn.setCellValueFactory(cellData -> cellData.getValue().fuelTypeProperty());
        plateNumberColumn.setCellValueFactory(cellData -> cellData.getValue().plateNumberProperty());
        manufacturerColumn.setCellValueFactory(cellData -> cellData.getValue().manufacturerProperty());
        firmNameColumn.setCellValueFactory(cellData -> cellData.getValue().firmNameProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        requestTable.setItems(requestData);


        requestTable.refresh();
    }

    public void updateTableView(List<Request> requests) {
        requestData.setAll(requests);
        for (Request request : requestData) {
            System.out.println("Request Details:");
            System.out.println("Name: " + request.getName());
            System.out.println("VIN: " + request.getVIN());
            System.out.println("Manufacturer: " + request.getManufacturer());
            System.out.println("Model: " + request.getModel());
            System.out.println("Production Year: " + request.getProductionYear());
            System.out.println("Fuel Type: " + request.getFuelType());
            System.out.println("Plate Number: " + request.getPlateNumber());
            System.out.println("Color: " + request.getColor());
            System.out.println("Firm:"+ request.getFirmName());
            System.out.println("Status: " + request.getStatus());
            System.out.println();

        }
    }
}