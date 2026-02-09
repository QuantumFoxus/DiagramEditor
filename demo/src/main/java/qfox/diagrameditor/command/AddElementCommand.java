package qfox.diagrameditor.command;

import qfox.diagrameditor.elements.Diagram;
import qfox.diagrameditor.elements.DiagramElement;

public class AddElementCommand implements Command {

    private Diagram diagram;
    private DiagramElement element;

    public AddElementCommand(Diagram diagram, DiagramElement element) {
        this.diagram = diagram;
        this.element = element;
    }

    @Override
    public void execute() {
        if (!diagram.getElements().contains(element)) {
            diagram.addElement(element);
        }
    }

    @Override
    public void undo() {
        diagram.removeElement(element);
    }
}
