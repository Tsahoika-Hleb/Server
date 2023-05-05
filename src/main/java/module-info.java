module com.server.server {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.sql;
    requires mysql.connector.java;
    requires json.simple;


    opens com.lab3 to javafx.fxml;
    opens com.lab3.DBModels to javafx.base;
    exports com.lab3;
}