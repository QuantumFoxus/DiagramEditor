module qfox.DiagramEditor {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires com.google.gson;
    requires java.desktop;
    exports qfox.diagrameditor.app;
    opens qfox.diagrameditor.app to javafx.fxml;
}
