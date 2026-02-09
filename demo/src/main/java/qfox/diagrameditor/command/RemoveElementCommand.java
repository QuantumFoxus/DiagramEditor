package qfox.diagrameditor.command;

import qfox.diagrameditor.elements.Diagram;
import qfox.diagrameditor.elements.DiagramConnector;
import qfox.diagrameditor.elements.DiagramElement;

public class RemoveElementCommand implements Command {

    private Diagram diagram;
    private DiagramElement element;

    public RemoveElementCommand(Diagram diagram, DiagramElement element) {
        this.diagram = diagram;
        this.element = element;
    }

    @Override
    public void execute() {
        if (diagram.getElements().contains(element)) {
            if (element instanceof DiagramConnector dc) {

            }
            diagram.removeElement(element);
        }
    }

    @Override
    public void undo() {
        if (!diagram.getElements().contains(element)) {
            diagram.addElement(element);
        }
    }
}
