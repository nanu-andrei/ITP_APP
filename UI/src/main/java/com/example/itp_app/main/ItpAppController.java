package com.example.itp_app.main;

import com.example.itp_app.utilities.FXMLoader;
import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ItpAppController implements Initializable {

    @FXML
    public AnchorPane layout;
    @FXML
    public JFXButton login;
    @FXML
    public JFXButton signup;
    @FXML
    public Button exit;
    @FXML
    private ToggleButton switchButton;
    @FXML
    public JFXButton login2;
    @FXML
    public JFXButton create;
    @FXML
    private Circle circle;
    private TranslateTransition transition;

    private boolean isSwitchOn = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            layout.getScene().getStylesheets().add(
                    getClass().getResource("/com/example/itp_app/main_views/start.css").toExternalForm()
            );
        });
        initializeContent();
    }

    public void initializeContent() {
        login.setVisible(true);
        signup.setVisible(true);
        login2.setVisible(false);
        create.setVisible(false);
    }
    @FXML
    public void onSwitch(ActionEvent e)
    {
        updateSwitchPosition();
        toggleSwitch();
    }
    @FXML
    public void onPressLogin(ActionEvent e)
    {
        if(!isSwitchOn) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/itp_app/main_views/login.fxml"));
            try
            {
                Parent root = loader.load();
                layout.getChildren().clear();
                layout.getChildren().add(root);
                LoginController loginController = loader.getController();
                if(loginController.log.isPressed() && loginController.log!= null )
                {
                    loginController.onLogAction();
                }
            }catch (IOException ex){ex.printStackTrace();}
        }
        else {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/itp_app/main_views/employee_login.fxml"));
            try
            {
                Parent root = loader.load();
                layout.getChildren().clear();
                layout.getChildren().add(root);
                LoginEmployeeController loginEmployeeController = loader.getController();
                if(loginEmployeeController.log2.isPressed() && loginEmployeeController.log2!= null )
                {

                }
            }catch (IOException ex){ex.printStackTrace();}
        }


    }
    @FXML
    public void onPressSignup(ActionEvent e)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/itp_app/main_views/signup.fxml"));
        try
        {
            Parent root = loader.load();
            layout.getChildren().clear();
            layout.getChildren().add(root);
             SignupController signupController = loader.getController();
            if(signupController.sign.isPressed() && signupController.sign!= null )
            {
                signupController.onSignupAction();
            }
        }catch (IOException ex){ex.printStackTrace();}

    }
    @FXML
    public void onPressCreate(ActionEvent e)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/itp_app/main_views/create_firm.fxml"));
        try {
            Parent root = loader.load();
            layout.getChildren().clear();
            layout.getChildren().add(root);
            RegisterFirmController registerFirmController = loader.getController();

        }catch (IOException ex){ex.printStackTrace();}
    }
    @FXML
    public void exitButtonAction(ActionEvent e) {

        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }


    public void toggleSwitch() {
        isSwitchOn = !isSwitchOn;
        updateSwitchPosition();
        if (isSwitchOn) {
            applyEmployeeTheme();
        } else {
            applyUserTheme();
        }
    }

    private void applyUserTheme() {
        layout.getScene().getStylesheets().clear();
        layout.getScene().getStylesheets().add(getClass().getResource("/com/example/itp_app/main_views/start.css").toExternalForm());
        switchButton.setStyle("-fx-background-color :#C5C5C5; -fx-background-radius: 15");
        login.setVisible(true);
        signup.setVisible(true);
        login2.setVisible(false);
        create.setVisible(false);

    }

    private void applyEmployeeTheme() {
        layout.getScene().getStylesheets().clear();
        layout.getScene().getStylesheets().add(getClass().getResource("/com/example/itp_app/main_views/start2.css").toExternalForm());
        switchButton.setStyle("-fx-background-color :#4A0000; -fx-background-radius: 15");
        login.setVisible(false);
        signup.setVisible(false);
        login2.setVisible(true);
        create.setVisible(true);
    }

    private void updateSwitchPosition() {
        double circleWidth = circle.getRadius() * 2;
        double switchWidth = switchButton.getWidth() - 10;
        moveCircle(isSwitchOn ? switchWidth - circleWidth : 0);
    }

    private void moveCircle(double targetX) {
        if (transition != null && transition.getStatus() == Animation.Status.RUNNING) {
            transition.stop();
        }
        transition = new TranslateTransition(Duration.millis(300), circle);
        transition.setToX(targetX);
        transition.play();
    }
}

