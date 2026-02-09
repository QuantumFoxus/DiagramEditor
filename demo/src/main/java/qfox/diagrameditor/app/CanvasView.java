package qfox.diagrameditor.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import qfox.diagrameditor.elementShapes.Shape;
import qfox.diagrameditor.elementShapes.TextShape;
import qfox.diagrameditor.elements.DiagramConnector;
import qfox.diagrameditor.elements.DiagramElement;
import qfox.diagrameditor.elements.DiagramNode;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class CanvasView {

    private DiagramElement selectedElement;
    private javafx.scene.shape.Shape selectionRect;
    private Circle selectedFrom;
    private Circle selectedTo;
    private DiagramElement draggedElement;
    private final Map<DiagramElement, List<Node>> elementNodes = new HashMap<>();
    private Pane canvasPane;

    private double dragStartX;
    private double dragStartY;
    private double gridSize = 0;

    private CanvasListener listener;

    private double SELECTION_POLYGON_OFFSET = 10;

    public CanvasView() {
    }

    public void setListener(CanvasListener listener) {
        this.listener = listener;
    }

    public void render(Pane pane, List<DiagramElement> elements) {
        this.canvasPane = pane;
        canvasPane.getChildren().clear();
        elementNodes.clear();
        drawGrid();

        for (DiagramElement element : elements) {
            if (element instanceof DiagramConnector) {
                renderElement(element);
            }
        }

        for (DiagramElement element : elements) {
            if (!(element instanceof DiagramConnector)) {
                renderElement(element);
            }
        }

        updateSelection();
    }

    private void renderElement(DiagramElement element) {
        Shape[] shapes = element.getShape();
        List<Node> nodes = new ArrayList<>();
        boolean isPolygon = false;

        if (element instanceof DiagramConnector dc) {
            isPolygon = true;
            List<Point2D> points = getPointsAroundLine(
                    dc.getPosition(),
                    dc.getPosition().add(dc.getSize()),
                    SELECTION_POLYGON_OFFSET
            );

            Polygon p = new Polygon(
                    points.get(0).getX(), points.get(0).getY(),
                    points.get(1).getX(), points.get(1).getY(),
                    points.get(2).getX(), points.get(2).getY(),
                    points.get(3).getX(), points.get(3).getY()
            );
            p.setFill(Color.TRANSPARENT);
            p.setStroke(Color.TRANSPARENT);

            canvasPane.getChildren().add(p);
            nodes.add(p);
            setSelectableShape(element, p);
        }

        for (Shape s : shapes) {
            Node node = s.render(element.getPosition());

            if (s instanceof TextShape ts && element instanceof DiagramConnector) {
                double angle = Math.toDegrees(
                        Math.atan2(element.getSize().getY(), element.getSize().getX())
                );
                if (angle > 90) {
                    angle -= 180;
                }
                if (angle < -90) {
                    angle += 180;
                }

                node = ts.render(element.getPosition(), angle);
            }

            node.setPickOnBounds(!isPolygon);
            canvasPane.getChildren().add(node);
            nodes.add(node);
            setSelectableShape(element, node);
        }

        elementNodes.put(element, nodes);
    }

    private void setSelectableShape(DiagramElement element, Node node) {
        DiagramElement el = element;

        node.setOnMouseClicked(e -> {
            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }
            select(el);
            e.consume();
        });

        node.setOnMousePressed(e -> {
            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }
            draggedElement = el;
            dragStartX = e.getSceneX();
            dragStartY = e.getSceneY();
            e.consume();
        });

        node.setOnMouseDragged(e -> {
            if (draggedElement != el || selectedElement != el) {
                return;
            }
            if (e.getButton() != MouseButton.PRIMARY) {
                return;
            }

            Point2D localStart = canvasPane.sceneToLocal(dragStartX, dragStartY);
            Point2D localCurrent = canvasPane.sceneToLocal(e.getSceneX(), e.getSceneY());

            double dx = localCurrent.getX() - localStart.getX();
            double dy = localCurrent.getY() - localStart.getY();

            moveSelectedElementView(dx, dy);

            dragStartX = e.getSceneX();
            dragStartY = e.getSceneY();
            e.consume();
        });

        node.setOnMouseReleased(e -> {
            if (draggedElement != el) {
                return;
            }

            finishMove(el);
            draggedElement = null;
            e.consume();
        });
    }

    public void select(DiagramElement element) {
        this.selectedElement = element;
        updateSelection();
        listener.onElementSelected(element);
        if (listener != null) {
            listener.onElementSelected(element);
        }
    }

    public void unselect() {
        selectedElement = null;
        draggedElement = null;
        removeSelection();
        listener.onElementUnselected();
        if (listener != null) {
            listener.onElementUnselected();
        }
    }

    private void updateSelection() {
        removeSelection();

        if (selectedElement == null) {
            return;
        }

        Point2D pos = selectedElement.getPosition();
        Point2D size = selectedElement.getSize();
        if (selectedElement instanceof DiagramConnector dc) {
            List<Point2D> points = getPointsAroundLine(dc.getPosition(), dc.getPosition().add(dc.getSize()), SELECTION_POLYGON_OFFSET);
            selectionRect = new Polygon(
                    points.get(0).getX(), points.get(0).getY(),
                    points.get(1).getX(), points.get(1).getY(),
                    points.get(2).getX(), points.get(2).getY(),
                    points.get(3).getX(), points.get(3).getY()
            );
            selectionRect.setFill(Color.TRANSPARENT);
            selectionRect.setStroke(Color.DODGERBLUE);
            selectionRect.setStrokeWidth(1);
            selectionRect.getStrokeDashArray().setAll(6.0);
            selectionRect.setMouseTransparent(true);
        } else {
            selectionRect = new Rectangle(
                    pos.getX(),
                    pos.getY(),
                    size.getX(),
                    size.getY()
            );

            selectionRect.setFill(Color.TRANSPARENT);
            selectionRect.setStroke(Color.DODGERBLUE);
            selectionRect.setStrokeWidth(3);
            selectionRect.getStrokeDashArray().setAll(6.0);
            selectionRect.setMouseTransparent(true);
        }

        selectedFrom = new Circle(pos.getX(), pos.getY(), SELECTION_POLYGON_OFFSET, Color.DODGERBLUE);
        selectedFrom.setFill(Color.TRANSPARENT);
        selectedFrom.setStroke(Color.DODGERBLUE);
        selectedFrom.setMouseTransparent(false);
        selectedFrom.setOnMousePressed(e -> {
            dragStartX = e.getSceneX();
            dragStartY = e.getSceneY();
            e.consume();
        });

        selectedFrom.setOnMouseDragged(e -> {
            Point2D localStart = canvasPane.sceneToLocal(dragStartX, dragStartY);
            Point2D localCurrent = canvasPane.sceneToLocal(e.getSceneX(), e.getSceneY());
            double dx = localCurrent.getX() - localStart.getX();
            double dy = localCurrent.getY() - localStart.getY();
            selectedFrom.setCenterX(selectedFrom.getCenterX() + dx);
            selectedFrom.setCenterY(selectedFrom.getCenterY() + dy);
            dragStartX = e.getSceneX();
            dragStartY = e.getSceneY();
            e.consume();
        });

        selectedFrom.setOnMouseReleased(e -> {
            Point2D newPos = new Point2D(
                    selectedFrom.getCenterX(),
                    selectedFrom.getCenterY()
            );
            newPos = snap(newPos, gridSize);
            if (selectedElement instanceof DiagramConnector dc) {
                DiagramElement el = findElementAt(e.getSceneX(), e.getSceneY(), selectedElement);
                if (el != null && el != dc.getToElement()) {
                    newPos = DiagramConnector.intersectRectEdge(el.getPosition(), el.getSize(), selectedElement.getPosition().add(selectedElement.getSize()));
                    dc.setFromElement(el);
                    if (el instanceof DiagramNode dn) {
                        dn.addConnectedElement(selectedElement);
                    }
                } else {
                    DiagramElement oldFrom = dc.getFromElement();
                    if (oldFrom instanceof DiagramNode oldDn) {
                        oldDn.removeConnectedElement(selectedElement);
                    }
                    dc.setFromElement(null);
                }
                DiagramElement toEl = dc.getToElement();
                if (toEl instanceof DiagramNode toDn) {
                    toDn.notifyConnectedElements();
                }
            }
            Point2D oldSize = selectedElement.getSize();
            Point2D oldPos = selectedElement.getPosition();
            Point2D newSize = snap(oldSize.add(oldPos.subtract(newPos)), gridSize);

            if (listener != null) {
                listener.onElementUpdated(selectedElement, newPos, newSize, null);
            }

            unselect();
            render(canvasPane, java.util.List.copyOf(elementNodes.keySet()));
            e.consume();
        });

        selectedTo = new Circle(pos.getX() + size.getX(), pos.getY() + size.getY(), SELECTION_POLYGON_OFFSET, Color.DODGERBLUE);
        selectedTo.setFill(Color.TRANSPARENT);
        selectedTo.setStroke(Color.DODGERBLUE);
        selectedTo.setMouseTransparent(false);
        selectedTo.setOnMousePressed(e -> {
            dragStartX = e.getSceneX();
            dragStartY = e.getSceneY();
            e.consume();
        });

        selectedTo.setOnMouseDragged(e -> {
            Point2D localStart = canvasPane.sceneToLocal(dragStartX, dragStartY);
            Point2D localCurrent = canvasPane.sceneToLocal(e.getSceneX(), e.getSceneY());
            double dx = localCurrent.getX() - localStart.getX();
            double dy = localCurrent.getY() - localStart.getY();
            selectedTo.setCenterX(selectedTo.getCenterX() + dx);
            selectedTo.setCenterY(selectedTo.getCenterY() + dy);
            dragStartX = e.getSceneX();
            dragStartY = e.getSceneY();
            e.consume();
        });

        selectedTo.setOnMouseReleased(e -> {
            Point2D newPos = new Point2D(
                    selectedTo.getCenterX(),
                    selectedTo.getCenterY()
            );
            newPos = snap(newPos, gridSize);
            if (selectedElement instanceof DiagramConnector dc) {
                DiagramElement el = findElementAt(e.getSceneX(), e.getSceneY(), selectedElement);
                if (el != null && el != dc.getFromElement()) {
                    newPos = DiagramConnector.intersectRectEdge(el.getPosition(), el.getSize(), selectedElement.getPosition());
                    dc.setToElement(el);
                    if (el instanceof DiagramNode dn) {
                        dn.addConnectedElement(selectedElement);
                    }
                } else {
                    DiagramElement oldTo = dc.getToElement();
                    if (oldTo instanceof DiagramNode oldDn) {
                        oldDn.removeConnectedElement(selectedElement);
                    }
                    dc.setToElement(null);
                }
                DiagramElement fromEl = dc.getFromElement();
                if (fromEl instanceof DiagramNode fromDn) {
                    fromDn.notifyConnectedElements();
                }
            }
            Point2D oldPos = selectedElement.getPosition();
            Point2D newSize = snap(newPos.subtract(oldPos), gridSize);

            if (listener != null) {
                listener.onElementResized(selectedElement, newSize);
            }

            unselect();
            render(canvasPane, List.copyOf(elementNodes.keySet()));
            e.consume();
        });
        canvasPane.getChildren().addAll(selectionRect, selectedFrom, selectedTo);
    }

    private void removeSelection() {
        if (selectionRect != null) {
            canvasPane.getChildren().removeAll(selectionRect, selectedFrom, selectedTo);
            selectionRect = null;
            selectedFrom = null;
            selectedTo = null;
        }
    }

    private void moveSelectedElementView(double dx, double dy) {
        if (selectionRect == null || selectedElement == null) {
            return;
        }

        Point2D pos = selectedElement.getPosition();
        Point2D size = selectedElement.getSize();

        double canvasWidth = canvasPane.getWidth();
        double canvasHeight = canvasPane.getHeight();

        double currentX = pos.getX() + selectionRect.getTranslateX();
        double currentY = pos.getY() + selectionRect.getTranslateY();

        double newX = currentX + dx;
        double newY = currentY + dy;

        if (newX < 0) {
            dx = -currentX;
        } else if (newX + size.getX() > canvasWidth) {
            dx = canvasWidth - size.getX() - currentX;
        }

        if (newY < 0) {
            dy = -currentY;
        } else if (newY + size.getY() > canvasHeight) {
            dy = canvasHeight - size.getY() - currentY;
        }

        selectionRect.setTranslateX(selectionRect.getTranslateX() + dx);
        selectionRect.setTranslateY(selectionRect.getTranslateY() + dy);
    }

    private void finishMove(DiagramElement element) {
        if (selectionRect == null) {
            return;
        }

        Point2D oldPos = element.getPosition();

        double dx = selectionRect.getTranslateX();
        double dy = selectionRect.getTranslateY();

        if (dx == 0 && dy == 0) {
            return;
        }

        Point2D newPos = oldPos.add(dx, dy);
        newPos = snap(newPos, gridSize);

        if (listener != null) {
            listener.onElementMoveFinished(element, newPos);
        }

        selectionRect.setTranslateX(0);
        selectionRect.setTranslateY(0);
        if (element instanceof DiagramConnector dc) {
            dc.setFromElement(null);
            dc.setToElement(null);
        }
        render(canvasPane, java.util.List.copyOf(elementNodes.keySet()));
    }

    public DiagramElement getSelectedElement() {
        return selectedElement;
    }

    private DiagramElement findElementAt(double sceneX, double sceneY, DiagramElement ignore) {
        if (canvasPane == null) {
            return null;
        }
        Point2D local = canvasPane.sceneToLocal(sceneX, sceneY);
        double x = local.getX();
        double y = local.getY();

        for (DiagramElement el : elementNodes.keySet()) {
            if (el == ignore) {
                continue;
            }
            if (el instanceof DiagramConnector) {
                continue;
            }

            List<Node> nodes = elementNodes.get(el);
            if (nodes == null) {
                continue;
            }

            for (Node n : nodes) {
                javafx.geometry.Bounds b = n.getBoundsInParent();
                if (b != null && b.contains(x, y)) {
                    return el;
                }
            }
        }
        return null;
    }

    public static List<Point2D> getPointsAroundLine(Point2D a, Point2D b, double distance) {
        Point2D dir = b.subtract(a);
        if (dir.magnitude() == 0) {
            return null;
        }

        Point2D n = dir.normalize();

        double cos45 = Math.cos(Math.toRadians(45));
        double sin45 = Math.sin(Math.toRadians(45));
        double cos_45 = Math.cos(Math.toRadians(-45));
        double sin_45 = Math.sin(Math.toRadians(-45));

        Point2D rotPlus = new Point2D(
                n.getX() * cos45 - n.getY() * sin45,
                n.getX() * sin45 + n.getY() * cos45
        ).multiply(distance);

        Point2D rotMinus = new Point2D(
                n.getX() * cos_45 - n.getY() * sin_45,
                n.getX() * sin_45 + n.getY() * cos_45
        ).multiply(distance);

        Point2D a1 = a.subtract(rotPlus);
        Point2D a2 = a.subtract(rotMinus);
        Point2D b1 = b.add(rotPlus);
        Point2D b2 = b.add(rotMinus);

        return List.of(a1, a2, b1, b2);
    }

    private static double snap(double value, double grid) {
        if (grid <= 1) {
            return value;
        }
        return Math.round(value / grid) * grid;
    }

    private static Point2D snap(Point2D p, double grid) {
        return new Point2D(
                snap(p.getX(), grid),
                snap(p.getY(), grid)
        );
    }

    private void drawGrid() {
        if (canvasPane == null) {
            return;
        }

        canvasPane.getChildren().removeIf(node -> "grid".equals(node.getUserData()));

        if (gridSize <= 1) {
            return;
        }

        double width = canvasPane.getWidth();
        double height = canvasPane.getHeight();

        for (int x = 0; x <= width; x += gridSize) {
            javafx.scene.shape.Line line = new javafx.scene.shape.Line(x, 0, x, height);
            line.setStroke(Color.LIGHTGRAY);
            line.setStrokeWidth(0.5);
            line.setMouseTransparent(true);
            line.setUserData("grid");
            canvasPane.getChildren().add(line);
        }

        for (int y = 0; y <= height; y += gridSize) {
            javafx.scene.shape.Line line = new javafx.scene.shape.Line(0, y, width, y);
            line.setStroke(Color.LIGHTGRAY);
            line.setStrokeWidth(0.5);
            line.setMouseTransparent(true);
            line.setUserData("grid");
            canvasPane.getChildren().add(line);
        }
    }

    public double getGridSize() {
        return gridSize;
    }

    public void setGridSize(double gridSize) {
        this.gridSize = gridSize;
    }
}
