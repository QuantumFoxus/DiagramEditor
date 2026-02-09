module com.DiagramEditor {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires com.google.gson;
    requires java.desktop;
    opens QFox.diagrameditor.app to javafx.fxml;
    exports QFox.diagrameditor.app;
}
