package com.example.itp_app.User;
import com.example.itp_app.utilities.SessionManager;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
public class AccountController {

    public void initialize()
    {
        userData = SessionManager.getInstance().getResponse("userData");
    }

    @FXML
    public Label first_name;
    @FXML
    public Label last_name;
    @FXML
    public  Label gender;
    @FXML
    public Label dob;
    @FXML
    public Label cnp;
    public JsonObject userData;

    @FXML
    public void onPressAccount()
    {
        String firstName =userData.get("firstName").getAsString();
        first_name.setText(firstName);
        last_name.setText(userData.get("lastName").getAsString());
        dob.setText(userData.get("dateOfBirth").getAsString());
        cnp.setText(userData.get("cnp").getAsString());
        gender.setText(userData.get("gender").getAsString());
        System.out.println(userData.get("firstName").getAsString());
    }





}
