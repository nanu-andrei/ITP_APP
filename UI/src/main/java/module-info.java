module com.example.itp_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;

    requires com.jfoenix;

    requires java.sql;
    requires java.desktop;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires org.slf4j;
    requires com.google.gson;
    requires lombok;

    opens com.example.itp_app to javafx.fxml;
    exports com.example.itp_app.User;
    opens com.example.itp_app.User to javafx.fxml;
    exports com.example.itp_app.main;
    opens com.example.itp_app.main to javafx.fxml;
    exports com.example.itp_app.utilities;
    opens com.example.itp_app.utilities to javafx.fxml;
    opens com.example.itp_app.Employee to javafx.fxml;
    opens com.example.itp_app.POJO to com.google.gson;
//    opens javafx.graphics to org.testfx;
}