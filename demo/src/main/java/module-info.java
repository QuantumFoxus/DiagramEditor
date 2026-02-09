module com.DiagramEditor {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires com.google.gson;
    requires java.desktop;
    opens qfox.diagrameditor.app to javafx.fxml;
    exports qfox.diagrameditor.app;
}
