package com.example.itp_app.POJO;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;

public class Request {
    private  StringProperty name;
    private  StringProperty VIN;
    private  StringProperty manufacturer;
    private  StringProperty model;
    private  StringProperty productionYear;
    private  StringProperty fuelType;
    private  StringProperty plateNumber;
    private  StringProperty color;
    private  StringProperty firmName;
    private  StringProperty status;

    public Request() {
        this.name = new SimpleStringProperty("");
        this.VIN = new SimpleStringProperty("");
        this.manufacturer = new SimpleStringProperty("");
        this.model = new SimpleStringProperty("");
        this.productionYear = new SimpleStringProperty("");
        this.fuelType = new SimpleStringProperty("");
        this.plateNumber = new SimpleStringProperty("");
        this.color = new SimpleStringProperty("");
        this.firmName = new SimpleStringProperty("");
        this.status = new SimpleStringProperty("");
    }

    public Request(String name, String VIN, String manufacturer, String model, String productionYear,
                   String fuelType, String plateNumber, String color, String firmName, String status) {
        this.name = new SimpleStringProperty(name);
        this.VIN = new SimpleStringProperty(VIN);
        this.manufacturer = new SimpleStringProperty(manufacturer);
        this.model = new SimpleStringProperty(model);
        this.productionYear =new SimpleStringProperty(productionYear);
        this.fuelType = new SimpleStringProperty(fuelType);
        this.plateNumber = new SimpleStringProperty(plateNumber);
        this.color = new SimpleStringProperty(color);
        this.firmName = new SimpleStringProperty(firmName);
        this.status = new SimpleStringProperty(status);
    }

    // Property getters
    public StringProperty nameProperty() { return name; }
    public StringProperty vinProperty() { return VIN; }
    public StringProperty manufacturerProperty() { return manufacturer; }
    public StringProperty modelProperty() { return model; }
    public StringProperty productionYearProperty() { return productionYear; }
    public StringProperty fuelTypeProperty() { return fuelType; }
    public StringProperty plateNumberProperty() { return plateNumber; }
    public StringProperty colorProperty() { return color; }
    public StringProperty firmNameProperty() { return firmName; }
    public StringProperty statusProperty() { return status; }

    // Regular getters and setters
    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public String getVIN() { return VIN.get(); }
    public void setVIN(String VIN) { this.VIN.set(VIN); }

    public String getManufacturer() { return manufacturer.get(); }
    public void setManufacturer(String manufacturer) { this.manufacturer.set(manufacturer); }

    public String getModel() { return model.get(); }
    public void setModel(String model) { this.model.set(model); }

    public String getProductionYear() { return productionYear.get(); }
    public void setProductionYear(String productionYear) { this.productionYear.set(productionYear); }

    public String getFuelType() { return fuelType.get(); }
    public void setFuelType(String fuelType) { this.fuelType.set(fuelType); }

    public String getPlateNumber() { return plateNumber.get(); }
    public void setPlateNumber(String plateNumber) { this.plateNumber.set(plateNumber); }

    public String getColor() { return color.get(); }
    public void setColor(String color) { this.color.set(color); }

    public String getFirmName() { return firmName.get(); }
    public void setFirmName(String firmName) { this.firmName.set(firmName); }

    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }
}
