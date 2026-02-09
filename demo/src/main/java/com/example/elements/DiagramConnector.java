package com.example.elements;

import java.util.Arrays;

import com.example.elementShapes.EditablePoint2D;
import com.example.elementShapes.Shape;
import com.example.elementShapes.ShapesFactory;
import com.example.elementShapes.TextShape;

import javafx.geometry.Point2D;

public class DiagramConnector implements DiagramElement {

    private String type;
    private DiagramElement fromElement;
    private DiagramElement toElement;
    private Point2D position;
    private Point2D size;
    private Shape[] shape;
    private String[] texts;

    public DiagramConnector(String type, String[] texts, DiagramElement fromElement, DiagramElement toElement, Point2D position, Point2D size) {
        this.type = type;
        this.fromElement = fromElement;
        this.toElement = toElement;
        this.position = position;
        this.size = size;
        this.shape = ShapesFactory.createShapeGroupByName(type);
        updateTexts(texts);
        updateShapes();
    }

    public DiagramConnector(String type, DiagramElement fromElement, DiagramElement toElement, Point2D position) {
        this.type = type;
        this.fromElement = fromElement;
        this.toElement = toElement;
        this.position = position;
        this.shape = ShapesFactory.createShapeGroupByName(type);
        this.size = this.shape[0].getSize();
        updateTexts(null);
    }

    public DiagramConnector(String type, DiagramElement fromElement, DiagramElement toElement) {
        this.type = type;
        this.fromElement = fromElement;
        this.toElement = toElement;
        this.position = new Point2D(0, 0);
        this.size = new Point2D(100, 100);
        this.shape = ShapesFactory.createShapeGroupByName(type);
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

    public DiagramElement getFromElement() {
        return fromElement;
    }

    public void setFromElement(DiagramElement fromElement) {
        this.fromElement = fromElement;
    }

    public DiagramElement getToElement() {
        return toElement;
    }

    public void setToElement(DiagramElement toElement) {
        this.toElement = toElement;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Point2D getPosition() {
        return position;
    }

    @Override
    public void setPosition(Point2D position) {
        this.position = position;
    }

    @Override
    public Point2D getSize() {
        return size;
    }

    @Override
    public void setSize(Point2D size) {
        this.size = size;
        updateShapes();
    }

    private void updateShapes() {
        int text_itt = 0;
        for (Shape s : shape) {
            if (s instanceof TextShape t) {
                t.setText(texts[text_itt++]);
            }
            s.setSize(new EditablePoint2D(size));
        }
    }

    public void updateConnections() {
        Point2D start = getPosition();
        Point2D end = start.add(getSize());

        if (fromElement != null) {
            Point2D target = (toElement != null) ? toElement.getPosition().add(toElement.getSize().multiply(0.5)) : end;
            start = intersectRectEdge(fromElement.getPosition(), fromElement.getSize(), target);
        }

        if (toElement != null) {
            Point2D target = (fromElement != null) ? fromElement.getPosition().add(fromElement.getSize().multiply(0.5)) : start;
            end = intersectRectEdge(toElement.getPosition(), toElement.getSize(), target);
        }

        setPosition(start);
        setSize(end.subtract(start));
    }

    @Override
    public String[] getTexts() {
        return texts;
    }

    @Override
    public void setTexts(String[] texts) {
        this.texts = texts;
        updateShapes();
    }

    @Override
    public String toString() {
        return "DiagramConnector{"
                + "type='" + type + '\''
                + ", fromElement='" + (fromElement != null ? fromElement.toString() : "none") + '\''
                + ", toElement='" + (toElement != null ? toElement.toString() : "none") + '\''
                + ", position=" + position
                + ", size=" + size
                + '}';
    }

    @Override
    public DiagramElement clone() {
        return new DiagramConnector(
                this.type,
                this.texts,
                this.fromElement,
                this.toElement,
                new Point2D(this.position.getX(), this.position.getY()),
                new Point2D(this.size.getX(), this.size.getY())
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

    public static Point2D intersectRectEdge(Point2D rectPos, Point2D rectSize, Point2D target) {
        Point2D center = rectPos.add(rectSize.multiply(0.5));

        double dx = target.getX() - center.getX();
        double dy = target.getY() - center.getY();

        if (dx == 0 && dy == 0) {
            return center;
        }

        double halfW = rectSize.getX() / 2.0;
        double halfH = rectSize.getY() / 2.0;

        double tx = dx != 0 ? halfW / Math.abs(dx) : Double.POSITIVE_INFINITY;
        double ty = dy != 0 ? halfH / Math.abs(dy) : Double.POSITIVE_INFINITY;

        double t = Math.min(tx, ty);

        return new Point2D(
                center.getX() + dx * t,
                center.getY() + dy * t
        );
    }
}
