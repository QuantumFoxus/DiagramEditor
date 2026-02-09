package qfox.diagrameditor.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import qfox.diagrameditor.controller.DiagramController;
import qfox.diagrameditor.elementShapes.ShapesFactory;
import qfox.diagrameditor.elements.DiagramElement;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

public class MainController implements CanvasListener {

    @FXML
    private BorderPane MainPane;

    @FXML
    private ScrollPane canvasScroll;

    @FXML
    private Pane canvasPane;

    @FXML
    private Accordion elementsAccordion;

    @FXML
    private VBox blocksContainer;

    @FXML
    private VBox linesContainer;

    @FXML
    private VBox otherContainer;

    @FXML
    private ScrollPane propertiesPane;

    @FXML
    private VBox propertiesContainer;

    @FXML
    private Spinner<Integer> GridSizeSpinner;

    private static final double PREVIEW_WIDTH = 120;
    private static final double PREVIEW_HEIGHT = 60;

    private double zoom = 1.0;
    private final double zoomFactor = 1.1;

    private DiagramController controller;
    private CanvasView view;

    private static boolean addMode = false;
    private String selectedType = "none";
    private DiagramElement lastSelected;

    @FXML
    private void onUndoClicked(ActionEvent event) {
        controller.undo();
        updateCanvas();
        view.unselect();
    }

    @FXML
    private void onRedoClicked(ActionEvent event) {
        controller.redo();
        updateCanvas();
        view.unselect();
    }

    @FXML
    private void onDeleteClicked(ActionEvent event) {
        if (lastSelected != null) {
            controller.removeElement(lastSelected);
            updateCanvas();
            view.unselect();
        }
    }

    @FXML
    private void onSaveClicked(ActionEvent event) {
        SaveFile();
    }

