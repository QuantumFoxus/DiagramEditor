package com.example.elementShapes;

import javafx.geometry.Point2D;
import javafx.scene.Node;

public interface Shape {

    Node render(Point2D globalPos);

    Shape clone();

    Point2D getSize();

    void setSize(EditablePoint2D newSize);

    EditablePoint2D getPos();

    void setPos(EditablePoint2D newPos);

    void mult(Point2D multipler);

    void reset();

    void setBaseSize(Point2D baseSize);
}
