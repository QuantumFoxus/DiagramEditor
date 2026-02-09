package com.example.elementShapes;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RectangleShape implements Shape {

    protected EditablePoint2D pos;
    protected EditablePoint2D size;
    protected boolean hasStroke = true;

    protected Point2D basePos;
    protected Point2D baseSize;

    public RectangleShape(EditablePoint2D pos, EditablePoint2D size) {
        this.pos = pos;
        this.size = size;
        this.basePos = new Point2D(pos.getX(), pos.getY());
        this.baseSize = new Point2D(size.getX(), size.getY());
    }

    public RectangleShape(EditablePoint2D pos, EditablePoint2D size, boolean hasStroke) {
        this.pos = pos;
        this.size = size;
        this.hasStroke = hasStroke;
        this.basePos = new Point2D(pos.getX(), pos.getY());
        this.baseSize = new Point2D(size.getX(), size.getY());
    }

    @Override
    public EditablePoint2D getPos() {
        return pos;
    }

    @Override
    public void reset() {
        this.size = new EditablePoint2D(baseSize, size.isEditableX(), size.isEditableY());
        this.pos = new EditablePoint2D(basePos, pos.isEditableX(), pos.isEditableY());
    }

    @Override
    public void setPos(EditablePoint2D newPos) {
        double newX = pos.isEditableX() ? newPos.getX() : pos.getX();
        double newY = pos.isEditableY() ? newPos.getY() : pos.getY();
        this.pos = new EditablePoint2D(newX, newY, pos.isEditableX(), pos.isEditableY());
    }

    @Override
    public void setSize(EditablePoint2D newSize) {
        double newX = size.isEditableX() ? newSize.getX() : size.getX();
        double newY = size.isEditableY() ? newSize.getY() : size.getY();
        this.size = new EditablePoint2D(newX, newY, size.isEditableX(), size.isEditableY());
    }

    @Override
    public EditablePoint2D getSize() {
        return size;
    }

    @Override
    public void mult(Point2D multiplier) {
        double sx = multiplier.getX();
        double sy = multiplier.getY();

        double newW = baseSize == null ? size.getX() * sx : (baseSize.getX() * sx);
        double newH = baseSize == null ? size.getY() * sy : (baseSize.getY() * sy);

        double newX = basePos == null ? pos.getX() * sx : basePos.getX() * sx;
        double newY = basePos == null ? pos.getY() * sy : basePos.getY() * sy;

        this.size = new EditablePoint2D(size.isEditableX() ? newW : size.getX(), size.isEditableY() ? newH : size.getY(), size.isEditableX(), size.isEditableY());
        this.pos = new EditablePoint2D(pos.isEditableX() ? newX : pos.getX(), pos.isEditableY() ? newY : pos.getY(), pos.isEditableX(), pos.isEditableY());
    }

    @Override
    public Node render(Point2D globalPos) {
        Rectangle r = new Rectangle(
                pos.getX() + globalPos.getX(),
                pos.getY() + globalPos.getY(),
                size.getX(),
                size.getY()
        );
        r.setFill(Color.WHITE);
        r.setStroke(hasStroke ? Color.BLACK : null);
        r.setStrokeWidth(2);
        return r;
    }

    public void setStroke(boolean hasStroke) {
        this.hasStroke = hasStroke;
    }

    public boolean isStroke() {
        return hasStroke;
    }

    @Override
    public Shape clone() {
        RectangleShape copy = new RectangleShape(pos.copy(), size.copy(), hasStroke);
        copy.basePos = new Point2D(this.basePos.getX(), this.basePos.getY());
        copy.baseSize = new Point2D(this.baseSize.getX(), this.baseSize.getY());
        return copy;
    }

    @Override
    public void setBaseSize(Point2D baseSize) {
        this.baseSize = baseSize;
        this.basePos = new Point2D(pos.getX(), pos.getY());
    }
}
