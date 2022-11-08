module com.example.lab1gui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lab1gui to javafx.fxml;
    exports com.example.lab1gui;
}