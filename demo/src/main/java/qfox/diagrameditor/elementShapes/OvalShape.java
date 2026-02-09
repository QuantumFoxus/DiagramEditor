package qfox.diagrameditor.elementShapes;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class OvalShape implements Shape {

    private EditablePoint2D pos;
    private EditablePoint2D size;
    private Point2D basePos;
    private Point2D baseSize;

    public OvalShape(EditablePoint2D pos, EditablePoint2D size) {
        this.pos = pos;
        this.size = size;
        this.basePos = new Point2D(pos.getX(), pos.getY());
        this.baseSize = new Point2D(size.getX(), size.getY());
    }

    @Override
    public EditablePoint2D getSize() {
        return size;
    }

    @Override
    public void setSize(EditablePoint2D newSize) {
        double newX = newSize.isEditableX() ? newSize.getX() : size.getX();
        double newY = newSize.isEditableY() ? newSize.getY() : size.getY();
        this.size = new EditablePoint2D(newX, newY, newSize.isEditableX(), newSize.isEditableY());
    }

    @Override
    public Shape clone() {
        OvalShape copy = new OvalShape(pos.copy(), size.copy());
        copy.basePos = new Point2D(this.basePos.getX(), this.basePos.getY());
        copy.baseSize = new Point2D(this.baseSize.getX(), this.baseSize.getY());
        return copy;
    }

    @Override
    public Node render(Point2D globalPos) {
        double centerX = globalPos.getX() + pos.getX() + size.getX() / 2.0;
        double centerY = globalPos.getY() + pos.getY() + size.getY() / 2.0;

        Ellipse node = new Ellipse(centerX, centerY, size.getX() / 2.0, size.getY() / 2.0);
        node.setStroke(Color.BLACK);
        node.setFill(Color.WHITE);
        return node;
    }

    @Override
    public void reset() {
        this.size = new EditablePoint2D(baseSize.getX(), baseSize.getY(), size.isEditableX(), size.isEditableY());
        this.pos = new EditablePoint2D(basePos.getX(), basePos.getY(), pos.isEditableX(), pos.isEditableY());
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
    public EditablePoint2D getPos() {
        return pos;
    }

    @Override
    public void setPos(EditablePoint2D newPos) {
        double newX = pos.isEditableX() ? newPos.getX() : pos.getX();
        double newY = pos.isEditableY() ? newPos.getY() : pos.getY();
        this.pos = new EditablePoint2D(newX, newY, pos.isEditableX(), pos.isEditableY());
    }

    @Override
    public void setBaseSize(Point2D baseSize) {
        this.baseSize = new Point2D(baseSize.getX(), baseSize.getY());
        this.basePos = new Point2D(pos.getX(), pos.getY());
    }
}