    @FXML
    private void onLoadClicked(ActionEvent event) {
        if (!controller.isUndoEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Загрузить диаграмму");
            alert.setHeaderText("Загрузить диаграмму?");
            alert.setContentText("ВНИМАНИЕ! Имеются не сохраненные изменения.\nЖелаете сохранить?");

            ButtonType saveBtn = new ButtonType("Сохранить");
            ButtonType discardBtn = new ButtonType("Не сохранять");
            ButtonType cancelBtn = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(saveBtn, discardBtn, cancelBtn);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isEmpty() || result.get() == cancelBtn) {
                return;
            }

            if (result.get() == saveBtn) {
                SaveFile();
            }
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open diagram");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Diagram files (*.dia)", "*.dia")
        );

        File file = fileChooser.showOpenDialog(MainPane.getScene().getWindow());
        if (file != null) {
            controller.loadFromFile(file);
            updateCanvas();
        }
    }

    @FXML
    private void onCreateClicked(ActionEvent event) {
        if (!controller.isUndoEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Создать новую диаграмму");
            alert.setHeaderText("Создать новую диаграмму?");
            alert.setContentText("ВНИМАНИЕ! Имеются не сохраненные изменения.\nЖелаете сохранить?");

            ButtonType saveBtn = new ButtonType("Сохранить");
            ButtonType discardBtn = new ButtonType("Не сохранять");
            ButtonType cancelBtn = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(saveBtn, discardBtn, cancelBtn);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isEmpty() || result.get() == cancelBtn) {
                return;
            }

            if (result.get() == saveBtn) {
                SaveFile();
            }
        }

        controller = new DiagramController("unnamed");
        updateCanvas();
    }

    @FXML
    private void onExportClicked(ActionEvent event) {
        canvasPane.setScaleX(1);
        canvasPane.setScaleY(1);
        double gridSize = view.getGridSize();
        view.setGridSize(1);
        updateCanvas();
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export to PNG");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PNG Image (*.png)", "*.png")
        );

        File file = chooser.showSaveDialog(MainPane.getScene().getWindow());
        if (file == null) {
            return;
        }

        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        for (Node node : canvasPane.getChildren()) {
            Bounds b = node.getBoundsInParent();

            minX = Math.min(minX, b.getMinX());
            minY = Math.min(minY, b.getMinY());
            maxX = Math.max(maxX, b.getMaxX());
            maxY = Math.max(maxY, b.getMaxY());
        }

        if (minX == Double.MAX_VALUE) {
            return;
        }
        SnapshotParameters params = new SnapshotParameters();
        double offset = 10;
        params.setViewport(new Rectangle2D(
                minX - offset, minY - offset,
                maxX - minX + offset * 2,
                maxY - minY + offset * 2
        ));
        params.setFill(Color.TRANSPARENT);

        WritableImage img = canvasPane.snapshot(params, null);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        canvasPane.setScaleX(zoom);
        canvasPane.setScaleY(zoom);
        view.setGridSize(gridSize);
        updateCanvas();
    }

    private void SaveFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save diagram");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Diagram files (*.dia)", "*.dia")
        );

        File file = fileChooser.showSaveDialog(MainPane.getScene().getWindow());
        if (file != null) {
            controller.saveToFile(file);
        }
    }

    @FXML
    private void initialize() {
        controller = new DiagramController();
        view = new CanvasView();

        view.setListener(this);

        initBlockButtons();
        initLineButtons();
        initOtherButtons();

        MainPane.setOnKeyPressed(ev -> {
            if (ev.isControlDown() && ev.getCode() == KeyCode.Z) {
                onUndoClicked(null);
                ev.consume();
            } else if (ev.isControlDown() && ev.getCode() == KeyCode.Y) {
                onRedoClicked(null);
                ev.consume();
            } else if (ev.getCode() == KeyCode.DELETE) {
                onDeleteClicked(null);
                ev.consume();
            }
        });

        canvasPane.setOnMouseClicked(event -> {
            if (addMode) {
                double x = event.getX();
                double y = event.getY();
                controller.addElement(selectedType, new Point2D(x, y));
                addMode = false;
                selectedType = "none";
                updateCanvas();
            }
            if (event.getTarget() == canvasPane) {
                view.unselect();
            }
        });

        canvasScroll.setHvalue(0.5);
        canvasScroll.setVvalue(0.5);
        canvasPane.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                canvasScroll.setPannable(false);
            }
        });

        canvasPane.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            canvasScroll.setPannable(true);
        });

        GridSizeSpinner.setEditable(false);

        List<Integer> values = new ArrayList<>();
        values.add(1);
        for (int v = 8; v <= 256; v *= 2) {
            values.add(v);
        }

        SpinnerValueFactory<Integer> geomFactory = new SpinnerValueFactory<Integer>() {
            private int index = 3;

            {
                setValue(values.get(3));
            }

            @Override
            public void decrement(int steps) {
                index = Math.max(0, index - steps);
                setValue(values.get(index));
            }

            @Override
            public void increment(int steps) {
                index = Math.min(values.size() - 1, index + steps);
                setValue(values.get(index));
            }
        };

        GridSizeSpinner.setValueFactory(geomFactory);

        geomFactory.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                view.setGridSize(newVal);
                updateCanvas();
            }
        });

        canvasPane.setOnScroll(event -> {
            if (event.isControlDown()) {
                double oldZoom = zoom;

                if (event.getDeltaY() > 0) {
                    zoom *= zoomFactor;
                } else {
                    zoom /= zoomFactor;
                }

                zoom = Math.max(0.1, Math.min(10, zoom));

                applyZoom();

                double f = zoom / oldZoom;
                canvasPane.setLayoutX(canvasPane.getLayoutX() - (event.getX() - canvasPane.getLayoutX()) * (f - 1));
                canvasPane.setLayoutY(canvasPane.getLayoutY() - (event.getY() - canvasPane.getLayoutY()) * (f - 1));

                event.consume();
            }
        });
        view.setGridSize(values.get(3));
        Platform.runLater(() -> {
            updateCanvas();
        });
    }

    private void applyZoom() {
        if (canvasPane == null) {
            return;
        }
        canvasPane.setScaleX(zoom);
        canvasPane.setScaleY(zoom);
    }

    private void initBlockButtons() {
        for (String type : ShapesFactory.BLOCK_TYPE) {
            Button processButton = new Button(type);
            processButton.setOnAction(e -> {
                addMode = type.equals(selectedType) ? !addMode : true;
                selectedType = type;
            });
            blocksContainer.getChildren().add(processButton);
        }

    }

    private void initLineButtons() {
        for (String type : ShapesFactory.LINE_TYPE) {
            Button processButton = new Button(type);
            processButton.setOnAction(e -> {
                addMode = type.equals(selectedType) ? !addMode : true;
                selectedType = type;
            });
            linesContainer.getChildren().add(processButton);
        }
    }

    private void initOtherButtons() {
        for (String type : ShapesFactory.OTHER_TYPE) {
            Button processButton = new Button(type);
            processButton.setOnAction(e -> {
                addMode = type.equals(selectedType) ? !addMode : true;
                selectedType = type;
            });
            otherContainer.getChildren().add(processButton);
        }
    }

    private void updateCanvas() {
        view.render(canvasPane, controller.getModel().getElements());
    }

    public void showProperties(DiagramElement element) {
        VBox content = new VBox(5);

        Label posLabel = new Label("Позиция:");
        HBox posBox = new HBox(5);
        Label xLabel = new Label("X:");
        xLabel.setMinWidth(20);
        TextField xField = new TextField(String.valueOf(element.getPosition().getX()));
        Label yLabel = new Label("Y:");
        yLabel.setMinWidth(20);
        TextField yField = new TextField(String.valueOf(element.getPosition().getY()));
        posBox.getChildren().addAll(xLabel, xField, yLabel, yField);
        content.getChildren().addAll(posLabel, posBox);

        Label sizeLabel = new Label("Размер:");
        HBox sizeBox = new HBox(5);
        Label wLabel = new Label("W:");
        wLabel.setMinWidth(20);
        TextField widthField = new TextField(String.valueOf(element.getSize().getX()));
        Label hLabel = new Label("H:");
        hLabel.setMinWidth(20);
        TextField heightField = new TextField(String.valueOf(element.getSize().getY()));
        sizeBox.getChildren().addAll(wLabel, widthField, hLabel, heightField);
        content.getChildren().addAll(sizeLabel, sizeBox);

        Label textsLabel = new Label("Текст:");
        content.getChildren().add(textsLabel);
        String[] texts = element.getTexts();
        TextArea[] textFields = new TextArea[texts.length];
        Button saveBtn = new Button("Сохранить изменения");
        for (int i = 0; i < texts.length; i++) {
            TextArea ta = new TextArea(texts[i]);
            ta.setWrapText(true);
            ta.setPrefRowCount(5);
            textFields[i] = ta;
            ta.setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case ENTER:
                        saveBtn.fire();
                        event.consume();
                        break;
                    default:
                        break;
                }
            });
            content.getChildren().add(ta);
            xField.setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case ENTER:
                        saveBtn.fire();
                        event.consume();
                        break;
                    default:
                        break;
                }
            });
            yField.setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case ENTER:
                        saveBtn.fire();
                        event.consume();
                        break;
                    default:
                        break;
                }
            });
            widthField.setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case ENTER:
                        saveBtn.fire();
                        event.consume();
                        break;
                    default:
                        break;
                }
            });
            heightField.setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case ENTER:
                        saveBtn.fire();
                        event.consume();
                        break;
                    default:
                        break;
                }
            });
        }

        saveBtn.setOnAction(ev -> {
            Point2D newPos = null;
            Point2D newSize = null;
            String[] newTexts = null;

            try {
                double x = Double.parseDouble(xField.getText());
                double y = Double.parseDouble(yField.getText());
                if (x != element.getPosition().getX() || y != element.getPosition().getY()) {
                    newPos = new Point2D(x, y);
                }
            } catch (NumberFormatException ignored) {
            }

            try {
                double w = Double.parseDouble(widthField.getText());
                double h = Double.parseDouble(heightField.getText());
                if (w != element.getSize().getX() || h != element.getSize().getY()) {
                    newSize = new Point2D(w, h);
                }
            } catch (NumberFormatException ignored) {
            }

            boolean changed = false;
            String[] updatedTexts = texts.clone();
            for (int i = 0; i < textFields.length; i++) {
                String val = textFields[i].getText();
                if (!val.equals(texts[i])) {
                    updatedTexts[i] = val;
                    changed = true;
                }
            }
            if (changed) {
                newTexts = updatedTexts;
            }

            controller.updateElement(element, newPos, newSize, newTexts);
            updateCanvas();
        });

        content.getChildren().add(saveBtn);
        propertiesPane.setContent(content);
    }

    public void saveButton() {

    }

    @Override
    public void onElementSelected(DiagramElement element) {
        lastSelected = element;
        showProperties(element);
    }

    @Override
    public void onElementUnselected() {
        lastSelected = null;
        propertiesPane.setContent(null);
    }

    @Override
    public void onElementMoveFinished(DiagramElement element, Point2D newPos) {
        controller.moveElement(element, newPos);
        updateCanvas();
    }

    @Override
    public void onElementResized(DiagramElement element, Point2D newSize) {
        controller.resizeElement(element, newSize);
        updateCanvas();
    }

    @Override
    public void onElementUpdated(DiagramElement element, Point2D newPos, Point2D newSize, String[] newTexts) {
        controller.updateElement(element, newPos, newSize, newTexts);
        updateCanvas();
    }
}
