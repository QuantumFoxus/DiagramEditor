package qfox.diagrameditor.elementShapes;

import javafx.geometry.Point2D;

public class EditablePoint2D extends Point2D {

    private boolean editableX;
    private boolean editableY;

    public EditablePoint2D(Point2D p) {
        super(p.getX(), p.getY());
        this.editableX = true;
        this.editableY = true;
    }

    public EditablePoint2D(Point2D p, boolean editableX, boolean editableY) {
        super(p.getX(), p.getY());
        this.editableX = editableX;
        this.editableY = editableY;
    }

    public EditablePoint2D(double x, double y, boolean editableX, boolean editableY) {
        super(x, y);
        this.editableX = editableX;
        this.editableY = editableY;
    }

    public EditablePoint2D(double x, double y) {
        super(x, y);
        this.editableX = true;
        this.editableY = true;
    }

    public EditablePoint2D() {
        super(0, 0);
        this.editableX = true;
        this.editableY = true;
    }

    public boolean isEditableX() {
        return editableX;
    }

    public boolean isEditableY() {
        return editableY;
    }

    public void setEditableX(boolean editableX) {
        this.editableX = editableX;
    }

    public void setEditableY(boolean editableY) {
        this.editableY = editableY;
    }

    public EditablePoint2D copy() {
        return new EditablePoint2D(getX(), getY(), editableX, editableY);
    }

}
