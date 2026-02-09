package com.example.app;

import com.example.elements.DiagramElement;

import javafx.geometry.Point2D;

public interface CanvasListener {

    void onElementSelected(DiagramElement element);

    void onElementUnselected();

    void onElementMoveFinished(DiagramElement element, Point2D newPos);

    void onElementResized(DiagramElement element, Point2D newSize);

    void onElementUpdated(DiagramElement element, Point2D newPos, Point2D newSize, String[] newTexts);

}
