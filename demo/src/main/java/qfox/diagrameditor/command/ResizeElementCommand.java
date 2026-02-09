package qfox.diagrameditor.command;

import qfox.diagrameditor.elements.Diagram;
import qfox.diagrameditor.elements.DiagramElement;

import javafx.geometry.Point2D;

public class ResizeElementCommand implements Command {

    private Diagram diagram;
    private DiagramElement element;
    private Point2D oldSize;
    private Point2D newSize;

    public ResizeElementCommand(Diagram diagram, DiagramElement element, Point2D newSize) {
        this.diagram = diagram;
        this.element = element;
        this.oldSize = element.getSize();
        this.newSize = newSize;
    }

    @Override
    public void execute() {
        element.setSize(newSize);
    }

    @Override
    public void undo() {
        element.setSize(oldSize);
    }
}
