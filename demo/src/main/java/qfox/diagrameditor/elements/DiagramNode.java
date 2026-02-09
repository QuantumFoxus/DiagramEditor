package qfox.diagrameditor.elements;

import java.util.ArrayList;
import java.util.Arrays;

import qfox.diagrameditor.elementShapes.ShapesFactory;
import qfox.diagrameditor.elementShapes.Shape;
import qfox.diagrameditor.elementShapes.TextShape;

import javafx.geometry.Point2D;

public class DiagramNode implements DiagramElement {

    private String type;
    private String[] texts;
    private Point2D position;
    private Point2D size = new Point2D(0, 0);
    private Point2D baseSize = new Point2D(0, 0);
    private Shape[] shape;
    private ArrayList<DiagramElement> connectedElements = new ArrayList<>();

    private DiagramNode(String type, String[] texts, Point2D position, Point2D size, Point2D baseSize, ArrayList<DiagramElement> connectedElements) {
        this.type = type;
        this.position = position;
        this.shape = ShapesFactory.createShapeGroupByName(type);
        this.baseSize = baseSize;
        this.size = size;
        this.connectedElements = connectedElements != null ? connectedElements : new ArrayList<>();

        updateTexts(texts);

        updateShapes(this.size);
        System.err.println("base size: " + this.baseSize);
    }

    public DiagramNode(String type, Point2D position) {
        this.type = type;
        this.position = position;
        this.shape = ShapesFactory.createShapeGroupByName(type);
        this.size = this.shape[0].getSize();
        this.baseSize = size;
        updateTexts(null);
    }

    private void updateTexts(String[] texts) {
        if (texts != null && texts.length > 0) {
            this.texts = texts;
        } else {
            int count = (int) Arrays.stream(shape).filter(s -> s instanceof TextShape).count();
            this.texts = new String[count];
            int i = 0;
            for (Shape s : shape) {
                if (s instanceof TextShape t) {
                    this.texts[i++] = t.getText();
                }
            }
        }
    }

    private void updateShapes(Point2D newSize) {
        if (baseSize.getX() == 0 || baseSize.getY() == 0) {
            return;
        }

        double scaleX = newSize.getX() / baseSize.getX();
        double scaleY = newSize.getY() / baseSize.getY();

        int textIdx = 0;
        for (Shape s : shape) {
            if (s instanceof TextShape t) {
                t.setText(texts[textIdx++]);
            }
            s.reset();
            s.mult(new Point2D(scaleX, scaleY));
        }
    }

    public void addConnectedElement(DiagramElement element) {
        connectedElements.add(element);
    }

    public void removeConnectedElement(DiagramElement element) {
        connectedElements.remove(element);
    }

    public void clearConnectedElements() {
        connectedElements.clear();
    }

    public void notifyConnectedElements() {
        if (connectedElements == null) {
            return;
        }
        for (DiagramElement element : connectedElements) {
            if (element instanceof DiagramConnector dc) {
                dc.updateConnections();
            }
        }
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String[] getTexts() {
        return texts;
    }

    @Override
    public void setTexts(String[] texts) {
        this.texts = texts;
        updateShapes(size);
    }

    @Override
    public Point2D getPosition() {
        return position;
    }

    @Override
    public void setPosition(Point2D position) {
        this.position = position;
        notifyConnectedElements();
    }

    @Override
    public Point2D getSize() {
        return size;
    }

    @Override
    public void setSize(Point2D size) {
        updateShapes(size);
        this.size = size;
        notifyConnectedElements();
    }

    @Override
    public String toString() {
        return "DiagramNode{"
                + "type='" + type + '\''
                + ", text='" + Arrays.deepToString(texts) + '\''
                + ", position=" + position
                + ", size=" + size
                + ", connectedElements Size=" + connectedElements.size()
                + '}';
    }

    @Override
    public DiagramElement clone() {
        ArrayList<DiagramElement> connectedCopy = null;
        if (connectedElements != null) {
            connectedCopy = new ArrayList<>();
            for (DiagramElement element : connectedElements) {
                connectedCopy.add(element != null ? element.clone() : null);
            }
        }
        return new DiagramNode(
                this.type,
                this.texts,
                new Point2D(this.position.getX(), this.position.getY()),
                new Point2D(this.size.getX(), this.size.getY()),
                new Point2D(this.baseSize.getX(), this.baseSize.getY()),
                connectedCopy
        );
    }

    @Override
    public Shape[] getShape() {
        Shape[] copy = new Shape[shape.length];
        for (int i = 0; i < shape.length; i++) {
            copy[i] = shape[i].clone();
        }
        return copy;
    }

}
