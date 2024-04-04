module com.example.jfxdemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.jfxdemo to javafx.fxml;
    exports com.example.jfxdemo;
}