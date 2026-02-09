module com.DiagramEditor {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires com.google.gson;
    requires java.desktop;
    opens com.example.app to javafx.fxml;
    exports com.example.app;
}
