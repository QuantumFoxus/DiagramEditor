package qfox.diagrameditor.elementShapes;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Line;

public class LineShape implements Shape {

    protected EditablePoint2D pos;
    protected EditablePoint2D size;
    protected Point2D basePos;
    protected Point2D baseSize;
    protected boolean dashed = false;
    protected boolean bold = false;

    public LineShape(EditablePoint2D pos, EditablePoint2D size) {
        this.pos = pos;
        this.size = size;
        this.basePos = new Point2D(pos.getX(), pos.getY());
        this.baseSize = new Point2D(size.getX(), size.getY());
    }

    public LineShape(EditablePoint2D pos, EditablePoint2D size, boolean dashed, boolean isBold) {
        this.pos = pos;
        this.size = size;
        this.dashed = dashed;
        this.basePos = new Point2D(pos.getX(), pos.getY());
        this.baseSize = new Point2D(size.getX(), size.getY());
        this.bold = isBold;
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

    public EditablePoint2D getPos2() {
        return size;
    }

    @Override
    public void setSize(EditablePoint2D newSize) {
        double newX = size.isEditableX() ? newSize.getX() : size.getX();
        double newY = size.isEditableY() ? newSize.getY() : size.getY();
        this.size = new EditablePoint2D(newX, newY, size.isEditableX(), size.isEditableY());
    }

    @Override
    public void reset() {
        this.size = new EditablePoint2D(baseSize.getX(), baseSize.getY(), size.isEditableX(), size.isEditableY());
        this.pos = new EditablePoint2D(basePos.getX(), basePos.getY(), pos.isEditableX(), pos.isEditableY());
    }

    @Override
    public Node render(Point2D globalPos) {
        double startX = pos.getX() + globalPos.getX();
        double startY = pos.getY() + globalPos.getY();
        double endX = pos.getX() + size.getX() + globalPos.getX();
        double endY = pos.getY() + size.getY() + globalPos.getY();

        Line line = new Line(startX, startY, endX, endY);

        if (dashed) {
            line.getStrokeDashArray().setAll(10.0, 5.0);
        } else {
            line.getStrokeDashArray().clear();
        }
        if (bold) {
            line.setStrokeWidth(2);
        } else {
            line.setStrokeWidth(1);
        }
        return line;
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

    public boolean isDashed() {
        return dashed;
    }

    public void setDashed(boolean dashed) {
        this.dashed = dashed;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    @Override
    public Shape clone() {
        LineShape copy = new LineShape(pos.copy(), size.copy(), dashed, bold);
        copy.basePos = new Point2D(this.basePos.getX(), this.basePos.getY());
        copy.baseSize = new Point2D(this.baseSize.getX(), this.baseSize.getY());
        return copy;
    }

    @Override
    public Point2D getSize() {
        return size;
    }

    @Override
    public void setBaseSize(Point2D baseSize) {
        this.baseSize = new Point2D(baseSize.getX(), baseSize.getY());
        this.basePos = new Point2D(pos.getX(), pos.getY());
    }
}
