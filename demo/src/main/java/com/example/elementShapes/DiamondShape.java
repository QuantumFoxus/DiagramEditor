package com.example.elementShapes;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class DiamondShape extends RectangleShape {

    public DiamondShape(EditablePoint2D pos, EditablePoint2D size) {
        super(pos, size);
    }

    public DiamondShape(EditablePoint2D pos, EditablePoint2D size, boolean hasStroke) {
        super(pos, size, hasStroke);
    }

    @Override
    public Shape clone() {
        return new DiamondShape(pos.copy(), size.copy(), hasStroke);
    }

    @Override
    public Node render(Point2D globalPos) {
        Polygon diamond = new Polygon();
        double centerX = pos.getX() + globalPos.getX() + size.getX() / 2;
        double centerY = pos.getY() + globalPos.getY() + size.getY() / 2;
        diamond.getPoints().addAll(
                centerX, pos.getY() + globalPos.getY(),
                pos.getX() + globalPos.getX() + size.getX(), centerY,
                centerX, pos.getY() + globalPos.getY() + size.getY(),
                pos.getX() + globalPos.getX(), centerY
        );
        diamond.setFill(Color.WHITE);
        if (hasStroke == true) {
            diamond.setStroke(Color.BLACK);
        } else {
            diamond.setStroke(null);
        }
        diamond.setStrokeWidth(2);
        return diamond;
    }

}
