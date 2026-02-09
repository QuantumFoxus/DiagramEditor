package qfox.diagrameditor.elements;

import qfox.diagrameditor.elementShapes.Shape;

import javafx.geometry.Point2D;

public interface DiagramElement {

    String getType();

    Point2D getPosition();

    void setPosition(Point2D position);

    Point2D getSize();

    void setSize(Point2D size);

    DiagramElement clone();

    Shape[] getShape();

    String[] getTexts();

    void setTexts(String[] texts);

    @Override
    public String toString();
}
